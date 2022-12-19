//
//  SettingCell.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/18/22.
//

import UIKit

class SettingCell: UITableViewCell {
    @IBOutlet weak var  titleLbl:UILabel!
    @IBOutlet weak var  switchItem:UISwitch!
    @IBOutlet weak var  switchItemContainer:UIView!
    @IBOutlet weak var  arrowImage:UIImageView!
    var notificationStateChanged:((Bool)->Void)? = nil
   static var identifier = "SettingCell"
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

    @IBAction func switchChanged(_ sender: UISwitch) {
        print(sender.isOn)
        notificationStateChanged!(sender.isOn)
    }
    func setupText(item:SettingOptionItems){
        switchItem.isHidden = true
        switchItemContainer.isHidden = true
        arrowImage.isHidden = false

        titleLbl.text = item.title
        titleLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX15)
        if item.type == .redLbl{
            titleLbl.textColor = UIColor.red
        }else{
            titleLbl.textColor = UIColor.white
        }
    }
    func setupToggle(item:SettingOptionItems,state:Bool){
        switchItem.isHidden = false
        switchItem.isOn = state
        switchItemContainer.isHidden = false
        arrowImage.isHidden = true
        titleLbl.text = item.title
        titleLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX15)
        if item.type == .redLbl{
            titleLbl.textColor = UIColor.theme
        }else{
            titleLbl.textColor = UIColor.white
        }
    }
    
}
