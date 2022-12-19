//
//  ReplyTblCell.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 12/12/22.
//

import UIKit
protocol ReplyDelegate{
    func gotoProfile(withId id:String)
}
class ReplyTblCell: UITableViewCell {
    @IBOutlet weak var commentLbl: UILabel!
    @IBOutlet weak var timeLbl: UILabel!
    @IBOutlet weak var profileImage: UIImageView!
    @IBOutlet weak var nameLbl: UILabel!
    static var identifier = "ReplyTblCell"
    var replyData: ReplyDataModel!
    var delegate:ReplyDelegate?
    override func awakeFromNib() {
        super.awakeFromNib()
        profileImage.layer.cornerRadius = profileImage.frame.height / 2
        selectionStyle = .none
        profileImage.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(gotoProfile)))
        nameLbl.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(gotoProfile)))
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    func setup(data:ReplyDataModel){
        replyData = data
        self.commentLbl.text = data.reply
        self.nameLbl.text = data.username
        self.profileImage.loadImgForProfile(url: data.profileImage)
        //loadImg(url: data.commentprofileImage)
    }
    @objc func gotoProfile(){
        delegate?.gotoProfile(withId: replyData.userId)
        print("REPLYUSERIS",replyData.userId)
    }
    
}
