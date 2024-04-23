//
//  UserHeaderReusableView.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/21/22.
//

import UIKit

class UserHeaderReusableView:
    UICollectionReusableView {
    @IBOutlet weak var followingLbl: UILabel!
    @IBOutlet weak var followerLbl: UILabel!
    @IBOutlet weak var postLbl: UILabel!
    @IBOutlet weak var totalDonatedLbl: UILabel!
    @IBOutlet weak var followingCount: UILabel!
    @IBOutlet weak var folllowerCount: UILabel!
    @IBOutlet weak var postCount: UILabel!
    @IBOutlet weak var verifiedUserIcon: UIImageView!
    @IBOutlet weak var shortBioLbl: UILabel!
    @IBOutlet weak var userNameLbl: UILabel!
    @IBOutlet weak var profileImg: UIImageView!
    @IBOutlet weak var userCoverImg: UIImageView!
    
    @IBOutlet weak var profileImgView: UIView!
    
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
    static var identifier = "UserHeaderReusableView"
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
        //self.profileImg.layer.cornerRadius = self.profileImg.frame.height / 2
        
        self.profileImg.layer.borderWidth = 3
        self.profileImg.layer.borderColor = UIColor(named: "bgColor")?.cgColor
        self.profileImg.layer.cornerRadius =  self.profileImg.frame.height / 2
        self.profileImg.layer.masksToBounds = true
        
        
        
        self.profileImg.loadImgForProfile(url: data.profileImage)
        self.userCoverImg.loadImgForCover(url: data.coverImage)
        self.userNameLbl.text = "@\(data.userName)"
        self.shortBioLbl.text = data.shortBio
        self.verifiedUserIcon.isHidden = !data.isVerified
        self.folllowerCount.text = data.followersCount
        self.followingCount.text = data.followingCount
        self.postCount.text = data.videoCount
        setAmountLbl(withAmount: data.donatedAmount)
    }
//    func setAmountLbl_old(withAmount amt:String){
//        let text = "Total Supported  ($\(amt))"
//        let underlineAttriString = NSMutableAttributedString(string: text)
//        let range1 = (text as NSString).range(of: "($\(amt))")
//             underlineAttriString.addAttribute(NSAttributedString.Key.font, value: AppFont.FontName.bold.getFont(size: AppFont.pX12), range: range1)
//        totalDonatedLbl.attributedText = underlineAttriString
//    }
    func setAmountLbl(withAmount amt: String) {
        let text = "Total Supported ($\(amt))"
        
        // Find the range of the amount in parentheses
        if let range1 = text.range(of: "($\(amt))") {
            // The range1 is now a non-optional Range<String.Index>
            
            // Create an attributed string with the whole text
            let attributedString = NSMutableAttributedString(string: text)
            
            // Apply bold font to the amount range
            attributedString.addAttribute(.font, value: AppFont.FontName.regular.getFont(size: AppFont.pX12), range: NSRange(range1, in: text))
            
            // Create an NSTextAttachment for the image
            let imageAttachment = NSTextAttachment()
            imageAttachment.image = UIImage(named: "ic_coin") // Replace with your image name
            imageAttachment.bounds = CGRect(x: 0, y: -2, width: 15, height: 15) // Adjust the bounds as needed
            
            // Create an attributed string for the image
            let imageString = NSAttributedString(attachment: imageAttachment)
            
            // Insert the image attributed string at the beginning of the total string
            attributedString.insert(imageString, at: 15)
            
            // Set the final attributed text to the label
            totalDonatedLbl.attributedText = attributedString
        }
    }
    @objc func gotoFollowing(){
        delegate?.gotoFollowersListVC(isForFollowing:true)
    }
    @objc func gotoFollower(){
        delegate?.gotoFollowersListVC(isForFollowing:false)
    }
    
}
protocol UserHeaderReusableViewProtocol{
    func gotoFollowersListVC(isForFollowing:Bool)
}
