//
//  HomeTableViewCell.swift
//  KD Tiktok-Clone
//
//  Created by Sam Ding on 9/8/20.
//  Copyright © 2020 Kaishan. All rights reserved.
//

import UIKit
import AVFoundation


protocol HomeCellNavigationDelegate: AnyObject {
    // Navigate to Profile Page
    func donationPopUp()
    func navigateToTryAudio()
    func reportVideo(withId id:String)
    func shareVideo(withUrl  url:String)
    func gotoUserProfile(withUser user:UserProfileData,isFollowing:Bool)
    func showComments(id:String,numberOfComments num :String)
    func errorOnLike(withMessage message:String)
    func goTiTryAudio(withId audio :AudioDataModel)
}

class HomeTableViewCell: UITableViewCell {
    
    @IBOutlet weak var musicStack: UIStackView!
    @IBOutlet weak var musicNoteImg: UIImageView!
    @IBOutlet weak var playerView: VideoPlayerView!
    @IBOutlet weak var image_cell: UIImageView!
    @IBOutlet weak var profileImg: UIImageView!
    @IBOutlet weak var followBtnView: CardView!
    @IBOutlet weak var reportBtn: UIButton!
    @IBOutlet weak var donationLbl: UILabel!
    @IBOutlet weak var followLbl: UILabel!
    @IBOutlet weak var songIcon: UIImageView!
    @IBOutlet weak var totalRaisedLbl: UILabel!
    @IBOutlet weak var userNameLbl: UILabel!
    static var identifier = "HomeTableViewCell"
    // MARK: - UI Components
    @IBOutlet weak var nameLbl: UILabel!
    //var playerView = VideoPlayerView()
    @IBOutlet weak var nameBtn: UIButton!
    @IBOutlet weak var captionLbl: UILabel!
    @IBOutlet weak var musicLbl: MarqueeLabel!
    @IBOutlet weak var profileImgView: UIImageView!{
        didSet{
            profileImgView.isUserInteractionEnabled = true
            let tapGesture = UITapGestureRecognizer(target: self, action: #selector(donationPopUp))
            profileImgView.addGestureRecognizer(tapGesture)
        }
    }
    @IBOutlet weak var followBtn: UIButton!
    @IBOutlet weak var likeBtn: UIButton!
    @IBOutlet weak var likeCountLbl: UILabel!
    @IBOutlet weak var commentBtn: UIButton!
    @IBOutlet weak var commentCountLbl: UILabel!
    @IBOutlet weak var shareBtn: UIButton!
    @IBOutlet weak var musicBtn: UIButton!
    @IBOutlet weak var shareCountLbl: UILabel!
    @IBOutlet weak var pauseImgView: UIImageView!{
        didSet{
            pauseImgView.alpha = 0
        }
    }
    
    // MARK: - Variables
    private(set) var isPlaying = false
    private(set) var liked = false
    var post: Post?
    var isFollowing: Bool = false
    var videoPlayer:AVPlayerLayer?=nil
    weak var delegate: HomeCellNavigationDelegate?
    private var url: URL!
    
    // MARK: LIfecycles
    override func prepareForReuse() {
        super.prepareForReuse()
        
        resetViewsForReuse()
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        
        followBtn.makeRounded(color: .clear, borderWidth: 0)
        musicBtn.makeRounded(color: .clear, borderWidth: 0)
        if let avLayer = videoPlayer{
            avLayer.frame = image_cell.frame

        }
    }
    override func awakeFromNib() {
        super.awakeFromNib()
        
        playerView.contentMode = .scaleAspectFit
        profileImg.layer.cornerRadius = profileImg.frame.height / 2
        profileImg.contentMode = .scaleAspectFill
        profileImg.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(openProfile)))
        nameBtn.addTarget(self, action: #selector(openProfile), for: .touchUpInside)
        reportBtn.layer.cornerRadius = reportBtn.frame.height / 2
        musicBtn.layer.cornerRadius = 20
        selectionStyle = .none
        likeCountLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX10)
        commentCountLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX10)
        shareCountLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX10)
        donationLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX10)
        followLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        musicLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX12)
        totalRaisedLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX10)
        userNameLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
        nameLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        captionLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        songIcon.layer.cornerRadius = songIcon.frame.height / 2
        musicLbl.holdScrolling = false
        musicLbl.animationDelay = 0
        //musicBtn.addTarget(self, action: #selector(navigatetotryaudio), for: .touchUpInside)
        let pauseGesture = UITapGestureRecognizer(target: self, action: #selector(handlePause))
        self.contentView.addGestureRecognizer(pauseGesture)
        
        let likeDoubleTapGesture = UITapGestureRecognizer(target: self, action: #selector(handleLikeGesture(sender:)))
        likeDoubleTapGesture.numberOfTapsRequired = 2
        self.contentView.addGestureRecognizer(likeDoubleTapGesture)
        
        pauseGesture.require(toFail: likeDoubleTapGesture)
        songIcon.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(navigatetotryaudio)))
        musicLbl.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(navigatetotryaudio)))
        musicNoteImg.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(navigatetotryaudio)))
    }
    
    func viewProfileVideo(){
        profileImgView.isHidden = true
        donationLbl.isHidden = true
        followBtnView.isHidden = true
        musicBtn.isHidden = true
        reportBtn.isHidden = true
        
    }
    func configure(post: Post){
        self.post = post
        self.liked = post.isLiked
        if self.liked{
            self.likeBtn.tintColor = .red
        }else{
            self.likeBtn.tintColor = .white
        }
        self.isFollowing = post.isFollow
        if self.isFollowing{
//            followBtnView.borderWidth = 1
//            followBtnView.backgroundColor = .clear
//            followBtnView.borderColor = UIColor.theme
            self.followLbl.text = "Following"
        }else{
//            followBtnView.borderWidth = 0
//            followBtnView.backgroundColor = UIColor.theme
            self.followLbl.text = "Follow"
        }
        image_cell.loadImg(url: post.videoImage)
        nameLbl.text = post.userDetails?.name.localizedCapitalized
        userNameLbl.text = "@"+(post.userDetails?.userName ?? "")
        //nameBtn.setTitle("@" + post.autherName, for: .normal)
        nameBtn.titleLabel?.font = UIFont.systemFont(ofSize: 18, weight: .semibold)
        //musicLbl.text = "music" + "   music   " + "music" + "   music  " + "music" + "   "// Long enough to enable scrolling
        musicLbl.text = post.audioDetails?.songName
        if post.audioDetails?.songName == ""{
            self.hideMusic()
        }
        songIcon.loadImg(url: post.audioDetails?.thumbnail ?? "")
        captionLbl.text = post.videoCaption
        likeCountLbl.text = Int(post.videoLikeCount)?.shorten()
        commentCountLbl.text = Int(post.videoCommentCount)?.shorten()
        print("LIKES",post.videoLikeCount,Int(post.videoLikeCount)?.shorten() ?? "0")
        shareCountLbl.text = Int(post.videoShareCount)?.shorten()
        profileImg.loadImgForProfile(url: post.userDetails?.profileImage ?? "")
        //loadImg(url: post.userDetails?.profileImage ?? "")
        if post.donationAmount != ""{
            totalRaisedLbl.text = "Total Raised: $\(post.donationAmount)"
        }else{
            totalRaisedLbl.text = "Total Raised: $0"
        }
        
        guard let url = URL(string: post.videoLink) else{return}
        self.url = url
        print("ALKMLKADMKA",url)
    }
    
  
    func replay(){
        if !isPlaying {
            playerView.replay()
            play()
            videoPlayer?.player?.play()
        }
    }
    
    func play() {
        //guard let url = URL(string: post?.videoLink ?? "") else{return}
        playerView.play(for: self.url)
//        pauseImgView.isHidden = true
        if !isPlaying {
            
//            videoPlayer?.player?.play()
            //playerView.play()
            musicLbl.holdScrolling = false
            isPlaying = true
        }
    }
    
    func pause(){
        playerView.pause(reason: .hidden)
        if isPlaying {
//            videoPlayer?.player?.pause()
//            playerView.pause()
            musicLbl.holdScrolling = true
            isPlaying = false
        }
    }
    func pauseWithoutHide(){
        playerView.pause(reason: .userInteraction)
        if isPlaying {
//            videoPlayer?.player?.pause()
//            playerView.pause()
            musicLbl.holdScrolling = true
            isPlaying = false
        }
    }
    @objc func openProfile(){
        
        delegate?.gotoUserProfile(withUser:post?.userDetails ?? UserProfileData(), isFollowing: post?.isFollow ?? false)
    }
    @objc func navigatetotryaudio(){
        //delegate?.navigateToTryAudio()
        delegate?.goTiTryAudio(withId: post?.audioDetails ?? AudioDataModel())
        print("CURRENTPOSTAUDIOID",post?.audioDetails?.id)
    }
    @objc func handlePause(){
        if isPlaying {
            // Pause video and show pause sign
            UIView.animate(withDuration: 0.075, delay: 0, options: .curveEaseIn, animations: { [weak self] in
                guard let self = self else { return }
                self.pauseImgView.alpha = 1.0
                //0.35
                self.pauseImgView.transform = CGAffineTransform.init(scaleX: 0.45, y: 0.45)
            }, completion: { [weak self] _ in
                self?.pauseWithoutHide()
                self?.videoPlayer?.player?.pause()
            })
        } else {
            // Start video and remove pause sign
            UIView.animate(withDuration: 0.075, delay: 0, options: .curveEaseInOut, animations: { [weak self] in
                guard let self = self else { return }
                self.pauseImgView.alpha = 0
            }, completion: { [weak self] _ in
                self?.play()
                self?.videoPlayer?.player?.play()
                self?.pauseImgView.transform = .identity
            })
        }
    }
    
    func resetViewsForReuse(){
        //likeBtn.tintColor = .white
        //likeVideo()
        pauseImgView.alpha = 0
    }
    func hideMusic(){
//        self.songIcon.isHidden = true
//        self.musicLbl.isHidden = true
//        musicNoteImg.isHidden = true
        musicStack.isHidden = true
    }
    
    @IBAction func reportClicked(_ sender: Any) {
        delegate?.reportVideo(withId: post?.id ?? "")
    }
    // MARK: - Actions
    // Like Video Actions
    @IBAction func like(_ sender: Any) {
        let param = ["slug":post?.slug ?? ""]
        DataManager.likeUnlikeVideoApi(param: param) { errorMessage in
            self.delegate?.errorOnLike(withMessage: errorMessage)
        } completion: { likeCount in
            DispatchQueue.main.async {
                self.likeCountLbl.text = likeCount.shorten()
                self.likeVideo()
            }
        }
    }
    @IBAction func follow(_ sender: Any) {
        let param = ["followerTo":post?.userDetails?.id ?? ""]
        DataManager.followUnfollowUser(param: param) { errorMessage in
            print("FOLLOWERROr",errorMessage)
        } completion: {
            DispatchQueue.main.async {
                self.isFollowing = !self.isFollowing
                if self.isFollowing{
        //            followBtnView.borderWidth = 1
        //            followBtnView.backgroundColor = .clear
        //            followBtnView.borderColor = UIColor.theme
                    self.followLbl.text = "Following"
                }else{
        //            followBtnView.borderWidth = 0
        //            followBtnView.backgroundColor = UIColor.theme
                    self.followLbl.text = "Follow"
                }
            }
        }
    }
    @objc func likeVideo(){
        if !liked {
            liked = true
            likeBtn.tintColor = .red
        }else{
            liked = false
            likeBtn.tintColor = .white
        }
    }
    
    // Heart Animation with random angle
    @objc func handleLikeGesture(sender: UITapGestureRecognizer){
        let location = sender.location(in: self)
        let heartView = UIImageView(image: UIImage(systemName: "heart.fill"))
        heartView.tintColor = .red
        let width : CGFloat = 110
        heartView.contentMode = .scaleAspectFit
        heartView.frame = CGRect(x: location.x - width / 2, y: location.y - width / 2, width: width, height: width)
        heartView.transform = CGAffineTransform(rotationAngle: CGFloat.random(in: -CGFloat.pi * 0.2...CGFloat.pi * 0.2))
        self.contentView.addSubview(heartView)
        UIView.animate(withDuration: 0.3, delay: 0, usingSpringWithDamping: 0.8, initialSpringVelocity: 3, options: [.curveEaseInOut], animations: {
            heartView.transform = heartView.transform.scaledBy(x: 0.85, y: 0.85)
        }, completion: { _ in
            UIView.animate(withDuration: 0.4, delay: 0.1, usingSpringWithDamping: 0.8, initialSpringVelocity: 3, options: [.curveEaseInOut], animations: {
                heartView.transform = heartView.transform.scaledBy(x: 2.3, y: 2.3)
                heartView.alpha = 0
            }, completion: { _ in
                heartView.removeFromSuperview()
            })
        })
        likeVideo()
    }
    
    @IBAction func comment(_ sender: Any) {
        delegate?.showComments(id: post?.id ?? "", numberOfComments: post?.videoCommentCount ?? "0")
//        let commentView = CommentPopUpView.instanceFromNib() as! CommentPopUpView
//        commentView.frame = CGRect(x: 0, y: ScreenSize.Height, width: ScreenSize.Width, height: ScreenSize.Height)
//        commentView.setup()
//        commentView.show()
    }
    
    @IBAction func share(_ sender: Any) {
        delegate?.shareVideo(withUrl: post?.videoLink ?? "")
    }
    
    @objc func donationPopUp(){
        print("pOPuO")
        //guard let post = post else { return }
        //let post = Post(dictionary: [:])
        delegate?.donationPopUp()
    }
    
    
    
    
}
