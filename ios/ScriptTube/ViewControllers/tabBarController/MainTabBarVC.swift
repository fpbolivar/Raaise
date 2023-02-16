//
//  MainTabBarVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 01/12/22.
//

import UIKit
protocol MainTabbarVCDelegate{
    func cameraOpened()
}
class MainTabBarVC: UITabBarController, UITabBarControllerDelegate {
    var homeNavigationController: UIViewController!
    var homeViewController: UIViewController!
    var discoverViewController: UIViewController!
    var mediaViewController: UIViewController!
    var inboxViewController: UIViewController!
    var profileViewController: UIViewController!
    var thisDelegate:MainTabbarVCDelegate?
    //MARK: - Setup
    override func viewDidLoad() {
        super.viewDidLoad()
        DataManager.getsettingsData(delegate: self ,param: ["type":"s3bucket"],needLoader: false) { data in
            UserDefaultHelper.setBaseUrl(value: data["description"].stringValue)
        }
        AuthManager.getProfileApi(delegate: self, needLoader: false) {
            print("REPLYTOCOMMENT",AuthManager.currentUser.profileImage,AuthManager.currentUser.userName)
        }
        tabBar.tintColor = UIColor.theme
        tabBar.unselectedItemTintColor = .white
        tabBar.barTintColor = .black
        tabBar.backgroundColor = .black
        delegate = self
        homeViewController = HomeVC2()
        homeNavigationController = UINavigationController(rootViewController: homeViewController)
        discoverViewController = UINavigationController(rootViewController:SearchVC())//
        mediaViewController = AddMediaVC()
        inboxViewController = UINavigationController(rootViewController:InboxVC()) //
        profileViewController = UINavigationController(rootViewController:ProfileVC())// 
     
        
        homeViewController.tabBarItem.image = UIImage(named: "ic_un_home")
        homeViewController.tabBarItem.selectedImage = UIImage(named: "ic_selected_home")
        
        discoverViewController.tabBarItem.image = UIImage(named: "ic_un_search")
        discoverViewController.tabBarItem.selectedImage = UIImage(named: "ic_selected_search")
        
        mediaViewController.tabBarItem.image = UIImage(named: "addMedia")
        
        inboxViewController.tabBarItem.image = UIImage(named: "ic_un_inbox")
        inboxViewController.tabBarItem.selectedImage = UIImage(named: "ic_selected_inbox")
        
        profileViewController.tabBarItem.image = UIImage(named: "ic_un_profile")
        profileViewController.tabBarItem.selectedImage = UIImage(named: "ic_selected_profile")
        
        viewControllers = [homeNavigationController, discoverViewController, mediaViewController, inboxViewController, profileViewController]
        
        let tabBarItemTitle = ["Home", "Search", "Add", "Inbox", "Profile"]
        
        for (index, tabBarItem) in tabBar.items!.enumerated() {
            if index == 3{
                DataManager.getUnreadChatCount(delegate: self) { json in
                    if json["unreadMessageCount"].intValue > 0{
                        tabBarItem.badgeColor = .theme
                        tabBarItem.badgeValue =  "\(json["unreadMessageCount"].intValue)"
                    }else{
                        tabBarItem.badgeValue =  nil
                    }
                }
            }
            if index == 2 {
                // Media Button
                tabBarItem.title = ""
                tabBarItem.imageInsets = UIEdgeInsets(top: -6, left: 0, bottom: -6, right: 0)
                
            }else{
                tabBarItem.imageInsets = UIEdgeInsets(top: 6, left: 0, bottom: 0, right: 0)
            }
        }
    }
    override func tabBar(_ tabBar: UITabBar, didSelect item: UITabBarItem) {
        print(#function)
        DataManager.getUnreadChatCount(delegate: self) { json in
            if json["unreadMessageCount"].intValue > 0{
                tabBar.items?[3].badgeValue =  "\(json["unreadMessageCount"].intValue)"
            }else{
                tabBar.items?[3].badgeValue =  nil
            }
        }
    }
    //MARK: - Camera PopUp
   func tabBarController(_ tabBarController: UITabBarController, shouldSelect viewController: UIViewController) -> Bool {
        print(#function)
       if viewController.isKind(of: AddMediaVC.self) {
           let vc =  AddMediaVC()
           vc.delegate = self
           let navigationController = UINavigationController(rootViewController: vc)
           
           navigationController.modalPresentationStyle = .fullScreen
           thisDelegate?.cameraOpened()
           self.present(navigationController, animated: true, completion: nil)
           return false
       }
     return true
    }
}

extension MainTabBarVC:CustomVideoPostedDelegate{
    func videoPosted(status:UploadStatus) {
        if status == .start{
            ToastManager.successToast(delegate: self, msg: "Your Video is uploading, DO NOT close the App")
        }else{
            ToastManager.successToast(delegate: self, msg: "Video Posted Successfully.")
        }
        
    }
}
