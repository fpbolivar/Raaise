//
//  InboxVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/21/22.
//

import UIKit

class InboxVC: BaseControllerVC {

    @IBOutlet weak var searchTf: UITextField!
    @IBOutlet weak var notificationTable: UITableView!
    override func viewDidLoad() {
        super.viewDidLoad()
        //hideNavbar()
        searchTf.paddingLeftRightTextField(left: 35, right: 0)
        searchTf.layer.cornerRadius = 10
        searchTf.overrideUserInterfaceStyle = .light
        setTableView()
        addNavBar(headingText: "Chat", redText: "")
        searchTf.attributedPlaceholder = NSAttributedString(string: "Search Users",attributes: [.foregroundColor: UIColor.lightGray])
        // Do any additional setup after loading the view.
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.tabBarController?.tabBar.isHidden = false
    }
    func setTableView(){
        notificationTable.register(UINib(nibName: SelectionCell.identifier, bundle: nil), forCellReuseIdentifier: SelectionCell.identifier)
        notificationTable.dataSource = self
        notificationTable.delegate = self
    }
}
extension InboxVC: UITableViewDelegate, UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 10
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 70
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: SelectionCell.identifier, for: indexPath) as! SelectionCell
        cell.nameLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        cell.detailLbl.font = AppFont.FontName.regular.getFont(size: 8)
        cell.timeLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
        cell.selectionStyle = .none
        cell.chatList()
        cell.containerView.backgroundColor = UIColor(named: "TFcolor")
        //cell.updateCell()
        return cell
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
            let vc = ChatVC()
            self.navigationController?.pushViewController(vc, animated: true)
        
    }
}
