//
//  CancelEditingPopUp.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 28/11/22.
//

import UIKit

class CancelEditingPopUp: UIViewController {
    @IBOutlet weak var discardLbl: UILabel!
    @IBOutlet weak var backToEditLbl: UILabel!
    @IBOutlet weak var titleLbl: UILabel!
    var delegate:CancelEditDelegate?
    override func viewDidLoad() {
        super.viewDidLoad()

        discardLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX12)
        backToEditLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX12)
        // Do any additional setup after loading the view.
    }
    @IBAction func backToEdit(_ sender: Any) {
        self.dismiss(animated: false)
    }
    
    @IBAction func discardChanges(_ sender: Any) {
        weak var pvc = self.presentingViewController
        self.dismiss(animated: false){
            //pvc?.tabBarController?.selectedIndex = 0
            self.delegate?.didDismissPopUp()
        }
    }
}

protocol CancelEditDelegate{
    func didDismissPopUp()
    
}
