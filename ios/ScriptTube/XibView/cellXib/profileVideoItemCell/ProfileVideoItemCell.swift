//
//  ProfileVideoItemCell.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/21/22.
//

import UIKit

class ProfileVideoItemCell: UICollectionViewCell {
    @IBOutlet weak var donateImg: UIImageView!
    @IBOutlet weak var donationAmtLbl: UILabel!
    @IBOutlet weak var numberOfViewsLbl: UILabel!
    @IBOutlet weak var thumbnailImg: UIImageView!
    static var identifier = "ProfileVideoItemCell"
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    override func prepareForReuse() {
        super.prepareForReuse()
        //donateImg.isHidden = false
        donationAmtLbl.isHidden = false
    }
    func updateCell(withImg img: String){
//        contentView.layer.borderColor = UIColor.white.cgColor
//        contentView.layer.borderWidth = 1
        self.thumbnailImg.loadImg(url: img)
    }
    func updateCellData(data:Post){
        print("lallll",data.donationAmount)
        if data.isDonation{
            //donateImg.isHidden = false
            donationAmtLbl.isHidden = false
        }else{
            //donateImg.isHidden = true
            donationAmtLbl.isHidden = true
        }
        self.thumbnailImg.loadImg(url: data.videoImage)
//        self.donationAmtLbl.text = data.donationAmount
        print("DONATIONAMT",data.donationAmount)
        if data.donationAmount != "" && data.donationAmount != "0.00"{
            //donationAmtLbl.text = "$ \(Int(data.donationAmount)?.shorten() ?? "0")"
            donationAmtLbl.text = "$ \(Double(data.donationAmount)?.shortAmt() ?? "0.00")"
        }else{
            donationAmtLbl.text = "$ 0.00"
        }
        self.numberOfViewsLbl.text = Int(data.videoViewCount)?.shorten() ?? "0"
    }
}
