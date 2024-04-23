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
    @IBOutlet weak var followerCount: UILabel!
    @IBOutlet weak var postCount: UILabel!
    
    @IBOutlet weak var verifiedUserIcon: UIImageView!
    @IBOutlet weak var shortBioLbl: UILabel!
    @IBOutlet weak var messageBtnLbl: UILabel!
    @IBOutlet weak var followingBtnLbl: UILabel!
    @IBOutlet weak var userNameLbl: UILabel!
    @IBOutlet weak var profileImg: UIImageView!
    @IBOutlet weak var btnStack: UIStackView!
    
    @IBOutlet weak var aboutUsLbl: UILabel!
    @IBOutlet weak var donationLbl: UILabel!
    
   //separator views
    @IBOutlet weak var postSepView: UIView!
    @IBOutlet weak var aboutUsSepView: UIView!
    @IBOutlet weak var donationSepView: UIView!
    @IBOutlet weak var followerSepView: UIView!
    @IBOutlet weak var followingSepView: UIView!
    //=======
    @IBOutlet weak var coverImg: UIImageView!
    
    var userDetails = UserProfileData()
    var delegate: UserHeaderReusableViewProtocol?
    var btnDelegate:VisitProfileBtnDelegate?
    
    
    static var identifier = "VisitVCHeader"
    override func awakeFromNib() {
        super.awakeFromNib()
        //postLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        //followerLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        //followingLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        userNameLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        totalDonatedLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
        shortBioLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        //postCount.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
        //followerCount.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
        //followingCount.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
        followingBtnLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        messageBtnLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        
        //aboutUsLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        //donationLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        // Initialization code
        
//        postSepView.isHidden = false
//        aboutUsSepView.isHidden = true
//        donationSepView.isHidden = true
//        followerSepView.isHidden = true
//        followingSepView.isHidden = true
    }
    
    
    func setData(data:UserProfileData,isAccountDeleted:Bool){
        self.userDetails = data
//        if Int(data.followersCount) ?? 0 > 1{
//            //followerLbl.text = "Followers (\(data.followersCount))"
//            //btnFollow.setTitle("Followers (\(data.followersCount))", for: .normal)
//        }else{
//            //followerLbl.text = "Follower (\(data.followersCount))"
//            //btnFollow.setTitle("Follower (\(data.followersCount))", for: .normal)
//        }
//        if Int(data.videoCount) ?? 0 > 1{
//            postLbl.text = "Posts (\(data.videoCount))"
//        }else{
//            postLbl.text = "Post (\(data.videoCount))"
//        }//
        
        //self.followingLbl.text = "Following (\(data.followingCount))"
        if data.id == AuthManager.currentUser.id || isAccountDeleted{
            btnStack.isHidden = true
        }else{
            btnStack.isHidden = false
        }
        self.profileImg.layer.cornerRadius = self.profileImg.frame.height / 2
        self.profileImg.loadImgForProfile(url: data.profileImage)
        self.coverImg.loadImgForCover(url: data.coverImage)
        
        if data.userName != ""{
            self.userNameLbl.text = "@\(data.userName)"
        }
        self.shortBioLbl.text = data.shortBio
        self.verifiedUserIcon.isHidden = !data.isVerified
        self.coverImg.loadImgForCover(url: data.coverImage)
        //self.followerCount.text = "(\(data.followersCount))"
        //self.followingCount.text = "(\(data.followingCount))"
        //self.postCount.text = "(\(data.videoCount))"
        setAmountLbl(withAmount: data.donatedAmount)
    }
    func setAmountLbl(withAmount amt:String){
        let text = "Total Supported   ($\(amt))"
        let underlineAttriString = NSMutableAttributedString(string: text)
        let range1 = (text as NSString).range(of: "($\(amt))")
             
             underlineAttriString.addAttribute(NSAttributedString.Key.font, value: AppFont.FontName.bold.getFont(size: AppFont.pX12), range: range1)
        totalDonatedLbl.attributedText = underlineAttriString
    }
    
    @IBAction func followingBtn(_ sender: Any) {
        DataManager.followUnfollowUser(param: ["followerTo":userDetails.id]) { error in
            print(error)
        } completion: {
            self.userDetails.follow = !self.userDetails.follow
            if self.userDetails.follow{
             var count = Int(self.userDetails.followersCount) ?? 0
                count = count + 1
                self.userDetails.followersCount = "\(count)"
                
                //self.followerLbl.text = "Followers \(count)"
                if count > 1{
                    self.followerLbl.text = "Followers (\(count))"
                }else{
                    self.followerLbl.text = "Follower (\(count))"
                }
                //self.followerCount.text = "\(count)"
                self.followingBtnLbl.text = "Unfollow"
            }else{
                var count = Int(self.userDetails.followersCount) ?? 1
                   count = count - 1
                
                self.userDetails.followersCount = "\(count)"
                
                self.followingBtnLbl.text = "Follow"
                
                if count > 1{
                    self.followerLbl.text = "Followers (\(count))"
                }else{
                    self.followerLbl.text = "Follower (\(count))"
                }
                //self.followerCount.text = "\(count)"
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
