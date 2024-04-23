//
//  TopDonatorCell.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 01/03/24.
//

import UIKit

class TopDonatorCell: UICollectionViewCell {

    static var identifier = "TopDonatorCell"
    @IBOutlet weak var donatorName: UILabel!
    @IBOutlet weak var donatorImg: UIImageView!
//    static leTopDonatorCell
    override func awakeFromNib() {
        super.awakeFromNib()
        donatorImg.layer.cornerRadius = donatorImg.frame.height / 2
        // Initialization code
    }

}
