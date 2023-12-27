//
//  RoomCell.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 24/04/23.
//

import UIKit

class RoomCell: UICollectionViewCell {
    static var identifier = "RoomCell"
    @IBOutlet weak var descriptionLbl: UILabel!
    @IBOutlet weak var roomName: UILabel!
    @IBOutlet weak var onlineView: CardView!
    @IBOutlet weak var roomImg: UIImageView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    func updateCell(data:LiveRoomDataModel){
        self.descriptionLbl.text = data.description
        self.roomImg.loadImg(url: data.logo)
        self.roomName.text = data.title
        if data.isOnline == "1"{
            self.onlineView.isHidden = false
        }else{
            self.onlineView.isHidden = true
        }
        
    }
}
