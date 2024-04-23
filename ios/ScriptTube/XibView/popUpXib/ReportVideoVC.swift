//
//  ReportVideoVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 05/12/22.
//

import UIKit


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
    }
    @IBAction func dismissBtn(_ sender: Any) {
        self.dismiss(animated: true)
    }
    //MARK: - Setup
    func setup(){
        submitOLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX12)
        messageLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        reportLbl.font = AppFont.FontName.semiBold.getFont(size: AppFont.pX16)
    }
    func giveColor(){
        let text = "Report Video"
        let underlineAttriString = NSMutableAttributedString(string: text)
        let range1 = (text as NSString).range(of: "Video")
       // underlineAttriString.addAttribute(NSAttributedString.Key.foregroundColor, value: UIColor.theme, range: range1)
        underlineAttriString.addAttribute(NSAttributedString.Key.foregroundColor, value: UIColor.new_theme, range: range1)
        reportLbl.attributedText = underlineAttriString
    }
    @objc func closepopup(){
        self.dismiss(animated: true)
    }
    //MARK: - Report Api
    func reportVideoApi(param:[String:String]){
        DataManager.reportVideo(delegate: self, param: param) {
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

//MARK: - Report Video Protocols
protocol ReportVideoDelegate{
    func videoReported()
}
