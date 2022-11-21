//
//  HtmlTextRenderVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/18/22.
//

import UIKit

class HtmlTextRenderVC: BaseControllerVC {
    @IBOutlet weak var  infoDescription:UITextView!
    var fullNavTitle = ""
    var redNavTitle = ""
    override func viewDidLoad() {
        super.viewDidLoad()
        addNavBar(headingText:fullNavTitle,redText:redNavTitle)
        setfonts()
        // Do any additional setup after loading the view.
    }
    func setfonts(){
        infoDescription.font = AppFont.FontName.regular.getFont(size: AppFont.pX15)
    }

}
