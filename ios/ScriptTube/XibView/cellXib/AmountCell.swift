//
//  AmountCell.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 28/11/22.
//

import UIKit

class AmountCell: UICollectionViewCell {
    static var identifier = "AmountCell"
    @IBOutlet weak var amountLbl: UILabel!
    
    @IBOutlet weak var containerView: UIView!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        amountLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        containerView.layer.cornerRadius = 10
        // Initialization code
    }
    //MARK: - Update Data
    func updateCell(withAmt:String,forPopup:Bool=false){
        self.amountLbl.text = withAmt
        if forPopup{
            containerView.backgroundColor = UIColor(named: "DonationFieldColor")
            amountLbl.textColor = UIColor.black
        }else{
            
            amountLbl.textColor = UIColor(named: "textColor")
        }
        
    }
    func selectedCell(forPopup:Bool=false){
        if forPopup{
            self.containerView.layer.borderColor = UIColor.green.cgColor
            self.containerView.layer.borderWidth = 1
        }else{
            self.containerView.layer.borderColor = UIColor.white.cgColor
            self.containerView.layer.borderWidth = 1
            amountLbl.textColor = UIColor.white
        }
        
    }
    func unselectedCell(){
        self.containerView.layer.borderWidth = 0
        amountLbl.textColor = UIColor(named: "textColor")
    }

}
