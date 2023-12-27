//
//  RoomMemberCell.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 25/04/23.
//

import UIKit

class RoomMemberCell: UITableViewCell {
    @IBOutlet weak var nameLbl: UILabel!
    @IBOutlet weak var userImage: DesignableImageView!
    static var identifier = "RoomMemberCell"
    var data = UserListDataModel()
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        selectionStyle = .none
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    func updateCell(data:RoomMemberModel){
        self.userImage.loadImgForProfile(url: data.profileImg)
        self.nameLbl.text = data.username
    }
    func updateCellForUser(data:UserListDataModel){
        self.userImage.loadImgForProfile(url: data.image)
        self.nameLbl.text = data.userName
    }
    
}
