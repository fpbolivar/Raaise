//
//  BankAddDetailVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/18/22.
//

import UIKit

class BankAddDetailVC: BaseControllerVC {
    @IBOutlet weak var saveLbl: UILabel!
    @IBOutlet weak var phoneNumberTf: UITextField!
    @IBOutlet weak var postalCodeTf: UITextField!
    @IBOutlet weak var addressTf: UITextField!
    @IBOutlet weak var stateTf: UITextField!
    @IBOutlet weak var cityTf: UITextField!
    @IBOutlet weak var  descriptionLbl:UILabel!
    @IBOutlet weak var  submitBtn:UIButton!
    @IBOutlet weak var  accountNameTF:UITextField!
    @IBOutlet weak var  routingNoTF:UITextField!
    @IBOutlet weak var  accountNoTF:UITextField!
    @IBOutlet weak var  cnfAccountNoTF:UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()
        setfonts()
        addNavBar(headingText:"Bank Details",redText:"Details")
        setData()
        // Do any additional setup after loading the view.
    }
    func setData(){
        accountNameTF.text = AuthManager.currentUser.accountHolderName
        accountNoTF.text = AuthManager.currentUser.accountId
        cnfAccountNoTF.text = AuthManager.currentUser.accountId
        routingNoTF.text = AuthManager.currentUser.routingNumber
        cityTf.text = AuthManager.currentUser.city
        stateTf.text = AuthManager.currentUser.state
        addressTf.text = AuthManager.currentUser.address
        postalCodeTf.text = AuthManager.currentUser.postalCode
        phoneNumberTf.text = AuthManager.currentUser.bankPhone
    }
    
    func setfonts(){
        saveLbl.font = AppFont.FontName.medium.getFont(size: AppFont.pX18)
        descriptionLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
        submitBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: AppFont.pX17)
        accountNameTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        accountNoTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        routingNoTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        cnfAccountNoTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        phoneNumberTf.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        postalCodeTf.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        addressTf.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        stateTf.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        cityTf.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        paddingTF(tf:cnfAccountNoTF);
        paddingTF(tf:accountNameTF);
        paddingTF(tf:routingNoTF);
        paddingTF(tf:accountNoTF);
        paddingTF(tf:phoneNumberTf);
        paddingTF(tf:postalCodeTf);
        paddingTF(tf:addressTf);
        paddingTF(tf:stateTf);
        paddingTF(tf:cityTf);
        cnfAccountNoTF.layer.cornerRadius = 10
        routingNoTF.layer.cornerRadius = 10
        accountNameTF.layer.cornerRadius = 10
        accountNoTF.layer.cornerRadius = 10
        phoneNumberTf.layer.cornerRadius = 10
        postalCodeTf.layer.cornerRadius = 10
        addressTf.layer.cornerRadius = 10
        stateTf.layer.cornerRadius = 10
        cityTf.layer.cornerRadius = 10
        
        cnfAccountNoTF.attributedPlaceholder = NSAttributedString(string: "Confirm Account Number",attributes: [.foregroundColor: UIColor.lightGray])
        routingNoTF.attributedPlaceholder = NSAttributedString(string: "Routing Number",attributes: [.foregroundColor: UIColor.lightGray])
        accountNameTF.attributedPlaceholder = NSAttributedString(string: "Account holder name",attributes: [.foregroundColor: UIColor.lightGray])
        accountNoTF.attributedPlaceholder = NSAttributedString(string: "Account Number",attributes: [.foregroundColor: UIColor.lightGray])
        phoneNumberTf.attributedPlaceholder = NSAttributedString(string: "Phone Number",attributes: [.foregroundColor: UIColor.lightGray])
        postalCodeTf.attributedPlaceholder = NSAttributedString(string: "Postal Code",attributes: [.foregroundColor: UIColor.lightGray])
        addressTf.attributedPlaceholder = NSAttributedString(string: "Address",attributes: [.foregroundColor: UIColor.lightGray])
        stateTf.attributedPlaceholder = NSAttributedString(string: "State",attributes: [.foregroundColor: UIColor.lightGray])
        cityTf.attributedPlaceholder = NSAttributedString(string: "City",attributes: [.foregroundColor: UIColor.lightGray])
    }
    func paddingTF(tf:UITextField,value:Int=20){
        tf.paddingLeftRightTextField(left: CGFloat(value), right: CGFloat(value))
    }
    func checkValidations(){
        if accountNameTF.text!.isEmpty{
            ToastManager.errorToast(delegate: self, msg: "Please enter Account Holder Name")
        }else if accountNoTF.text!.isEmpty{
            ToastManager.errorToast(delegate: self, msg: "Please enter Account Number")
        }else if cnfAccountNoTF.text!.isEmpty{
            ToastManager.errorToast(delegate: self, msg: "Please Confirm Account Number")
        }else if accountNoTF.text! != cnfAccountNoTF.text!{
            ToastManager.errorToast(delegate: self, msg: "Account Number are not same")
        }else if routingNoTF.text!.isEmpty{
            ToastManager.errorToast(delegate: self, msg: "Please enter Routing Number")
        }else if cityTf.text!.isEmpty{
            ToastManager.errorToast(delegate: self, msg: "Please enter City")
        }else if stateTf.text!.isEmpty{
            ToastManager.errorToast(delegate: self, msg: "Please enter State")
        }else if addressTf.text!.isEmpty{
            ToastManager.errorToast(delegate: self, msg: "Please enter Address")
        }else if postalCodeTf.text!.isEmpty{
            ToastManager.errorToast(delegate: self, msg: "Please enter Postal Code")
        }else if phoneNumberTf.text!.isEmpty{
            ToastManager.errorToast(delegate: self, msg: LocalStrings.emptyMobile)
        }else if phoneNumberTf.text!.count < 8{
            ToastManager.errorToast(delegate: self, msg: LocalStrings.validMobile)
        }else{
            if(!(Constant.check_Internet?.isReachable)!){
                AlertView().showInternetErrorAlert(delegate: self)
                return
            }
            addBankDetailsApi()
        }
    }
    func addBankDetailsApi(){
        let param = ["accountHolderName":accountNameTF.text,"accountId":cnfAccountNoTF.text,"routingNumber":routingNoTF.text,"bankPhone":phoneNumberTf.text,"city":cityTf.text,"state":stateTf.text,"postalCode":postalCodeTf.text,"address":addressTf.text] as! [String:String]
        AuthManager.addBankDetails(delegate: self, param: param) {
            DispatchQueue.main.async {
                ToastManager.successToast(delegate: self, msg: "Bank Details Added Successfully")
            }
        }
    }
    @IBAction func submitBtnClicked(_ sender: Any) {
        checkValidations()
    }
}
