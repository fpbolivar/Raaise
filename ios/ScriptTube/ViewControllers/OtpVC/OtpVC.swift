//
//  OtpVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 26/11/22.
//

import UIKit

class OtpVC: BaseControllerVC {
    @IBOutlet weak var otpTf: UITextField!
    @IBOutlet weak var headingLbl: UILabel!
    
    @IBOutlet weak var verifyLbl: UILabel!
    var email = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        hideNavbar()
        setup()
        addNavBar(headingText: "OTP", redText: "")
        // Do any additional setup after loading the view.
    }
    //MARK: - Setup
    func setup(){
        headingLbl.text = "Otp has been sent on your email \(email)"
        verifyLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX18)
        headingLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
        otpTf.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        otpTf.attributedPlaceholder = NSAttributedString(string: "Enter OTP",attributes: [.foregroundColor: UIColor.white])
        otpTf.paddingLeftRightTextField(left: 25, right: 0)
        otpTf.overrideUserInterfaceStyle = .light
        otpTf.layer.cornerRadius = 10
    }
    //MARK: - Validation
    func checkValidations(){
        if (otpTf.text!.isEmpty){
            ToastManager.errorToast(delegate: self, msg: LocalStrings.emptyOtp)
            return
        }else{
            if(!(Constant.check_Internet?.isReachable)!){
                AlertView().showInternetErrorAlert(delegate: self)
                return
            }
            verifyOtpApi()
        }
    }

    func gotoCreatePassword(withToken token:String){
        let vc = CreatePasswordVC()
        vc.token = token
        self.navigationController?.pushViewController(vc, animated: true)
    }
    //MARK: - Api Methods
    func verifyOtpApi(){
        let param = ["email":email,"otp":otpTf.text ?? ""]
        AuthManager.verifyOtp(delegate: self, param: param) { token in
            DispatchQueue.main.async {
                self.gotoCreatePassword(withToken: token)
            }
        }
    }
    @IBAction func verifyBtnClicked(_ sender: Any) {
        checkValidations()
    }
}
