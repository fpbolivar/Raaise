//
//  PrivacyControlVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 13/05/23.
//

import UIKit

class PrivacyControlVC: BaseControllerVC {

    @IBOutlet weak var tableView: UITableView!
    var privacyData = [PrivacyData]()
    var selectedPrivacy = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        addNavBar(headingText:"Privacy Control",redText:"Control",color: UIColor(named: "bgColor"))
        tableView.register(UINib(nibName: PrivacyControlCell.identifier, bundle: nil), forCellReuseIdentifier: PrivacyControlCell.identifier)
        tableView.delegate = self
        tableView.dataSource = self
        
        privacyData = [PrivacyData(title: "Following", isSelected: false),
                       PrivacyData(title: "Followers", isSelected: false),
                       PrivacyData(title: "Anyone", isSelected: false),
                       PrivacyData(title: "Nobody", isSelected: false),]
        if AuthManager.currentUser.invitePrivacyControl == ""{
            privacyData[2].isSelected = true
            selectedPrivacy = privacyData[2].title.lowercased()
        }else{
            privacyData.forEach { data in
                if(data.title.lowercased() == AuthManager.currentUser.invitePrivacyControl.lowercased()){
                    data.isSelected = true
                    selectedPrivacy = data.title.lowercased()
                }
            }
        }
        tableView.reloadData()
        //AppFont.FontName.regular.getFont(size: AppFont.pX18)
        // Do any additional setup after loading the view.
    }
    @IBAction func updateBtnClicked(_ sender: Any) {
        AuthManager.updatePrivacy(delegate: self, param: ["invitePrivacyControl":selectedPrivacy]) {
            
        }
    }
}
extension PrivacyControlVC:UITableViewDelegate,UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return privacyData.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: PrivacyControlCell.identifier, for: indexPath) as! PrivacyControlCell
        cell.updateCell(data: privacyData[indexPath.row])
        return cell
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 60
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        selectedPrivacy = privacyData[indexPath.row].title.lowercased()
        for i in 0 ..< privacyData.count{
            if(i == indexPath.row){
                privacyData[i].isSelected = true
            }else{
                privacyData[i].isSelected = false
            }
        }
        tableView.reloadData()
    }
}
class PrivacyData{
    var title = ""
    var isSelected = false
    init(title: String = "", isSelected: Bool = false) {
        self.title = title
        self.isSelected = isSelected
    }
}

