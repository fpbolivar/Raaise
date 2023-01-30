//
//  AddNewPaymentMethodVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/21/22.
//

import UIKit

class AddNewPaymentMethodVC: BaseControllerVC {

    @IBOutlet weak var nameTf: UITextField!
    @IBOutlet weak var saveLbl: UILabel!
    @IBOutlet weak var bottomInfoLbl:UILabel!
    @IBOutlet weak var topInfoLbl:UILabel!
    @IBOutlet weak var payBtn:UIButton!
    @IBOutlet weak var  tableView:ContentSizedTableView!
    @IBOutlet weak var  cardTF:UITextField!
    @IBOutlet weak var  monthTF:UITextField!
    @IBOutlet weak var  cvvTF:UITextField!
    var newCardAdded : (()->Void)? = nil
    override func viewDidLoad() {
        super.viewDidLoad()
        //addNavBar(headingText:"Payment Methods",redText:"Methods")
        setfonts()
        tableView.separatorStyle = .none
        tableView.register(UINib(nibName: CardCell.identifier, bundle: nil), forCellReuseIdentifier: CardCell.identifier)
        executeTblDelegate()
        payBtn.addTarget(self, action: #selector(checkValidations), for: .touchUpInside)
        // Do any additional setup after loading the view.
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
    }
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        
    }
    @objc func pay(){
        let str = monthTF.text?.split(separator: "/").map({String($0)})
        guard let month = str?[0], let year = str?[1] else{return}
        print("MONTHYEARS",month,year)
        let cardNo = cardTF.text!.replacingOccurrences(of: " ", with: "")
        let param = ["name":nameTf.text!,"number":cardNo,"exp_month":month,"exp_year":year,"cvc":cvvTF.text!] //as! [String:String]
        self.pleaseWait()
        print("PARAMMMMM",param)
        AuthManager.addCardApi(delegate: self, param: param) {
            DispatchQueue.main.async {
                self.dismiss(animated: true)
                self.newCardAdded!()
                self.clearAllNotice()
            }
        }
    }
    @objc func checkValidations(){
        if cardTF.text!.isEmpty{
            ToastManager.errorToast(delegate: self, msg: "Enter Card Number")
        }else if cardTF.text!.count < 13{
            ToastManager.errorToast(delegate: self, msg: "Enter Valid Card Number")
        }else if monthTF.text!.isEmpty {
            ToastManager.errorToast(delegate: self, msg: "Enter Expiration Date")
        }else if cvvTF.text!.isEmpty{
            ToastManager.errorToast(delegate: self, msg: "Enter CVV Number")
        }else if cvvTF.text!.count < 3{
            ToastManager.errorToast(delegate: self, msg: "Enter Valid CVV Number")
        }else if nameTf.text!.isEmpty{
            ToastManager.errorToast(delegate: self, msg: "Enter Name")
        }else{
            pay()
        }
    }
    @IBAction func dismissBtn(_ sender: Any) {
        self.dismiss(animated: true)
    }
    func setfonts(){
        saveLbl.font = AppFont.FontName.light.getFont(size: AppFont.pX18)
        bottomInfoLbl.font = AppFont.FontName.light.getFont(size: AppFont.pX12)
        topInfoLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX14)
        //cardTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        nameTf.font = AppFont.FontName.bold.getFont(size: AppFont.pX14)
        cardTF.font = AppFont.FontName.bold.getFont(size: AppFont.pX14)
        monthTF.font = AppFont.FontName.bold.getFont(size: AppFont.pX14)
        cvvTF.font = AppFont.FontName.bold.getFont(size: AppFont.pX14)
        nameTf.placeholderColor(color: UIColor.darkGray, placeholderText: "Name on Card")
        cardTF.placeholderColor(color: UIColor.darkGray, placeholderText: "Card Number")
        monthTF.placeholderColor(color: UIColor.darkGray, placeholderText: "Valid thru")
        //monthTF.placeholderColor(color: UIColor.darkGray, placeholderText: "MM / YYYY")
        monthTF.delegate = self
        cardTF.delegate = self
        cvvTF.delegate = self
        cvvTF.placeholderColor(color: UIColor.darkGray, placeholderText: "CVV")
    }
    func executeTblDelegate(){
        tableView.delegate = self
        tableView.dataSource = self
        tableView.reloadData()
    }
}
extension AddNewPaymentMethodVC:UITableViewDelegate,UITableViewDataSource{
    func numberOfSections(in tableView: UITableView) -> Int {
        1
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        3
    }


    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {

        return 80
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier:  CardCell.identifier, for: indexPath) as!  CardCell

        return cell
    }

    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
    }

}
extension AddNewPaymentMethodVC:UITextFieldDelegate{
    func format(with mask: String, phone: String) -> String {
        let numbers = phone.replacingOccurrences(of: "[^0-9]", with: "", options: .regularExpression)
        var result = ""
        var index = numbers.startIndex // numbers iterator

        // iterate over the mask characters until the iterator of numbers ends
        for ch in mask where index < numbers.endIndex {
            if ch == "X" {
                // mask requires a number in this place, so take the next one
                result.append(numbers[index])

                // move numbers iterator to the next index
                index = numbers.index(after: index)

            } else {
                result.append(ch) // just append a mask character
            }
        }
        return result
    }
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        if textField == cardTF{
            let newString = (textField.text! as NSString).replacingCharacters(in: range, with: string)
            textField.text = format(with: "XXXX XXXX XXXX XXXX", phone: newString)
            return false
            //return textField.text!.count + (string.count - range.length) <= 16
        }else if textField == cvvTF{
            return textField.text!.count + (string.count - range.length) <= 4
        }else{
            var newStr = ""
            print(string)
            if string != "0" && string != "1" && string != "" && textField.text!.count == 0{
                newStr = "0"//"0\(string)"
                textField.text = newStr
            }
            if textField.text!.count == 2 && string != ""{
                textField.text?.append("/")
            }
            return textField.text!.count + (string.count - range.length) <= 7
        }
    }
}
