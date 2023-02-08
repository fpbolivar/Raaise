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
    //MARK: - Update Data
    func setup(data:ReplyDataModel){
        replyData = data
        self.commentLbl.text = data.reply
        self.nameLbl.text = data.username
        self.profileImage.loadImgForProfile(url: data.profileImage)
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US_POSIX")
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        let date = dateFormatter.date(from: data.replyTime)
        let currentDate = Date()
        if currentDate.offsetFrom(date: date!) == ""{
            self.timeLbl.text = "Just Now"
        }else{
            self.timeLbl.text = currentDate.offsetFrom(date: date!)
        }
    }
    @objc func gotoProfile(){
        delegate?.gotoProfile(withId: replyData.userId)
        print("REPLYUSERIS",replyData.userId)
    }
    
}
