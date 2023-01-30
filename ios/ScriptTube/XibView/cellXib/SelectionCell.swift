//
//  SelectionCell.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 23/11/22.
//

import UIKit
import AVFoundation

class SelectionCell: UITableViewCell {

    @IBOutlet weak var unreadMsgCountLbl: UILabel!
    @IBOutlet weak var unreadMsgView: CardView!
    @IBOutlet weak var containerView: CardView!
    @IBOutlet weak var progressBar: CircleProgress!
    @IBOutlet weak var timeLbl: UILabel!
    @IBOutlet weak var playBtn: UIButton!
    @IBOutlet weak var detailLbl: UILabel!
    @IBOutlet weak var nameLbl: UILabel!
    @IBOutlet weak var profileImg: UIImageView!
    static var identifier = "SelectionCell"
    var playAudio: ((String)->Void)?
    let vid = AudioVideoMerger()
    var player: AVPlayer?
    var asset : AVURLAsset!
    var audioData : AudioDataModel = AudioDataModel()
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        progressBar.forgroundColor = UIColor.white
        profileImg.layer.cornerRadius = profileImg.frame.height / 2
        self.nameLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        self.detailLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        self.timeLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    func play(){
        vid.downloadAudio(audioUrl: audioData.audioUrl) { audioUrl in
            self.asset = AVURLAsset(url: audioUrl)
            
            //let playerItem = AVPlayerItem(url: audioUrl as URL)
            self.player = AVPlayer(asset: self.asset)
            self.player?.play()
//                self.player?.addPeriodicTimeObserver(forInterval: CMTime(seconds: 1, preferredTimescale: CMTimeScale(NSEC_PER_SEC)), queue: DispatchQueue.main, using: { [weak self] (time) in
//                    /// Float(self?.asset.duration.seconds ?? 1)
//                })
            //AVPlayer(playerItem: playerItem)
            self.player?.automaticallyWaitsToMinimizeStalling = false
            
        }
        vid.onprogress = { progress in
            self.progressBar.isHidden = false
            self.progressBar.progress = CGFloat(progress)
            print(progress)
            if progress == 1.0{
                self.progressBar.isHidden = true
            }
        }
    }
    func pause(){
        self.player?.pause()
    }
    @IBAction func playBtnClicked(_ sender: UIButton) {
        playAudio!(audioData.audioUrl)
    }
    
    func updateCellForAudio(data:AudioDataModel){
        self.audioData = data
        self.nameLbl.text = data.songName.localizedCapitalized
        self.nameLbl.numberOfLines = 1
        print("GENRE",data.genre)
        self.detailLbl.text = "\(data.songName) | \(data.genre)".localizedCapitalized
        self.profileImg.loadImg(url: data.thumbnail)
    }
    func followerList(data:UserListDataModel){
        self.timeLbl.isHidden = true
        self.playBtn.isHidden = true
        self.detailLbl.text = (Int(data.count)?.shorten() ?? "0") + " Followers"
        self.nameLbl.text = data.name.localizedCapitalized
        self.profileImg.loadImgForProfile(url: data.image)
        //loadImg(url: data.thumbnail)
    }
    func userList(data:UserProfileData){
        self.timeLbl.isHidden = true
        self.playBtn.isHidden = true
        self.detailLbl.text = (Int(data.followersCount)?.shorten() ?? "0") + " Followers"
        self.nameLbl.text = data.name.localizedCapitalized
        self.profileImg.loadImgForProfile(url: data.profileImage)
    }
    func chatList(data:ChatChannelModel){
        self.timeLbl.isHidden = false
        self.playBtn.isHidden = true
        self.unreadMsgView.isHidden = false
        self.detailLbl.text = data.lastMessage
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US_POSIX")
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        let date = dateFormatter.date(from: data.chatTime.getDateStringFromMillisecond2())
        print("date: \(date)")
        let currentDate = Date()
        print("kkksksksksk",currentDate.offsetFrom(date: date!))
        if currentDate.offsetFrom(date: date!) == ""{
            self.timeLbl.text = "Just Now"
        }else{
            self.timeLbl.text = currentDate.shortOffsetFrom(date: date!)
        }
        //self.timeLbl.text = "\(data.chatTime.getDateStringFromMillisecond())"
        profileImg.loadImgForProfile(url: data.otherUser.profileImage)
        nameLbl.text = data.otherUser.name.localizedCapitalized
        self.nameLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        self.detailLbl.font = AppFont.FontName.regular.getFont(size: 8)
        self.timeLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
        if data.unreadMsgCount == "0"{
            unreadMsgView.isHidden = true
        }else{
            unreadMsgView.isHidden = false
            unreadMsgCountLbl.text = data.unreadMsgCount
        }
    }
    func sendList(data:ChatChannelModel){
        self.timeLbl.isHidden = true
        self.playBtn.isHidden = true
        self.unreadMsgView.isHidden = true
        self.detailLbl.text = (Int(data.otherUser.followersCount)?.shorten() ?? "0") + " Followers"
        self.timeLbl.text = "\(data.chatTime.getDateStringFromMillisecond())"
        profileImg.loadImgForProfile(url: data.otherUser.profileImage)
        nameLbl.text = data.otherUser.userName.localizedCapitalized
        self.nameLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        self.detailLbl.font = AppFont.FontName.regular.getFont(size: 10)
        self.timeLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
    }
}
