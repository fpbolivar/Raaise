//
//  ReplyTblCell.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 12/12/22.
//

import UIKit
protocol ReplyDelegate{
    func gotoProfile(withId id:String)
    func editReply(replyData:ReplyDataModel)
    func deleteReply(replyData:ReplyDataModel)
}
class ReplyTblCell: UITableViewCell {
    @IBOutlet weak var optionBtn: UIButton!
    @IBOutlet weak var commentLbl: UILabel!
    @IBOutlet weak var timeLbl: UILabel!
    @IBOutlet weak var profileImage: UIImageView!
    @IBOutlet weak var nameLbl: UILabel!
    static var identifier = "ReplyTblCell"
    var replyData: ReplyDataModel!
    let menuDropDown = DropDown()
    var settingList = ["Edit","Delete"]
    var delegate:ReplyDelegate?
    override func awakeFromNib() {
        super.awakeFromNib()
        optionBtn.addTarget(self, action: #selector(openOptions), for: .touchUpInside)
        profileImage.layer.cornerRadius = profileImage.frame.height / 2
        selectionStyle = .none
        profileImage.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(gotoProfile)))
        nameLbl.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(gotoProfile)))
        setupMenu()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    @objc func openOptions(){
        menuDropDown.show()
    }
    //MARK: - Update Data
    func setupMenu(){
        menuDropDown.align = .center
        menuDropDown.width = 100
        menuDropDown.cornerRadius = 10
        menuDropDown.anchorView = optionBtn
        menuDropDown.direction = .bottom
        menuDropDown.bottomOffset = CGPoint(x: -100, y:optionBtn.bounds.minY)
        menuDropDown.backgroundColor = UIColor.darkGray
        menuDropDown.textColor = UIColor.white
        menuDropDown.selectedTextColor = UIColor.white
        menuDropDown.selectionBackgroundColor = UIColor.darkGray
        menuDropDown.dataSource = settingList.map{$0}
        menuDropDown.selectionAction = { [weak self] (index, item) in
            guard let data = self?.replyData else{return}
            if index == 0{
                self?.delegate?.editReply(replyData: data)
            }else{
                self?.delegate?.deleteReply(replyData: data)
            }
            
        }
    }
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
