//
//  UserResultCell.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 23/12/22.
//

import UIKit

class UserResultCell: UICollectionViewCell {
static var identifier = "UserResultCell"
    @IBOutlet weak var nameLbl: UILabel!
    @IBOutlet weak var profileImage: UIImageView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    func updateCell(data:UserProfileData){
        self.profileImage.loadImgForProfile(url: data.profileImage)
        self.nameLbl.text = data.name.localizedCapitalized
    }
    override func layoutIfNeeded() {
        super.layoutIfNeeded()
        self.profileImage.layer.cornerRadius = self.profileImage.frame.height / 2
    }
    override func layoutSubviews() {
        super.layoutSubviews()
        self.profileImage.layer.cornerRadius = self.profileImage.frame.height / 2
    }
}
