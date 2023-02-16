//
//  AddMediaVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/21/22.
//

import UIKit
import Photos

class AddMediaVC: UIViewController {
    @IBOutlet weak var flashImg: UIImageView!
    @IBOutlet weak var demoImage: UIImageView!
    @IBOutlet weak var galleryView: CardView!
    @IBOutlet weak var timeLbl: UILabel!
    @IBOutlet weak var progress: RecordButton!
    @IBOutlet weak var previewView: UIView!
    @IBOutlet weak var buttonView: CardView!
    let cameraManager = CameraManager()
    var videoURL: URL?
    var timer : Timer!
    var second : CGFloat! = 0
    var selectedAudio:AudioDataModel?
    var vidManager = AudioVideoMerger()
    let imagePickerController = UIImagePickerController()
    var videoPicked = false
    var isFrontCamera = false
    var isRecording = false
    var needTorch = false
    var delegate:CustomVideoPostedDelegate?
    override func viewDidLoad() {
        super.viewDidLoad()
        hideNavbar()
        buttonView.addGestureRecognizer(UILongPressGestureRecognizer(target: self, action: #selector(recordVideo)))
        galleryView.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(openGallery)))
    }
    
    @IBAction func flashBtnClicked(_ sender: Any) {
        needTorch = !needTorch
        if needTorch{
            flashImg.image = UIImage(named: "flash_on")
        }else{
            flashImg.image = UIImage(named: "flash_off")
        }
        isRecording ? toggleTorch() : print("hh")
    }
    //MARK: - Setup
    func askForGalleryPermission(){
        let photos = PHPhotoLibrary.authorizationStatus()
        if photos == .denied || photos == .notDetermined || photos == .restricted{
            PHPhotoLibrary.requestAuthorization({status in
                if status == .authorized{
                    
                } else {
                    
                }
            })
        }
        
    }
    func fetchVideos()
    {
        let fetchOptions = PHFetchOptions()
        fetchOptions.sortDescriptors = [NSSortDescriptor(key:"creationDate", ascending: false)]
        fetchOptions.fetchLimit = 1
        let fetchResults = PHAsset.fetchAssets(with: PHAssetMediaType.video, options: fetchOptions)
        fetchResults.enumerateObjects({ [weak self] (object, count, stop) in
            PHCachingImageManager.default().requestImage(for: object,
                                                         targetSize: self?.demoImage.bounds.size ?? CGSize(width: 25, height: 25),
                                                         contentMode: .aspectFill,
                                                         options: nil) { (photo, _) in
                self?.demoImage.image = photo
            }
        })
    }
    @objc func openGallery(){
        imagePickerController.sourceType = .photoLibrary
        imagePickerController.delegate = self
        imagePickerController.mediaTypes = ["public.movie"]
        imagePickerController.allowsEditing = true
        present(imagePickerController, animated: true, completion: nil)
    }
    func setTimeLabelValue(withValue seconds:Int){
        if seconds < 10{
            self.timeLbl.text = "00:00:0\(seconds)"
        }else if seconds < 60{
            self.timeLbl.text = "00:00:\(seconds)"
        }else if seconds < 120{
            self.timeLbl.text = "00:01:\(seconds - 60)"
        }else if seconds < 180{
            self.timeLbl.text = "00:02:\(seconds - 120)"
        }else if seconds == 180{
            self.timeLbl.text = "00:03:00"
        }
        
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        cameraManager.removeAllTempFiles()
        openCustomCamera()
        fetchVideos()
        progress.buttonColor = UIColor.theme
        progress.progressColor = .red
        progress.closeWhenFinished = false
        progress.setProgress(0)
        second = 0
        isRecording = false
        setTimeLabelValue(withValue: Int(second))
        self.videoPicked = false
        cameraManager.urls = []
        let doubleTap = UITapGestureRecognizer(target: self, action: #selector(changeCamera))
        doubleTap.numberOfTapsRequired = 2
        previewView.addGestureRecognizer(doubleTap)
        flashImg.isHidden = false
        flashImg.image = UIImage(named: "flash_off")
    }
    //MARK: - Actions
    @objc func changeCamera(){
        if isRecording{
            if cameraManager.isRecording() ?? false{
                cameraManager.switchCamera()
            }
        }else{
            cameraManager.addPreviewLayerToView(view: self.previewView,position: isFrontCamera ? .back : .front)
        }
        isFrontCamera = !isFrontCamera
        if isRecording {
            flashImg.isHidden = true
        }else{
            flashImg.isHidden = isFrontCamera
        }
        
        
    }
    override func viewDidAppear(_ animated: Bool) {
       
    }
    @IBAction func closeBtnClicked(_ sender: Any) {
        self.dismiss(animated: true)
    }
    
    @objc func calculateSeconds() {

        let maxDuration = CGFloat(30) // Max duration of the recordButtons
        second = second + (CGFloat(0.05) / maxDuration)
        progress.setProgress(second/6)
        print("SECONDS",second * 30)
        setTimeLabelValue(withValue: Int(second * 30))
        if second >= 6 {
            cameraManager.stopRecording()
            timer.invalidate()
        }
    }
    @objc func recordVideo(sender:UILongPressGestureRecognizer){
        switch sender.state {
        case .began:
            self.isRecording = true
            cameraManager.startRecording()
            toggleTorch()
            flashImg.isHidden = true
            timer = Timer()
            self.timer = Timer.scheduledTimer(timeInterval: 0.05, target: self, selector: #selector(calculateSeconds), userInfo: nil, repeats: true)
        case .changed:
            print("ZOOOM")
        case .cancelled, .ended:
            if cameraManager.isRecording() ?? false{
                
                timer.invalidate()
                cameraManager.stopRecording()
            }else{
                DispatchQueue.main.asyncAfter(deadline: .now() + 0.2){
                    
                    self.timer.invalidate()
                    self.cameraManager.stopRecording()
                }
            }
            
        default:
            break
        }
    }
    func openCustomCamera(){
        if cameraManager.cameraAndAudioAccessPermitted {
            setUpSession()
        }else{
            cameraManager.askForCameraAccess({ [weak self] access in
                guard let self = self else { return }
                if access {
                    
                    if (self.cameraManager.cameraAndAudioAccessPermitted) { self.setUpSession() }
                } else {
                    self.alertCameraAccessNeeded()
                }
            })
            cameraManager.askForMicrophoneAccess({ [weak self] access in
                guard let self = self else { return }
                if access {
                    if (self.cameraManager.cameraAndAudioAccessPermitted) { self.setUpSession() }
                } else {
                    self.alertCameraAccessNeeded()
                }
            })
        }
    }
    func alertCameraAccessNeeded() {
        let settingsAppURL = URL(string: UIApplication.openSettingsURLString)!
        let alert = UIAlertController(
            title: "Camera & Microphone Access Need",
            message: "Camera & Microphone access is required to record your videos.",
            preferredStyle: .alert
        )
        alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: {(alert) -> Void in
            self.dismiss(animated: true)
        }))
        alert.addAction(UIAlertAction(title: "Settings", style: .default, handler: { (alert) -> Void in
            UIApplication.shared.open(settingsAppURL, options: [:], completionHandler: nil)
        }))
        present(alert, animated: true, completion: nil)
    }
    func toggleTorch(turnOff:Bool = false) {
        guard
            let device = AVCaptureDevice.default(for: AVMediaType.video),
            device.hasTorch
        else { return }
        if turnOff{
            device.torchMode = .off
        }else{
            do {
                try device.lockForConfiguration()
                if !needTorch
                {
                    flashImg.image = UIImage(named: "flash_off")
                    device.torchMode = .off
                    
                } else if needTorch && !isFrontCamera {
                    flashImg.image = UIImage(named: "flash_on")
                    device.torchMode = .on
                    
                }
                device.unlockForConfiguration()
            } catch {
                print("Torch could not be used")
            }
        }
    }
    func setUpSession(){
        cameraManager.delegate = self
        cameraManager.addPreviewLayerToView(view: self.previewView,position: .back)
    }
    func openDeviceCamera(){
        let cameraVc = UIImagePickerController()
        cameraVc.sourceType = .camera
        cameraVc.mediaTypes = ["public.movie"]
        cameraVc.delegate = self
        cameraVc.videoMaximumDuration = 30.0
        cameraVc.navigationBar.snapshotView(afterScreenUpdates: true)
        self.present(cameraVc, animated: true, completion: nil)
    }
    func goToCutomizeVideo(videoUrl:URL,audioDetails:AudioDataModel? = nil){
        let vc = CustomizeVideoVC()
        vc.url = videoUrl
        vc.videoPicked = self.videoPicked
        vc.delegate = self
        if audioDetails != nil{
            vc.audioTitle = "\(audioDetails!.artistName) - \(audioDetails!.songName)"
        }
        self.navigationController?.pushViewController(vc, animated: false)
    }
}
extension AddMediaVC: UIImagePickerControllerDelegate,UINavigationControllerDelegate{
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        guard let selectedVideo:URL = (info[UIImagePickerController.InfoKey.mediaURL] as? URL) else{return}
        picker.dismiss(animated: true) {
            let asset = AVURLAsset(url: selectedVideo)
            if asset.duration.seconds > 180.0{
                DispatchQueue.main.async {
                    AlertView().showAlert(message: "Video must be under 3 minutes", delegate: self, pop: false)
                }
                return
            }
            self.videoPicked = true
            encodeVideo(at: selectedVideo) { url, eren in
                guard let url = url else{return}
                DispatchQueue.main.async {
                    self.goToCutomizeVideo(videoUrl: url)
                }
                
            }
        }
        print("sdkjfbkjsbdkj",selectedVideo)
    }
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        print("skfhbwjhfbdjhbf")
        picker.dismiss(animated: true) {
            self.tabBarController?.selectedIndex = 0
        }
    }
}
//MARK: - Recording Delegate
extension AddMediaVC:RecordingDelegate{
    func finishRecording(_ videoURL: URL?, _ err: Error?) {
        print("recordedvideourl",videoURL)
        do {
            let resources = try videoURL?.resourceValues(forKeys:[.fileSizeKey])
            let fileSize = resources?.fileSize!
            let bcf = ByteCountFormatter()
                 bcf.allowedUnits = [.useMB] // optional: restricts the units to MB only
                 bcf.countStyle = .file
            let string = bcf.string(fromByteCount: Int64(fileSize!))
            print ("RECORDEDVIDEOSIZE\(string)")
        } catch {
            print("Error: \(error)")
        }
        if let audio = selectedAudio{
            self.pleaseWait()
            vidManager.downloadAudio(audioUrl: audio.audioUrl) { downloadedAudio in
                guard let vidUrl = videoURL else{
                    AlertView().showAlert(message: "NIL Video Url", delegate: self, pop: false)
                    return
                }
                self.vidManager.editVideo(videoURL: vidUrl, audioUrl: downloadedAudio) { outputUrl in
                    self.clearAllNotice()
                    self.goToCutomizeVideo(videoUrl: outputUrl,audioDetails: audio)
                }
            }
        }else{
            guard let videoURL = videoURL else{
                AlertView().showAlert(message: "Nil Video Url", delegate: self, pop: false)
                return
            }
            goToCutomizeVideo(videoUrl: videoURL)
        }
    }
}

extension AddMediaVC:CustomVideoPostedDelegate{
    func videoPosted(status: UploadStatus) {
        self.dismiss(animated: true){
            self.delegate?.videoPosted(status: status)
        }
    }
}
func encodeVideo(at videoURL: URL, completionHandler: ((URL?, Error?) -> Void)?)  {
    let avAsset = AVURLAsset(url: videoURL, options: nil)
        
    let startDate = Date()
    //Create Export session
    guard let exportSession = AVAssetExportSession(asset: avAsset, presetName: AVAssetExportPresetPassthrough) else {
        completionHandler?(nil, nil)
        return
    }
        
    //Creating temp path to save the converted video
    let documentsDirectory = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask)[0] as URL
    let filePath = documentsDirectory.appendingPathComponent("rendered-Video.mp4")
        
    //Check if the file already exists then remove the previous file
    if FileManager.default.fileExists(atPath: filePath.path) {
        do {
            try FileManager.default.removeItem(at: filePath)
        } catch {
            completionHandler?(nil, error)
        }
    }
        
    exportSession.outputURL = filePath
    exportSession.outputFileType = AVFileType.mp4
    exportSession.shouldOptimizeForNetworkUse = true
    let start = CMTimeMakeWithSeconds(0.0, preferredTimescale: 0)
    let range = CMTimeRangeMake(start: start, duration: avAsset.duration)
    exportSession.timeRange = range
        
    exportSession.exportAsynchronously(completionHandler: {() -> Void in
        switch exportSession.status {
        case .failed:
            print(exportSession.error ?? "NO ERROR")
            completionHandler?(nil, exportSession.error)
        case .cancelled:
            print("Export canceled")
            completionHandler?(nil, nil)
        case .completed:
            //Video conversion finished
            let endDate = Date()
                
            let time = endDate.timeIntervalSince(startDate)
            print(time)
            print("Successful!")
            print(exportSession.outputURL ?? "NO OUTPUT URL")
            completionHandler?(exportSession.outputURL, nil)
                
            default: break
        }
            
    })
}
