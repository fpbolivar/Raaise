//
//  SendVideoCell.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 02/01/23.
//

import UIKit

class SendVideoCell: UITableViewCell {
    static let identifier = "SendVideoCell"
    @IBOutlet weak var profileImg:UIImageView!
    @IBOutlet weak var videoImage:UIImageView!
    @IBOutlet weak var timeLbl:UILabel!
    var chatData: ChatModel? = nil
    var delegate:ChatVideoDelegate?
    var deleteDelegate:DeleteChatDelegate?
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        videoImage.addInteraction(UIContextMenuInteraction(delegate: self))
        videoImage.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(seeVideo)))
        videoImage.layer.cornerRadius = 10
        profileImg.layer.cornerRadius = profileImg.frame.height / 2
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    //MARK: - Update Data
    @objc func seeVideo(){
        guard let chatData = chatData else{return}
        delegate?.viewVideo(withSlug: chatData.videoSlug)
    }
    func updateCell(data:ChatModel){
        self.chatData = data
        profileImg.loadImgForProfile(url: data.otherUser.profileImage)
        self.videoImage.loadImg(url: data.videoImage)
        self.timeLbl.text = data.chatHistoryTime.getTime()
    }
}
//MARK: - Contextual Menu Delegate
extension SendVideoCell:UIContextMenuInteractionDelegate{
    func contextMenuInteraction(_ interaction: UIContextMenuInteraction, configurationForMenuAtLocation location: CGPoint) -> UIContextMenuConfiguration? {
        return UIContextMenuConfiguration(identifier: nil, previewProvider: nil) { _ in
            let all = UIAction(title: "Delete",image: UIImage(systemName: "trash"),attributes: .destructive) { _ in
                guard let chatData = self.chatData else{return}
                self.deleteDelegate?.deleteChat(withId: chatData.id)
                print("ALL OPTION CLICKED")
            }
            let menu = UIMenu(title: "", children: [all])
            
            return menu
        }
    }
}
