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
        selectionStyle = .none
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    func updateCell(){
        dateLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
        setMessage(name: "John Doe", message: "Liked Your Reel")
    }
    func setMessage(name:String,message:String){
        var attrName = NSAttributedString(string: name,attributes: [.font: AppFont.FontName.bold.getFont(size: AppFont.pX14)])
        var attrMsg = NSAttributedString(string: name,attributes: [.font: AppFont.FontName.regular.getFont(size: AppFont.pX14)])
        let text = NSMutableAttributedString()
        text.append(attrName)
        text.append(NSMutableAttributedString(string: " "))
        text.append(attrMsg)
        self.messageLbl.attributedText = text
    }
    
}
