//
//  ReportBtnPopUp.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 13/12/22.
//

import UIKit

class ReportBtnPopUp: UIViewController {
    var videoId = ""
    var forBlock = false
    var isvideoReported = false
    var blockDelegate:BlockUserDelegate?
    var delegate : ReportVideoDelegate?
    var btnDelegate : ReportBtnDelegate?
    @IBOutlet weak var blockUserLbl: UILabel!
    @IBOutlet weak var blockImg: UIImageView!
    @IBOutlet weak var blockView: UIView!
    @IBOutlet weak var reportView: UIView!
    @IBOutlet weak var reportLbl: UILabel!
    @IBOutlet weak var reportIcon: UIImageView!
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        blockUserLbl.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(blockUser)))
        blockImg.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(blockUser)))
        reportLbl.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(gotoReport)))
        reportIcon.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(gotoReport)))
        if forBlock{
            reportView.isHidden = true
            blockView.isHidden = false
        }else{
            reportView.isHidden = false
            blockView.isHidden = true
        }
    }
    //MARK: - Actions
    @IBAction func dismissBtn(_ sender: Any) {
        self.dismiss(animated: true)
    }
    @objc func blockUser(){
        self.dismiss(animated: true){
            self.blockDelegate?.blockUser()
        }
    }
    @objc func gotoReport(){
        if isvideoReported{
            self.dismiss(animated: true){
                self.btnDelegate?.alreadyReportedVideo()
            }
        }else{
            weak var pvc = self.presentingViewController

            self.dismiss(animated: true, completion: {
                let popUp = ReportVideoVC()
                popUp.delegate = self
                popUp.videoId = self.videoId
                popUp.modalTransitionStyle = .crossDissolve
                popUp.modalPresentationStyle = .overCurrentContext
                pvc?.present(popUp, animated: true, completion: nil)
            })
            
        }
    }
}
//MARK: - Report Delegate
extension ReportBtnPopUp:ReportVideoDelegate{
    func videoReported() {
        delegate?.videoReported()
    }
}
//MARK: - Protocols
protocol ReportBtnDelegate{
    func alreadyReportedVideo()
}
protocol BlockUserDelegate{
    func blockUser()
}
