//
//  NotificationVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 24/11/22.
//

import UIKit

class NotificationVC: BaseControllerVC {

    @IBOutlet weak var tableView: UITableView!
    var notificationData = [NotificationDataModel]()
    var customView: CustomRefreshControl!
    var page = 1
    var refreshControl = UIRefreshControl()
    var markedRead = false
    override func viewDidLoad() {
        super.viewDidLoad()
        hideNavbar()
        addNavBar(headingText: "Notifications", redText: "",type: .addNewCard,addNewCardSelector: #selector(markAllRead),addNewCardSelectorTitle: "Mark All As Read")
        self.pleaseWait()
        tableView.register(UINib(nibName: NotificationCell.identifier, bundle: nil), forCellReuseIdentifier: NotificationCell.identifier)
        let param = ["limit":"10","page":"\(page)"]
        getNotificationApi(param: param) {
            self.setTable()
            self.clearAllNotice()
        }
        
        // Do any additional setup after loading the view.
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
            tableView.refreshControl = refreshControl
        } else {
            tableView.addSubview(refreshControl)
        }
    }
    @objc func refresh(){
        refreshControl.beginRefreshing()
        self.customView.spinner.startAnimating()
        self.notificationData = []
        let param = ["limit":"15","page":"1"]
        getNotificationApi(param: param) {
            DispatchQueue.main.async {
                self.tableView.reloadData()
                self.refreshControl.endRefreshing()
                self.customView.spinner.stopAnimating()
            }
        }
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.tabBarController?.tabBar.isHidden = true
    }
    @objc func markAllRead(){
        if !markedRead{
            self.notificationData = []
            markedRead = true
            let param = ["limit":"10","page":"1","isAllRead":"true"]
            getNotificationApi(param: param) {
                DispatchQueue.main.async {
                    self.tableView.reloadData()
                }
            }
        }

    }
    func setTable(){
        DispatchQueue.main.async {
            self.tableView.dataSource = self
            self.tableView.delegate = self
            self.tableView.reloadData()
            self.addRefreshControl()
        }
    }
    //MARK: - Api Methods
    func getNotificationApiPagination(param:[String:String],completion:@escaping()->Void){
        DataManager.getNotifications(delegate: self, param: param) { json in
            json["notificationMessage"].forEach { (message,data) in
                self.notificationData.append(NotificationDataModel(data: data))
            }
            completion()
        }
    }
    func getNotificationApi(param:[String:String],completion:@escaping()->Void){
        var newdata = [NotificationDataModel]()
        DataManager.getNotifications(delegate: self, param: param) { json in
            json["notificationMessage"].forEach { (message,data) in
                newdata.append(NotificationDataModel(data: data))
            }
            self.notificationData = newdata
            completion()
        }
    }
    //MARK: - Pagination
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        let height = scrollView.frame.size.height
        let contentYOffset = scrollView.contentOffset.y
        let distanceFromBottom = scrollView.contentSize.height - contentYOffset
        if distanceFromBottom == height{
            page = page + 1
            let param = ["limit":"10","page":"\(page)"]
            getNotificationApiPagination(param: param){
                DispatchQueue.main.async {
                    self.tableView.reloadData()
                }
            }
        }
    }
}
//MARK: - Table View Delegate
extension NotificationVC: UITableViewDelegate, UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        print("COUNTTT",notificationData.count)
        return notificationData.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: NotificationCell.identifier, for: indexPath) as! NotificationCell
        print("FORRCOUNTTT",notificationData.count)
        if !notificationData.isEmpty{
            cell.updateCell(data: notificationData[indexPath.row])
        }
        return cell
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 70
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        print("NOTIFICATION",notificationData[indexPath.row].slug)
        if !notificationData[indexPath.row].isRead{
            let param = ["notificationId":notificationData[indexPath.row].id]
            DataManager.readNotification(delegate: self, param: param) {
                self.notificationData[indexPath.row].isRead = true
                AuthManager.currentUser.unReadNotificationCount -= 1
                self.tableView.reloadRows(at: [indexPath], with: .none)
            }
        }
        if notificationData[indexPath.row].slug != ""{
            if notificationData[indexPath.row].type == .userFollow{
                DispatchQueue.main.async {
                    let vc = VisitProfileVC()
                    vc.id = self.notificationData[indexPath.row].slug
                    self.navigationController?.pushViewController(vc, animated: true)
                }
            }else{
                DispatchQueue.main.async {
                    let vc = ViewVideoVC()
                    vc.errorDelegate = self
                    if self.notificationData[indexPath.row].type == .userVideo{
                        vc.visitingProfile = false
                    }
                    vc.slug = self.notificationData[indexPath.row].slug
                    self.navigationController?.pushViewController(vc, animated: true)
                }
            }
        }
    }
}
extension NotificationVC:ViewVideoErrorDelegate{
    func popAndShowError(error: String) {
        AlertView().showAlert(message: error, delegate: self, pop: false)
    }
}
