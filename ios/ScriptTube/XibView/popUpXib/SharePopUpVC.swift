//
//  SharePopUpVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 15/12/22.
//

import UIKit

class SharePopUpVC: UIViewController {

    var delegate:ShareActionDelegate?
    override func viewDidLoad() {
        super.viewDidLoad()
        print("POSTPOPUPview")
        // Do any additional setup after loading the view.
    }
    
    @IBAction func dismissBtn(_ sender: Any) {
        self.dismiss(animated: true)
    }
    
    @IBAction func postBtn(_ sender: Any) {
        self.dismiss(animated: true){
            self.delegate?.postVideo()
        }
    }
    
    @IBAction func shareBtn(_ sender: Any) {
        self.dismiss(animated: true){
            self.delegate?.shareVideo()
        }
    }
    
}
protocol ShareActionDelegate{
    func postVideo()
    func shareVideo()
}
