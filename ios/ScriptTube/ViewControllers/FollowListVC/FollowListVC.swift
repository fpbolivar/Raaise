//
//  FollowListVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 14/12/22.
//

import UIKit

class FollowListVC: BaseControllerVC {
    
    @IBOutlet weak var searchCardHeightCOnst: NSLayoutConstraint!
    @IBOutlet weak var searchTopConst: NSLayoutConstraint!
    
    @IBOutlet weak var searchCardView: CardView!
    
    @IBOutlet weak var noResultLbl:UILabel!
    @IBOutlet weak var searchTf: UITextField!
    @IBOutlet weak var tableView: UITableView!
    
    
    var isFromProfilePager = false
    var isFollowingList = false
    var userList : [UserListDataModel] = []
    var userName:String?
    var page = 1
    var tableSetup = false
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //Hide when open from visit profile
        if isFromProfilePager{
            searchCardHeightCOnst.constant = 0
            searchTopConst.constant = 0
            searchTf.isHidden = true
            searchCardView.isHidden = true
        }
        else{
            setup()
            hideNavbar()
            if isFollowingList{
                addNavBar(headingText: "Following List",
                          redText: "List",
                          color: UIColor(named: "bgColor")
                )
            }else{
                addNavBar(headingText: "Followers List",
                          redText: "List",
                          color: UIColor(named: "bgColor"))
                
            }
        }
        
        getData(needLoader: true){
            DispatchQueue.main.async {
                self.noResultLbl.isHidden = !self.userList.isEmpty
            }
        }// Do any additional setup after loading the view.
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.tabBarController?.tabBar.isHidden = true
    }
 
    //MARK: - Setup
    func setup(){
        searchTf.paddingLeftRightTextField(left: 35, right: 0)
        searchTf.attributedPlaceholder = NSAttributedString(string: "Search Users",attributes: [.foregroundColor: UIColor.lightGray])
        searchTf.layer.cornerRadius = 10
        searchTf.delegate = self
    }
    func setTableView(){
        tableSetup = true
        tableView.register(UINib(nibName: SelectionCell.identifier, bundle: nil), forCellReuseIdentifier: SelectionCell.identifier)
        tableView.delegate = self
        tableView.dataSource = self
        tableView.reloadData()
    }
    //MARK: - Api Methods
    func getData(searchText:String = "",needLoader:Bool = false,completion:@escaping()->Void){
        let param = ["limit":"30","page":"\(page)","search":"\(searchText)"]
        if isFollowingList{
            if userName != nil{
                let param2 = ["userName":userName!,"limit":"30","page":"\(page)","search":"\(searchText)"]
                if(!(Constant.check_Internet?.isReachable)!){
                    AlertView().showInternetErrorAlert(delegate: self)
                    return
                }
                getOtherUsersFollowerList(withParam: param2, isForFollowing: true, needLoader: needLoader) {
                    DispatchQueue.main.async {
                        self.tableSetup ? self.tableView.reloadData() : self.setTableView()
                        completion()
                    }
                }
            }else{
                if(!(Constant.check_Internet?.isReachable)!){
                    AlertView().showInternetErrorAlert(delegate: self)
                    return
                }
                getUsersFollowerList(withParam: param, isForFollowing: true, needLoader: needLoader){
                    DispatchQueue.main.async {
                        self.tableSetup ? self.tableView.reloadData() : self.setTableView()
                        completion()
                    }
                }
            }
        }else{
            if userName != nil{
                let param2 = ["userName":userName!,"limit":"30","page":"\(page)","search":"\(searchText)"]
                if(!(Constant.check_Internet?.isReachable)!){
                    AlertView().showInternetErrorAlert(delegate: self)
                    return
                }
                getOtherUsersFollowerList(withParam: param2, isForFollowing: false, needLoader: needLoader) {
                    DispatchQueue.main.async {
                        self.tableSetup ? self.tableView.reloadData() : self.setTableView()
                        completion()
                    }
                }
            }else{
                if(!(Constant.check_Internet?.isReachable)!){
                    AlertView().showInternetErrorAlert(delegate: self)
                    return
                }
                getUsersFollowerList(withParam: param, isForFollowing: false, needLoader: needLoader){
                    DispatchQueue.main.async {
                        self.tableSetup ? self.tableView.reloadData() : self.setTableView()
                        completion()
                    }
                }
            }
        }
    }
    
    
    func getOtherUsersFollowerList(withParam param:[String:String],isForFollowing:Bool,needLoader:Bool,completion:@escaping()->Void){
        DataManager.getOtherUserFollowes(delegate: self, isForFollowing: isForFollowing, needLoader: needLoader, param: param) { errorMessage in
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
    func getUsersFollowerList(withParam param:[String:String],isForFollowing:Bool,needLoader:Bool,completion:@escaping()->Void){
        DataManager.getUserFollowes(delegate: self, isForFollowing: isForFollowing, param: param, needLoader: needLoader) { errorMessage in
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
    //MARK: - Pagination
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        guard let scroll = scrollView as? UITableView else{return}
        let height = scroll.frame.size.height
        let contentYOffset = scroll.contentOffset.y
        let distanceFromBottom = scroll.contentSize.height - contentYOffset
        if distanceFromBottom == height{
            page = page + 1
            getData(searchText: searchTf.text!){
                
            }
        }
    }
}
//MARK: - Search Delegates
extension FollowListVC:UITextFieldDelegate{
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
        userList = []
        if !textField.text!.isEmpty{
            getData(searchText: textField.text!){
                DispatchQueue.main.async {
                    self.noResultLbl.isHidden = !self.userList.isEmpty
                }
            }
        }else{
            getData(){
                DispatchQueue.main.async {
                    self.noResultLbl.isHidden = !self.userList.isEmpty
                }
            }
        }
    }
}
