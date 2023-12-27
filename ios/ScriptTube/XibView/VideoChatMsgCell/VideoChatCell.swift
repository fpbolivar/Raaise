//
//  VideoChatCell.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 27/04/23.
//

import UIKit

class VideoChatCell: UITableViewCell {
static var identifier = "VideoChatCell"
    @IBOutlet weak var userImage: UIImageView!
    @IBOutlet weak var messageLbl: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        selectionStyle = .none
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    func updateCell(data:LiveRoomChatModel){
        self.messageLbl.text = "\(data.userName) : \(data.message)"
        print("CHATIMAGEURL",data.image)
        self.userImage.loadImgForProfile(url: data.image)
    }
}
