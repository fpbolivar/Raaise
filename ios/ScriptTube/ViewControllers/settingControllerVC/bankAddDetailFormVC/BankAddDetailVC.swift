//
//  BankAddDetailVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/18/22.
//

import UIKit

class BankAddDetailVC: BaseControllerVC {
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
        // Do any additional setup after loading the view.
    }
    func setfonts(){
        descriptionLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        submitBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: AppFont.pX17)
        accountNameTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        accountNoTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        routingNoTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        cnfAccountNoTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        paddingTF(tf:cnfAccountNoTF);
        paddingTF(tf:accountNameTF);
        paddingTF(tf:routingNoTF);
        paddingTF(tf:accountNoTF);
    }
    func paddingTF(tf:UITextField,value:Int=20){
        tf.paddingLeftRightTextField(left: CGFloat(value), right: CGFloat(value))
    }


    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}
