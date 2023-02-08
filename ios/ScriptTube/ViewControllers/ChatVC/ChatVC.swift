//
//  ChatVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 23/11/22.
//

import UIKit

class ChatVC: BaseControllerVC {
    @IBOutlet weak var messageLbl: UILabel!
    @IBOutlet weak var sendBtn: UIButton!
    let ch = SocketChatManager()
    var page = 1
    var chatSlug = ""
    var chatData = [ChatModel]()
    var otherUser = UserProfileData()
    @IBOutlet weak var messageTf: CustomTextView!
    @IBOutlet weak var tableView: UITableView!
    override func viewDidLoad() {
        super.viewDidLoad()
        hideNavbar()
        if otherUser.isDeleted{
            messageTf.isHidden = true
            sendBtn.isHidden = true
            messageLbl.isHidden = false
        }
        getchat {
            self.setupTable()
            self.scrollToLastMsg()
        }
        ch.listenDelete(slug: chatSlug) { data in
            print("DELETECHATDATA",data)
            let i = self.chatData.firstIndex { chat in
                chat.id == data
            }
            guard let i = i else{return}
            self.chatData.remove(at: i)
            DispatchQueue.main.async {
                self.tableView.deleteRows(at: [IndexPath(row: i, section: 1)], with: .fade)
            }
        }
        ch.setupSocketEvents()
        ch.listenSlug(slug: chatSlug){ data in
            self.appendChat(data: data)
        }
        ch.listenUser(otherUser: otherUser.id) { data in
            self.appendChat(data: data)
            self.chatSlug = data["chatSlug"].stringValue
           
        }
        ch.getChat { data in
            self.appendChat(data: data)
        }
        ch.register(user: AuthManager.currentUser.id)
        ch.connect()
        addNavBar(headingText:otherUser.name.localizedCapitalized,redText:"",type: .smallNavBarOnlyBack)
        setup()
        // Do any additional setup after loading the view.
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.tabBarController?.tabBar.isHidden = true
    }
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        ch.stop()
    }
    //MARK: - Append Chat
    func appendChat(data:JSON){
        
        self.chatData.append(ChatModel(data: data))
        DispatchQueue.main.async {
            
            if self.tableView.numberOfRows(inSection: 1) == self.chatData.count - 1{
                self.tableView.insertRows(at: [IndexPath(row: self.chatData.count - 1, section: 1)], with: .bottom)
                
            }else{
                let arr = (self.tableView.numberOfRows(inSection: 1)..<(self.chatData.count)).map({IndexPath(row: $0, section: 1)})
                self.tableView.beginUpdates()
                self.tableView.insertRows(at: arr, with: .bottom)
                self.tableView.endUpdates()
               
            }
        }
        self.scrollToLastMsg()
        
    }
    @IBAction func sendBtnClicked(_ sender: Any) {
        
        if !messageTf.text.trimmingCharacters(in: .whitespaces).isEmpty{
            
            let message = ["message":messageTf.text!,
                           "userId":AuthManager.currentUser.id,
                           "receiverId":otherUser.id,
                           "senderId":AuthManager.currentUser.id
                           ,"messageType":"Text",
                           "slug":chatSlug]
            ch.send(message: message)
            messageTf.text = ""
        }else{
            messageTf.text = ""
        }
    }
    func scrollToLastMsg(){
        if chatData.count > 1{
            DispatchQueue.main.async {
                self.tableView.scrollToRow(at: IndexPath(row: self.tableView.numberOfRows(inSection: 1) - 1, section: 1), at: .bottom, animated: false)
            }
        }
    }
    //MARK: - Api Method
    func getchat(pagination:Bool = false,completion:@escaping()->Void){
        let param = ["limit":"15","page":"\(page)","chatSlug":chatSlug]
        print("GETCHATPARAm",param)
        DataManager.getChatApi(delegate: self, param: param) { json in
            print("chatget",json)
            json["data"].forEach { (_,data) in
                if pagination{
                    self.chatData.insert(ChatModel(data: data), at:0)
                }else{
                    self.chatData.append(ChatModel(data: data))
                }
            }
            completion()
        }
    }
    //MARK: - Setup
    func setup(){
        tableView.register(UINib(nibName: ChatHeaderCell.identifier, bundle: nil), forCellReuseIdentifier: ChatHeaderCell.identifier)
        tableView.register(UINib(nibName: ChatCell.identifier, bundle: nil), forCellReuseIdentifier: ChatCell.identifier)
        tableView.register(UINib(nibName: SendChatCell.identifier, bundle: nil), forCellReuseIdentifier: SendChatCell.identifier)
        tableView.register(UINib(nibName: SendVideoCell.identifier, bundle: nil), forCellReuseIdentifier: SendVideoCell.identifier)
        tableView.register(UINib(nibName: ChatVideoCell.identifier, bundle: nil), forCellReuseIdentifier: ChatVideoCell.identifier)
    }
    func setupTable(){
        DispatchQueue.main.async {
            self.tableView.dataSource = self
            self.tableView.delegate = self
            self.tableView.reloadData()
        }
    }
    //MARK: - Pagination
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        guard let scroll = scrollView as? UITableView else{
            return
        }
        if scroll.contentOffset.y == 0{
            print("TOPPPPP")
            page = page + 1
            getchat(pagination: true) {
                self.tableView.reloadData()
            }
        }
    }
}
extension ChatVC: UITableViewDelegate, UITableViewDataSource{
    func numberOfSections(in tableView: UITableView) -> Int {
        return 2
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if section == 0{
            return 1
        }else{
            return chatData.count
        }
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.section == 0{
            let cell = tableView.dequeueReusableCell(withIdentifier: ChatHeaderCell.identifier, for: indexPath) as! ChatHeaderCell
            cell.profileImage.layer.cornerRadius = cell.profileImage.frame.height / 2
            cell.updateCell(data: otherUser)
            cell.selectionStyle = .none
            return cell
        }else{
            if !chatData[indexPath.row].otherUser.sender{
                switch chatData[indexPath.row].messageType{
                case .text:
                    let cell = tableView.dequeueReusableCell(withIdentifier: ChatCell.identifier, for: indexPath) as! ChatCell
                    cell.messageLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
                    cell.delegate = self
                    cell.deleteDelegate = self
                    cell.timeLbl.font = AppFont.FontName.regular.getFont(size: 8)
                    cell.selectionStyle = .none
                    cell.roundCorners()
                    cell.updateCell(data: chatData[indexPath.row])
                    return cell
                case .video:
                    let cell = tableView.dequeueReusableCell(withIdentifier: ChatVideoCell.identifier, for: indexPath) as! ChatVideoCell
                    cell.delegate = self
                    cell.videoDelegate = self
                    cell.timeLbl.font = AppFont.FontName.regular.getFont(size: 8)
                    cell.selectionStyle = .none
                    cell.updateCell(data: chatData[indexPath.row])
                    return cell
                case .none:
                    AlertView().showAlert(message: "No Known Chat cell", delegate: self, pop: false)
                    return UITableViewCell()
                }
                
            }else{
                switch chatData[indexPath.row].messageType{
                case .text:
                    let cell = tableView.dequeueReusableCell(withIdentifier: SendChatCell.identifier, for: indexPath) as! SendChatCell
                    cell.messageLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
                    cell.deleteDelegate = self
                    cell.timeLbl.font = AppFont.FontName.regular.getFont(size: 8)
                    cell.selectionStyle = .none
                    cell.roundCorners()
                    cell.updateCell(data: chatData[indexPath.row])
                    return cell
                case .video:
                    let cell = tableView.dequeueReusableCell(withIdentifier: SendVideoCell.identifier, for: indexPath) as! SendVideoCell
                    cell.delegate = self
                    cell.deleteDelegate = self
                    cell.timeLbl.font = AppFont.FontName.regular.getFont(size: 8)
                    cell.selectionStyle = .none
                    cell.updateCell(data: chatData[indexPath.row])
                    return cell
                    
                case .none:
                    AlertView().showAlert(message: "No Known Chat cell", delegate: self, pop: false)
                    return UITableViewCell()
                }
                
            }
        }
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return UITableView.automaticDimension
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
    }
}

extension ChatVC:ChatDelegate,ChatVideoDelegate{
    func viewVideo(withSlug slug: String) {
        let vc = ViewVideoVC()
        vc.slug = slug
        self.navigationController?.pushViewController(vc, animated: false)
    }
    
    func visitOtherUserProfile() {
        let vc = VisitProfileVC()
        vc.id = self.otherUser.id
        self.navigationController?.pushViewController(vc, animated: true)
    }
}
extension ChatVC:DeleteChatDelegate{
    func deleteChat(withId id: String) {
        let param = ["id":id,"slug":chatSlug]
        ch.deleteMsg(param: param)
    }
}
