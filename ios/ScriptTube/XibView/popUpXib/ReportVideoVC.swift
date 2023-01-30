//
//  ReportVideoVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 05/12/22.
//

import UIKit
protocol ReportVideoDelegate{
    func videoReported()
}

class ReportVideoVC: UIViewController {

    @IBOutlet weak var submitOLbl: UILabel!
    @IBOutlet weak var reportTv: CustomTextView!
    @IBOutlet weak var messageLbl: UILabel!
    @IBOutlet weak var reportLbl: UILabel!
    var videoId = ""
    var delegate :ReportVideoDelegate?
    override func viewDidLoad() {
        super.viewDidLoad()

        setup()
        //giveColor()
        // Do any additional setup after loading the view.
    }
    @IBAction func dismissBtn(_ sender: Any) {
        self.dismiss(animated: true)
    }
    
    func setup(){
        
        //self.view.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(closepopup)))
        submitOLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX12)
        messageLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        reportLbl.font = AppFont.FontName.semiBold.getFont(size: AppFont.pX16)
    
    }
    func giveColor(){
        //let attrString = NSMutableAttributedString()
        let text = "Report Video"
        let underlineAttriString = NSMutableAttributedString(string: text)
        let range1 = (text as NSString).range(of: "Video")
             
             //underlineAttriString.addAttribute(NSAttributedString.Key.font, value: AppFont.FontName.regular.getFont(size: AppFont.pX12), range: range1)
        underlineAttriString.addAttribute(NSAttributedString.Key.foregroundColor, value: UIColor.theme, range: range1)
        reportLbl.attributedText = underlineAttriString
    }
    @objc func closepopup(){
        self.dismiss(animated: true)
    }
    func reportVideoApi(param:[String:String]){
        guard let pvc = self.presentingViewController else{return}
        DataManager.reportVideo(delegate: self, param: param) {
            //ToastManager.successToast(delegate: self, msg: "Video Reported Successfully")
            self.closepopup()
            self.delegate?.videoReported()
        }
    }
    @IBAction func submitBtnClicked(_ sender: Any) {
        if reportTv.text == ""{
            ToastManager.errorToast(delegate: self, msg: "Please tell us reason")
        }else{
            let param = ["videoId":self.videoId,"reason":reportTv.text ?? ""]
            reportVideoApi(param: param)
        }
       
    }
}
