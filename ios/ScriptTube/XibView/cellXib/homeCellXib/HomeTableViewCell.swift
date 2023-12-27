//
//  HomeTableViewCell.swift
//  KD Tiktok-Clone
//
//  Created by Sam Ding on 9/8/20.
//  Copyright © 2020 Kaishan. All rights reserved.
//

import UIKit
import AVFoundation

// MARK: - Protocol
protocol HomeCellNavigationDelegate: AnyObject {
    // Navigate to Profile Page
    func donationPopUp(post:Post)
    func navigateToTryAudio()
    func reportVideo(withId id:String,isReported:Bool)
    func shareVideo(withUrl url:String,id:String)
    func gotoUserProfile(withUser user:UserProfileData,isFollowing:Bool)
    func showComments(id:String,numberOfComments num :String)
    func errorOnLike(withMessage message:String)
    func onLikeVideo(post:Post,isLike:Bool)
    func goTiTryAudio(withId audio :AudioDataModel)
    func viewCountError(error:String)
    func clickedFollowBtn(forUser id: String,isFollowing: Bool)
    func gotoUserProfileOfSupporter(withUser user:DonationUserModel)
   
}

class HomeTableViewCell: UITableViewCell {
    // MARK: - UI Components
    @IBOutlet weak var viewCountLbl: UILabel!
    @IBOutlet weak var postedOnLbl: UILabel!
    @IBOutlet weak var topRewardView3: UIView!
    @IBOutlet weak var topRewardView2: UIView!
    @IBOutlet weak var topRewardView1: UIView!
    @IBOutlet weak var topRewardStack: UIStackView!
    @IBOutlet weak var topRewardedLbl: UILabel!
    @IBOutlet weak var topRewardLbl1: UILabel!
    @IBOutlet weak var topRewardLbl2: UILabel!
    @IBOutlet weak var topRewardLbl3: UILabel!
    @IBOutlet weak var topRewardPic1: UIImageView!
    @IBOutlet weak var topRewardPic2: UIImageView!
    @IBOutlet weak var topRewardPic3: UIImageView!
    @IBOutlet weak var seeMoreBtn: UIButton!
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
    @IBOutlet weak var nameLbl: UILabel!
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
    var playedOnce:Bool = false
    weak var delegate: HomeCellNavigationDelegate?
    private var url: URL!
    
    // MARK: LIfecycles
    override func prepareForReuse() {
        super.prepareForReuse()
        
        resetViewsForReuse()
    }
    // MARK: - Setup
    override func layoutSubviews() {
        super.layoutSubviews()
        
        followBtn.makeRounded(color: .clear, borderWidth: 0)
        musicBtn.makeRounded(color: .clear, borderWidth: 0)
        if let avLayer = videoPlayer{
            avLayer.frame = image_cell.frame

        }
    }
    @objc func expandCaption(){
        if captionLbl.numberOfLines == 0{
            captionLbl.numberOfLines = 1
        }else{
            captionLbl.numberOfLines = 0
        }
        seeMoreBtn.isHidden = !seeMoreBtn.isHidden
    }
    override func awakeFromNib() {
        super.awakeFromNib()
        topRewardPic1.layer.cornerRadius = 15
        topRewardPic2.layer.cornerRadius = 12.5
        topRewardPic3.layer.cornerRadius = 10
        seeMoreBtn.titleLabel?.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        playerView.contentMode = .scaleAspectFit
        profileImg.layer.cornerRadius = profileImg.frame.height / 2
        profileImg.contentMode = .scaleAspectFill
        profileImg.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(openProfile)))
        nameLbl.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(openProfile)))
        nameBtn.addTarget(self, action: #selector(openProfile), for: .touchUpInside)
        reportBtn.layer.cornerRadius = reportBtn.frame.height / 2
        musicBtn.layer.cornerRadius = 20
        selectionStyle = .none
        likeCountLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX10)
        viewCountLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX10)
        commentCountLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX10)
        shareCountLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX10)
        donationLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX10)
        followLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        musicLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX12)
        totalRaisedLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX10)
        postedOnLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
        postedOnLbl.textColor = UIColor.lightGray
        userNameLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
        nameLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        captionLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        songIcon.layer.cornerRadius = songIcon.frame.height / 2
        musicLbl.holdScrolling = false
        musicLbl.animationDelay = 0
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
    // MARK: - Update Data
    func viewProfileVideo(){
        profileImgView.isHidden = true
        donationLbl.isHidden = true
        followBtnView.isHidden = true
        musicBtn.isHidden = true
        reportBtn.isHidden = true
        
    }
    func checkFollow(){
        self.isFollowing = post?.isFollow ?? true
        if self.isFollowing{
            self.followLbl.text = "Following"
        }else{
            self.followLbl.text = "Follow"
        }
    }
    func configure(post: Post){
        self.post = post
        self.liked = post.isLiked
        print("checkLIke",post.isLiked,post.videoCaption)
        if post.isLiked{
            self.likeBtn.tintColor = .red
        }else{
            self.likeBtn.tintColor = .white
        }
        self.isFollowing = post.isFollow
        if self.isFollowing{
            self.followLbl.text = "Following"
        }else{
            self.followLbl.text = "Follow"
        }
        if !self.post!.isDonation{
            donationLbl.isHidden = true
            profileImgView.isHidden = true
            totalRaisedLbl.isHidden = true
            topRewardedLbl.isHidden = true
            topRewardStack.isHidden = true
        }
        if AuthManager.currentUser.id == self.post?.userDetails?.id{
            self.viewProfileVideo()
        }
        setTopSupporters()
        
        image_cell.loadImg(url: post.videoImage)
        nameLbl.text = post.userDetails?.name.localizedCapitalized
        userNameLbl.text = "@"+(post.userDetails?.userName ?? "")
        nameBtn.titleLabel?.font = UIFont.systemFont(ofSize: 18, weight: .semibold)
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
        print("CUTKLABEL",captionLbl.isTruncated)
        if captionLbl.isTruncated{
            captionLbl.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(expandCaption)))
            seeMoreBtn.addTarget(self, action: #selector(expandCaption), for: .touchUpInside)
            seeMoreBtn.isHidden = false
        }
        guard let url = URL(string: UserDefaultHelper.getBaseUrl()+post.videoLink) else{return}
        self.url = url
        setDate(post: post)
        viewCountLbl.text = Int(post.videoViewCount)?.shorten()
        
    }
    func setDate(post:Post){
        let string = post.createdAt

        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US_POSIX")
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        guard let date = dateFormatter.date(from: post.createdAt) else{
        
            return
        }
        dateFormatter.dateFormat = "EEEE d, yyyy"
        let dateString = dateFormatter.string(from: date)
        print("sdsadcsasx",dateString)
        postedOnLbl.text = "Posted on \(dateString)"
    }
    func setTopSupporters(){
        guard let supportersList = self.post?.topSupportersList else{return}
        switch supportersList.count{
        case 0:
            print("asd")
            topRewardStack.isHidden = true
            topRewardedLbl.isHidden = true
            break
        case 1:
            print("asd1")
            topRewardPic1.loadImgForProfile(url: supportersList[0].profileImage)
            topRewardLbl1.text = "@"+supportersList[0].name
            topRewardView2.isHidden = true
            topRewardView3.isHidden = true
            break
        case 2:
            topRewardPic1.loadImgForProfile(url: supportersList[0].profileImage)
            topRewardLbl1.text = "@"+supportersList[0].name
            topRewardPic2.loadImgForProfile(url: supportersList[1].profileImage)
            topRewardLbl2.text = "@"+supportersList[1].name
            topRewardView3.isHidden = true
            print("asd2")
            break
        case 3:
            topRewardPic1.loadImgForProfile(url: supportersList[0].profileImage)
            topRewardLbl1.text = "@"+supportersList[0].name
            topRewardPic2.loadImgForProfile(url: supportersList[1].profileImage)
            topRewardLbl2.text = "@"+supportersList[1].name
            topRewardPic3.loadImgForProfile(url: supportersList[2].profileImage)
            topRewardLbl3.text = "@"+supportersList[2].name
            print("asd3")
            break
        default:
            print("asd4")
            break
        }
        
    }
    // MARK: - Video Play/Pause methods
    func replay(){
        if !isPlaying {
            playerView.replay()
            play()
            videoPlayer?.player?.play()
        }
    }
    
    func play() {
        playerView.play(for: self.url)
        self.pauseImgView.alpha = 0
        print("VIEWCHECKK",self.post?.isViewed,self.playedOnce,playerView.bufferProgress)
       
        playerView.playToEndTime = {
            guard let alreadyViewed = self.post?.isViewed else {return}
            if !alreadyViewed  {
                print("ONEVIEW")
                self.playedOnce = true
                print("VIEWCHECKK",self.post?.isViewed)
                self.post?.isViewed = true
                self.countViewApi()
            }else{
                self.playedOnce = true
                print("MULTIPLEVIEW")
            }
        }
        if !isPlaying {
            musicLbl.holdScrolling = false
            isPlaying = true
        }
    }
    
    func pause(){
        playerView.pause(reason: .hidden)
        if isPlaying {
            musicLbl.holdScrolling = true
            isPlaying = false
        }
    }
    func pauseWithoutHide(){
        playerView.pause(reason: .userInteraction)
        if isPlaying {
            musicLbl.holdScrolling = true
            isPlaying = false
        }
    }
    @objc func openProfile(){
        
        delegate?.gotoUserProfile(withUser:post?.userDetails ?? UserProfileData(), isFollowing: post?.isFollow ?? false)
    }
    @objc func navigatetotryaudio(){
        delegate?.goTiTryAudio(withId: post?.audioDetails ?? AudioDataModel())
        print("CURRENTPOSTAUDIOID",post?.audioDetails?.id)
    }
    @objc func handlePause(){
        if isPlaying {
            // Pause video and show pause sign
            UIView.animate(withDuration: 0.075, delay: 0, options: .curveEaseIn, animations: { [weak self] in
                guard let self = self else { return }
                self.pauseImgView.alpha = 1.0
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
        donationLbl.isHidden = false
        profileImgView.isHidden = false
        pauseImgView.alpha = 0
        musicStack.isHidden = false
        profileImgView.isHidden = false
        donationLbl.isHidden = false
        followBtnView.isHidden = false
        musicBtn.isHidden = true
        reportBtn.isHidden = false
        totalRaisedLbl.isHidden = false
        seeMoreBtn.isHidden = true
        topRewardView1.isHidden = false
        topRewardView2.isHidden = false
        topRewardView3.isHidden = false
        topRewardedLbl.isHidden = false
        topRewardStack.isHidden = false
    }
    func hideMusic(){
        musicStack.isHidden = true
    }
    // MARK: - Count Video Views
    func countViewApi(){
        guard let id = self.post?.id else{
            return
        }
        let param = ["videoId":id]
        let dispatchQueue = DispatchQueue(label: "QueueIdentification", qos: .background)
        dispatchQueue.async{
            AuthManager.videoViewApi(param: param) { error in
                self.delegate?.viewCountError(error: error)
            } completion: {
                self.post?.isViewed = true
                print("VIEWCOUNTSUCCESS",self.post?.isViewed)
                
            }
        }
    }
    // MARK: - Actions
    @IBAction func topRewardedBtnClicked(_ sender: Any) {
        guard let post = self.post else{return}
        let sender = sender as! UIButton
        switch sender.tag{
        case 0:
            print("0")
            delegate?.gotoUserProfileOfSupporter(withUser: post.topSupportersList[0])
            break;
        case 1:
            print("1")
            delegate?.gotoUserProfileOfSupporter(withUser: post.topSupportersList[1])
            break;
        case 2:
            print("2")
            delegate?.gotoUserProfileOfSupporter(withUser: post.topSupportersList[2])
            break;
        default:
            print("3")
            break;
        }
    }
    
    @IBAction func reportClicked(_ sender: Any) {
        guard let post = self.post else{return}
        
        delegate?.reportVideo(withId: post.id,isReported: post.isReported)
        
    }
    
    // Like Video Actions
    @IBAction func like(_ sender: Any) {
        let param = ["slug":post?.slug ?? ""]
        DataManager.likeUnlikeVideoApi(param: param) { errorMessage in
            self.delegate?.errorOnLike(withMessage: errorMessage)
            
            
        } completion: { likeCount in
            DispatchQueue.main.async {
                print("checkLIke2",self.post?.isLiked)
                self.post?.videoLikeCount = "\(likeCount)"
                self.likeCountLbl.text = likeCount.shorten()
                self.likeVideo()
                guard let post = self.post else{return}
                self.delegate?.onLikeVideo(post:post,isLike: post.isLiked)
                //self.post?.videoCaption = "CHANGE AFTER LIKE"
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
                self.delegate?.clickedFollowBtn(forUser: (self.post?.userDetails?.id)!, isFollowing: self.isFollowing)
                if self.isFollowing{
                    self.followLbl.text = "Following"
                }else{
                    self.followLbl.text = "Follow"
                }
            }
        }
    }
    @objc func likeVideo(){
        if !self.post!.isLiked {
            liked = true
            self.post?.isLiked = true
            likeBtn.tintColor = .red
        }else{
            liked = false
            self.post?.isLiked = false
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
    }
    
    @IBAction func share(_ sender: Any) {
        guard let post = self.post else{return}
        delegate?.shareVideo(withUrl: UserDefaultHelper.getBaseUrl()+post.videoLink, id: post.id)
    }
    
    @objc func donationPopUp(){
        print("pOPuO")
        guard let post = post else { return }
        delegate?.donationPopUp(post: post)
    }
    
}
//Label Truncation Extension
extension UILabel {
    var isTruncated: Bool {

        guard let labelText = text else {
            return false
        }
        print("TEXT",labelText)
        let labelTextSize = (labelText as NSString).boundingRect(
            with: CGSize(width: frame.size.width, height: .greatestFiniteMagnitude),
            options: .usesLineFragmentOrigin,
            attributes: [.font: font],
            context: nil).size

        return labelTextSize.height > bounds.size.height
    }
}
