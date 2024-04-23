//
//  DeactivateAccountVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/18/22.
//

import UIKit

class DeactivateAccountVC: BaseControllerVC {
    @IBOutlet weak var submitLbl: UILabel!
    @IBOutlet weak var profileImage: UIImageView!
    @IBOutlet weak var  questionLbl:UILabel!
    @IBOutlet weak var nameLbl:UILabel!
    @IBOutlet weak var deactiveLbl:UILabel!
    @IBOutlet weak var messageLbl:UILabel!
    @IBOutlet weak var submitBtn:UIButton!
   
    override func viewDidLoad() {
        super.viewDidLoad()
        addNavBar(headingText:"Deactivate Account",
                  redText:"Account",
                  color: UIColor(named: "bgColor"))
        setfonts()
        // Do any additional setup after loading the view.
    }
    //MARK: -Setup
    func setfonts(){
        submitLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX18)
        questionLbl.font = AppFont.FontName.semiBold.getFont(size: AppFont.pX22)
        nameLbl.font = AppFont.FontName.semiBold.getFont(size: AppFont.pX22)
        deactiveLbl.font = AppFont.FontName.semiBold.getFont(size: AppFont.pX14)
        messageLbl.font = AppFont.FontName.light.getFont(size: AppFont.pX12)
        submitBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: AppFont.pX17)
        nameLbl.text = AuthManager.currentUser.name
        profileImage.layer.cornerRadius = profileImage.frame.height / 2
        profileImage.loadImgForProfile(url: AuthManager.currentUser.profileImage)
    }
    @IBAction func submitBtnClicked(_ sender: Any) {
        createDeactivateAlert()
    }
    //MARK: -Api method
    func deactivateApi(completion:@escaping()->Void){
        AuthManager.deactivateAccount(delegate: self, param: ["userId":AuthManager.currentUser.id]) {
            DispatchQueue.main.async {
                completion()
            }
        }
    }
    func createDeactivateAlert(){
        let alert = UIAlertController(title: "Raaise App", message: "Are you sure you want to Deactivate Your Account?", preferredStyle: .alert)
        let logOutAction = UIAlertAction(title: "Deactivate Account", style: .destructive){action in
            if(!(Constant.check_Internet?.isReachable)!){
                AlertView().showInternetErrorAlert(delegate: self)
                return
            }
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
