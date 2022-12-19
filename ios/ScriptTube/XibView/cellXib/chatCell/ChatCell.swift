//
//  ChatCell.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 23/11/22.
//

import UIKit

class ChatCell: UITableViewCell {
    @IBOutlet weak var messageLbl: UILabel!
    @IBOutlet weak var messageView: UIView!
    @IBOutlet weak var profileImg: UIImageView!
    static var identifier = "ChatCell"
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
        messageView.layer.maskedCorners = [.layerMaxXMaxYCorner,.layerMaxXMinYCorner,.layerMinXMaxYCorner]
    }
}
