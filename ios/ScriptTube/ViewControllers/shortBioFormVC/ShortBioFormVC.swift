//
//  ShortBioFormVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/17/22.
//

import UIKit

class ShortBioFormVC: BaseControllerVC {

    @IBOutlet weak var updateLbl: UILabel!
    @IBOutlet weak var  descriptionLbl:UILabel!
    @IBOutlet weak var  textCountLbl:UILabel!
    @IBOutlet weak var  submitBtn:UIButton!
    @IBOutlet weak var  bioTF:UITextView!
    override func viewDidLoad() {
        super.viewDidLoad()
        setfonts()
        textCountLbl.text = "\(bioTF.text.count)/45"
        if bioTF.text.count == 45{
            textCountLbl.textColor = .red
        }
        addNavBar(headingText:"Short Bio",redText:"Bio",color: UIColor(named:"bgColor" ))
    }
    //MARK: - Setup
    func setfonts(){
        updateLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX18)
        textCountLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
        descriptionLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
        submitBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: AppFont.pX17)

        bioTF.delegate = self
        bioTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        bioTF.text = AuthManager.currentUser.shortBio
    }
    //MARK: -Api method
    func updateProfileApi(){
        let param = ["shortBio":bioTF.text ?? ""]
        AuthManager.updateUserProfileApi(delegate: self, param: param) {
            DispatchQueue.main.async {
                ToastManager.successToast(delegate: self, msg: "Bio Updated Successfully")
            }
        }
    }
    @IBAction func submitAction(_ sender: AnyObject) {

        if(!(Constant.check_Internet?.isReachable)!){
            AlertView().showInternetErrorAlert(delegate: self)
            return
        }
        updateProfileApi()
    }
}
extension ShortBioFormVC:UITextViewDelegate{
    func textView(_ textView: UITextView, shouldChangeTextIn range: NSRange, replacementText text: String) -> Bool {
        if text == "\n"{
            return false
        }
        return textView.text.count + (text.count - range.length) <= 45
    }
    func textViewDidChange(_ textView: UITextView) {
        textCountLbl.text = "\(textView.text.count)/45"
        if textView.text.count == 45{
            textCountLbl.textColor = .red
        }else{
            textCountLbl.textColor = .lightGray
        }
    }
}
