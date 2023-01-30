////
////  CustomNavBar.swift
////  ScriptTube
////
////  Created by Dilpreet Singh on 11/18/22.
////
//
//
//import Foundation
//import UIKit
enum NavBarType{
    case onlyBack
    case smallNavBarOnlyBack
    case largeNavBarOnlyBack
    case largeNavBarOnlyBackWithRightBtn
    case addNewCard
    case onlyTopTitle
    case filter
    case none

}
enum NavBarStyle{
    case backgroundClear
    case none

}
//extension UIViewController{
//    func addNavBar(style:NavBarStyle?=nil,title:String="",type:NavBarType = .sideMenuWithMsgContact,leftAction:Selector,deleteSelector: Selector? = nil)-> NavigationBar{
//        let navView  =  NavigationBar.instanceFromNib() as! NavigationBar
//        navView.contactIcon.isUserInteractionEnabled = true
//        navView.leftIcon.isUserInteractionEnabled = false
//        navView.msgIcon.isUserInteractionEnabled = true
//        navView.title.text = title
//        navView.title.adjustsFontSizeToFitWidth = true
//        navView.title.font =  AppFont.FontName.regular.getFont(size: AppFont.pX15)
//       // navView.contactIcon.image = UIImage(named: "ic_profile")!.addPadding(5)
//       // navView.msgIcon.image = UIImage(named: "ic_messages")!.addPadding(5)
//        navView.translatesAutoresizingMaskIntoConstraints = false
//        navView.leftIcon.image = UIImage(named: "ic_back")!.addPadding(10)
//        navView.leftIcon.addGestureRecognizer(UITapGestureRecognizer(target: self, action: leftAction))
////        if let style = style {
////            if(style == .backgroundClear){
////                navView.backgroundColor = .clear
////                navView.title.textColor = .white
////            }
////        }
////        if(type == .sideMenuWithMsgContact){
////            navView.leftIcon.image = UIImage(named: "ic_menu")!.addPadding(5)
////        }else if (type == .backWithMsgContact){
////            navView.leftIcon.image = UIImage(named: "ic_back")!.addPadding(5)
////        }
////        else if (type == .onlyBack){
////            navView.contactIcon.image = nil
////            navView.msgIcon.image = nil
////            navView.leftIcon.image = UIImage(named: "ic_back")!.addPadding(5)
////        } else if (type == .onlyContactWithBack){
////            navView.msgIcon.image = nil
////            navView.leftIcon.image = UIImage(named: "ic_back")!.addPadding(5)
////        }
////        else if (type == .onlySettingWithBack){
////            navView.msgIcon.image = nil
////            navView.contactIcon.image = UIImage(named: "ic_navbar_setting")!.addPadding(5)
////            navView.leftIcon.image = UIImage(named: "ic_back")!.addPadding(5)
////        }
////
////        else if (type == .none){
////            navView.contactIcon.image = nil
////            navView.msgIcon.image = nil
////            navView.leftIcon.image = nil
////
////        }  else if (type == .onlyDeleteWithBack){
////            navView.msgIcon.image = nil
////            navView.contactIcon.image = UIImage(named: "ic_delete")!.addPadding(8)
////
////            navView.leftIcon.image = UIImage(named: "ic_back")!.addPadding(5)
////        }
////
////        navView.leftIcon.addGestureRecognizer(UITapGestureRecognizer(target: self, action: leftAction))
////        if (type == .onlySettingWithBack){
////            navView.contactIcon.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(gotoSetting)))
////        }else
////        if (type == .onlyDeleteWithBack){
////            navView.contactIcon.addGestureRecognizer(UITapGestureRecognizer(target: self, action: deleteSelector))
////        }else{
////            navView.contactIcon.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(gotoProfile)))
////        }
////        navView.msgIcon.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(gotoFriendChatList)))
//
//        self.view.addSubview(navView)
//        navView.topAnchor.constraint(equalTo: self.view.safeAreaLayoutGuide.topAnchor, constant: 0).isActive = true
//        navView.leadingAnchor.constraint(equalTo: self.view.leadingAnchor, constant: 0).isActive = true
//        navView.trailingAnchor.constraint(equalTo: self.view.trailingAnchor, constant: 0).isActive = true
//        navView.heightAnchor.constraint(equalToConstant: 90).isActive = true
//        return navView
//    }
//    func hideNavBar(navigationBar: NavigationBar){
//        navigationBar.isHidden = true
//    }
//    func unhideNavBar(navigationBar: NavigationBar){
//        navigationBar.isHidden = false
//    }
//    @objc func gotoProfile(){
//
//    }
//    @objc  func gotoFriendChatList(){
//
//    }
//    @objc  func gotoSetting(){
//
//    }
//}
//
