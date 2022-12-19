//
//  ReportBtnPopUp.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 13/12/22.
//

import UIKit

class ReportBtnPopUp: UIViewController {
var videoId = ""
    @IBOutlet weak var reportLbl: UILabel!
    @IBOutlet weak var reportIcon: UIImageView!
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        reportLbl.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(gotoReport)))
        reportIcon.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(gotoReport)))
    }
    @IBAction func dismissBtn(_ sender: Any) {
        self.dismiss(animated: true)
    }
    @objc func gotoReport(){
       // let pvc = self.presentingViewController
        self.dismiss(animated: true){
            
        }
        weak var pvc = self.presentingViewController

        self.dismiss(animated: true, completion: {
            let popUp = ReportVideoVC()
            popUp.videoId = self.videoId
            popUp.modalTransitionStyle = .crossDissolve
            popUp.modalPresentationStyle = .overCurrentContext
           // pvc?.tabBarController?.present(popUp, animated: true)
            pvc?.present(popUp, animated: true, completion: nil)
        })
        
    }
}
