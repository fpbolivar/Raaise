//
//  CategoryCell.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 15/12/22.
//

import UIKit

class CategoryCell: UITableViewCell {

    static var identifier = "CategoryCell"
    @IBOutlet weak var label: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        selectionStyle = .none
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
