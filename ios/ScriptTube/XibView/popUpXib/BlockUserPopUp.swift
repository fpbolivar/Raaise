//
//  BlockUserPopUp.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 01/02/23.
//

import UIKit

class BlockUserPopUp: UIViewController {
    var username = ""
    var userId = ""
    @IBOutlet weak var cancelLbl: UILabel!
    @IBOutlet weak var blockLbl: UILabel!
    @IBOutlet weak var messageLbl: UILabel!
    @IBOutlet weak var blockUserLbl: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        setFonts()
        setData()
        // Do any additional setup after loading the view.
    }
    //MARK: - Setup
    func setData(){
        blockUserLbl.text = "Block \(username)?"
    }
    func setFonts(){
        cancelLbl.font = AppFont.FontName.semiBold.getFont(size: AppFont.pX14)
        blockLbl.font = AppFont.FontName.semiBold.getFont(size: AppFont.pX14)
        blockUserLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX18)
        messageLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
    }
    //MARK: - Actions
    @IBAction func dismissBtnClicked(_ sender: Any) {
        self.dismiss(animated: true)
    }
    
    @IBAction func blockUserClicked(_ sender: Any) {
        AuthManager.blockUser(delegate: self, param: ["userIdentity":userId]) {
            self.dismiss(animated: true)
        }
    }
}
