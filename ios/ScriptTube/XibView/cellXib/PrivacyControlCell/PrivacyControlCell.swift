//
//  PrivacyControlCell.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 13/05/23.
//

import UIKit

class PrivacyControlCell: UITableViewCell {
    @IBOutlet weak var chechBox: CardView!
    @IBOutlet weak var titleLbl: UILabel!
    static var identifier = "PrivacyControlCell"
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        selectionStyle  = .none
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    func updateCell(data:PrivacyData){
        self.titleLbl.text = data.title
        data.isSelected ? activeCheckbox() : inactiveCheckbox()
    }
    func activeCheckbox(){
        //chechBox.borderWidth = 0
        chechBox.backgroundColor = UIColor.white//UIColor(named: "new_theme")
    }
    func inactiveCheckbox(){
        //chechBox.borderWidth = 1
        chechBox.backgroundColor = UIColor(named: "bgColor")
    }
    
}
