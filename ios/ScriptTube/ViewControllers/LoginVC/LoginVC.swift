//
//  LoginVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 09/11/22.
//

import UIKit

class LoginVC: BaseControllerVC {


    @IBOutlet weak var  orSignInWithLbl:UILabel!
    @IBOutlet weak var  donthaveLbl:UILabel!

    @IBOutlet weak var  forgotBtn:UIButton!
    @IBOutlet weak var  loginLbl:UILabel!
    @IBOutlet weak var  signInBtn:UIButton!
    @IBOutlet weak var  emailTF:UITextField!
    @IBOutlet weak var  passTF:UITextField!
    @IBOutlet weak var  googleLbl:UILabel!
    @IBOutlet weak var  donthaveAccountLbl:ActiveLabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        hideNavbar()
        setfonts()
        setPlaceholder()
        redColorUnderline()
       
       // titleText = "Login"
       // headingText = "Personal Information"
       // redText = "Information"
       // addNavBar(leftAction: #selector(backAction))
        // Do any additional setup after loading the view.
    }

    func setfonts(){
        orSignInWithLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)

        googleLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        forgotBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: AppFont.pX15)
        signInBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: AppFont.pX17)
        passTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        emailTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        emailTF.paddingLeftRightTextField(left: 20, right: 20)
        passTF.paddingLeftRightTextField(left: 20, right: 20)

    }
    func setPlaceholder(){
        passTF.placeholder = "********"
        emailTF.placeholder = "abc@ymail.uk"
    }
    func redColorUnderline(){

        let customType = ActiveType.custom(pattern:  "SignUp")
        donthaveAccountLbl.enabledTypes.append(customType)
        donthaveAccountLbl.textColor = UIColor.black
        donthaveAccountLbl.underLineEnable = false
        donthaveAccountLbl.text = "You don’t have an account ?SignUp"
        donthaveAccountLbl.customColor[customType] = UIColor.theme
        donthaveAccountLbl.customSelectedColor[customType] = UIColor.gray
        donthaveAccountLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        donthaveAccountLbl.handleCustomTap(for: customType) { element in
                        let destination = SignUpVC()
                        self.navigationController?.pushViewController(destination, animated: true)

        }

    }
    @IBAction func forgotAction(_ sender: AnyObject) {
        let vc = ForgotVC()
        self.navigationController?.pushViewController(vc, animated: true)
    }
    @IBAction func loginAction(_ sender: AnyObject) {
        let vc  = TabBarController()
        UIApplication.keyWin!.rootViewController = vc
        UIApplication.keyWin!.makeKeyAndVisible()
    }
}
