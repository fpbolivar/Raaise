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
        donationAmtLbl.isHidden = false
    }
    //MARK: - Update Data
    func updateCell(withImg img: String){
        self.thumbnailImg.loadImg(url: img)
    }
    func updateCellData(data:Post){
        print("lallll",data.donationAmount)
        if data.isDonation{
            donationAmtLbl.isHidden = false
        }else{
            donationAmtLbl.isHidden = true
        }
        self.thumbnailImg.loadImg(url: data.videoImage)
        print("DONATIONAMT",data.donationAmount)
        if data.donationAmount != "" && data.donationAmount != "0.00"{
            donationAmtLbl.text = "$ \(Double(data.donationAmount)?.shortAmt() ?? "0.00")"
        }else{
            donationAmtLbl.text = "$ 0.00"
        }
        self.numberOfViewsLbl.text = Int(data.videoViewCount)?.shorten() ?? "0"
    }
}
