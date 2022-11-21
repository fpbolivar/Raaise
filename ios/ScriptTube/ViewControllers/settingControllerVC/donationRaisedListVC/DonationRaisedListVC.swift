//
//  DonationRaisedListVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/21/22.
//

import UIKit

class DonationRaisedListVC: BaseControllerVC {

    @IBOutlet weak var topInfoLbl:UILabel!
    @IBOutlet weak var givenAmountLbl:UILabel!
    @IBOutlet weak var givenTitleLbl:UILabel!
    @IBOutlet weak var pendingAmountLbl:UILabel!
    @IBOutlet weak var pendingTitleLbl:UILabel!
    @IBOutlet weak var recievedAmountLbl:UILabel!
    @IBOutlet weak var recievedTitleLbl:UILabel!
  
    @IBOutlet weak var  tableView:ContentSizedTableView!
    override func viewDidLoad() {
        super.viewDidLoad()
        addNavBar(headingText:"Donation Raised",redText:"Raised",type: .largeNavBarOnlyBackWithRightBtn)
        navView.rigthBtn.setTitle("Withdrawal Donation", for: .normal)
        navView.rigthBtn.addTarget(self, action: #selector(rightAction), for: .touchUpInside)
        setfonts()
        tableView.separatorStyle = .none
        tableView.register(UINib(nibName: DonationCell.identifier, bundle: nil), forCellReuseIdentifier: DonationCell.identifier)
        executeTblDelegate()
        // Do any additional setup after loading the view.
    }
       @objc private  func rightAction(){

        }
    func setfonts(){

      
        topInfoLbl.font = AppFont.FontName.light.getFont(size: AppFont.pX14)
        givenTitleLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX13)
        pendingTitleLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX13)
        recievedTitleLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX13)


        givenAmountLbl.font = AppFont.FontName.semiBold.getFont(size: AppFont.pX15)
        pendingAmountLbl.font = AppFont.FontName.semiBold.getFont(size: AppFont.pX15)
        recievedAmountLbl.font = AppFont.FontName.semiBold.getFont(size: AppFont.pX15)
        navView.rigthBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: AppFont.pX15)
    }
    func executeTblDelegate(){
        tableView.delegate = self
        tableView.dataSource = self
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
extension DonationRaisedListVC:UITableViewDelegate,UITableViewDataSource{
    func numberOfSections(in tableView: UITableView) -> Int {
        1
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        3
    }


    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {

        return 80
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier:  DonationCell.identifier, for: indexPath) as!  DonationCell
        cell.config()
        cell.selectionStyle = .none
        return cell
    }

    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
    }

}
