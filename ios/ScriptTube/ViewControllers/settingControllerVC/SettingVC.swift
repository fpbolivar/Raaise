//
//  SettingVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/18/22.
//

import UIKit

class SettingVC: BaseControllerVC {
    @IBOutlet weak var  tableView:UITableView!
    var listData = [SettingOptionModal]()
    let manager = SettingOptionManager()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        listData = manager.getList()
        //addNavBar(headingText:"Settings",redText:"")
        addNavBar(headingText:"Settings",
                  redText:"",type: .smallNavBarOnlyBack,
                  color: UIColor(named: "bgColor"))
        tableView.register(UINib(nibName: SettingCell.identifier, bundle: nil), forCellReuseIdentifier: SettingCell.identifier)
        
        executeTblDelegate()
        // Do any additional setup after loading the view.
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.tabBarController?.tabBar.isHidden = true
        getProfileApi()
        
    }
    //MARK: -Api method
    func getProfileApi(){
        AuthManager.getProfileApi(delegate: self,needLoader: false) {
            
        }
    }
    func executeTblDelegate(){
        
        tableView.delegate = self
        tableView.dataSource = self
        tableView.reloadData()
    }
}
//MARK: -Table View Delegate
extension SettingVC:UITableViewDelegate,UITableViewDataSource{
    func notificationApi(param:[String:Bool]){
        AuthManager.notificationsApi(delegate: self, param: param) {
            
        }
    }
    func numberOfSections(in tableView: UITableView) -> Int {
        listData.count
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        listData[section].items.count
    }
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let headerView = UIView.init(frame: CGRect.init(x: 0, y: 0, width: tableView.frame.width, height: 20))
        
        let label = UILabel()
        label.frame = CGRect.init(x: 10, y: 15, width: headerView.frame.width-10, height: headerView.frame.height-5)
        label.text = listData[section].title.uppercased()
        //label.textColor = UIColor(named: "Gradient1")//UIColor.white//UIColor(named: "borderColor2")
        label.font = AppFont.FontName.medium.getFont(size: AppFont.pX14)
        label.applyGradientColorToLabelText(colors: [UIColor(named: "Gradient1") ?? .black, UIColor(named: "Gradient2") ?? .white])
        
        headerView.addSubview(label)
        
        return headerView
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        40
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        
        return 55
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier:  SettingCell.identifier, for: indexPath) as!  SettingCell
        //cell.contentView.backgroundColor = UIColor(named: "newBgColor")
        let item = listData[indexPath.section].items[indexPath.row]
        let type = listData[indexPath.section].type
        
        if type == .switchBtn{
            switch item.title.lowercased(){
            case "Push Notifications".lowercased():
                cell.setupToggle(item:listData[indexPath.section].items[indexPath.row],state: AuthManager.currentUser.pushNotification)
                break
            case "Email Notifications".lowercased():
                cell.setupToggle(item:listData[indexPath.section].items[indexPath.row],state: AuthManager.currentUser.emailNotification)
                break
            default:
                print("NONOTIFICATIONS")
            }
            
            cell.notificationStateChanged = { state in
                switch item.title.lowercased(){
                case "Push Notifications".lowercased():
                    self.notificationApi(param: ["pushNotification":state])
                    break
                case "Email Notifications".lowercased():
                    self.notificationApi(param: ["emailNotification":state])
                    break
                default:
                    print("NONOTIFICATIONS")
                }
            }
        }else{
            cell.setupText(item:listData[indexPath.section].items[indexPath.row])
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let item = listData[indexPath.section].items[indexPath.row]
        print(item.title.lowercased())
        tableView.deselectRow(at: indexPath, animated: true)
        switch item.title.lowercased() {
            
        case "Personal Information".lowercased():
            let vc = PersonalInfoFormVC()
            self.navigationController?.pushViewController(vc, animated: true)
            break
            
        case "Username".lowercased():
            let vc = UserNameFormVC()
            self.navigationController?.pushViewController(vc, animated: true)
            break
            
        case "Short Bio".lowercased():
            let vc = ShortBioFormVC()
            self.navigationController?.pushViewController(vc, animated: true)
            break
            
        case "Change Password".lowercased():
            let vc = ChangePasswordVC()
            self.navigationController?.pushViewController(vc, animated: true)
            break
            
        case "Bank Details".lowercased():
            let vc = BankAddDetailVC()
            self.navigationController?.pushViewController(vc, animated: true)
            break
            
        case "Terms of Service".lowercased():
            let vc = HtmlTextRenderVC()
            vc.fullNavTitle = "Terms of Service"
            vc.redNavTitle = "Service"
            vc.viewType = .service
            self.navigationController?.pushViewController(vc, animated: true)
            break
            
        case "Privacy Policy".lowercased():
            let vc = HtmlTextRenderVC()
            vc.viewType = .privacy
            vc.fullNavTitle = "Privacy Policy"
            vc.redNavTitle = "Policy"
            self.navigationController?.pushViewController(vc, animated: true)
            break
        case "Privacy Control".lowercased():
            let vc = PrivacyControlVC()
            self.navigationController?.pushViewController(vc, animated: true)
            break
            
        case "Copyright Policy".lowercased():
            let vc = HtmlTextRenderVC()
            vc.viewType = .copyright
            vc.fullNavTitle = "Copyright Policy"
            vc.redNavTitle = "Policy"
            self.navigationController?.pushViewController(vc, animated: true)
            break
            
        case "Deactivate Account".lowercased():
            let vc = DeactivateAccountVC()
            self.navigationController?.pushViewController(vc, animated: true)
            break
            
        case "Support Raised".lowercased():
            let vc = DonationRaisedListVC()
            self.navigationController?.pushViewController(vc, animated: true)
            break
            
        case "Delete Account".lowercased():
            let vc = DeleteAccountVC()
            self.navigationController?.pushViewController(vc, animated: true)
            break
            
        case "Withdrawals".lowercased():
            let vc = WithdrawListVC()
            self.navigationController?.pushViewController(vc, animated: true)
            break
            
        case "Payment Methods".lowercased():
            let vc =  PaymemtSavedCardListVC()
            self.navigationController?.pushViewController(vc, animated: true)
            break
            
        case "logout".lowercased():
            createLogoutAlert()
            break
            
        default:
            print(item)
            
        }
    }
    func createLogoutAlert(){
        let alert = UIAlertController(title: "Raaise App", message: "Are you sure you want to logout?", preferredStyle: .alert)
        let logOutAction = UIAlertAction(title: "Logout", style: .destructive){action in
            if(!(Constant.check_Internet?.isReachable)!){
                AlertView().showInternetErrorAlert(delegate: self)
                return
            }
            self.logoutApi()
        }
        let cancelAction = UIAlertAction(title: "Cancel", style: .cancel)
        alert.addAction(cancelAction)
        alert.addAction(logOutAction)
        self.present(alert,animated: true)
    }
    func logoutApi(){
        AuthManager.logoutApi(delegate: self, param: ["userId":AuthManager.currentUser.id]) {
            DispatchQueue.main.async {
                self.gotoLogin()
            }
        }
    }
    func gotoLogin(){
        let vc  = LoginVC()
        let navVC = UINavigationController(rootViewController: vc)
        UIApplication.keyWin!.rootViewController = navVC
        UIApplication.keyWin!.makeKeyAndVisible()
    }
}
