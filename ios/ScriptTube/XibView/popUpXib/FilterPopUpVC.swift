//
//  FilterPopUpVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 20/01/23.
//

import UIKit

class FilterPopUpVC: UIViewController {

    @IBOutlet weak var toTf: UITextField!
    @IBOutlet weak var fromTf: UITextField!
    let datePicker = UIDatePicker()
    override func viewDidLoad() {
        super.viewDidLoad()
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
        // Do any additional setup after loading the view.
    }


    @IBAction func dissmissBtnClicked(_ sender: Any) {
        self.dismiss(animated: true)
    }
    
}
extension FilterPopUpVC:UITextFieldDelegate{
    func textFieldDidEndEditing(_ textField: UITextField) {
        let formatter = DateFormatter()
           formatter.dateFormat = "dd/MM/yyyy"
        textField.text = formatter.string(from: datePicker.date)
    }
}
