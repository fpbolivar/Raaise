//
//  PaymentSuccessVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 09/01/23.
//

import UIKit

class PaymentSuccessVC: UIViewController {
    @IBOutlet weak var successLbl: UILabel!
    @IBOutlet weak var okLbl: UILabel!
    @IBOutlet weak var confirmLbl: UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()

        setup()
        // Do any additional setup after loading the view.
    }
    func setup(){
        okLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        successLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        confirmLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
    }
    @IBAction func okBtnClicked(_ sender: Any) {
        self.dismiss(animated: true)
    }
    @IBAction func dismissBtn(_ sender: Any) {
        self.dismiss(animated: true)
    }
}
