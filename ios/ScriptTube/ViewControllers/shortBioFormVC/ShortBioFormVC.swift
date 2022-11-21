//
//  ShortBioFormVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/17/22.
//

import UIKit

class ShortBioFormVC: BaseControllerVC {

    @IBOutlet weak var  descriptionLbl:UILabel!
    @IBOutlet weak var  textCountLbl:UILabel!
    @IBOutlet weak var  submitBtn:UIButton!
    @IBOutlet weak var  bioTF:UITextView!
    override func viewDidLoad() {
        super.viewDidLoad()
        setfonts()
        addNavBar(headingText:"Short Bio",redText:"Bio")

        // Do any additional setup after loading the view.
    }
    func setfonts(){
        textCountLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        descriptionLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        submitBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: AppFont.pX17)

        bioTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)



    }
    @IBAction func submitAction(_ sender: AnyObject) {

    }
}
