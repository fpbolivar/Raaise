//
//  FollowListVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 14/12/22.
//

import UIKit

class FollowListVC: BaseControllerVC {
    @IBOutlet weak var searchTf: UITextField!
    @IBOutlet weak var tableView: UITableView!
    var isFollowingList = false
    var userList : [UserListDataModel] = []
    override func viewDidLoad() {
        super.viewDidLoad()
        setup()
        hideNavbar()
        let param = ["limit":"30","page":"1"]
        if isFollowingList{
            addNavBar(headingText: "Following List", redText: "List")
            getUsersFollowerList(withParam: param, isForFollowing: true){
                DispatchQueue.main.async {
                    self.setTableView()
                }
            }

        }else{
           
            addNavBar(headingText: "Followers List", redText: "List")
            getUsersFollowerList(withParam: param, isForFollowing: false){
                DispatchQueue.main.async {
                    self.setTableView()
                }
            }
        }
        // Do any additional setup after loading the view.
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.tabBarController?.tabBar.isHidden = true
    }
    func setup(){
        searchTf.paddingLeftRightTextField(left: 35, right: 0)
        searchTf.attributedPlaceholder = NSAttributedString(string: "Search Users",attributes: [.foregroundColor: UIColor.lightGray])
        searchTf.layer.cornerRadius = 10
    }
    func setTableView(){
        tableView.register(UINib(nibName: SelectionCell.identifier, bundle: nil), forCellReuseIdentifier: SelectionCell.identifier)
        tableView.delegate = self
        tableView.dataSource = self
        tableView.reloadData()
    }
    func getUsersFollowerList(withParam param:[String:String],isForFollowing:Bool,completion:@escaping()->Void){
        DataManager.getUserFollowes(delegate: self, isForFollowing: isForFollowing, param: param) { errorMessage in
            print(errorMessage)
        } completion: { json in
            json["data"].forEach { (message,data) in
                print("EACHUSER",data)
                self.userList.append(UserListDataModel(data: data, isForFollowing: isForFollowing))
            }
            print("LISTCOUBY",self.userList.count)
            completion()
        }
    }
}
extension FollowListVC:UITableViewDelegate,UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return userList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: SelectionCell.identifier, for: indexPath) as! SelectionCell
        cell.followerList(data: self.userList[indexPath.row])
        cell.selectionStyle = .none
        return cell
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 70
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let vc = VisitProfileVC()
        vc.id = self.userList[indexPath.row].userId
        self.navigationController?.pushViewController(vc, animated: true)
    }
}
