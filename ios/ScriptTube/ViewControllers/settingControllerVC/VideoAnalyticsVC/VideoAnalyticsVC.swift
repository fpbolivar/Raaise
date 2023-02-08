//
//  VideoAnalyticsVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 20/01/23.
//

import UIKit

protocol VideoAnalyticsDelegate{
    func amountClaimed(withId id:String)
}

class VideoAnalyticsVC: BaseControllerVC {
    var statusType:StatusType!
    var videoId = ""
    var totaRaised = ""
    var totaWithdraw = ""
    var totalClaim = ""
    var data = [VideoAnalyticsData]()
    var canClaim = false
    var delegate:VideoAnalyticsDelegate?
    @IBOutlet weak var messageView: CardView!
    @IBOutlet weak var claimNowLbl: UILabel!
    @IBOutlet weak var claimBtnView: CardView!
    @IBOutlet weak var pendingLbl: UILabel!
    @IBOutlet weak var raisedLbl: UILabel!
    @IBOutlet weak var dateLbl: UILabel!
    @IBOutlet weak var overViewLbl: UILabel!
    @IBOutlet weak var withdrawAmtLbl: UILabel!
    @IBOutlet weak var tWithdrawLbl: UILabel!
    @IBOutlet weak var claimedAmtLbl: UILabel!
    @IBOutlet weak var amountClaimedLbl: UILabel!
    @IBOutlet weak var raisedAmtLbl: UILabel!
    @IBOutlet weak var tRaisedLbl: UILabel!
    @IBOutlet weak var noOfPostsLbl: UILabel!
    @IBOutlet weak var tPostsLbl: UILabel!
    @IBOutlet weak var topInfoLbl: UILabel!
    @IBOutlet weak var tableView: ContentSizedTableView!
    override func viewDidLoad() {
        super.viewDidLoad()
        hideNavbar()
        setup()
        setFonts()
        // Do any additional setup after loading the view.
    }
    func setData(){
        DispatchQueue.main.async {
            self.raisedAmtLbl.text = "$\(self.totaRaised)"
            self.withdrawAmtLbl.text = "$\(self.totaWithdraw)"
            self.claimedAmtLbl.text = "$\(self.totalClaim)"
        }
    }
    //MARK: - Api Methods
    func claimApi(){
        AuthManager.claimDonationApi(delegate: self, param: ["videoId":videoId]) {
            DispatchQueue.main.async {
                ToastManager.successToast(delegate: self, msg: "Request sent successfully")
                self.claimBtnView.isHidden = true
                self.delegate?.amountClaimed(withId: self.videoId)
            }
        }
    }
    func getVideoData(completion:@escaping()->Void){
        DataManager.videoAnalyticsData(delegate: self, param: ["videoId":videoId]) { data in
            self.totaRaised = data["raisedDonationAmount"].stringValue
            self.totaWithdraw = data["completedDonationAmount"].stringValue
            self.totalClaim = data["claimedAmount"].stringValue
            data["data"].forEach { (_,json) in
                self.data.append(VideoAnalyticsData(data: json))
            }
            completion()
        }
    }
    //MARK: - Setup
    func setup(){
        if statusType == .review{
            addNavBar(headingText:"Video Analytics",redText:"Analytics",type: .filter,addNewCardSelector: #selector(openFilter),addNewCardSelectorTitle: "")
            claimedAmtLbl.textColor = UIColor(named: "reviewBtnColor")
                
        }else if statusType == .claim && !canClaim{
            addNavBar(headingText: "Video Analytics", redText: "Analytics")
            claimedAmtLbl.textColor = UIColor.green
            claimBtnView.isHidden = false
        }else if statusType == .claim && canClaim{
            addNavBar(headingText: "Video Analytics", redText: "Analytics")
            claimedAmtLbl.textColor = UIColor.green
            claimBtnView.isHidden = true
            messageView.isHidden = false
        }else{
            addNavBar(headingText: "Video Analytics", redText: "Analytics")
        }
        noOfPostsLbl.text = AuthManager.currentUser.videoCount
        setupTable()
        getVideoData{
            self.setData()
            self.executeDelegates()
        }
    }
    func setFonts(){
        claimNowLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX14)
        overViewLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX12)
        dateLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX12)
        raisedLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX12)
        pendingLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX12)
        topInfoLbl.font = AppFont.FontName.light.getFont(size: AppFont.pX10)
        tPostsLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        tRaisedLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        tWithdrawLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        amountClaimedLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        
        noOfPostsLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        raisedAmtLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        withdrawAmtLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        claimedAmtLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
    }
    func setupTable(){
        tableView.separatorStyle = .none
        tableView.register(UINib(nibName: VideoAnalyticsCell.identifier, bundle: nil), forCellReuseIdentifier: VideoAnalyticsCell.identifier)
    }
    func executeDelegates(){
        DispatchQueue.main.async {
            self.tableView.delegate = self
            self.tableView.dataSource = self
            self.tableView.reloadData()
        }
    }
    @objc func openFilter(){
        let vc = FilterPopUpVC()
        vc.delegate = self
        vc.modalTransitionStyle = .crossDissolve
        vc.modalPresentationStyle = .overCurrentContext
        self.present(vc, animated: true)
    }
    
    @IBAction func claimNowBtnClicked(_ sender: Any) {
        claimApi()
    }
}
//MARK: -Table View Delegate
extension VideoAnalyticsVC:UITableViewDelegate,UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return data.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier:  VideoAnalyticsCell.identifier, for: indexPath) as!  VideoAnalyticsCell
        cell.selectionStyle = .none
        if self.statusType == .claim{
            cell.pendingAmtLbl.textColor = UIColor.green
        }else if self.statusType == .review{
            cell.pendingAmtLbl.textColor = UIColor(named: "reviewBtnColor")
        }
        cell.updateCell(data: data[indexPath.row], statusType: self.statusType)
        return cell
    }
}
extension VideoAnalyticsVC:FilterPopUpDelegate{
    func filterwithdata(fromDate: String, toDate: String) {
        DataManager.videoAnalyticsData(delegate: self, param: ["videoId":videoId,"startDate":fromDate,"endDate":toDate]) { data in
            var newData = [VideoAnalyticsData]()
            data["data"].forEach { (_,json) in
                newData.append(VideoAnalyticsData(data: json))
            }
            self.data = newData
            self.tableView.reloadData()
        }
    }
}

class VideoAnalyticsData{
    var userName = ""
    var raisedAmount = ""
    var pendingAmount = ""
    var date = ""
    var paymentStatus:PaymentStatus = .pending
    init(){
        
    }
    init(data:JSON){
        self.userName = data["name"].stringValue
        self.raisedAmount = data["raisedAmount"].stringValue
        self.pendingAmount = data["pendingAmount"].stringValue
        self.date = data["date"].stringValue
    }
}
enum PaymentStatus{
    case raised
    case pending
}
