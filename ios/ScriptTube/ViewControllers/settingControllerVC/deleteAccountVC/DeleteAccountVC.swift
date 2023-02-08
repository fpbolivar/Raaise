//
//  DeleteAccountVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/18/22.
//

import UIKit

class DeleteAccountVC: BaseControllerVC {
    @IBOutlet weak var profileImage: UIImageView!
    @IBOutlet weak var questionLbl:UILabel!
    @IBOutlet weak var nameLbl:UILabel!
    @IBOutlet weak var deactiveLbl:UILabel!
    @IBOutlet weak var messageLbl:UILabel!
    @IBOutlet weak var submitBtn:UIButton!

    override func viewDidLoad() {
        super.viewDidLoad()
        addNavBar(headingText:"Delete Account",redText:"Account")
        setfonts()
        // Do any additional setup after loading the view.
    }
    //MARK: -Setup
    func setfonts(){
        questionLbl.font = AppFont.FontName.semiBold.getFont(size: AppFont.pX22)
        nameLbl.font = AppFont.FontName.semiBold.getFont(size: AppFont.pX22)
        deactiveLbl.font = AppFont.FontName.semiBold.getFont(size: AppFont.pX14)
        messageLbl.font = AppFont.FontName.light.getFont(size: AppFont.pX12)
        submitBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: AppFont.pX17)
        profileImage.layer.cornerRadius = profileImage.frame.height / 2
        nameLbl.text = AuthManager.currentUser.name
        profileImage.loadImgForProfile(url: AuthManager.currentUser.profileImage)
    }
    func gotoLogin(){
        let vc  = LoginVC()
        let navVC = UINavigationController(rootViewController: vc)
        UIApplication.keyWin!.rootViewController = navVC
        UIApplication.keyWin!.makeKeyAndVisible()
    }
    //MARK: -Api method
    func createDeleteAlert(){
        let alert = UIAlertController(title: "Raaise App", message: "Are you sure you want to Delete Your Account?", preferredStyle: .alert)
        let logOutAction = UIAlertAction(title: "Delete Account", style: .destructive){action in
            if(!(Constant.check_Internet?.isReachable)!){
                AlertView().showInternetErrorAlert(delegate: self)
                return
            }
            self.deleteAccountApi()
        }
        let cancelAction = UIAlertAction(title: "Cancel", style: .cancel)
        alert.addAction(cancelAction)
        alert.addAction(logOutAction)
        self.present(alert,animated: true)
    }
    func deleteAccountApi(){
        AuthManager.deleteAccount(delegate: self, param: ["userId":AuthManager.currentUser.id]) {
            DispatchQueue.main.async {
                self.gotoLogin()
            }
        }
    }
    @IBAction func deleteBtnClicked(_ sender: Any) {
        createDeleteAlert()
    }
}
