//
//  TryAudioVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 23/11/22.
//

import UIKit
import AVFoundation

class TryAudioVC: BaseControllerVC {

    @IBOutlet weak var playBtn: UIButton!
    var audioId = ""
    var audioData = AudioDataModel()
    var userVideoData: [String] = []
    var userVideos = [Post]()
    @IBOutlet weak var truAudioLbl: UILabel!
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var saveAudioLbl: UILabel!
    @IBOutlet weak var timeLbl: UILabel!
    @IBOutlet weak var userNameLbl: UILabel!
    @IBOutlet weak var audioNameLbl: UILabel!
    @IBOutlet weak var profileImg: UIImageView!
    @IBOutlet weak var slider: CustomSlider!
    let vid = AudioVideoMerger()
    var player: AVPlayer?
    var audioPlayer : AVAudioPlayer?
    var asset : AVURLAsset!
    var isPlaying = false
    var notificationObserver = NSObject()
    override func viewDidLoad() {
        super.viewDidLoad()
        hideNavbar()
        if(!(Constant.check_Internet?.isReachable)!){
            AlertView().showInternetErrorAlert(delegate: self)
            return
        }
        getAudioDetails(){
            self.setupCollectionView()
            
        }
        setup()
        vid.onprogress = { progress in
            print(progress)
        }
        vid.downloadAudio(audioUrl: audioData.audioUrl) { audioUrl in
            self.asset = AVURLAsset(url: audioUrl)
            self.setTime()
            //let playerItem = AVPlayerItem(url: audioUrl as URL)
            self.player = AVPlayer(asset: self.asset)
            self.player?.addPeriodicTimeObserver(forInterval: CMTime(seconds: 1, preferredTimescale: CMTimeScale(NSEC_PER_SEC)), queue: DispatchQueue.main, using: { [weak self] (time) in
                guard self?.slider.isTracking == false else { return }
                self?.slider.value = Float(CMTimeGetSeconds(time))
            })
            self.player?.automaticallyWaitsToMinimizeStalling = false
            
        }
        addNavBar(headingText:"",redText:"",type: .smallNavBarOnlyBack)
        // Do any additional setup after loading the view.
    }
    func loopVideo(videoPlayer:AVPlayer){
        self.notificationObserver = NotificationCenter.default.addObserver(forName: NSNotification.Name.AVPlayerItemDidPlayToEndTime, object: nil, queue: nil){
            [weak videoPlayer] notification in
            videoPlayer?.seek(to: CMTime.zero)
            self.slider.value = 0
            self.playBtn.setImage(UIImage(systemName: "play.fill"), for: .normal)
        } as! NSObject
    }
    deinit{
        NotificationCenter.default.removeObserver(self.notificationObserver)
    }
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        player?.pause()
        NotificationCenter.default.removeObserver(self.notificationObserver)
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.tabBarController?.tabBar.isHidden = true
    }
    //MARK: - Setup
    func setTime(){
        let duration = asset.duration.seconds
        var time: String
        if duration > 3600 {
            time = String(format:"%dh %dm %ds",
                Int(duration/3600),
                Int((duration/60).truncatingRemainder(dividingBy: 60)),
                Int(duration.truncatingRemainder(dividingBy: 60)))
        } else {
            if Int(duration.truncatingRemainder(dividingBy: 60)) > 9{
                time = String(format:"0%d:%d",
                    Int((duration/60).truncatingRemainder(dividingBy: 60)),
                    Int(duration.truncatingRemainder(dividingBy: 60)))
            }else{
                time = String(format:"0%d:0%d",
                    Int((duration/60).truncatingRemainder(dividingBy: 60)),
                    Int(duration.truncatingRemainder(dividingBy: 60)))
            }
        }
        self.timeLbl.text = "\(time)"
        slider.value = 0
        slider.maximumValue = Float(duration)
    }
    func setup(){
        truAudioLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX12)
        slider.setThumbImage(UIImage(named: "slider_thumb"), for: .normal)
        slider.setThumbImage(UIImage(named: "slider_thumb"), for: .highlighted)
        saveAudioLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX18)
        audioNameLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        userNameLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
        timeLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
        profileImg.layer.cornerRadius = profileImg.frame.height / 2
        self.audioNameLbl.text = self.audioData.songName.localizedCapitalized
        self.userNameLbl.text = self.audioData.artistName.localizedCapitalized
        self.profileImg.loadImg(url: self.audioData.thumbnail)
    }
    func setupCollectionView(){
        DispatchQueue.main.async {
            self.collectionView.register(UINib(nibName: ProfileVideoItemCell.identifier, bundle: nil), forCellWithReuseIdentifier: ProfileVideoItemCell.identifier)
            self.collectionView.dataSource = self
            self.collectionView.delegate = self
            self.collectionView.reloadData()
        }
    }
    //MARK: - Actions
    @IBAction func sliderMoved(_ sender: CustomSlider) {
        print(sender.value)
        
        self.player?.seek(to: CMTimeMakeWithSeconds(Float64(sender.value),preferredTimescale: player?.currentItem!.duration.timescale ?? 1)) { [weak self](state) in
        }
    }
    @IBAction func tryAudioBtnClicked(_ sender: Any) {

    }
    
    @IBAction func saveAudioBtnClicked(_ sender: Any) {
        let vc =  AddMediaVC()
        vc.selectedAudio = self.audioData
        let navigationController = UINavigationController(rootViewController: vc)
        navigationController.modalPresentationStyle = .overFullScreen
        self.present(navigationController, animated: true, completion: nil)
        player?.pause()
        isPlaying = false
        playBtn.setImage(UIImage(systemName: "play.fill"), for: .normal)
    }
    @IBAction func playBtnClicked(_ sender: Any) {
        managePlayPauseAudio()
    }
    func managePlayPauseAudio(){
        if isPlaying{
            player?.pause()
            isPlaying = false
            playBtn.setImage(UIImage(systemName: "play.fill"), for: .normal)
        }else{
            player?.play()
            playBtn.setImage(UIImage(systemName: "pause.fill"), for: .normal)
            isPlaying = true
            loopVideo(videoPlayer: self.player ?? AVPlayer())
        }
    }
    //MARK: - Api Methods
    func getAudioDetails(completion:@escaping()->Void){
        let param = ["audioId":self.audioData.id]
        DataManager.getVideoByAudio(delegate: self, param: param) { json in
            json["videos"].forEach { (message,data) in
                print("VIDEOIMAHGE",data["videoImage"].stringValue)
                self.userVideoData.append(data["videoImage"].stringValue)
                self.userVideos.append(Post(data: data))
            }
            print(self.userVideoData.count,self.userVideos.count)
            completion()
        }
    }
}
//MARK: - Collection View Delegate
extension TryAudioVC:UICollectionViewDelegateFlowLayout,UICollectionViewDelegate,UICollectionViewDataSource{
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.userVideoData.count
    }
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: ProfileVideoItemCell.identifier, for: indexPath) as! ProfileVideoItemCell
        cell.updateCellData(data: self.userVideos[indexPath.row])
        return cell
    }
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let vc = ViewVideoVC()
        vc.data = self.userVideos
        vc.visitingProfile = false
        vc.selectedRow = indexPath.row
        self.navigationController?.pushViewController(vc, animated: true)
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {

        let wFrame = collectionView.frame.width
        let itemWidth = (wFrame/3) - 2
        let itemHeight =  itemWidth * 1.3
        return CGSize.init(width: itemWidth, height: itemHeight)
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return 2
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
        return 2
    }
    
}
class CustomSlider:UISlider{
    override func trackRect(forBounds bounds: CGRect) -> CGRect {
        let point = CGPoint(x: bounds.minX, y: bounds.midY)
        return CGRect(origin: point, size: CGSize(width: bounds.width, height: 2))
    }
}
