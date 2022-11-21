//
//  DeleteAccountVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/18/22.
//

import UIKit

class DeleteAccountVC: BaseControllerVC {
    @IBOutlet weak var  questionLbl:UILabel!
    @IBOutlet weak var nameLbl:UILabel!
    @IBOutlet weak var deactiveLbl:UILabel!
    @IBOutlet weak var messageLbl:UILabel!
    @IBOutlet weak var submitBtn:UIButton!

    override func viewDidLoad() {
        super.viewDidLoad()
        addNavBar(headingText:"Delete Account",redText:"Account")
        setfonts()
        // Do any additional setup after loading the view.
    }
    func setfonts(){
        questionLbl.font = AppFont.FontName.semiBold.getFont(size: AppFont.pX30)
        nameLbl.font = AppFont.FontName.semiBold.getFont(size: AppFont.pX18)
        deactiveLbl.font = AppFont.FontName.semiBold.getFont(size: AppFont.pX15)
        messageLbl.font = AppFont.FontName.light.getFont(size: AppFont.pX14)
        submitBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: AppFont.pX17)
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
