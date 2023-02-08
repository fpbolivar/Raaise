//
//  WithdrawalsCell.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/21/22.
//

import UIKit

class WithdrawalsCell: UITableViewCell {
    static var identifier = "WithdrawalsCell"
    @IBOutlet weak var  titleLbl:UILabel!
    @IBOutlet weak var  dateLbl:UILabel!
    @IBOutlet weak var  statusLbl:UILabel!
    @IBOutlet weak var  priceLbl:UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        config()
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    // MARK: - Setup
    func config(){
        titleLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        dateLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX13)
        priceLbl.font = AppFont.FontName.semiBold.getFont(size: AppFont.pX16)
        statusLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
    }
    // MARK: - Update Data
    func updateCell(data:WithdrawListData){
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US_POSIX")
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        guard let date = dateFormatter.date(from: data.date) else{
            dateLbl.text = ""
            return
        }
        dateLbl.text = date.MMMddyyyy.capitalized
        titleLbl.text = date.hh_mm_AM_PM
        priceLbl.text = "$\(data.amount)"
        if data.status == "pending"{
            statusLbl.text = data.status.localizedCapitalized
            statusLbl.textColor = UIColor.red
        }else{
            statusLbl.text = data.status.localizedCapitalized
            statusLbl.textColor = UIColor.green
        }
    }
}
