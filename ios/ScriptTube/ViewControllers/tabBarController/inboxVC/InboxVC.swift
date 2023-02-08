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
    @IBOutlet weak var noResultLbl:UILabel!
    var customView: CustomRefreshControl!
    var refreshControl = UIRefreshControl()
    var page = 1
    var chatListData:[ChatChannelModel] = []
    var tableSetup = false
    override func viewDidLoad() {
        super.viewDidLoad()
        hideNavbar()
        notificationTable.register(UINib(nibName: SelectionCell.identifier, bundle: nil), forCellReuseIdentifier: SelectionCell.identifier)
        searchTf.paddingLeftRightTextField(left: 35, right: 0)
        searchTf.layer.cornerRadius = 10
        searchTf.overrideUserInterfaceStyle = .light
        searchTf.delegate = self
        addNavBar(headingText: "Chat", redText: "",type: .onlyTopTitle)
        searchTf.attributedPlaceholder = NSAttributedString(string: "Search Users",attributes: [.foregroundColor: UIColor.lightGray])
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        page = 1
        searchTf.text = ""
        noResultLbl.isHidden = true
        inboxData()
        self.tabBarController?.tabBar.isHidden = false
    }
    //MARK: - Setup
    func setTableView(){
        DispatchQueue.main.async {
            self.notificationTable.dataSource = self
            self.notificationTable.delegate = self
            self.notificationTable.reloadData()
            self.tableSetup = true
            self.addRefreshControl()
        }
    }
    func inboxData(){
        DataManager.getUnreadChatCount(delegate: self) { json in
            if json["unreadMessageCount"].intValue > 0{
                self.tabBarController?.tabBar.items?[3].badgeValue = "\(json["unreadMessageCount"].intValue)"
                self.tabBarController?.tabBar.items?[3].badgeColor = .theme
            }else{
                self.tabBarController?.tabBar.items?[3].badgeValue =  nil
            }
        }
        let param = ["limit":"10","page":"\(page)"]
        getChatListApi(param: param){
            DispatchQueue.main.async {
                self.notificationTable.isUserInteractionEnabled = false
                self.tableSetup ?  self.notificationTable.reloadData() : self.setTableView()
                self.notificationTable.isUserInteractionEnabled = true
                guard let _ = self.customView else{return}
                self.refreshControl.endRefreshing()
                self.customView.spinner.stopAnimating()
                self.noResultLbl.isHidden = !self.chatListData.isEmpty
            }
        }
    }
    func getChatListApi(param:[String:String],completion:@escaping()->Void){
        DataManager.getChatListAPI(delegate: self, param: param) { json in
            var chat:[ChatChannelModel] = []
            json["data"].forEach { (message,data) in
                print("MESAGE",message)
                chat.append(ChatChannelModel(data: data))
            }
            self.chatListData = chat
            completion()
        }
    }
    func addRefreshControl() {

        guard let customView = Bundle.main.loadNibNamed("RefreshContents", owner: nil, options: nil) else {
            return
        }

        guard let refreshView = customView[0] as? CustomRefreshControl else {
            return
        }


        refreshView.frame = refreshControl.frame
        self.customView = refreshView
        refreshControl.addSubview(refreshView)

        refreshControl.tintColor = UIColor.clear
        refreshControl.backgroundColor = UIColor.clear

        refreshControl.addTarget(self, action: #selector(refresh), for: .valueChanged)
        if #available(iOS 10.0, *) {
            notificationTable.refreshControl = refreshControl
        } else {
            notificationTable.addSubview(refreshControl)
        }
    }
    @objc func refresh(){
        refreshControl.beginRefreshing()
        self.customView.spinner.startAnimating()
        inboxData()
    }
    //MARK: - Pagination
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        let height = scrollView.frame.size.height
        let contentYOffset = scrollView.contentOffset.y
        let distanceFromBottom = scrollView.contentSize.height - contentYOffset
        if distanceFromBottom == height{
            print("YYYYYYYYYYYYY")
            page = page + 1
            let param = ["limit":"10","page":"\(page)"]
            getChatListApi(param: param) {
                DispatchQueue.main.async {
                    self.notificationTable.isUserInteractionEnabled = false
                    self.notificationTable.reloadData()
                    self.notificationTable.isUserInteractionEnabled = true
                }
            }
        }
    }

}
//MARK: - Table View Delegate
extension InboxVC: UITableViewDelegate, UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return chatListData.count
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 70
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: SelectionCell.identifier, for: indexPath) as! SelectionCell
       
        cell.selectionStyle = .none
        cell.chatList(data: chatListData[indexPath.row])
        return cell
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let vc = ChatVC()
        vc.otherUser = chatListData[indexPath.row].otherUser
        vc.chatSlug = chatListData[indexPath.row].slug
        self.navigationController?.pushViewController(vc, animated: true)
        
    }
}
//MARK: - Search Delegate
extension InboxVC:UITextFieldDelegate{
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        NSObject.cancelPreviousPerformRequests(
            withTarget: self,
            selector: #selector(getHintsFromTextField),
            object: textField)
        self.perform(
            #selector(getHintsFromTextField),
            with: textField,
            afterDelay: 0.5)
        return true
    }
    @objc func getHintsFromTextField(textField: UITextField) {
        if !textField.text!.isEmpty{
            let param = ["search":textField.text!,"limit":"10","page":"1"]
            getChatListApi(param: param) {
                DispatchQueue.main.async {
                    self.noResultLbl.isHidden = !self.chatListData.isEmpty
                    self.notificationTable.reloadData()
                }
            }
        }else{
            let param = ["limit":"10","page":"1"]
            getChatListApi(param: param) {
                DispatchQueue.main.async {
                    self.noResultLbl.isHidden = !self.chatListData.isEmpty
                    self.notificationTable.reloadData()
                }
            }
        }
    }
}
