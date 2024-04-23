//
//  InboxVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/21/22.
//

import UIKit

class InboxVC: BaseControllerVC,UIScrollViewDelegate {
    
    @IBOutlet weak var roomsView: UIView!
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var yourRoomsLbl: UILabel!
    @IBOutlet weak var searchTf: UITextField!
    @IBOutlet weak var notificationTable: UITableView!
    @IBOutlet weak var noResultLbl:UILabel!
    
    
    @IBOutlet weak var bannerImgView: UIImageView!
    @IBOutlet weak var bannerHeightConst: NSLayoutConstraint!
    
    var customView: CustomRefreshControl!
    var refreshControl = UIRefreshControl()
    var page = 1
    var roomPage = 1
    var chatListData:[ChatChannelModel] = []
    var liveRoomListData:[LiveRoomDataModel] = []
    var getBannerData = UserBannerModel()
    var tableSetup = false
    
//    self.yourRoomsLbl.isHidden = true
//    self.collectionView.isHidden = true
//    self.roomsView.isHidden = true
    
    override func viewDidLoad() {
        
        super.viewDidLoad()
        hideNavbar()
        print("CURRENTTIMEIN UNIX",Date().millisecondsSince1970)
        notificationTable.register(UINib(nibName: SelectionCell.identifier, bundle: nil), forCellReuseIdentifier: SelectionCell.identifier)
        collectionView.register(UINib(nibName: RoomCell.identifier, bundle: nil), forCellWithReuseIdentifier: RoomCell.identifier)
        searchTf.paddingLeftRightTextField(left: 35, right: 0)
        searchTf.layer.cornerRadius = 10
        searchTf.overrideUserInterfaceStyle = .light
        searchTf.delegate = self
//        addNavBar(headingText: "Chat", redText: "",type: .addRoom,addNewCardSelector: #selector(addRooms))
        addNavBar(headingText: "Chat", 
                  redText: "",
                  type: .onlyTopTitle,
                  color: UIColor(named: "bgColor"))
        searchTf.attributedPlaceholder = NSAttributedString(string: "Search Users",attributes: [.foregroundColor: UIColor.lightGray])
        setupCollectionView()
        noResultLbl.text = "No Chats Found"
        
        //Set banner image tap gesture
        setUI()
    }
    
    func setUI(){
        bannerImgView.isUserInteractionEnabled = true
        bannerImgView.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(openCoverImageLink)))
    }
    
    @objc func openCoverImageLink(){
        if let urlLink = self.getBannerData.link as? String
        {
            if urlLink != ""
            {
                if UIApplication.shared.canOpenURL(URL(string: urlLink)!)
                {
                    UIApplication.shared.open(URL(string: urlLink)!)
                }
            }
        }
    }
    
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        page = 1
        searchTf.text = ""
        noResultLbl.isHidden = true
        inboxData()
        scrollView.delegate = self
        self.tabBarController?.tabBar.isHidden = false
        
        getBannerApi(needloader: true){}
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
    func setupCollectionView(){
        DispatchQueue.main.async {
            self.collectionView.dataSource = self
            self.collectionView.delegate = self
            self.collectionView.reloadData()
            
            
        }
    }
    @objc func addRooms(){
        let alertController = UIAlertController(title: "Raaise", message: "Select Option", preferredStyle: .alert)
        
        // create the actions
        let createAction = UIAlertAction(title: "Create Room", style: .default) { _ in
            // code to handle create room action
            let vc = CreateRoomVC()
            self.navigationController?.pushViewController(vc, animated: true)
        }
        
        let joinPublicAction = UIAlertAction(title: "Join Public Room", style: .default) { _ in
            // code to handle join public room action
            let vc = PublicRoomVC()
            vc.hideTabbar = true
            self.navigationController?.pushViewController(vc, animated: true)
        }
        
        let cancelAction = UIAlertAction(title: "Cancel", style: .destructive) { _ in
            // code to handle cancel action
        }
        createAction.setValue(UIColor.white, forKey: "titleTextColor")
        joinPublicAction.setValue(UIColor.white, forKey: "titleTextColor")
        
        // add the actions to the alert controller
        alertController.addAction(createAction)
        alertController.addAction(joinPublicAction)
        alertController.addAction(cancelAction)
        
        // present the alert controller
        present(alertController, animated: true, completion: nil)
    }
    func inboxData(){
        DataManager.getUnreadChatCount(delegate: self) { json in
            if json["unreadMessageCount"].intValue > 0{
                self.tabBarController?.tabBar.items?[3].badgeValue = "\(json["unreadMessageCount"].intValue)"
                self.tabBarController?.tabBar.items?[3].badgeColor = .new_theme//.theme
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
        let roomParam = ["page":"\(roomPage)","limit":"10","query":""]
        getRoomListApi(param: roomParam) {
            DispatchQueue.main.async {
                self.collectionView.reloadData()
                if(self.liveRoomListData.isEmpty){
                    self.yourRoomsLbl.isHidden = true
                    self.collectionView.isHidden = true
                    self.roomsView.isHidden = true
                }else{
                    self.yourRoomsLbl.isHidden = true
                    self.collectionView.isHidden = true
                    self.roomsView.isHidden = true
                }
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
            if self.page == 1{
                self.chatListData = chat
            }else{
                self.chatListData.append(contentsOf: chat)
            }
            
            completion()
        }
    }
    func getRoomListApi(param:[String:String],completion:@escaping()->Void){
        DataManager.getRoomListAPI(delegate: self, param: param) { json in
            
            var liveRoomData:[LiveRoomDataModel] = []
            json["data"].forEach { (message,data) in
                print("MESAGE",message)
                liveRoomData.append(LiveRoomDataModel(json: data))
            }
            if self.roomPage == 1{
                self.liveRoomListData = liveRoomData
            }else{
                self.liveRoomListData.append(contentsOf: liveRoomData)
            }
            
            completion()
        }
    }
    func getBannerApi(needloader:Bool,completion:@escaping()->Void){
        needloader ? self.pleaseWait() : print("NOLOADER")
        DataManager.getBannerApi(delegate: self) { [self] jsonData in
            self.getBannerData = UserBannerModel()
            self.getBannerData = UserBannerModel(data: jsonData["data"])
//            jsonData["data"].forEach { (message,data) in
//                
//            }
            
            debugPrint("getBannerData\(getBannerData.image)")
            if getBannerData.image != ""{
                bannerImgView.loadImgForCover(url: getBannerData.image)
            }else{
                bannerImgView.isHidden = true
                bannerHeightConst.constant = 0
            }
            needloader ? self.clearAllNotice() : print("NOLOADER")
            completion()
            guard let _ = self.customView else{return}
            
            self.customView.spinner.stopAnimating()
            
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
        //        if #available(iOS 10.0, *) {
        //            notificationTable.refreshControl = refreshControl
        //        } else {
        //            notificationTable.addSubview(refreshControl)
        //        }
        scrollView.refreshControl = refreshControl
    }
    @objc func refresh(){
        refreshControl.beginRefreshing()
        self.customView.spinner.startAnimating()
        inboxData()
    }
    //MARK: - Pagination
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        
        if let _ = scrollView as? UICollectionView{
            let height = scrollView.frame.size.height
            let contentYOffset = scrollView.contentOffset.y
            let distanceFromBottom = scrollView.contentSize.height - contentYOffset
            
            if distanceFromBottom == height{
                roomPage = roomPage + 1
                let roomParam = ["page":"\(roomPage)","limit":"10","query":""]
                getRoomListApi(param: roomParam) {
                    DispatchQueue.main.async {
                        self.collectionView.reloadData()
                        if(self.liveRoomListData.isEmpty){
                            self.yourRoomsLbl.isHidden = true
                            self.collectionView.isHidden = true
                            self.roomsView.isHidden = true
                        }else{
                            self.yourRoomsLbl.isHidden = false
                            self.collectionView.isHidden = false
                            self.roomsView.isHidden = false
                        }
                    }
                }
            }
        }else{
            let height = scrollView.frame.size.height
            let contentYOffset = scrollView.contentOffset.y
            let distanceFromBottom = scrollView.contentSize.height - contentYOffset
            
            if distanceFromBottom <= height{
                
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
//MARK: - Collection View Delegate
extension InboxVC:UICollectionViewDelegate,UICollectionViewDataSource,UICollectionViewDelegateFlowLayout{
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return liveRoomListData.count
    }
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: RoomCell.identifier, for: indexPath) as! RoomCell
        cell.descriptionLbl.isHidden = true
        cell.updateCell(data: liveRoomListData[indexPath.row])
        return cell
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: 80, height: collectionView.frame.height)
    }
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let vc = RoomDetailVC()
        vc.roomData = liveRoomListData[indexPath.row]
        self.navigationController?.pushViewController(vc, animated: true)
    }
}
