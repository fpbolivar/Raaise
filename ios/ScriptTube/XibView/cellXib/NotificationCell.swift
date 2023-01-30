//
//  NotificationCell.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 22/11/22.
//

import UIKit

class NotificationCell: UITableViewCell {
    @IBOutlet weak var messageLbl: UILabel!
    @IBOutlet weak var dateLbl: UILabel!
    @IBOutlet weak var onlineBtnView: CardView!
    @IBOutlet weak var profileImg: UIImageView!
    static var identifier = "NotificationCell"
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        profileImg.layer.cornerRadius = profileImg.frame.height / 2
        dateLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
        selectionStyle = .none
    }
    override func prepareForReuse() {
        super.prepareForReuse()
        onlineBtnView.isHidden = true
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    func updateCell(data:NotificationDataModel){
        onlineBtnView.isHidden = data.isRead
        profileImg.loadImgForProfile(url: data.profileImage)
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US_POSIX")
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        let date = dateFormatter.date(from: data.time)
        print("date: \(date)")
        let currentDate = Date()
        dateLbl.text = currentDate.offsetFrom(date: date!)
        setMessage(name: data.name, message: data.message)
    }
    func setMessage(name:String,message:String){
        var attrName = NSAttributedString(string: name,attributes: [.font: AppFont.FontName.bold.getFont(size: AppFont.pX14)])
        var attrMsg = NSAttributedString(string: message,attributes: [.font: AppFont.FontName.regular.getFont(size: AppFont.pX14)])
        let text = NSMutableAttributedString()
        text.append(attrName)
        text.append(NSMutableAttributedString(string: " "))
        text.append(attrMsg)
        self.messageLbl.attributedText = text
    }
    
}
