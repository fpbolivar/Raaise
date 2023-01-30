//
//  DonationRaisedListVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/21/22.
//

import UIKit

class DonationRaisedListVC: BaseControllerVC {

    @IBOutlet weak var videosLbl: UILabel!
    @IBOutlet weak var raisedAmtLbl: UILabel!
    @IBOutlet weak var withdrawAmtLbl: UILabel!
    @IBOutlet weak var donatedAmtLbl: UILabel!
    @IBOutlet weak var tRaisedbl: UILabel!
    @IBOutlet weak var tWithdrawLbl: UILabel!
    @IBOutlet weak var tDonatedLbl: UILabel!
    @IBOutlet weak var overViewLbl: UILabel!
    @IBOutlet weak var topInfoLbl:UILabel!
    @IBOutlet weak var givenAmountLbl:UILabel!
    @IBOutlet weak var givenTitleLbl:UILabel!
    @IBOutlet weak var pendingAmountLbl:UILabel!
    @IBOutlet weak var pendingTitleLbl:UILabel!
    @IBOutlet weak var recievedAmountLbl:UILabel!
    @IBOutlet weak var recievedTitleLbl:UILabel!
    @IBOutlet weak var  tableView:ContentSizedTableView!
    var totalDonated = ""
    var totalRaised = ""
    var totalWithdraw = ""
    var videoDetails = [DonationRaisedVideoList]()
    override func viewDidLoad() {
        super.viewDidLoad()
        addNavBar(headingText: "Support Raised", redText: "Raised")
//        navView.rigthBtn.setTitle("Withdrawal Donation", for: .normal)
//        navView.rigthBtn.addTarget(self, action: #selector(rightAction), for: .touchUpInside)
        setfonts()
        tableView.separatorStyle = .none
        tableView.register(UINib(nibName: DonationRaisedCell.identifier, bundle: nil), forCellReuseIdentifier: DonationRaisedCell.identifier)
        self.pleaseWait()
        getDonationRaisedData{
            self.clearAllNotice()
            self.executeTblDelegate()
            self.setData()
        }
        
        // Do any additional setup after loading the view.
    }
    func getDonationRaisedData(completion:@escaping()->Void){
        DataManager.donationRaisedData(delegate: self, param: ["userId":AuthManager.currentUser.id]) { data in
            self.totalDonated = data["data"]["donatedAmount"].stringValue
            self.totalRaised = data["data"]["totalRaised"].stringValue
            self.totalWithdraw = data["data"]["totalWithdraw"].stringValue
            data["data"]["userVideo"].forEach { (_,json) in
                self.videoDetails.append(DonationRaisedVideoList(data: json))
            }
            completion()
        }
    }
    func setData(){
        DispatchQueue.main.async {
            self.raisedAmtLbl.text = "$\(self.totalRaised)"
            self.withdrawAmtLbl.text = "$\(self.totalWithdraw)"
            self.donatedAmtLbl.text = "$\(self.totalDonated)"
        }
    }
    func setfonts(){
        overViewLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX12)
        videosLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX12)
        tDonatedLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        tWithdrawLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        tRaisedbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        raisedAmtLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        withdrawAmtLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        donatedAmtLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        topInfoLbl.font = AppFont.FontName.light.getFont(size: AppFont.pX10)
        givenTitleLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX13)
        pendingTitleLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX13)
        recievedTitleLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX13)
        givenAmountLbl.font = AppFont.FontName.semiBold.getFont(size: AppFont.pX15)
        pendingAmountLbl.font = AppFont.FontName.semiBold.getFont(size: AppFont.pX15)
        recievedAmountLbl.font = AppFont.FontName.semiBold.getFont(size: AppFont.pX15)
        navView.rigthBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: AppFont.pX15)
    }
    func executeTblDelegate(){
        DispatchQueue.main.async {
            self.tableView.delegate = self
            self.tableView.dataSource = self
            self.tableView.reloadData()
        }
    }
}
extension DonationRaisedListVC:UITableViewDelegate,UITableViewDataSource{
    func numberOfSections(in tableView: UITableView) -> Int {
        1
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return videoDetails.count
    }


    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {

        return 60
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier:  DonationRaisedCell.identifier, for: indexPath) as!  DonationRaisedCell
        cell.updateCell(data: videoDetails[indexPath.row])
        cell.selectionStyle = .none
        cell.delegate = self
        return cell
    }

    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
    }

}
extension DonationRaisedListVC:DonationRaisedCellDelegate{
    func statusBtnClicked(status: StatusType,videoId:String) {
        let vc = VideoAnalyticsVC()
        vc.statusType = status
        vc.videoId = videoId
        self.navigationController?.pushViewController(vc, animated: true)
    }
}


class DonationRaisedVideoList{
    var id = ""
    var videoName = ""
    var videoDate = ""
    var status: StatusType = .view
    init(){
        
    }
    init(data:JSON){
        self.id = data["_id"].stringValue
        self.videoName = data["videoCaption"].stringValue
        self.videoDate = data["createdAt"].stringValue
        if data["status"].stringValue == "View"{
            status = .view
        }else if data["status"].stringValue == "Claim"{
            status = .claim
        }else{
            status = .review
        }
    }
}
