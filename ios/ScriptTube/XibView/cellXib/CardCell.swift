//
//  CardCell.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/21/22.
//

import UIKit

class CardCell: UITableViewCell {
    @IBOutlet weak var deleteImage: UIImageView!
    static var identifier = "CardCell"
    @IBOutlet weak var checkImage: UIImageView!
    @IBOutlet weak var cardNumber: UILabel!
    @IBOutlet weak var cardImage: UIImageView!
    var cardDelete:(()->Void)? = nil
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        deleteImage.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(deleteCard)))
    }
    override func prepareForReuse() {
        super.prepareForReuse()
        checkImage.isHidden = true
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    @objc func deleteCard(){
        cardDelete!()
    }
    func cellSelected(){
        checkImage.isHidden = false
    }
    func cellUnselected(){
        checkImage.isHidden = true
    }
    func update(data:CardListModel){
        cardNumber.text = "**** **** **** \(data.last4)"
        switch data.brand{
        case "Visa":
            cardImage.image = UIImage(named: "ic_visa")
            break
        case "MasterCard":
            cardImage.image = UIImage(named: "ic_mastercard")
            break
        case "Barclays":
            cardImage.image = UIImage(named: "ic_barclays")
            break
        case "Discover":
            cardImage.image = UIImage(named: "ic_discover")
            break
        case "Citi":
            cardImage.image = UIImage(named: "ic_citi")
            break
        default:
            cardImage.image = UIImage(named: "ic_american_express")
        }
        if data.defaultCard{
            self.cellSelected()
        }
    }
}
