//
//  CreatePasswordVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 26/11/22.
//

import UIKit

class CreatePasswordVC: BaseControllerVC {
    @IBOutlet weak var oldEyeImg: UIImageView!
    @IBOutlet weak var newEyeImg: UIImageView!
    @IBOutlet weak var updateLbl: UILabel!
    @IBOutlet weak var headingLbl: UILabel!
    @IBOutlet weak var newTf: UITextField!
    @IBOutlet weak var oldTf: UITextField!
    var token = ""
    var newTap: UITapGestureRecognizer!
    var oldTap: UITapGestureRecognizer!
    override func viewDidLoad() {
        super.viewDidLoad()
        hideNavbar()
        setup()
        addNavBar(headingText: "Create Password", 
                  redText: "Password",
                  color: UIColor(named: "bgColor")) //Date:: 02, Mar 2024
        // Do any additional setup after loading the view.
    }
    @IBAction func updateBtnClicked(_ sender: Any) {
        checkValidations()
    }
    //MARK: - Setup
    @objc func showPassword(sender: UITapGestureRecognizer){
        if sender == newTap{
            newTf.isSecureTextEntry = !newTf.isSecureTextEntry
            if newTf.isSecureTextEntry{
                newEyeImg.image = UIImage(systemName: "eye")
            }else{
                newEyeImg.image = UIImage(systemName: "eye.slash")
            }
        }else{
            oldTf.isSecureTextEntry = !oldTf.isSecureTextEntry
            if oldTf.isSecureTextEntry{
                oldEyeImg.image = UIImage(systemName: "eye")
            }else{
                oldEyeImg.image = UIImage(systemName: "eye.slash")
            }
        }
    }
    func setup(){
        newTf.attributedPlaceholder = NSAttributedString(string: "New Password",attributes: [.foregroundColor: UIColor.white])
        oldTf.attributedPlaceholder = NSAttributedString(string: "Confirm Password",attributes: [.foregroundColor: UIColor.white])
        newTap = UITapGestureRecognizer(target: self, action: #selector(showPassword))
        oldTap = UITapGestureRecognizer(target: self, action: #selector(showPassword))
        newEyeImg.addGestureRecognizer(newTap)
        oldEyeImg.addGestureRecognizer(oldTap)
        headingLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
        newTf.overrideUserInterfaceStyle = .light
        oldTf.overrideUserInterfaceStyle = .light
        
        newTf.layer.cornerRadius = 10
        oldTf.layer.cornerRadius = 10
        
        newTf.paddingLeftRightTextField(left: 25, right: 0)
        oldTf.paddingLeftRightTextField(left: 25, right: 0)
        
        newTf.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        oldTf.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        updateLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX18)
    }
    //MARK: - Validation
    func checkValidations(){
        if (newTf.text!.isEmpty){
            ToastManager.errorToast(delegate: self, msg: LocalStrings.emptyPassword)
            return
        }else if(!Validator.isValidPassword(value: newTf.text ?? "")){
            ToastManager.errorToast(delegate: self, msg: LocalStrings.validPassword)
            return
        }else if (newTf.text != oldTf.text){
            ToastManager.errorToast(delegate: self, msg: LocalStrings.passwordNotSame)
        }else{
            if(!(Constant.check_Internet?.isReachable)!){
                AlertView().showInternetErrorAlert(delegate: self)
                return
            }
            resetPasswordApi()
        }
    }
    func gotoLogin(){
        var inStack = false
        for viewController in self.navigationController!.viewControllers{
            if viewController == LoginVC(){
                inStack = true
                self.navigationController?.popViewController(animated: true)
            }
        }
        if !inStack{
            let vc = LoginVC()
            self.navigationController?.pushViewController(vc, animated: true)
        }
    }
    //MARK: - Api Method
    func resetPasswordApi(){
        let param = ["newpassword":newTf.text ?? "","token":self.token]
        AuthManager.resetPasswordApi(delegate: self, param: param) {
            DispatchQueue.main.async {
                self.gotoLogin()
            }
        }
    }
}
