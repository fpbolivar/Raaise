//
//  UserDonationListVC.swift
//  ScriptTube
//
//  Created by VivaJiva  on 04/03/24.
//

import UIKit

class UserDonationListVC: BaseControllerVC {
    var tableSetup = false
    @IBOutlet weak var noRecordLbl: UILabel!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var lblTotalRaised: UILabel!
    
    var userList : [DonationUserModel] = []
    var page = 1
    var totalRaisedDonation = ""
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        lblTotalRaised.text = "$\(totalRaisedDonation)"
       
        setNoResultFoundLbl()
        
        setTableView()
        hideNavbar()
        applyStyle()
        print(userList.count)
    }
    func setNoResultFoundLbl() {
        if userList.isEmpty {
            let noDataLabel = UILabel(frame: CGRect(x: 0, y: 0, width: tableView.bounds.size.width, height: tableView.bounds.size.height))
            noDataLabel.text = "No Result found"
            noDataLabel.textAlignment = .center
            tableView.backgroundView = noDataLabel
            tableView.separatorStyle = .none
        } else {
            tableView.backgroundView = nil
            tableView.separatorStyle = .singleLine
        }
    }
    
    func applyStyle(){
        tableSetup = true
        tableView.register(UINib(nibName: DonationProfileCellXib.identifier, bundle: nil), forCellReuseIdentifier: DonationProfileCellXib.identifier)
        tableView.delegate = self
        tableView.dataSource = self
    }
    
    func setTableView(){
        tableSetup = true
        tableView.reloadData()
    }

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}

extension UserDonationListVC:UITableViewDelegate,UITableViewDataSource{
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return userList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: DonationProfileCellXib.identifier, for: indexPath) as! DonationProfileCellXib
        cell.lblDonorName.text = "@\(userList[indexPath.row].name)"
        cell.lblDonationAmt.text = userList[indexPath.row].credit
        cell.donorImg.loadImg(url: userList[indexPath.row].profileImage)
        
        cell.donorImg.layer.cornerRadius = cell.donorImg.frame.height / 2
        cell.donorImg.loadImgForProfile(url: userList[indexPath.row].profileImage)
        
        
        return cell
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 70
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
//        let vc = VisitProfileVC()
//        vc.id = self.userList[indexPath.row].userId
//        self.navigationController?.pushViewController(vc, animated: true)
    }
    //MARK: - Pagination
//    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
//        guard let scroll = scrollView as? UITableView else{return}
//        let height = scroll.frame.size.height
//        let contentYOffset = scroll.contentOffset.y
//        let distanceFromBottom = scroll.contentSize.height - contentYOffset
//        if distanceFromBottom == height{
//            page = page + 1
//            
//        }
//    }
}
