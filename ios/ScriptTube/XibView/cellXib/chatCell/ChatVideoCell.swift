//
//  ChatVideoCell.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 02/01/23.
//

import UIKit

class ChatVideoCell: UITableViewCell {
    static let identifier = "ChatVideoCell"
    @IBOutlet weak var videoImage:UIImageView!
    @IBOutlet weak var profileImg:UIImageView!
    @IBOutlet weak var timeLbl:UILabel!
    var chatData: ChatModel? = nil
    var videoDelegate:ChatVideoDelegate?
    var delegate:ChatDelegate?
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        profileImg.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(visitProfile)))
        videoImage.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(seeVideo)))
        videoImage.layer.cornerRadius = 10
        profileImg.layer.cornerRadius = profileImg.frame.height / 2
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    @objc func seeVideo(){
        guard let chatData = chatData else{return}
        videoDelegate?.viewVideo(withSlug: chatData.videoSlug)
    }
    @objc func visitProfile(){
        delegate?.visitOtherUserProfile()
    }
    func updateCell(data:ChatModel){
        self.chatData = data
        profileImg.loadImgForProfile(url: data.otherUser.profileImage)
        self.videoImage.loadImg(url: data.videoImage)
        self.timeLbl.text = data.chatHistoryTime.getTime()
    }
}
