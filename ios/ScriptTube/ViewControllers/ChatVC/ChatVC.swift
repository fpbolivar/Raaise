//
//  ChatVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 23/11/22.
//

import UIKit

class ChatVC: BaseControllerVC {

    @IBOutlet weak var messageTf: UITextField!
    @IBOutlet weak var tableView: UITableView!
    override func viewDidLoad() {
        super.viewDidLoad()

        hideNavbar()
        addNavBar(headingText:"User",redText:"",type: .smallNavBarOnlyBack)
        setup()
        // Do any additional setup after loading the view.
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.tabBarController?.tabBar.isHidden = true
    }
    @IBAction func sendBtnClicked(_ sender: Any) {
    }
    func setup(){
        messageTf.paddingLeftRightTextField(left: 20, right: 0)
        messageTf.attributedPlaceholder = NSAttributedString(string: "Message....",attributes: [.foregroundColor:UIColor.lightGray])
        tableView.register(UINib(nibName: ChatHeaderCell.identifier, bundle: nil), forCellReuseIdentifier: ChatHeaderCell.identifier)
        tableView.register(UINib(nibName: ChatCell.identifier, bundle: nil), forCellReuseIdentifier: ChatCell.identifier)
        tableView.register(UINib(nibName: SendChatCell.identifier, bundle: nil), forCellReuseIdentifier: SendChatCell.identifier)
//        tableView.register(UINib(nibName: ChatHeaderView.identifier, bundle: nil), forHeaderFooterViewReuseIdentifier: ChatHeaderView.identifier)
        tableView.dataSource = self
        tableView.delegate = self
    }
}
extension ChatVC: UITableViewDelegate, UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 40
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.row == 0{
            let cell = tableView.dequeueReusableCell(withIdentifier: ChatHeaderCell.identifier, for: indexPath) as! ChatHeaderCell
            cell.profileImage.layer.cornerRadius = cell.profileImage.frame.height / 2
            cell.nameLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
            cell.totalDonationLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
            cell.amountLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX12)
            cell.detailsLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
            cell.selectionStyle = .none
            return cell
        }else{
            if indexPath.row % 2 == 0{
                let cell = tableView.dequeueReusableCell(withIdentifier: ChatCell.identifier, for: indexPath) as! ChatCell
                cell.messageLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
                cell.selectionStyle = .none
                cell.roundCorners()
                return cell
            }else{
                let cell = tableView.dequeueReusableCell(withIdentifier: SendChatCell.identifier, for: indexPath) as! SendChatCell
                cell.messageLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
                cell.selectionStyle = .none
                cell.roundCorners()
                return cell
            }
        }
    }
//    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
//        return 300
//    }
//    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
//        let view = self.tableView.dequeueReusableHeaderFooterView(withIdentifier: ChatHeaderView.identifier)  as! ChatHeaderView
//        return view
//    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableView.automaticDimension
    }
}
