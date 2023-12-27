//
//  SelectedUserCell.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 25/04/23.
//

import UIKit

class SelectedUserCell: UICollectionViewCell {
    static var identifier = "SelectedUserCell"
    @IBOutlet weak var userImage: DesignableImageView!
    var clearUser:(()->Void)?
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    
    @IBAction func clearBtnClicked(_ sender: Any) {
        if clearUser != nil{
            clearUser!()
        }
        
    }
    func updateCellForUser(data:UserListDataModel){
        self.userImage.loadImgForProfile(url: data.image)
    }
}
