//
//  DonationProfileCellXib.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 05/03/24.
//

import UIKit

class DonationProfileCellXib: UITableViewCell {
    
    static var identifier = "DonationProfileCellXib"

    @IBOutlet weak var lblDonationAmt: UILabel!
    @IBOutlet weak var lblDonorName: UILabel!
    @IBOutlet weak var donorImg: UIImageView!
    // doner
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
      
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
