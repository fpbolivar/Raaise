//
//  SharePopUp.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 02/01/23.
//

import UIKit

class SharePopUp: BaseControllerVC {
    @IBOutlet weak var searchTf:UITextField!
    @IBOutlet weak var otherImg:UIImageView!
    @IBOutlet weak var tableView:UITableView!
    @IBOutlet weak var noResultLbl:UILabel!
    var userListData:[ChatChannelModel] = []
    var delegate:SharePopUpDelegate?
    var videoId = ""
    var receiverId = ""
    var page = 1
    override func viewDidLoad() {
        super.viewDidLoad()
        hideNavbar()
        searchTf.attributedPlaceholder = NSAttributedString(string: "Search Users",attributes: [.foregroundColor: UIColor.lightGray])
        searchTf.paddingLeftRightTextField(left: 35, right: 0)
        searchTf.layer.cornerRadius = 10
        searchTf.overrideUserInterfaceStyle = .light
        tableView.register(UINib(nibName: SelectionCell.identifier, bundle: nil), forCellReuseIdentifier: SelectionCell.identifier)
        otherImg.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(shareToOtherApp)))
        searchTf.delegate = self
        let param = ["limit":"10","page":"\(page)"]
        getChatListApi(param: param) {
            self.setupTable()
        }
        // Do any additional setup after loading the view.
    }
    @objc func shareToOtherApp(){
        delegate?.shareToOtherApp()
    }
    
    @IBAction func dismissBtn(_ sender: Any) {
        self.dismiss(animated: true)
    }
    func getChatListApi(param:[String:String],completion:@escaping()->Void){
        
        DataManager.getChatListAPI(delegate: self, param: param) { json in
            //
            json["data"].forEach { (message,data) in
                print("MESAGE",message)
                self.userListData.append(ChatChannelModel(data: data))
            }
            completion()
        }
    }
    func sharePost(completion:@escaping(String)->Void){
        let param = ["receiverId":receiverId,"senderId":AuthManager.currentUser.id,"videoId":videoId]
        AuthManager.shareVideoToChatApi(delegate: self, param: param) { data in
            let videoDetail = data["data"]["videoId"]
            completion(videoDetail["videoShareCount"].stringValue)
        }
    }
    func setupTable(){
        DispatchQueue.main.async {
            self.tableView.delegate = self
            self.tableView.dataSource = self
            self.tableView.reloadData()
        }
    }
}
extension SharePopUp:UITableViewDelegate,UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return userListData.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: SelectionCell.identifier, for: indexPath) as! SelectionCell
        cell.selectionStyle = .none
        cell.sendList(data: userListData[indexPath.row])
        return cell
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        self.receiverId = userListData[indexPath.row].otherUser.id
        self.dismiss(animated: true){
            self.sharePost { count in
                self.delegate?.newShareCount(count: count,otherUser:self.userListData[indexPath.row].otherUser,slug:self.userListData[indexPath.row].slug)
                
               // self.dismiss(animated: true)
            }
        }
        
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 70
    }
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        let height = scrollView.frame.size.height
        let contentYOffset = scrollView.contentOffset.y
        let distanceFromBottom = scrollView.contentSize.height - contentYOffset
        if distanceFromBottom == height{
            print("YYYYYYYYYYYYY")
            page = page + 1
            let param = ["limit":"10","page":"\(page)"]
            getChatListApi(param: param) {
                self.tableView.reloadData()
            }
        }
    }
}
protocol SharePopUpDelegate{
    func shareToOtherApp()
    func newShareCount(count:String,otherUser:UserProfileData,slug:String)
}
extension SharePopUp:UITextFieldDelegate{
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        //print(textField.text)
        NSObject.cancelPreviousPerformRequests(
            withTarget: self,
            selector: #selector(getHintsFromTextField),
            object: textField)
        self.perform(
            #selector(getHintsFromTextField),
            with: textField,
            afterDelay: 0.5)
        return true
//        return true
    }
    @objc func getHintsFromTextField(textField: UITextField) {
        print("Hints for textField: \(textField.text)")
        if !textField.text!.isEmpty{
            print(textField.text)
            let param = ["search":textField.text!,"limit":"10","page":"1"]
            self.userListData = []
            getChatListApi(param: param) {
                DispatchQueue.main.async {
                    self.tableView.reloadData()
                    if self.userListData.isEmpty{
                        self.noResultLbl.isHidden = false
                    }else{
                        self.noResultLbl.isHidden = true
                    }
                }
            }
        }else{
            let param = ["limit":"10","page":"1"]
            self.userListData = []
            getChatListApi(param: param) {
                DispatchQueue.main.async {
                    self.tableView.reloadData()
                    if self.userListData.isEmpty{
                        self.noResultLbl.isHidden = false
                    }else{
                        self.noResultLbl.isHidden = true
                    }
                }
            }
        }
    }
}
