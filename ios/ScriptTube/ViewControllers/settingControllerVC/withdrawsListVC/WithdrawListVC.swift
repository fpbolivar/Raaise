//
//  WithdrawListVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/21/22.
//

import UIKit

class WithdrawListVC: BaseControllerVC {

    @IBOutlet weak var topInfoLbl:UILabel!
   var data = [WithdrawListData]()

    @IBOutlet weak var  tableView:ContentSizedTableView!
    override func viewDidLoad() {
        super.viewDidLoad()
        addNavBar(headingText:"Withdrawals",
                  redText:"",
                  type: .largeNavBarOnlyBackWithRightBtn,
                  color: UIColor(named: "bgColor"))
        setup()
        // Do any additional setup after loading the view.
    }
    @objc private  func rightAction(){
        let vc = BankAddDetailVC()
        self.navigationController?.pushViewController(vc, animated: true)
    }
    //MARK: - Api Methods
    func getWithdrawlList(completion:@escaping()->Void){
        DataManager.withdrawList(delegate: self, param: ["userId":AuthManager.currentUser.id]) { data in
            data["data"].forEach { (_,json) in
                self.data.append(WithdrawListData(data: json))
            }
            completion()
        }
    }
    //MARK: - Setup
    func setup(){
        tableView.separatorStyle = .singleLine
        tableView.separatorColor = .white
        navView.rigthBtn.setTitle("Bank Details", for: .normal)
        navView.rigthBtn.addTarget(self, action: #selector(rightAction), for: .touchUpInside)
        navView.rigthBtn.setTitleColor(.new_theme, for: .normal)
        setfonts()
        tableView.separatorStyle = .none
        tableView.register(UINib(nibName: WithdrawalsCell.identifier, bundle: nil), forCellReuseIdentifier: WithdrawalsCell.identifier)
        getWithdrawlList{
            DispatchQueue.main.async {
                self.executeTblDelegate()
            }
        }
    }
    func setfonts(){
        topInfoLbl.font = AppFont.FontName.light.getFont(size: AppFont.pX14)
        navView.rigthBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: AppFont.pX15)
    }
    func executeTblDelegate(){
        tableView.delegate = self
        tableView.dataSource = self
        tableView.reloadData()
    }

}
//MARK: - Table View Delegate
extension WithdrawListVC:UITableViewDelegate,UITableViewDataSource{
    func numberOfSections(in tableView: UITableView) -> Int {
        1
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return data.count
    }


    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {

        return 80
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier:  WithdrawalsCell.identifier, for: indexPath) as!  WithdrawalsCell
        cell.updateCell(data: data[indexPath.row] )
        cell.selectionStyle = .none
        return cell
    }

    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
    }

}
class WithdrawListData{
    var date = ""
    var amount = ""
    var status = ""
    
    init(){
        
    }
    init(data:JSON){
        date = data["createdAt"].stringValue
        amount = data["amount"].stringValue
        status = data["withdrawalStatus"].stringValue
    }
}
