//
//  PersonalInfoFormVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/17/22.
//

import UIKit

class PersonalInfoFormVC: BaseControllerVC {
    @IBOutlet weak var  descriptionLbl:UILabel!
    @IBOutlet weak var  submitBtn:UIButton!
    @IBOutlet weak var  emailTF:UITextField!
    @IBOutlet weak var  mobileTF:UITextField!
    @IBOutlet weak var  nameTF:UITextField!

    override func viewDidLoad() {
        super.viewDidLoad()
        setfonts()
        addNavBar(headingText:"Personal Information",redText:"Information")
        // Do any additional setup after loading the view.
    }
    func setfonts(){
        descriptionLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        submitBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: AppFont.pX17)
        emailTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        nameTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        mobileTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        paddingTF(tf:emailTF);
        paddingTF(tf:mobileTF);
        paddingTF(tf:nameTF);
    }
    func paddingTF(tf:UITextField,value:Int=20){
        tf.paddingLeftRightTextField(left: CGFloat(value), right: CGFloat(value))
    }
    func setPlaceholder(){
        mobileTF.placeholder = ""
        nameTF.placeholder = ""
        emailTF.placeholder = "abc@ymail.uk"
    }

    @IBAction func submitAction(_ sender: AnyObject) {
        let vc = UserNameFormVC()
        self.navigationController?.pushViewController(vc, animated: true)
    }

}
