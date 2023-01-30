//
//  VideoAnalyticsCell.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 20/01/23.
//

import UIKit

class VideoAnalyticsCell: UITableViewCell {

    @IBOutlet weak var pendingAmtLbl: UILabel!
    @IBOutlet weak var raisedAmtLLbl: UILabel!
    @IBOutlet weak var userNameLbl: UILabel!
    @IBOutlet weak var dateLbl: UILabel!
    static var identifier = "VideoAnalyticsCell"
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        raisedAmtLLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        pendingAmtLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        dateLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        userNameLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    func updateCell(data:VideoAnalyticsData,statusType:StatusType){
        userNameLbl.text = "@"+data.userName
        raisedAmtLLbl.text = "$\(data.amount)"
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US_POSIX")
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        guard let date = dateFormatter.date(from: data.date) else{
            dateLbl.text = ""
            return
        }
        self.dateLbl.text =  date.MMMddyyyy.uppercased() + " " + date.hh_mm_AM_PM
        if statusType == .claim && data.paymentStatus == .pending{
            pendingAmtLbl.textColor = UIColor.green
            pendingAmtLbl.text = "$\(data.amount)"
        }else if statusType == .review && data.paymentStatus == .pending{
            pendingAmtLbl.textColor = UIColor(named: "reviewBtnColor")
            pendingAmtLbl.text = "$\(data.amount)"
        }else{
            pendingAmtLbl.text = "$00.00"
        }
    }
}
