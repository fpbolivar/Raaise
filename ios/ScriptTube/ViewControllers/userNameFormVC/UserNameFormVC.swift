//
//  UserNameFormVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/17/22.
//

import UIKit

class UserNameFormVC: BaseControllerVC {
    @IBOutlet weak var  descriptionLbl:UILabel!
    @IBOutlet weak var  submitBtn:UIButton!
    @IBOutlet weak var  nameTF:UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()
        addNavBar(headingText:"Username",redText:"")
        setfonts()
        // Do any additional setup after loading the view.
    }
    func setfonts(){
        nameTF.overrideUserInterfaceStyle = .light
        descriptionLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        submitBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: AppFont.pX17)
        nameTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        paddingTF(tf:nameTF);
        setPlaceholder()
    }
    func paddingTF(tf:UITextField,value:Int=20){
        tf.paddingLeftRightTextField(left: CGFloat(value), right: CGFloat(value))
    }
    func setPlaceholder(){
        //nameTF.placeholder = "Username"
        nameTF.attributedPlaceholder = NSAttributedString(string: "UserName",attributes: [.foregroundColor: UIColor.lightGray])
        nameTF.layer.cornerRadius = 10
        nameTF.text = AuthManager.currentUser.userName
    }
    func checkValidations(){
        if (nameTF.text!.isEmpty){
            ToastManager.errorToast(delegate: self, msg: LocalStrings.emptyUsername)
            return
        }
//        else if(!Validator.isValidEmail(email: nameTF.text ?? "")){
//            ToastManager.errorToast(delegate: self, msg: LocalStrings.validUsername)
//            return
//        }
        else{
            updateProfileApi()
        }
    }
    func updateProfileApi(){
        let param = ["userName":nameTF.text ?? ""]
        AuthManager.updateUserProfileApi(delegate: self, param: param) {
            DispatchQueue.main.async {
                //self.gotoShortBioForm()
                self.navigationController?.popViewController(animated: true)
            }
        }
    }
    func gotoShortBioForm(){
        let vc = ShortBioFormVC()
        self.navigationController?.pushViewController(vc, animated: true)
    }
    @IBAction func submitAction(_ sender: AnyObject) {
        checkValidations()
    }

}
