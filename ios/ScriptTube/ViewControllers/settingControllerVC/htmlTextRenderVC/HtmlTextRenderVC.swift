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
    var viewType: ViewType!
    var isPresented:Bool = false
    override func viewDidLoad() {
        super.viewDidLoad()
        if isPresented{
            addNavBar(headingText:fullNavTitle,leftAction: #selector(dismissPage),redText:redNavTitle)
        }else{
            addNavBar(headingText:fullNavTitle,redText:redNavTitle)
        }
        setfonts()
        getData(type: viewType)
        // Do any additional setup after loading the view.
    }
    @objc func dismissPage(){
        self.dismiss(animated: true)
    }
    func setfonts(){
        infoDescription.font = AppFont.FontName.regular.getFont(size: AppFont.pX15)
    }
    //MARK: -Api method
    func getData(type:ViewType){
        var param: [String:String] = [:]
        switch type{
            
        case .privacy:
            param = ["type":ViewType.privacy.rawValue]
        case .service:
            param = ["type":ViewType.service.rawValue]
        case .copyright:
            param = ["type":ViewType.copyright.rawValue]
        }
        DataManager.getsettingsData(delegate: self, param: param) { data in
            let text = data["description"].stringValue
            DispatchQueue.main.async {
                self.convertHtml(htmlString: text) { attStr in
                    print("jsdkjasdhj",attStr)
                    self.infoDescription.attributedText = attStr
                }
                
            }
        }
    }

}
enum ViewType:String{
    case privacy = "privacy"
    case service = "service"
    case copyright = "copyright"
}
