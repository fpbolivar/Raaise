//
//  BaseControllerVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/18/22.
//

import Foundation
import UIKit
class BaseControllerVC:UIViewController{
    var navView:NavigationBar!
    var  type:NavBarType = .largeNavBarOnlyBack

    var headingText: String? {

        didSet { print("Name was changed.")
            headingtitleSet()
        }
    }
    var redText: String? {

        didSet { print("Name was changed.")
            headingtitleSet()
        }
    }
   // var rightBtnAction:(()->Void)!
    override func viewDidLoad() {
        super.viewDidLoad()
        self.hideNavbar()
        navView =  NavigationBar.instanceFromNib() as? NavigationBar
        
    }
    func addNavBar(headingText:String,redText:String,type:NavBarType = .largeNavBarOnlyBack){

        self.type = type

        self.headingText = headingText
        self.redText = redText
        addNavBar(type:type)
    }
    private func addNavBar(style:NavBarStyle?=nil,title:String="",type:NavBarType = .largeNavBarOnlyBack,leftAction:Selector?=nil,deleteSelector: Selector? = nil){
       // navView.contactIcon.isUserInteractionEnabled = true
        navView.leftIcon.isUserInteractionEnabled = true
       // navView.msgIcon.isUserInteractionEnabled = true
        titleSet()
        // navView.contactIcon.image = UIImage(named: "ic_profile")!.addPadding(5)
        // navView.msgIcon.image = UIImage(named: "ic_messages")!.addPadding(5)
        navView.translatesAutoresizingMaskIntoConstraints = false
        navView.leftIcon.image = UIImage(named: "ic_back")!.addPadding(0, 0, 20, 0)
        navView.rigthBtn.isHidden = true
        if let leftAction = leftAction{
        navView.leftIcon.addGestureRecognizer(UITapGestureRecognizer(target: self, action: leftAction))
        }else{
            navView.leftIcon.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(backAction)))
        }
        self.view.addSubview(navView)
        navView.topAnchor.constraint(equalTo: self.view.safeAreaLayoutGuide.topAnchor, constant: 0).isActive = true
        navView.leadingAnchor.constraint(equalTo: self.view.leadingAnchor, constant: 0).isActive = true
        navView.trailingAnchor.constraint(equalTo: self.view.trailingAnchor, constant: 0).isActive = true
        if .largeNavBarOnlyBack == type{
            navView.heightAnchor.constraint(equalToConstant: 60).isActive = true
        }
        if .largeNavBarOnlyBackWithRightBtn == type{
            navView.heightAnchor.constraint(equalToConstant: 60).isActive = true
            navView.rigthBtn.isHidden = false
           // navView.rigthBtn.addTarget(self, action: #selector(rightAction), for: .touchUpInside)
        }
        if .smallNavBarOnlyBack == type{
            navView.heightAnchor.constraint(equalToConstant: 40).isActive = true
        }

    }

    private  func titleSet(){
        //navView.title.text = titleText ?? ""
        //navView.title.adjustsFontSizeToFitWidth = true
        //navView.title.font =  AppFont.FontName.regular.getFont(size: AppFont.pX15)
    }
    private  func headingtitleSet(){
        navView.title.text =  ""
        navView.headingLbl.text =  ""
        if .largeNavBarOnlyBack == type || .largeNavBarOnlyBackWithRightBtn == type{

        navView.headingLbl.adjustsFontSizeToFitWidth = true
        navView.headingLbl.font =  AppFont.FontName.regular.getFont(size: AppFont.pX20)
        let customType = ActiveType.custom(pattern:   redText ?? "")
        navView.headingLbl.enabledTypes.append(customType)
        navView.headingLbl.textColor = UIColor.black
        navView.headingLbl.underLineEnable = false
        navView.headingLbl.text =  "    " + (headingText ?? "")

        navView.headingLbl.customColor[customType] = UIColor.theme
        navView.headingLbl.customSelectedColor[customType] = UIColor.gray
        }
        if .smallNavBarOnlyBack == type{
            navView.title.adjustsFontSizeToFitWidth = true
            navView.title.font =  AppFont.FontName.regular.getFont(size: AppFont.pX20)
            navView.title.text =  self.headingText
        }


    }
    func hideCustomNavBar(){
        self.navView.isHidden = true
    }
    func unhideCustomNavBar(navigationBar: NavigationBar){
        navigationBar.isHidden = false
    }
    @objc func gotoProfile(){

    }
    @objc  func gotoFriendChatList(){

    }
    @objc  func gotoSetting(){

    }
    @objc  func backAction(){
        self.navigationController?.popViewController(animated: true)
    }
}
