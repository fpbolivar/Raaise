//
//  ChatHeaderCell.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 24/11/22.
//

import UIKit

class ChatHeaderCell: UITableViewCell {
    @IBOutlet weak var detailsLbl: UILabel!
    @IBOutlet weak var amountLbl: UILabel!
    @IBOutlet weak var totalDonationLbl: UILabel!
    @IBOutlet weak var nameLbl: UILabel!
    @IBOutlet weak var profileImage: UIImageView!
    static var identifier = "ChatHeaderCell"
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    func updateCell(){
        
    }
    
}
