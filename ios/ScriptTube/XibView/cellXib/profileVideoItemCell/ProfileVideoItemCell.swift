//
//  ProfileVideoItemCell.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/21/22.
//

import UIKit

class ProfileVideoItemCell: UICollectionViewCell {
    @IBOutlet weak var thumbnailImg: UIImageView!
    static var identifier = "ProfileVideoItemCell"
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    func updateCell(withImg img: String){
//        contentView.layer.borderColor = UIColor.white.cgColor
//        contentView.layer.borderWidth = 1
        self.thumbnailImg.loadImg(url: img)
    }
}
