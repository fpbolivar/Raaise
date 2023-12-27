//
//  RoomDetailVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 25/04/23.
//

import UIKit

class RoomDetailVC: BaseControllerVC {
    
    @IBOutlet weak var callView: CardView!
    @IBOutlet weak var stackView: UIStackView!
    @IBOutlet weak var tableView: ContentSizedTableView!
    @IBOutlet weak var descriptionLbl: UILabel!
    @IBOutlet weak var nameLbl: UILabel!
    @IBOutlet weak var editImage: UIImageView!
    @IBOutlet weak var roomImage: UIImageView!
    @IBOutlet weak var cameraBtn: UIButton!
    @IBOutlet weak var micBtn: UIButton!
    var isJoin = false
    var roomData = LiveRoomDataModel()
    override func viewDidLoad() {
        super.viewDidLoad()
        setupBtns()
        callView.isHidden = true
        stackView.isHidden = true
        tableView.register(UINib(nibName: RoomMemberCell.identifier, bundle: nil), forCellReuseIdentifier: RoomMemberCell.identifier)
        if isJoin{
            callView.isHidden = true
            stackView.isHidden = true
            editImage.isHidden = true
            addNavBar(headingText: "", redText: "",type: .joinRoom,addNewCardSelector: #selector(joinRoom))
            
        }else{
            addNavBar(headingText: "", redText: "",type: .leaveRoom,addNewCardSelector: #selector(leaveRoom))
            let param = ["slug":roomData.slug]
            getPublicRoomListApi(param: param) {
                self.setupData()
                if(self.roomData.scheduleDateTime == ""){
                    self.callView.isHidden = false
                    self.stackView.isHidden = false
                }else{
                    if self.compareDates(){
                        self.callView.isHidden = false
                        self.stackView.isHidden = false
                    }else{
                        AlertView().showAlert(message: "This room is shceduled to go live at \(self.roomData.scheduleDateTime)", delegate: self, pop: false)
                    }
                }
            }
        }
        
        editImage.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(gotoEditRoom)))
        
        setupData()
        
        // Do any additional setup after loading the view.
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        //setupData()
        self.tabBarController?.tabBar.isHidden = true
    }
    func compareDates()->Bool{
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
        let futureDate = dateFormatter.date(from: roomData.scheduleDateTime)!

        let calendar = Calendar.current
        let comparisonResult = calendar.compare(Date(), to: futureDate, toGranularity: .second)

        if comparisonResult == .orderedDescending {
            
            print("The future date has passed.")
            return true
        } else if comparisonResult == .orderedAscending {
            
            print("The future date is still in the future.")
            return false
        } else {
            
            print("The future date is now.")
            return true
        }
    }
    func setupData(){
        DispatchQueue.main.async {
            print("LOGOOOO",self.roomData.logo)
            print("IDDDDDDD",self.roomData.hostId,AuthManager.currentUser.id)
            if self.roomData.hostId == AuthManager.currentUser.id{
                self.editImage.isHidden = false
            }else{
                self.editImage.isHidden = true
            }
            self.roomImage.loadImg(url: self.roomData.logo)
            self.nameLbl.text = self.roomData.title
            self.descriptionLbl.text = self.roomData.description
            self.setupTableView()
        }
        
    }
    @IBAction func micBtnClicked(_ sender: UIButton) {
        sender.checkboxAnimation {
            
        }
    }
    
    @IBAction func cameraBtnClicked(_ sender: UIButton) {
        sender.checkboxAnimation {
            
        }
    }
    func setupBtns(){
        micBtn.setImage(UIImage(systemName: "mic.slash"), for: .selected)//
        micBtn.setImage(UIImage(systemName: "mic"), for: .normal)
        cameraBtn.setImage(UIImage(systemName: "video.slash.fill"), for: .selected)
        cameraBtn.setImage(UIImage(systemName: "video.fill"), for: .normal)
    }
    func getPublicRoomListApi(param:[String:String],completion:@escaping()->Void){
        DataManager.getPublicRoomBySlugAPI(delegate: self, param: param) { json in
            self.roomData = LiveRoomDataModel(json: json["data"])
            completion()
        }
    }
    @objc func joinRoom(_ sender:UIButton){
        self.joinApi{
            sender.isHidden = true
            let param = ["slug":self.roomData.slug]
            self.getPublicRoomListApi(param: param) {
                self.setupData()
                self.callView.isHidden = false
                self.stackView.isHidden = false
            }
        }
    }
    @objc func gotoEditRoom(){
        let vc = EditRoomVC()
        vc.updateDelegate = self
        vc.roomData = self.roomData
        self.navigationController?.pushViewController(vc, animated: true)
    }
    @objc func leaveRoom(_ sender:UIButton){
        let alertController = UIAlertController(title: "Raaise", message: "Are you sure you want to leave this room?", preferredStyle: .alert)
        
        // create the actions
        let leaveAction = UIAlertAction(title: "Leave Room", style: .destructive) { _ in
            // code to handle create room action
            self.leaveApi()
        }
        let cancelAction = UIAlertAction(title: "Cancel", style: .default) { _ in
            // code to handle cancel action
        }
        cancelAction.setValue(UIColor.white, forKey: "titleTextColor")
        // add the actions to the alert controller
        alertController.addAction(leaveAction)
        
        alertController.addAction(cancelAction)
        
        // present the alert controller
        present(alertController, animated: true, completion: nil)
    }
    func joinApi(completion:@escaping()->Void){
        let param = ["slug":self.roomData.slug]
        AuthManager.joinLiveRoom(delegate: self, param: param) {
            completion()
            AlertView().showAlert(message: "Room Joined", delegate: self, pop: false)
        }
    }
    func leaveApi(){
        let param = ["slug":self.roomData.slug]
        AuthManager.leaveLiveRoom(delegate: self, param: param) {
            self.navigationController?.popViewController(animated: true)
        }
    }
    func setupTableView(){
        tableView.delegate = self
        tableView.dataSource = self
        tableView.reloadData()
    }
    @IBAction func callBtnClicked(_ sender: Any) {
        let vc = MeetingVC()
        vc.needCam = !cameraBtn.isSelected
        vc.needMic = !micBtn.isSelected
        vc.roomData = self.roomData
        self.navigationController?.pushViewController(vc, animated: true)
    }
}
extension RoomDetailVC:UITableViewDelegate,UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return roomData.members.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: RoomMemberCell.identifier, for: indexPath) as! RoomMemberCell
        cell.updateCell(data: roomData.members[indexPath.row])
        return cell
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 80
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let vc = VisitProfileVC()
        vc.id = roomData.members[indexPath.row].id
        self.navigationController?.pushViewController(vc, animated: true)
    }
}
extension RoomDetailVC:UpdateRoom{
    func roomUpdateSuccess(newData: LiveRoomDataModel) {
        self.roomData.logo = newData.logo
        print("LOGOOOO2",self.roomData.logo)
        setupData()
    }
}
