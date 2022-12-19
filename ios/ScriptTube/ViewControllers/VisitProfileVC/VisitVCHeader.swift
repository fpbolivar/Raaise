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
    var delegate: UserHeaderReusableViewProtocol?
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
        // Initialization code
    }
    func setData(data:UserProfileData){
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
        self.profileImg.layer.cornerRadius = self.profileImg.frame.height / 2
        self.profileImg.loadImgForProfile(url: data.profileImage)
        //loadImg(url:data.profileImage)
        self.userNameLbl.text = "@\(data.userName)"
        //self.amountLbl.text = "$123"
        self.shortBioLbl.text = data.shortBio
        self.verifiedUserIcon.isHidden = !data.isVerified
        self.folllowerCount.text = data.followersCount
        self.followingCount.text = data.followingCount
        self.postCount.text = data.videoCount
        setAmountLbl(withAmount: "113")
    }
    func setAmountLbl(withAmount amt:String){
        let text = "Total Donated($\(amt))"
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
    }
    @IBAction func messageBtn(_ sender: Any) {
    }
}
