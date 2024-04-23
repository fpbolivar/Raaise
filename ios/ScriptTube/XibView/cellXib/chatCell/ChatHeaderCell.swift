//
//  ChatHeaderCell.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 24/11/22.
//

import UIKit

class ChatHeaderCell: UITableViewCell {
    @IBOutlet weak var detailsLbl: UILabel!
    
    @IBOutlet weak var totalDonationLbl: UILabel!
    @IBOutlet weak var nameLbl: UILabel!
    @IBOutlet weak var profileImage: UIImageView!
    @IBOutlet weak var verifiedImg: UIImageView!
    static var identifier = "ChatHeaderCell"
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        nameLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        totalDonationLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
        
        detailsLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    //MARK: - Update Data
    func updateCell(data:UserProfileData){
        detailsLbl.text = data.shortBio
        profileImage.loadImgForProfile(url: data.profileImage)
        setAmountLbl(withAmount: data.donatedAmount)
        print("donationamt",data.donatedAmount)
        nameLbl.text = "@\(data.userName)"
        if data.isVerified{
            verifiedImg.isHidden = false
        }else{
            verifiedImg.isHidden = true
        }
    }
    //Date:: 08, Mar 2024 - here we set only Amount
//    func setAmountLbl_old(withAmount amt:String){
//        let text = "Total Supported   ($\(amt))"
//        let underlineAttriString = NSMutableAttributedString(string: text)
//        let range1 = (text as NSString).range(of: "($\(amt))")
//             
//             underlineAttriString.addAttribute(NSAttributedString.Key.font, value: AppFont.FontName.bold.getFont(size: AppFont.pX12), range: range1)
//        totalDonationLbl.attributedText = underlineAttriString
//    }
    
    //Date:: 08, Mar 2024 - here we set Amount with coin image
    func setAmountLbl(withAmount amt: String) {
        let text = "Total Supported   $\(amt)"
        
        if let range1 = text.range(of: "$\(amt)") {
            
            let attributedString = NSMutableAttributedString(string: text)
            
            attributedString.addAttribute(.font, value: AppFont.FontName.regular.getFont(size: AppFont.pX12), range: NSRange(range1, in: text))
            
            let imageAttachment = NSTextAttachment()
            imageAttachment.image = UIImage(named: "ic_coin") // Replace with your image name
            imageAttachment.bounds = CGRect(x: 0, y: -5, width: 17, height: 17) // Adjust the bounds as needed
            //imageAttachment.ali
            
            let imageString = NSAttributedString(attachment: imageAttachment)
            
            attributedString.insert(imageString, at: 16)
            
            // Set the final attributed text to the label
            totalDonationLbl.attributedText = attributedString
        }
    }
}
