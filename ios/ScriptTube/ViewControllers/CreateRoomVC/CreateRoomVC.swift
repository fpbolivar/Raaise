//
//  CreateRoomVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 25/04/23.
//

import UIKit

class CreateRoomVC: BaseControllerVC {

    @IBOutlet weak var searchTf: UITextField!
    @IBOutlet weak var liveSwitch: UISwitch!
    @IBOutlet weak var goLiveLbl: UILabel!
    @IBOutlet weak var selectUserView: UIView!
    @IBOutlet weak var scheduleView: UIView!
    @IBOutlet weak var typeSwitch: UISwitch!
    @IBOutlet weak var tableView: ContentSizedTableView!
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var selectedUserView: UIView!
    @IBOutlet weak var typeLbl: UILabel!
    @IBOutlet weak var descriptionTf: UITextField!
    @IBOutlet weak var dateTf: UITextField!
    @IBOutlet weak var timeTf: UITextField!
    @IBOutlet weak var nameTf: UITextField!
    var userList : [UserListDataModel] = []
    var memberIdList : [String] = []
    var selectedUserList : [UserListDataModel] = []
    var page = 1
    var search = ""
    var scheduleType = "go_live_now"
    var scheduleDateTime = ""
    var datePicker = UIDatePicker()
    override func viewDidLoad() {
        super.viewDidLoad()
        setupPicker()
        dateTf.inputView = datePicker
        timeTf.inputView = datePicker
        dateTf.delegate = self
        
        addNavBar(headingText: "Create Room", redText: "Room")
        self.selectedUserView.isHidden = true
        tableView.register(UINib(nibName: RoomMemberCell.identifier, bundle: nil), forCellReuseIdentifier: RoomMemberCell.identifier)
        collectionView.register(UINib(nibName: SelectedUserCell.identifier, bundle: nil), forCellWithReuseIdentifier: SelectedUserCell.identifier)
        //setupTableView()
        searchTf.delegate = self
        let param = ["limit":"30","page":"\(page)","query":"\(search)"]
        
        getUsersFollowerList(withParam: param, needLoader: true){
            DispatchQueue.main.async {
                if (self.userList.isEmpty){
                    self.selectUserView.isHidden = true
                    self.searchTf.isHidden = true
                }else{
                    self.selectUserView.isHidden = false
                    self.searchTf.isHidden = false
                }
               self.setupTableView()
                
            }
        }
        setupCollectionView()
        setup()
        // Do any additional setup after loading the view.
    }
    func setupPicker(){
         datePicker = UIDatePicker()
//        let locale = Locale(identifier: "en_US_POSIX")
//        datePicker.locale = locale
        datePicker.datePickerMode = .dateAndTime
        if #available(iOS 13.4, *) {
            datePicker.preferredDatePickerStyle = .wheels
        } else {
            // Fallback on earlier versions
        }

        // Optional: set minimum and maximum dates
        let minDate = Date()
        let maxDate = Calendar.current.date(byAdding: .year, value: 1, to: minDate)!
        datePicker.minimumDate = minDate
        datePicker.maximumDate = maxDate
        // Optional: set default date and time
        //datePicker.date = minDate

        // Present the date picker
        //self.present(datePicker, animated: true)
        //view.addSubview(datePicker)
    }
    func paddingTF(tf:UITextField,value:Int=20){
        tf.paddingLeftRightTextField(left: CGFloat(value), right: CGFloat(value))
    }
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        guard let scroll = scrollView as? UITableView else{return}
        let height = scroll.frame.size.height
        let contentYOffset = scroll.contentOffset.y
        let distanceFromBottom = scroll.contentSize.height - contentYOffset
        if distanceFromBottom == height{
            page = page + 1
            let param = ["limit":"30","page":"\(page)","query":"\(search)"]
            getUsersFollowerList(withParam: param, needLoader: true){
                DispatchQueue.main.async {
                   self.setupTableView()
                    
                }
            }
        }
    }
    func getUsersFollowerList(withParam param:[String:String],needLoader:Bool,completion:@escaping()->Void){
        DataManager.getUserListForRooms(delegate: self, needLoader: needLoader, param: param) { errorMessage in
            print(errorMessage)
        } completion: { json in
            json["data"].forEach { (message,data) in
                print("EACHUSER",data)
                self.userList.append(UserListDataModel(roomData: data))
            }
            print("LISTCOUBY",self.userList.count)
            completion()
        }
    }
    func setup(){
        
        typeSwitch.addTarget(self, action: #selector(changeType), for: .valueChanged)
        liveSwitch.addTarget(self, action: #selector(changeSchedule), for: .valueChanged)
        changeType()
        changeSchedule()
        paddingTF(tf:descriptionTf)
        paddingTF(tf:nameTf)
        paddingTF(tf:searchTf)
        descriptionTf.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        nameTf.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        searchTf.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        descriptionTf.layer.cornerRadius = 10
        nameTf.layer.cornerRadius = 10
        nameTf.attributedPlaceholder = NSAttributedString(string: "Room Name",attributes: [.foregroundColor: UIColor.lightGray])
        searchTf.layer.cornerRadius = 10
        searchTf.attributedPlaceholder = NSAttributedString(string: "Search Users",attributes: [.foregroundColor: UIColor.lightGray])
        descriptionTf.attributedPlaceholder = NSAttributedString(string: "Room Description",attributes: [.foregroundColor: UIColor.lightGray])
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.tabBarController?.tabBar.isHidden = true
    }
    @objc func changeType(){
        if typeSwitch.isOn{
            typeLbl.text = "Private"
        }else{
            typeLbl.text = "Public"
        }
    }
    @objc func changeSchedule(){
        if liveSwitch.isOn{
            scheduleView.isHidden = false
            goLiveLbl.text = "Schedule Live Room"
            scheduleType = "schedule_live_room"
        }else{
            goLiveLbl.text = "Go Live Now"
            scheduleType = "go_live_now"
            scheduleView.isHidden = true
        }
    }
    func setupTableView(){
        tableView.delegate = self
        tableView.dataSource = self
        tableView.reloadData()
    }
    func setupCollectionView(){
        collectionView.delegate = self
        collectionView.dataSource = self
    }
    
    @IBAction func createRoomBtnClicked(_ sender: Any) {
        checkValidation()
    }
    func checkValidation(){
        if nameTf.text!.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty{
            AlertView().showAlert(message: "Enter room name", delegate: self, pop: false)
            return
        }
        if descriptionTf.text!.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty{
            AlertView().showAlert(message: "Enter room description", delegate: self, pop: false)
            return
        }
        if selectedUserList.isEmpty && AuthManager.currentUser.invitePrivacyControl.lowercased() != "nobody"{
            AlertView().showAlert(message: "Atleast Select one Member to create room.", delegate: self, pop: false)
            return
        }
        if liveSwitch.isOn && scheduleDateTime == ""{
            AlertView().showAlert(message: "Schedule Date and Time for live room", delegate: self, pop: false)
            return
        }
        memberIdList = selectedUserList.map { $0.userId }
        let list = memberIdList.joined(separator: ",")
        let roomType = typeSwitch.isOn ? "private":"public"
        let param = ["title":nameTf.text!.trimmingCharacters(in: .whitespaces),
                     "description":descriptionTf.text!.trimmingCharacters(in: .whitespaces),
                     "memberIds":list,
                     "scheduleType":scheduleType,
                     "scheduleDateTime":"",
                     "roomType":roomType]
        AuthManager.createLiveRoomApi(delegate: self, param: param) {
            AlertView().showAlert(message: "Room Created Successfully", delegate: self, pop: true)
        }
    }
}
extension CreateRoomVC:UITableViewDelegate,UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return userList.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: RoomMemberCell.identifier, for: indexPath) as! RoomMemberCell
        cell.updateCellForUser(data: userList[indexPath.row])
        return cell
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 80
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        self.selectedUserList.append(self.userList[indexPath.row])
        self.userList.remove(at: indexPath.row)
        if userList.isEmpty{
            self.selectUserView.isHidden = true
        }
        self.tableView.deleteRows(at: [indexPath], with: .none)
        let indexPath = IndexPath(item: selectedUserList.count - 1, section: 0)
        collectionView.performBatchUpdates({
            collectionView.insertItems(at: [indexPath])
        }, completion: {_ in
            self.selectedUserView.isHidden = false
        })
       
    }
//    func tableView(_ tableView: UITableView, trailingSwipeActionsConfigurationForRowAt indexPath: IndexPath) -> UISwipeActionsConfiguration? {
//        let deleteAction = UIContextualAction(style: .destructive, title: "Delete") { (action, view, completionHandler) in
//            // Handle delete action
//            completionHandler(true)
//        }
//
//        let editAction = UIContextualAction(style: .normal, title: "Edit") { (action, view, completionHandler) in
//            // Handle edit action
//            completionHandler(true)
//        }
//        editAction.backgroundColor = .blue // Set custom background color for the edit action
//
//        let configuration = UISwipeActionsConfiguration(actions: [deleteAction, editAction])
//        return configuration
//    }
//    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
//      if editingStyle == .delete {
//        print("Deleted")
//
//
//      }
//        if editingStyle == .insert {
//          print("Deleted")
//
//          self.userList.remove(at: indexPath.row)
//          self.tableView.deleteRows(at: [indexPath], with: .automatic)
//        }
//    }
}
extension CreateRoomVC:UICollectionViewDelegate,UICollectionViewDataSource,UICollectionViewDelegateFlowLayout{
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return selectedUserList.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: SelectedUserCell.identifier, for: indexPath) as! SelectedUserCell
        cell.clearUser = {
            
            self.userList.append(self.selectedUserList[indexPath.row])
            self.selectedUserList.remove(at: indexPath.row)
            let indexPathTable = IndexPath(item: self.userList.count - 1, section: 0)
            self.tableView.insertRows(at: [indexPathTable], with: .none)
            self.selectUserView.isHidden = false
            self.collectionView.performBatchUpdates({
                self.collectionView.deleteItems(at: [indexPath])
            }, completion: {_ in
                self.collectionView.reloadData()
                if self.selectedUserList.isEmpty{
                    self.selectedUserView.isHidden = true
                }else{
                    self.selectedUserView.isHidden = false
                }
            })
        }
        cell.updateCellForUser(data: selectedUserList[indexPath.row])
        return cell
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: 60, height: collectionView.frame.height)
    }
}
extension CreateRoomVC:UITextFieldDelegate{
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
    func textFieldDidEndEditing(_ textField: UITextField) {
        if textField == dateTf || textField == timeTf{
            print("datepickervalue",datePicker.date)
            let selectedDate = datePicker.date
            let inputDateString = "\(selectedDate)"

            let inputDateFormatter = DateFormatter()
            inputDateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss Z"
            let inputDate = inputDateFormatter.date(from: inputDateString)!

            let outputDateFormatter = DateFormatter()
            outputDateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"//"yyyy-MM-dd HH:mm:ss"
            let outputDateString = outputDateFormatter.string(from: inputDate)
            print("datepickervalueFormatted",outputDateString)
            scheduleDateTime = outputDateString
            let dateateFormatter = DateFormatter()
            dateateFormatter.dateFormat = "yyyy-MM-dd"//"yyyy-MM-dd HH:mm:ss"// HH:mm:ss
            let dateString = dateateFormatter.string(from: inputDate)
            dateTf.text = dateString
            let timeFormatter = DateFormatter()
            timeFormatter.dateFormat = "yyyy-MM-dd"//"yyyy-MM-dd HH:mm:ss"// HH:mm:ss
            let timeString = timeFormatter.string(from: inputDate)
            timeTf.text = timeString
        }
    }
    @objc func getHintsFromTextField(textField: UITextField) {
        page = 1
        search = textField.text ?? ""
        let param = ["limit":"30","page":"\(page)","query":"\(search)"]
        getUsersFollowerList(withParam: param, needLoader: true){
            DispatchQueue.main.async {
               self.setupTableView()
                
            }
        }
        
    }
}
