//
//  VisitVCHeader.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 11/12/22.
//

import UIKit

class VisitVCHeader: UICollectionReusableView {
    @IBOutlet weak var followingLbl: UILabel!
    @IBOutlet weak var followerLbl: UILabel!
    @IBOutlet weak var postLbl: UILabel!
    @IBOutlet weak var totalDonatedLbl: UILabel!
    @IBOutlet weak var followingCount: UILabel!
    @IBOutlet weak var folllowerCount: UILabel!
    @IBOutlet weak var postCount: UILabel!
    @IBOutlet weak var verifiedUserIcon: UIImageView!
    @IBOutlet weak var shortBioLbl: UILabel!
    @IBOutlet weak var messageBtnLbl: UILabel!
    @IBOutlet weak var followingBtnLbl: UILabel!
    @IBOutlet weak var userNameLbl: UILabel!
    @IBOutlet weak var profileImg: UIImageView!
    @IBOutlet weak var btnStack: UIStackView!
    var userDetails = UserProfileData()
    var delegate: UserHeaderReusableViewProtocol?
    var btnDelegate:VisitProfileBtnDelegate?
    @IBOutlet weak var followingView: UIView!{
        didSet{
            followingView.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(gotoFollowing)))
        }
    }
    @IBOutlet weak var followerView: UIView!{
        didSet{
            followerView.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(gotoFollower)))
        }
    }
static var identifier = "VisitVCHeader"
    override func awakeFromNib() {
        super.awakeFromNib()
        postLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
        followerLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
        followingLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
        userNameLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        totalDonatedLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
        shortBioLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        postCount.font = AppFont.FontName.bold.getFont(size: AppFont.pX16)
        folllowerCount.font = AppFont.FontName.bold.getFont(size: AppFont.pX16)
        followingCount.font = AppFont.FontName.bold.getFont(size: AppFont.pX16)
        followingBtnLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        messageBtnLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        // Initialization code
    }
    func setData(data:UserProfileData,isAccountDeleted:Bool){
        self.userDetails = data
        if Int(data.followersCount) ?? 0 > 1{
            followerLbl.text = "Followers"
        }else{
            followerLbl.text = "Follower"
        }
        if Int(data.videoCount) ?? 0 > 1{
            postLbl.text = "Posts"
        }else{
            postLbl.text = "Post"
        }
        if data.id == AuthManager.currentUser.id || isAccountDeleted{
            btnStack.isHidden = true
        }
        self.profileImg.layer.cornerRadius = self.profileImg.frame.height / 2
        self.profileImg.loadImgForProfile(url: data.profileImage)
        //loadImg(url:data.profileImage)
        if data.userName != ""{
            self.userNameLbl.text = "@\(data.userName)"
        }
        //self.amountLbl.text = "$123"
        self.shortBioLbl.text = data.shortBio
        self.verifiedUserIcon.isHidden = !data.isVerified
        self.folllowerCount.text = data.followersCount
        self.followingCount.text = data.followingCount
        self.postCount.text = data.videoCount
        setAmountLbl(withAmount: data.donatedAmount)
    }
    func setAmountLbl(withAmount amt:String){
        let text = "Total Supported   ($\(amt))"
        let underlineAttriString = NSMutableAttributedString(string: text)
        let range1 = (text as NSString).range(of: "($\(amt))")
             
             underlineAttriString.addAttribute(NSAttributedString.Key.font, value: AppFont.FontName.bold.getFont(size: AppFont.pX12), range: range1)
//        underlineAttriString.addAttribute(NSAttributedString.Key.foregroundColor, value: UIColor.theme, range: range1)
        totalDonatedLbl.attributedText = underlineAttriString
    }
    @objc func gotoFollowing(){
        delegate?.gotoFollowersListVC(isForFollowing: true)
    }
    @objc func gotoFollower(){
        delegate?.gotoFollowersListVC(isForFollowing: false)
    }
    
    @IBAction func followingBtn(_ sender: Any) {
        DataManager.followUnfollowUser(param: ["followerTo":userDetails.id]) { error in
            print(error)
        } completion: {
            self.userDetails.follow = !self.userDetails.follow
            if self.userDetails.follow{
                self.followingBtnLbl.text = "Unfollow"
            }else{
                self.followingBtnLbl.text = "Follow"
            }
            self.btnDelegate?.followBtnClicked(withId: self.userDetails.id,isFollowing: self.userDetails.follow)
        }
    }
    @IBAction func messageBtn(_ sender: Any) {
        self.btnDelegate?.messageBtnClicked(withId: userDetails)
    }
}
protocol VisitProfileBtnDelegate{
    func followBtnClicked(withId id:String,isFollowing:Bool)
    func messageBtnClicked(withId id:UserProfileData)
}
