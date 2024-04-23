//
//  BankAddDetailVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/18/22.
//

import UIKit

class BankAddDetailVC: BaseControllerVC {
    @IBOutlet weak var ssnTf: UITextField!
    @IBOutlet weak var dobTf: UITextField!
    @IBOutlet weak var accountHolderLastName: UITextField!
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
    let datePicker = UIDatePicker()
    var dob = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        setfonts()
        addNavBar(headingText:"Bank Details",
                  redText:"Details", 
                  color: UIColor(named: "bgColor"))
        setData()
        datePicker.maximumDate = Date()
        datePicker.datePickerMode = .date
        if #available(iOS 13.4, *) {
            datePicker.preferredDatePickerStyle = .wheels
        } else {
            // Fallback on earlier versions
        }
        // Do any additional setup after loading the view.
    }
    //MARK: -Setup
    func setData(){
        accountHolderLastName.text = AuthManager.currentUser.accountHolderLastName
        ssnTf.text = AuthManager.currentUser.ssnNumber
        accountNameTF.text = AuthManager.currentUser.accountHolderName
        accountNoTF.text = AuthManager.currentUser.accountId
        cnfAccountNoTF.text = AuthManager.currentUser.accountId
        routingNoTF.text = AuthManager.currentUser.routingNumber
        cityTf.text = AuthManager.currentUser.city
        stateTf.text = AuthManager.currentUser.state
        addressTf.text = AuthManager.currentUser.address
        postalCodeTf.text = AuthManager.currentUser.postalCode
        phoneNumberTf.text = AuthManager.currentUser.bankPhone
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy/MM/dd"
        guard let date = dateFormatter.date(from:AuthManager.currentUser.dob)
        else{
            dobTf.text = ""
            return
        }
        let dateFormatter2 = DateFormatter()
        dateFormatter2.dateFormat = "dd/MM/yyyy"
        dobTf.text = dateFormatter2.string(from: date)
    }
    
    func setfonts(){
        dobTf.inputView = datePicker
        dobTf.delegate = self
        ssnTf.delegate = self
        saveLbl.font = AppFont.FontName.medium.getFont(size: AppFont.pX18)
        descriptionLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
        submitBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: AppFont.pX17)
        accountNameTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        accountHolderLastName.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        accountNoTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        routingNoTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        cnfAccountNoTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        phoneNumberTf.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        postalCodeTf.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        addressTf.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        stateTf.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        dobTf.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        ssnTf.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
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
        paddingTF(tf:ssnTf);
        paddingTF(tf:dobTf);
        paddingTF(tf:accountHolderLastName);
        cnfAccountNoTF.layer.cornerRadius = 10
        routingNoTF.layer.cornerRadius = 10
        accountNameTF.layer.cornerRadius = 10
        accountNoTF.layer.cornerRadius = 10
        phoneNumberTf.layer.cornerRadius = 10
        postalCodeTf.layer.cornerRadius = 10
        addressTf.layer.cornerRadius = 10
        stateTf.layer.cornerRadius = 10
        cityTf.layer.cornerRadius = 10
        dobTf.layer.cornerRadius = 10
        ssnTf.layer.cornerRadius = 10
        accountHolderLastName.layer.cornerRadius = 10
        dobTf.attributedPlaceholder = NSAttributedString(string: "D.O.B",attributes: [.foregroundColor: UIColor.lightGray])
        ssnTf.attributedPlaceholder = NSAttributedString(string: "SSN(Last 4-digits)",attributes: [.foregroundColor: UIColor.lightGray])
        accountHolderLastName.attributedPlaceholder = NSAttributedString(string: "Account holder last name",attributes: [.foregroundColor: UIColor.lightGray])
        cnfAccountNoTF.attributedPlaceholder = NSAttributedString(string: "Confirm Account Number",attributes: [.foregroundColor: UIColor.lightGray])
        routingNoTF.attributedPlaceholder = NSAttributedString(string: "Routing Number",attributes: [.foregroundColor: UIColor.lightGray])
        accountNameTF.attributedPlaceholder = NSAttributedString(string: "Account holder first name",attributes: [.foregroundColor: UIColor.lightGray])
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
    //MARK: -Validation
    func checkValidations(){
        if accountNameTF.text!.isEmpty{
            ToastManager.errorToast(delegate: self, msg: "Please enter Account Holder Name")
        }else if accountHolderLastName.text!.isEmpty{
            ToastManager.errorToast(delegate: self, msg: "Please enter Last Name")
        }
        else if accountNoTF.text!.isEmpty{
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
        }else if dobTf.text!.isEmpty{
            ToastManager.errorToast(delegate: self, msg: "Please enter Date of Birth")
        }else if ssnTf.text!.isEmpty{
            ToastManager.errorToast(delegate: self, msg: "Please enter Social Security Number")
        }else if ssnTf.text!.count < 4{
            ToastManager.errorToast(delegate: self, msg: "Please enter valid Social Security Number")
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
    //MARK: -Api method
    func addBankDetailsApi(){
        let param = ["accountHolderName":accountNameTF.text!,
                     "accountHolderLastName":accountHolderLastName.text!,
                     "dob":dob,
                     "accountId":cnfAccountNoTF.text!,
                     "routingNumber":routingNoTF.text!,
                     "bankPhone":phoneNumberTf.text!,
                     "city":cityTf.text!,
                     "state":stateTf.text!,
                     "postalCode":postalCodeTf.text!,
                     "address":addressTf.text!,
                     "ssn_last_4":ssnTf.text!] as! [String:String]
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

extension BankAddDetailVC:UITextFieldDelegate{
    func textFieldDidEndEditing(_ textField: UITextField) {
        if textField == dobTf{
            let formatter = DateFormatter()
               formatter.dateFormat = "dd/MM/yyyy"
            textField.text = formatter.string(from: datePicker.date)
            let formatter2 = DateFormatter()
            formatter2.dateFormat = "yyyy/MM/dd"
            self.dob = formatter2.string(from: datePicker.date)
        }
    }
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if textField == ssnTf{
            return textField.text!.count + (string.count - range.length) <= 4
        }else{
            return true
        }
    }
}
