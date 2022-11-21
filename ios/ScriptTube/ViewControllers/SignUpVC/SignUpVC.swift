//
//  SignUpVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/17/22.
//

import UIKit

class SignUpVC: BaseControllerVC {
    @IBOutlet weak var  orSignInWithLbl:UILabel!
    @IBOutlet weak var  alreadyhaveLbl:ActiveLabel!
    @IBOutlet weak var  signUpBtn:UIButton!
    @IBOutlet weak var  emailTF:UITextField!
    @IBOutlet weak var  passTF:UITextField!
    @IBOutlet weak var  cnfPassTF:UITextField!
    @IBOutlet weak var  mobileTF:UITextField!
    @IBOutlet weak var  nameTF:UITextField!
    @IBOutlet weak var  usernameTF:UITextField!
    @IBOutlet weak var  googleLbl:UILabel!
    @IBOutlet weak var  fbLbl:UILabel!

    override func viewDidLoad() {
        super.viewDidLoad()
        setfonts()
        redColorUnderline()
        addNavBar(headingText:"Sign Up for Scriptube",redText:"Scriptube")
        // Do any additional setup after loading the view.
    }
    func setfonts(){
        orSignInWithLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)

        googleLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)

        fbLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)

        signUpBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: AppFont.pX17)
        passTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        emailTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        emailTF.paddingLeftRightTextField(left: 20, right: 20)
        passTF.paddingLeftRightTextField(left: 20, right: 20)
        nameTF.paddingLeftRightTextField(left: 20, right: 20)
        usernameTF.paddingLeftRightTextField(left: 20, right: 20)
        mobileTF.paddingLeftRightTextField(left: 20, right: 20)
        passTF.paddingLeftRightTextField(left: 20, right: 20)
        cnfPassTF.paddingLeftRightTextField(left: 20, right: 20)

    }
    func setPlaceholder(){
        passTF.placeholder = "********"
        emailTF.placeholder = "abc@ymail.uk"
    }
    func redColorUnderline(){

        let customType = ActiveType.custom(pattern:  "SignIn")
        alreadyhaveLbl.enabledTypes.append(customType)
        alreadyhaveLbl.textColor = UIColor.black
        alreadyhaveLbl.underLineEnable = false
        alreadyhaveLbl.text = "Already have an account? SignIn"
        alreadyhaveLbl.customColor[customType] = UIColor.theme
        alreadyhaveLbl.customSelectedColor[customType] = UIColor.gray
        alreadyhaveLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        alreadyhaveLbl.handleCustomTap(for: customType) { element in
//            let destination = ForgotVC()
//            self.navigationController?.pushViewController(destination, animated: true)

        }

    }
    @IBAction func signUpAction(_ sender: AnyObject) {
       let vc = PersonalInfoFormVC()
        self.navigationController?.pushViewController(vc, animated: true)
    }
    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
