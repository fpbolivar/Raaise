//
//  DeactivateAccountVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/18/22.
//

import UIKit

class DeactivateAccountVC: BaseControllerVC {
    @IBOutlet weak var profileImage: UIImageView!
    @IBOutlet weak var  questionLbl:UILabel!
    @IBOutlet weak var nameLbl:UILabel!
    @IBOutlet weak var deactiveLbl:UILabel!
    @IBOutlet weak var messageLbl:UILabel!
    @IBOutlet weak var submitBtn:UIButton!
   
    override func viewDidLoad() {
        super.viewDidLoad()
        addNavBar(headingText:"Deactivate Account",redText:"Account")
        setfonts()
        // Do any additional setup after loading the view.
    }
    func setfonts(){
        questionLbl.font = AppFont.FontName.semiBold.getFont(size: AppFont.pX30)
        nameLbl.font = AppFont.FontName.semiBold.getFont(size: AppFont.pX18)
        deactiveLbl.font = AppFont.FontName.semiBold.getFont(size: AppFont.pX15)
        messageLbl.font = AppFont.FontName.light.getFont(size: AppFont.pX14)
        submitBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: AppFont.pX17)
        nameLbl.text = AuthManager.currentUser.name
        profileImage.layer.cornerRadius = profileImage.frame.height / 2
        profileImage.loadImgForProfile(url: AuthManager.currentUser.profileImage)
        //loadImg(url: AuthManager.currentUser.profileImage)
    }
    @IBAction func submitBtnClicked(_ sender: Any) {
        createDeactivateAlert()
    }
    func deactivateApi(completion:@escaping()->Void){
        AuthManager.deactivateAccount(delegate: self, param: ["userId":AuthManager.currentUser.id]) {
            DispatchQueue.main.async {
                //ToastManager.successToast(delegate: self, msg: "Account Deactivated")
                completion()
            }
        }
    }
    func createDeactivateAlert(){
        let alert = UIAlertController(title: "ScripTube", message: "Are you sure you want to Deactivate Your Account", preferredStyle: .alert)
        let logOutAction = UIAlertAction(title: "Deactivate Account", style: .destructive){action in
            self.deactivateApi {
                self.gotoLogin()
            }
        }
        let cancelAction = UIAlertAction(title: "Cancel", style: .cancel)
        alert.addAction(cancelAction)
        alert.addAction(logOutAction)
        self.present(alert,animated: true)
    }
    func gotoLogin(){
        let vc  = LoginVC()
        let navVC = UINavigationController(rootViewController: vc)
        UIApplication.keyWin!.rootViewController = navVC
        UIApplication.keyWin!.makeKeyAndVisible()
    }
}
