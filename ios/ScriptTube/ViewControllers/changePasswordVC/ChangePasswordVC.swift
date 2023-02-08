//
//  ChangePasswordVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/17/22.
//

import UIKit

class ChangePasswordVC: BaseControllerVC {
    @IBOutlet weak var confirmEyeImg: UIImageView!
    @IBOutlet weak var newEyeImg: UIImageView!
    @IBOutlet weak var currentEyeImg: UIImageView!
    @IBOutlet weak var  descriptionLbl:UILabel!
    @IBOutlet weak var  submitBtn:UIButton!
    @IBOutlet weak var  currentPassTF:UITextField!
    @IBOutlet weak var  newPassTF:UITextField!
    @IBOutlet weak var  cnfPassTF:UITextField!
    var currentTap: UITapGestureRecognizer!
    var newTap: UITapGestureRecognizer!
    var confirmTap: UITapGestureRecognizer!
    override func viewDidLoad() {
        super.viewDidLoad()
        setfonts()
        setPlaceholder()
        addNavBar(headingText:"Change Password",redText:"Password")
    }
    //MARK: -Setup
    @objc func showPassword(sender: UITapGestureRecognizer){
        if sender == currentTap{
            currentPassTF.isSecureTextEntry = !currentPassTF.isSecureTextEntry
            if currentPassTF.isSecureTextEntry{
                currentEyeImg.image = UIImage(systemName: "eye")
            }else{
                currentEyeImg.image = UIImage(systemName: "eye.slash")
            }
        }else if sender == newTap{
            newPassTF.isSecureTextEntry = !newPassTF.isSecureTextEntry
            if newPassTF.isSecureTextEntry{
                newEyeImg.image = UIImage(systemName: "eye")
            }else{
                newEyeImg.image = UIImage(systemName: "eye.slash")
            }
        }else{
            cnfPassTF.isSecureTextEntry = !cnfPassTF.isSecureTextEntry
            if cnfPassTF.isSecureTextEntry{
                confirmEyeImg.image = UIImage(systemName: "eye")
            }else{
                confirmEyeImg.image = UIImage(systemName: "eye.slash")
            }
        }
    }
    func setfonts(){
        currentTap = UITapGestureRecognizer(target: self, action: #selector(showPassword))
        newTap = UITapGestureRecognizer(target: self, action: #selector(showPassword))
        confirmTap = UITapGestureRecognizer(target: self, action: #selector(showPassword))
        currentEyeImg.addGestureRecognizer(currentTap)
        newEyeImg.addGestureRecognizer(newTap)
        confirmEyeImg.addGestureRecognizer(confirmTap)
        currentPassTF.overrideUserInterfaceStyle = .light
        newPassTF.overrideUserInterfaceStyle = .light
        cnfPassTF.overrideUserInterfaceStyle = .light
        descriptionLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        submitBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: AppFont.pX17)
        cnfPassTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        newPassTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        currentPassTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        paddingTF(tf:currentPassTF);
        paddingTF(tf:newPassTF);
        paddingTF(tf:cnfPassTF);
        currentPassTF.layer.cornerRadius = 10
        newPassTF.layer.cornerRadius = 10
        cnfPassTF.layer.cornerRadius = 10
    }
    func paddingTF(tf:UITextField,value:Int=20){
        tf.paddingLeftRightTextField(left: CGFloat(value), right: CGFloat(value))
    }
    func setPlaceholder(){
        currentPassTF.attributedPlaceholder = NSAttributedString(string: "Old Password",attributes: [.foregroundColor: UIColor.lightGray])
        newPassTF.attributedPlaceholder = NSAttributedString(string: "New Password",attributes: [.foregroundColor: UIColor.lightGray])
        cnfPassTF.attributedPlaceholder = NSAttributedString(string: "Confirm Password",attributes: [.foregroundColor: UIColor.lightGray])
    }
    //MARK: -Api method
    func changePasswordApi(){
        let param = ["oldpassword":currentPassTF.text ?? "","newpassword":newPassTF.text ?? "","confirmpassword":cnfPassTF.text ?? ""]
        AuthManager.changePasswordApi(delegate: self, param: param) {
            DispatchQueue.main.async {
                ToastManager.successToast(delegate: self, msg: "Password Changed Successfully")
            }
        }
    }
    //MARK: -Validation
    func checkValidations(){
        if (currentPassTF.text!.isEmpty){
            ToastManager.errorToast(delegate: self, msg: LocalStrings.emptyOldPassword)
            return
        }else if(newPassTF.text!.isEmpty){
            ToastManager.errorToast(delegate: self, msg: LocalStrings.emptyNewPassword)
            return
        }else if(cnfPassTF.text!.isEmpty){
            ToastManager.errorToast(delegate: self, msg: LocalStrings.emptyConfirmPassword)
            return
        }else{
            if(!(Constant.check_Internet?.isReachable)!){
                AlertView().showInternetErrorAlert(delegate: self)
                return
            }
            changePasswordApi()
        }
    }
    @IBAction func submitAction(_ sender: AnyObject) {

        checkValidations()
    }


}
