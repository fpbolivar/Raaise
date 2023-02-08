//
//  ForgotVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/17/22.
//

import UIKit

class ForgotVC: BaseControllerVC {
    @IBOutlet weak var sendLbl: UILabel!
    @IBOutlet weak var enterEmailLbl: ActiveLabel!
    @IBOutlet weak var nameTf: UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()
        addNavBar(headingText:"Forgot Password",redText:"Password")
        enterEmailLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
        nameTf.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        sendLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX18)
        nameTf.overrideUserInterfaceStyle = .light
        nameTf.layer.cornerRadius = 10
        nameTf.attributedPlaceholder = NSAttributedString(string: "Email Address",attributes: [.foregroundColor: UIColor.lightGray])
        nameTf.paddingLeftRightTextField(left: 15, right: 0)
        // Do any additional setup after loading the view.
    }
    @IBAction func submitAction(_ sender: AnyObject) {
        checkValidations()
    }
    //MARK: -Validation
    func checkValidations(){
        if (nameTf.text!.isEmpty){
            ToastManager.errorToast(delegate: self, msg: LocalStrings.emptyEmail)
            return
        }else if (!Validator.isValidEmail(email: nameTf.text ?? "")){
            ToastManager.errorToast(delegate: self, msg: LocalStrings.validEmail)
            return
        }else{
            if(!(Constant.check_Internet?.isReachable)!){
                AlertView().showInternetErrorAlert(delegate: self)
                return
            }
            forgetPasswordApi()
        }
    }
    func showOtp(otp:String,completion:@escaping()->Void){
        let alert = UIAlertController(title: "OTP", message: otp, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: { action in
            self.dismiss(animated: true)
            completion()
        }))
        self.present(alert, animated: true, completion: nil)
    }
    func gotoOtpScreen(){
        let vc = OtpVC()
        vc.email = nameTf.text ?? ""
        self.navigationController?.pushViewController(vc, animated: true)
    }
    //MARK: -Api method
    func forgetPasswordApi(){
        let param = ["email":nameTf.text ?? ""]
        AuthManager.forgetPasswordApi(delegate: self, param: param) { otp in
            DispatchQueue.main.async {
                self.showOtp(otp: otp) {
                    self.gotoOtpScreen()
                }
            }
        }
    }
}
