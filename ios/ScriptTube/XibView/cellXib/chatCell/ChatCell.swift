//
//  ChatCell.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 23/11/22.
//

import UIKit

class ChatCell: UITableViewCell  {
    @IBOutlet weak var timeLbl: UILabel!
    @IBOutlet weak var messageLbl: UILabel!
    @IBOutlet weak var messageView: UIView!
    @IBOutlet weak var profileImg: UIImageView!
    static var identifier = "ChatCell"
    var delegate:ChatDelegate?
    var deleteDelegate:DeleteChatDelegate?
    var chat = ChatModel()
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        
        profileImg.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(visitProfile)))
        profileImg.layer.cornerRadius = profileImg.frame.height / 2
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    @objc func visitProfile(){
        delegate?.visitOtherUserProfile()
    }
    func updateCell(data:ChatModel){
        chat = data
        messageLbl.text = data.message
        timeLbl.text = data.chatHistoryTime.getTime()
        profileImg.loadImgForProfile(url: data.otherUser.profileImage)
    }
    func roundCorners(){
        messageView.clipsToBounds = true
        messageView.layer.cornerRadius = 10
        messageView.layer.maskedCorners = [.layerMaxXMaxYCorner,.layerMaxXMinYCorner,.layerMinXMaxYCorner]
    }
}

protocol ChatDelegate {
    func visitOtherUserProfile()
}
protocol ChatVideoDelegate {
    func viewVideo(withSlug slug:String)
}
protocol DeleteChatDelegate{
    func deleteChat(withId id:String)
}
