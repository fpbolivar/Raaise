//
//  FilterPopUpVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 20/01/23.
//

import UIKit
protocol FilterPopUpDelegate{
    func filterwithdata(fromDate:String,toDate:String)
}
class FilterPopUpVC: UIViewController {
    var toDate:String!
    var fromDate:String!
    var delegate:FilterPopUpDelegate?
    @IBOutlet weak var toTf: UITextField!
    @IBOutlet weak var fromTf: UITextField!
    let datePicker = UIDatePicker()
    override func viewDidLoad() {
        super.viewDidLoad()
        setup()
        // Do any additional setup after loading the view.
    }
    //MARK: - Setup
    func setup(){
        datePicker.datePickerMode = .date
        if #available(iOS 13.4, *) {
            datePicker.preferredDatePickerStyle = .wheels
        } else {
            // Fallback on earlier versions
        }
        toTf.attributedPlaceholder = NSAttributedString(string: "To",attributes: [.foregroundColor: UIColor.lightGray])
        fromTf.attributedPlaceholder = NSAttributedString(string: "From",attributes: [.foregroundColor: UIColor.lightGray])
        toTf.layer.cornerRadius = 10
        fromTf.layer.cornerRadius = 10
        toTf.paddingLeftRightTextField(left: 10, right: 0)
        fromTf.paddingLeftRightTextField(left: 10, right: 0)
        toTf.inputView = datePicker
        fromTf.inputView = datePicker
        fromTf.delegate = self
        toTf.delegate = self
        datePicker.maximumDate = Date()
    }
    //MARK: - Validation
    func checkValidations(){
        if fromTf.text!.isEmpty{
            ToastManager.errorToast(delegate: self, msg: "Enter start date")
        }else if toTf.text!.isEmpty{
            ToastManager.errorToast(delegate: self, msg: "Enter end date")
        }else{
            self.dismiss(animated: true){
                self.delegate?.filterwithdata(fromDate: self.fromDate, toDate: self.toDate)
            }
        }
    }
    @IBAction func applyBtnClicked(_ sender: Any) {
        checkValidations()
    }
    
    @IBAction func dissmissBtnClicked(_ sender: Any) {
        self.dismiss(animated: true)
    }
    
}
//MARK: - Delegate
extension FilterPopUpVC:UITextFieldDelegate{
    func textFieldDidEndEditing(_ textField: UITextField) {
        let formatter = DateFormatter()
           formatter.dateFormat = "dd/MM/yyyy"
        textField.text = formatter.string(from: datePicker.date)
        let formatter2 = DateFormatter()
        formatter2.dateFormat = "yyyy/MM/dd"
        if textField == toTf{
            self.toDate = formatter2.string(from: datePicker.date)
        }else{
            self.fromDate = formatter2.string(from: datePicker.date)
        }
    }
}
