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
    func setup(){
        verifyLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX15)
        //otpTf.placeholder = "Enter Otp"
        otpTf.attributedPlaceholder = NSAttributedString(string: "Enter OTP",attributes: [.foregroundColor: UIColor.white])
        otpTf.paddingLeftRightTextField(left: 25, right: 0)
        otpTf.overrideUserInterfaceStyle = .light
        otpTf.layer.cornerRadius = 10
    }
    func checkValidations(){
        if (otpTf.text!.isEmpty){
            ToastManager.errorToast(delegate: self, msg: LocalStrings.emptyOtp)
            return
        }else{
            verifyOtpApi()
        }
    }

    func gotoCreatePassword(withToken token:String){
        let vc = CreatePasswordVC()
        vc.token = token
        self.navigationController?.pushViewController(vc, animated: true)
    }
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
