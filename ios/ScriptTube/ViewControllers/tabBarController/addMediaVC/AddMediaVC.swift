//
//  AddMediaVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/21/22.
//

import UIKit
import Photos

class AddMediaVC: UIViewController {
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
    let imagePickerController = UIImagePickerController()
    override func viewDidLoad() {
        super.viewDidLoad()
        hideNavbar()
        buttonView.addGestureRecognizer(UILongPressGestureRecognizer(target: self, action: #selector(recordVideo)))
        galleryView.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(openGallery)))
        
//        PHPhotoLibrary.requestAuthorization(for: .readWrite) { [weak self] status in
//
//                    // Handle restricted or denied state
//                    if status == .restricted || status == .denied
//                    {
//                        print("Status: Restricted or Denied")
//                    }
//
//                    // Handle limited state
//                    if status == .limited
//                    {
//                        self?.fetchVideos()
//                        print("Status: Limited")
//                    }
//
//                    // Handle authorized state
//                    if status == .authorized
//                    {
//                        self?.fetchVideos()
//                        print("Status: Full access")
//                    }
//                }
        //self.tabBarController?.delegate = self
        // Do any additional setup after loading the view.
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
        
        present(imagePickerController, animated: true, completion: nil)
    }
    func setTimeLabelValue(withValue seconds:Int){
        if seconds < 10{
            self.timeLbl.text = "00:00:0\(seconds)"
        }else{
            self.timeLbl.text = "00:00:\(seconds)"
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
        setTimeLabelValue(withValue: Int(second))
        //progress.makeRounded(color: UIColor(named: "Red")!, borderWidth: 3)
        //self.tabBarController?.tabBar.isHidden = true
        //self.tabBarController?.tabBar.isTranslucent = false
        
    }
    override func viewDidAppear(_ animated: Bool) {
       
    }
    @IBAction func closeBtnClicked(_ sender: Any) {
        self.dismiss(animated: true)
    }
    
    @objc func calculateSeconds() {

        let maxDuration = CGFloat(30) // Max duration of the recordButton
        
        second = second + (CGFloat(0.05) / maxDuration)
        progress.setProgress(second)
        print("SECONDS",second * 30)
        setTimeLabelValue(withValue: Int(second * 30))
        if second >= 1 {
            cameraManager.stopRecording()
            timer.invalidate()
        }
    }
    @objc func recordVideo(sender:UILongPressGestureRecognizer){
        let location = sender.location(in: self.view)
        switch sender.state {
        case .began:
            cameraManager.startRecording()
            timer = Timer()
            self.timer = Timer.scheduledTimer(timeInterval: 0.05, target: self, selector: #selector(calculateSeconds), userInfo: nil, repeats: true)

            //timer = Timer.scheduledTimer(timeInterval: 1, target: self, selector: #selector(calculateSeconds), userInfo: nil, repeats: true)
        case .changed:
//            recordView.locationChanged(location: location)
            print("ZOOOM")
        case .cancelled, .ended:
            timer.invalidate()
            cameraManager.stopRecording()
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
                    //self.alertCameraAccessNeeded()
                }
            })
            cameraManager.askForMicrophoneAccess({ [weak self] access in
                guard let self = self else { return }
                if access {
                    //self.permissionView.microphoneAccessPermitted()
                    if (self.cameraManager.cameraAndAudioAccessPermitted) { self.setUpSession() }
                } else {
                    //self.alertCameraAccessNeeded()
                }
            })
        }
    }
    func setUpSession(){
//        setupRecognizers()
//        permissionView.removeFromSuperview()
        cameraManager.delegate = self
        //previewView.layer.cornerRadius = cornerRadius
        cameraManager.addPreviewLayerToView(view: self.previewView)
        //view.sendSubviewToBack(previewView)
        
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
    
}
extension AddMediaVC: UIImagePickerControllerDelegate,UINavigationControllerDelegate{
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        guard let selectedVideo:URL = (info[UIImagePickerController.InfoKey.mediaURL] as? URL) else{return}
        picker.dismiss(animated: true) {
            let vc = CustomizeVideoVC()
            vc.url = selectedVideo
            
            self.navigationController?.pushViewController(vc, animated: true)
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
extension AddMediaVC:RecordingDelegate{
    func finishRecording(_ videoURL: URL?, _ err: Error?) {
        print("recordedvideourl",videoURL)
        //let pvc = self.presentingViewController
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
        let vc = CustomizeVideoVC()
        vc.url = videoURL
        self.navigationController?.pushViewController(vc, animated: false)
        //pvc?.present(vc, animated: true)
        
    }
}

