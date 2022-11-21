//
//  UserNameFormVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/17/22.
//

import UIKit

class UserNameFormVC: BaseControllerVC {
    @IBOutlet weak var  descriptionLbl:UILabel!
    @IBOutlet weak var  submitBtn:UIButton!
    @IBOutlet weak var  nameTF:UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()
        addNavBar(headingText:"Username",redText:"")
        setfonts()
        // Do any additional setup after loading the view.
    }
    func setfonts(){
        descriptionLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        submitBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: AppFont.pX17)

        nameTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)


        paddingTF(tf:nameTF);
    }
    func paddingTF(tf:UITextField,value:Int=20){
        tf.paddingLeftRightTextField(left: CGFloat(value), right: CGFloat(value))
    }
    func setPlaceholder(){

        nameTF.placeholder = "Username"

    }
    @IBAction func submitAction(_ sender: AnyObject) {
        let vc = ShortBioFormVC()
        self.navigationController?.pushViewController(vc, animated: true)
    }

}
