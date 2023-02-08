//
//  CameraManager.swift
//  KD Tiktok-Clone
//
//  Created by Sam Ding on 9/30/20.
//  Copyright © 2020 Kaishan. All rights reserved.


import Foundation
import AVFoundation
import Photos
import UIKit

/// When user finish permitting access to camera, microphone, or photo library
typealias AccessPermissionCompletionBlock = (Bool) -> Void
/// No input and returns nothing
typealias RegularCompletionBlock = () -> Void
/// Finish Recording Completion Block: URL(*Path to the video*), Error
protocol RecordingDelegate: class {
    func finishRecording(_ videoURL: URL?, _ err: Error?)
}


class CameraManager: NSObject, AVCaptureFileOutputRecordingDelegate {
    var frontCamera : AVCaptureDeviceInput!
    var rearCamera : AVCaptureDeviceInput!
    var audioDeviceInput: AVCaptureDeviceInput!
    /// Capture session with customized settings
    var captureSession: AVCaptureSession?
    /// Capture Device with customized settings
    var captureDevice: AVCaptureDevice!
    /// Access Permission to both Camera and Microphone
    var cameraAndAudioAccessPermitted: Bool!
    /// Parent View that contains preview layer
    var embeddingView: UIView?
    var urls = [URL]()
    /// Recording Delegate
    weak var delegate: RecordingDelegate!
    let VIDEO_FILE_EXTENSION = "mp4"
    fileprivate var sessionQueue: DispatchQueue = DispatchQueue(label: "com.CameraSessionQueue")
    fileprivate var movieOutput: AVCaptureMovieFileOutput?
    fileprivate var previewLayer: AVCaptureVideoPreviewLayer?
    fileprivate var photoLibrary: PHPhotoLibrary?
    fileprivate var tempFilePath: URL {
        get{
            let tempURL = URL(fileURLWithPath: NSTemporaryDirectory()).appendingPathComponent("tempMovie\(Date())").appendingPathExtension(VIDEO_FILE_EXTENSION)
            return tempURL
        }
    }
    
    
    // MARK: - Initializers
    override init() {
        super.init()
        cameraAndAudioAccessPermitted = (AVCaptureDevice.authorizationStatus(for: .video) == .authorized) &&
                                        (AVCaptureDevice.authorizationStatus(for: .audio) == .authorized)
        photoLibrary = PHPhotoLibrary.shared()
    }
    
    /**
     Set up the camera and add view to previerw layer
     */
    func addPreviewLayerToView(view: UIView,position:AVCaptureDevice.Position = .back){
        if cameraAndAudioAccessPermitted {
            if let previewLayer = previewLayer {
                previewLayer.removeFromSuperlayer()
            }
            setupCamera(position: position) {
                self.embeddingView = view
                DispatchQueue.main.async {
                    guard let previewLayer = self.previewLayer else { return }
                    previewLayer.frame = view.layer.bounds
                    view.clipsToBounds = true
                    view.layer.addSublayer(previewLayer)
                }
            }

        }
    }
    
    // MARK: - Recording Session
    func startRecording(){
        movieOutput?.startRecording(to: tempFilePath, recordingDelegate: self)
    }
    func isRecording()->Bool?{
        return self.movieOutput?.isRecording
    }
    func stopRecording(){
        captureSession?.stopRunning()
        captureSession?.stopRunning()
        if let output = self.movieOutput, output.isRecording {
            output.stopRecording()
        }
    }
    
    // TODO: Reset Capture session and other settings if user reshoot
    func resetCaptureSession(){
        removeAllTempFiles()
    }
    
    //Method to setup camera
    
    fileprivate func setupCamera(position:AVCaptureDevice.Position,completion: @escaping RegularCompletionBlock){
        captureSession = AVCaptureSession()
        guard let device = AVCaptureDevice.default(AVCaptureDevice.DeviceType.builtInWideAngleCamera, for: .video, position: position) else { return }
        captureDevice = device
        guard let audioDevice = AVCaptureDevice.default(for: .audio) else { return }
        do {
            if let frontCamera = AVCaptureDevice.default(.builtInWideAngleCamera, for: .video, position: .front) {
                self.frontCamera = try AVCaptureDeviceInput(device: frontCamera)
            }
            if let rearCamera = AVCaptureDevice.default(.builtInWideAngleCamera, for: .video, position: .back) {
                self.rearCamera = try AVCaptureDeviceInput(device: rearCamera)
            }
            audioDeviceInput = try AVCaptureDeviceInput(device: audioDevice)
            guard audioDeviceInput != nil else {
                print("error: cant get audioDeviceInput")
                return
            }
            sessionQueue.async {
                if let session = self.captureSession {
                    session.beginConfiguration()
                    session.sessionPreset = .high
                    
                    // Add video
                    
                    if session.canAddInput(position == .front ? self.frontCamera : self.rearCamera){
                        session.addInput(position == .front ? self.frontCamera : self.rearCamera)
                    }
                    // Add audio
                    if session.canAddInput(self.audioDeviceInput){
                        session.addInput(self.audioDeviceInput)
                    }
                    
                    
                    self.movieOutput = AVCaptureMovieFileOutput()
                    if session.canAddOutput(self.movieOutput!){
                        session.addOutput(self.movieOutput!)
                    }
                    
                    if let connection = self.movieOutput!.connection(with: .video) {
                        connection.isVideoMirrored = self.captureDevice.position == .front
                        if connection.isVideoStabilizationSupported {
                            connection.preferredVideoStabilizationMode = .auto
                        }
                    }
                    
                    self.setupPreviewLayer()
                    session.commitConfiguration()
                    session.startRunning()
                    completion()
                }
            }
            
        } catch let error as NSError {
            print("Device Input Error: \(error.localizedDescription)")
        }
        
    }
    //Method to switch between rear and front camera
    func switchCamera(){
        guard let session = captureSession, let frontCamera = frontCamera, let rearCamera = rearCamera, let audio = audioDeviceInput else { return }
        session.removeInput(audio)
        let currentInput = session.inputs[0] as? AVCaptureDeviceInput
        if currentInput == rearCamera {
            session.removeInput(rearCamera)
            session.addInput(frontCamera)
            if let connection = self.movieOutput!.connection(with: .video) {
                print("CASE",self.captureDevice.position == .front)
                connection.isVideoMirrored = true
                if connection.isVideoStabilizationSupported {
                    connection.preferredVideoStabilizationMode = .auto
                }
            }
            
        } else if currentInput == frontCamera {
            session.removeInput(frontCamera)
            session.addInput(rearCamera)
        }
        session.addInput(audio)
        movieOutput?.startRecording(to: tempFilePath, recordingDelegate: self)
    }
    
    fileprivate func setupPreviewLayer(){
        // Preview Layer
        
        if let session = captureSession{
            previewLayer = AVCaptureVideoPreviewLayer(session: session)
            previewLayer?.videoGravity = .resizeAspectFill
        }
        
    }
    
    // Finish Recording
    func fileOutput(_ output: AVCaptureFileOutput, didFinishRecordingTo outputFileURL: URL, from connections: [AVCaptureConnection], error: Error?) {
        urls.append(outputFileURL)
        if let error = error {
            print("Saving video failed: \(error.localizedDescription)",outputFileURL)
        } else {
            AVMutableComposition().mergeVideo(urls) { url, error in
                guard let url = url else{
                    self.delegate.finishRecording(nil, error)
                    return
                }
                self.delegate.finishRecording(url, error)
            }
        }
    }
    
    
    func removeAllTempFiles(){
        var directory = NSTemporaryDirectory()
        sessionQueue.async {
            directory.removeAll()
        }
    }
    
}

// MARK: - Access Permission
extension CameraManager {
    /**
     Ask the users for access to camera, only when the authrization status is not `Authorized`
     */
    @objc func askForCameraAccess(_ completion: @escaping AccessPermissionCompletionBlock){
        AVCaptureDevice.requestAccess(for: .video, completionHandler: { [weak self] access in
            self?.checkIfBothPermissionGranted()
            DispatchQueue.main.async {
                completion(access)
            }
        })
    }
    
    /**
     Ask the users for access to microphone, only when the authrization status is not `Authorized`
     */
    @objc func askForMicrophoneAccess(_ completion: @escaping AccessPermissionCompletionBlock){
        AVCaptureDevice.requestAccess(for: .audio, completionHandler: { [weak self] access in
            self?.checkIfBothPermissionGranted()
            DispatchQueue.main.async {
                completion(access)
            }
        })
    }
    
    fileprivate func checkIfBothPermissionGranted(){
        if (AVCaptureDevice.authorizationStatus(for: .video) == .authorized) &&
            (AVCaptureDevice.authorizationStatus(for: .audio) == .authorized) {
            cameraAndAudioAccessPermitted = true
        }
    }
}

// MARK: - Save To Photo Library
extension CameraManager {
    /**
     Ask the users for access to photo library, if authorized save the video
     */
    func saveToLibrary(videoURL: URL){
        func save(){
            sessionQueue.async { [weak self] in
                self?.photoLibrary?.performChanges({
                    PHAssetChangeRequest.creationRequestForAssetFromVideo(atFileURL: videoURL)
                }, completionHandler: { (saved, error) in
                    if let error = error {
                        print("Saving to Photo Library Failed: \(error.localizedDescription)")
                    } else {
                        print("Saved at: \(videoURL)")
                    }
                })
            }
        }
        
        if PHPhotoLibrary.authorizationStatus() != .authorized {
            PHPhotoLibrary.requestAuthorization({ status in
                if status == .authorized {
                    save()
                }
            })
        } else {
            save()
        }
        
    }
}
