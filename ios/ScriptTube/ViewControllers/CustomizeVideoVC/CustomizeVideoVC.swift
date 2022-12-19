//
//  CustomizeVideoVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 28/11/22.
//

import UIKit
import AVFoundation

class CustomizeVideoVC: UIViewController {
    @IBOutlet weak var audioNameLbl: UILabel!
    @IBOutlet weak var musicImg: UIImageView!
    @IBOutlet weak var videoContainer: AVPlayerView!
    var url: URL!
    var videoPlayer : AVPlayer!
    var playerLayer: AVPlayerLayer!
    var selectedAudioId: String = ""
    var finalVideo:URL!
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.finalVideo = url
        // Do any additional setup after loading the view.
    }
    deinit{
        //videoPlayer.removeObserver(self, forKeyPath: "status", context: nil)
    }
    override func viewWillLayoutSubviews() {
        super.viewWillLayoutSubviews()
    }
    @objc func gotoAudioSelection(){
        let vc = SearchWithListVC()
        vc.videoUrl = url
        vc.selectedAudioUrl = { outputUrl,title,id in
            print("outputUrl",outputUrl)
            self.selectedAudioId = id
            self.audioNameLbl.text = title
            DispatchQueue.main.async {
                //self.removeallLayers()
                self.finalVideo = outputUrl
               // self.playVideo(withUrl: self.finalVideo)
            }
//            AudioVideoMerger().downloadAudio(audioUrl: audioUrl){ downloadedAudio in
//
//            }
//            AudioVideoMerger().editVideo(videoURL: self.url,audioUrl: audioUrl){ outputUrl in
//
//            }
            
        }
        vc.isForSelectAudio = true
        self.navigationController?.pushViewController(vc, animated: true)
    }
    func removeallLayers(){
        self.videoContainer.layer.sublayers?.forEach { $0.removeFromSuperlayer() }
    }
    func playVideo(withUrl url:URL){
        print("PLAYINGURL",url)
        let video = AVURLAsset(url: (url))
        let avPlayerItem = AVPlayerItem(asset: video)
        self.videoPlayer = AVPlayer(playerItem: avPlayerItem)
        self.playerLayer = AVPlayerLayer(player: videoPlayer)
        self.playerLayer.videoGravity = .resizeAspect
        //resizeAspectFill //
//        let avPlayer = AVPlayer(url: video)
        let castLayer = videoContainer.layer as! AVPlayerLayer
        castLayer.player = videoPlayer
//        avPlayer.play()

//        self.playerLayer.needsDisplayOnBoundsChange = true //
//        self.playerLayer.frame = self.videoContainer.bounds
//        self.playerLayer.frame.origin = self.videoContainer.frame.origin
//        self.videoContainer.layer.addSublayer(playerLayer)
        videoPlayer.play()
        NotificationCenter.default.addObserver(self,
                                               selector: #selector(self.playerItemDidReachEnd(notification:)),
                                               name: .AVPlayerItemDidPlayToEndTime,
                                               object:  self.videoPlayer.currentItem)
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        musicImg.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(gotoAudioSelection)))
        audioNameLbl.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(gotoAudioSelection)))
        
        self.tabBarController?.tabBar.isHidden = true
    }
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        playVideo(withUrl: self.finalVideo)
        //videoPlayer.play()
    }
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        if videoPlayer != nil{
            videoPlayer.pause()
        }
    }
    func gotoAddPost(withImage image:UIImage){
        let vc = AddPostVC()
        vc.videoUrl = self.finalVideo
        vc.thumbnailimage = image
        vc.selectedAudioId = self.selectedAudioId
        self.navigationController?.pushViewController(vc, animated: true)
    }
    @IBAction func cancelBtnClicked(_ sender: Any) {
        let vc = CancelEditingPopUp()
        vc.delegate = self
        vc.modalTransitionStyle = .crossDissolve
        vc.modalPresentationStyle = .overCurrentContext
        self.present(vc, animated: true)
    }
    @IBAction func nextBtnClicked(_ sender: Any) {
        let vc = SharePopUpVC()
        vc.delegate = self
        vc.modalTransitionStyle = .crossDissolve
        vc.modalPresentationStyle = .overCurrentContext
        self.present(vc, animated: true)

    }
    @objc func playerItemDidReachEnd(notification: Notification) {
        if let playerItem = notification.object as? AVPlayerItem {
            playerItem.seek(to: CMTime.zero, completionHandler: nil)
            videoPlayer.play()
        }
    }
}
extension CustomizeVideoVC:CancelEditDelegate{
    func didDismissPopUp() {
        self.navigationController?.popToRootViewController(animated: true)
    }
}
extension CustomizeVideoVC:ShareActionDelegate{
    func postVideo() {
        let asset = AVURLAsset(url: url, options: nil)
        let imgGenerator = AVAssetImageGenerator(asset: asset)
        do{
            let cgImage = try imgGenerator.copyCGImage(at: CMTimeMake(value: 0, timescale: 1), actualTime: nil)
            let uiImage = UIImage(cgImage: cgImage)
            gotoAddPost(withImage: uiImage)
            //let imageView = UIImageView(image: uiImage)
        }catch{
            print(error)
        }
    }
    
    func shareVideo() {
        //
    }
}
class AVPlayerView: UIView {
    override class var layerClass: AnyClass {
        return AVPlayerLayer.self
    }
}
