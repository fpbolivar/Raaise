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
    @IBOutlet weak var timeLbl: UILabel!
    @IBOutlet weak var profileImg: UIImageView!
    var chat = ChatModel()
    var deleteDelegate:DeleteChatDelegate?
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        messageView.addInteraction(UIContextMenuInteraction(delegate: self))
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    func updateCell(data:ChatModel){
        chat = data
        messageLbl.text = data.message
        timeLbl.text = data.chatHistoryTime.getTime()
        profileImg.loadImgForProfile(url: AuthManager.currentUser.profileImage)
    }
    func roundCorners(){
        messageView.clipsToBounds = true
        messageView.layer.cornerRadius = 10
        messageView.layer.maskedCorners = [.layerMaxXMinYCorner,.layerMinXMaxYCorner,.layerMinXMinYCorner]
    }
    
}
extension SendChatCell:UIContextMenuInteractionDelegate{
    func contextMenuInteraction(_ interaction: UIContextMenuInteraction, configurationForMenuAtLocation location: CGPoint) -> UIContextMenuConfiguration? {
        return UIContextMenuConfiguration(identifier: nil, previewProvider: nil) { _ in
            let all = UIAction(title: "Delete",image: UIImage(systemName: "trash"),attributes: .destructive) { _ in
                self.deleteDelegate?.deleteChat(withId: self.chat.id)
                print("ALL OPTION CLICKED")
            }
            let menu = UIMenu(title: "", children: [all])
            
            return menu
        }
    }
}
