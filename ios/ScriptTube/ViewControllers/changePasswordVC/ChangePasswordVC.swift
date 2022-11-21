//
//  ChangePasswordVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/17/22.
//

import UIKit

class ChangePasswordVC: BaseControllerVC {
    @IBOutlet weak var  descriptionLbl:UILabel!
    @IBOutlet weak var  submitBtn:UIButton!
    @IBOutlet weak var  currentPassTF:UITextField!
    @IBOutlet weak var  newPassTF:UITextField!
    @IBOutlet weak var  cnfPassTF:UITextField!

    override func viewDidLoad() {
        super.viewDidLoad()
        setfonts()
        addNavBar(headingText:"Change Password",redText:"Password")

        // Do any additional setup after loading the view.
    }
    func setfonts(){
        descriptionLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        submitBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: AppFont.pX17)
        cnfPassTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        newPassTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        currentPassTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        paddingTF(tf:currentPassTF);
        paddingTF(tf:newPassTF);
        paddingTF(tf:cnfPassTF);
    }
    func paddingTF(tf:UITextField,value:Int=20){
        tf.paddingLeftRightTextField(left: CGFloat(value), right: CGFloat(value))
    }
    func setPlaceholder(){
        currentPassTF.placeholder = "Old Password"
        newPassTF.placeholder = "New Password"
        cnfPassTF.placeholder = "Confirm Password"
    }
    @IBAction func submitAction(_ sender: AnyObject) {

    }


}
