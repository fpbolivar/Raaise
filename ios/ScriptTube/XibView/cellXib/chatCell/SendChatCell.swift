//
//  ReceiveChatCell.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 23/11/22.
//

import UIKit

class SendChatCell: UITableViewCell {
    static var identifier = "SendChatCell"
    @IBOutlet weak var messageView: UIView!
    
    @IBOutlet weak var messageLbl: UILabel!
    @IBOutlet weak var profileImg: UIImageView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    func updateCell(){
        
    }
    func roundCorners(){
        messageView.clipsToBounds = true
        messageView.layer.cornerRadius = 10
        messageView.layer.maskedCorners = [.layerMaxXMinYCorner,.layerMinXMaxYCorner,.layerMinXMinYCorner]
    }
    
}
