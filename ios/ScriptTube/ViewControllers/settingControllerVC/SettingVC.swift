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
        addNavBar(headingText:"Settings",redText:"",type: .smallNavBarOnlyBack)
        tableView.register(UINib(nibName: SettingCell.identifier, bundle: nil), forCellReuseIdentifier: SettingCell.identifier)
        
        executeTblDelegate()
        // Do any additional setup after loading the view.
    }
    func executeTblDelegate(){
        tableView.delegate = self
        tableView.dataSource = self
        tableView.reloadData()
    }
}
extension SettingVC:UITableViewDelegate,UITableViewDataSource{
    func numberOfSections(in tableView: UITableView) -> Int {
        listData.count
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        listData[section].items.count
    }
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let headerView = UIView.init(frame: CGRect.init(x: 0, y: 0, width: tableView.frame.width, height: 20))

        let label = UILabel()
        label.frame = CGRect.init(x: 10, y: 15, width: headerView.frame.width-10, height: headerView.frame.height-10)
        label.text = listData[section].title.uppercased()
        label.textColor = UIColor.lightGray//UIColor(named: "borderColor2")
        label.font = AppFont.FontName.semiBold.getFont(size: AppFont.pX14)
        headerView.addSubview(label)

        return headerView
    }
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        40
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {

        return 50
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier:  SettingCell.identifier, for: indexPath) as!  SettingCell
        let type = listData[indexPath.section].type
        if type == .switchBtn{
            cell.setupToggle(item:listData[indexPath.section].items[indexPath.row])
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
                vc.redNavTitle = ""
                self.navigationController?.pushViewController(vc, animated: true)
                break
            case "Privacy Policy".lowercased():
                let vc = HtmlTextRenderVC()

                vc.fullNavTitle = "Privacy Policy"
                vc.redNavTitle = ""
                self.navigationController?.pushViewController(vc, animated: true)
                break
            case "Copyright Policy".lowercased():
                let vc = HtmlTextRenderVC()
                vc.fullNavTitle = "Copyright Policy"
                vc.redNavTitle = ""
                self.navigationController?.pushViewController(vc, animated: true)
                break
            case "Deactivate Account".lowercased():
                let vc = DeactivateAccountVC()

                self.navigationController?.pushViewController(vc, animated: true)
                break


            case "Donation Raised".lowercased():
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
          
//            case "logout".lowercased():
//                self.logoutAlert()

             //   break
            default:
                print(item)

        }
    }

    func logoutAlert(){
//        let alert = UIAlertController(title:AlertView.self.title, message: NSLocalizedString(Localize.alert_Logout, comment: ""), preferredStyle: UIAlertController.Style.alert)
//
//        let disconnectAction =   UIAlertAction(title:"Logout", style: UIAlertAction.Style.default, handler: {(alert: UIAlertAction!) in
//
//        })
//        disconnectAction.setValue(UIColor.red, forKey: "titleTextColor")
//        let cancelAction =   UIAlertAction(title:"Cancel", style: UIAlertAction.Style.cancel, handler: {(alert: UIAlertAction!) in
//
//        })
//        alert.addAction(disconnectAction)
//        alert.addAction(cancelAction)
//        self.present(alert, animated: true, completion: nil)
    }

}
