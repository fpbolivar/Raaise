//
//  DonationRaisedCell.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 20/01/23.
//

import UIKit
protocol DonationRaisedCellDelegate{
    func statusBtnClicked(status:StatusType,videoId:String)
}
class DonationRaisedCell: UITableViewCell {
    @IBOutlet weak var statusView: CardView!
    @IBOutlet weak var statusLbl: UILabel!
    @IBOutlet weak var detailLbl: UILabel!
    @IBOutlet weak var dateLbl: UILabel!
    var delegate:DonationRaisedCellDelegate?
    var data:DonationRaisedVideoList!
    static var identifier = "DonationRaisedCell"
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        statusLbl.font = AppFont.FontName.semiBold.getFont(size: AppFont.pX10)
        detailLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        dateLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)

    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    //MARK: - Update Data
    func updateCell(data:DonationRaisedVideoList){
        self.data = data
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US_POSIX")
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        self.detailLbl.text = data.videoName
        switch data.status{
        case .review:
            statusView.backgroundColor = UIColor(named: "reviewBtnColor")
            statusLbl.text = "In Review"
        case .view:
            //statusView.backgroundColor = UIColor(named: "theme")
            statusView.backgroundColor = .new_theme
            statusLbl.text = "View"
        case .claim:
            statusView.backgroundColor = UIColor(named: "claimBtnColor")
            statusLbl.text = "Claim"
        }
        guard let date = dateFormatter.date(from: data.videoDate) else{
            dateLbl.text = ""
            return
        }
        self.dateLbl.text =  date.MMMddyyyy.uppercased() + " " + date.hh_mm_AM_PM
    }
    //MARK: - Action
    @IBAction func statusBtnClicked(_ sender: Any) {
        delegate?.statusBtnClicked(status: self.data.status, videoId: self.data.id)
    }
}
enum StatusType{
    case review
    case view
    case claim
}
