//
//  UserNameFormVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/17/22.
//

import UIKit

class UserNameFormVC: BaseControllerVC {
    @IBOutlet weak var updateLbl: UILabel!
    @IBOutlet weak var  descriptionLbl:UILabel!
    @IBOutlet weak var  submitBtn:UIButton!
    @IBOutlet weak var  nameTF:UITextField!
    override func viewDidLoad() {
        super.viewDidLoad()
        addNavBar(headingText:"Username",redText:"")
        setfonts()
    }
    //MARK: -Setup
    func setfonts(){
        nameTF.overrideUserInterfaceStyle = .light
        descriptionLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
        submitBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: AppFont.pX17)
        updateLbl.font = AppFont.FontName.medium.getFont(size: AppFont.pX18)
        nameTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        paddingTF(tf:nameTF);
        setPlaceholder()
    }
    func paddingTF(tf:UITextField,value:Int=20){
        tf.paddingLeftRightTextField(left: CGFloat(value), right: CGFloat(value))
    }
    func setPlaceholder(){
        nameTF.attributedPlaceholder = NSAttributedString(string: "Username",attributes: [.foregroundColor: UIColor.lightGray])
        nameTF.layer.cornerRadius = 10
        nameTF.text = AuthManager.currentUser.userName
    }
    //MARK: -Validation
    func checkValidations(){
        if (nameTF.text!.isEmpty){
            ToastManager.errorToast(delegate: self, msg: LocalStrings.emptyUsername)
            return
        }
        else{
            if(!(Constant.check_Internet?.isReachable)!){
                AlertView().showInternetErrorAlert(delegate: self)
                return
            }
            updateProfileApi()
        }
    }
    //MARK: -Api method
    func updateProfileApi(){
        let param = ["userName":nameTF.text ?? ""]
        AuthManager.updateUserProfileApi(delegate: self, param: param) {
            DispatchQueue.main.async {
                ToastManager.successToast(delegate: self, msg: "Username Updated Successfully")
            }
        }
    }
    func gotoShortBioForm(){
        let vc = ShortBioFormVC()
        self.navigationController?.pushViewController(vc, animated: true)
    }
    @IBAction func submitAction(_ sender: AnyObject) {
        checkValidations()
    }

}
