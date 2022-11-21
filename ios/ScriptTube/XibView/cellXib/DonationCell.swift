//
//  DonationCell.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/21/22.
//

import UIKit

class DonationCell: UITableViewCell {
    static var identifier = "DonationCell"
    @IBOutlet weak var  titleLbl:UILabel!
    @IBOutlet weak var  dateLbl:UILabel!
    @IBOutlet weak var  statusLbl:UILabel!
    @IBOutlet weak var  priceLbl:UILabel!

    @IBOutlet weak var  userImage:UIImageView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    func config(){
        titleLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        dateLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX13)
        priceLbl.font = AppFont.FontName.semiBold.getFont(size: AppFont.pX16)
        statusLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
    }
}
