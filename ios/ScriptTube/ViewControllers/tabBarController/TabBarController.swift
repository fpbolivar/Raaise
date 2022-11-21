//
//  TabbarController.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/18/22.
//


import Foundation
import UIKit

class TabBarController:  UITabBarController, UITabBarControllerDelegate {
    
    var homeNavigationController: UIViewController!
    var homeViewController: UIViewController!
    var discoverViewController: UIViewController!
    var mediaViewController: UIViewController!
    var inboxViewController: UIViewController!
    var profileViewController: UIViewController!

    
    //MARK: - LifeCycles
    override func viewDidLoad(){
        super.viewDidLoad()
        self.delegate = self
        
        tabBar.barTintColor = .white
        tabBar.isTranslucent = false
        tabBar.unselectedItemTintColor = .gray
        tabBar.tintColor = .black
        
        homeViewController = HomeVC()
        homeNavigationController = UINavigationController(rootViewController: homeViewController)
        discoverViewController = UINavigationController(rootViewController:SearchVC())
        mediaViewController = UINavigationController(rootViewController:AddMediaVC())
        inboxViewController = UINavigationController(rootViewController:InboxVC())
        profileViewController = UINavigationController(rootViewController:ProfileVC())
     
        
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
            tabBarItem.title = tabBarItemTitle[index]
            if index == 2 {
                // Media Button
                tabBarItem.title = ""
                tabBarItem.imageInsets = UIEdgeInsets(top: -6, left: 0, bottom: -6, right: 0)
                
            }
        }
    }
    
//    //MARK: UITabbar Delegate
//    func tabBarController(_ tabBarController: UITabBarController, shouldSelect viewController: UIViewController) -> Bool {
//        if viewController.isKind(of: MediaViewController.self) {
//            let vc =  UIStoryboard(name: "MediaViews", bundle: nil).instantiateViewController(identifier: "MediaVC") as! MediaViewController
//            let navigationController = BaseNavigationController.init(rootViewController: vc)
//            navigationController.modalPresentationStyle = .overFullScreen
//            self.present(navigationController, animated: true, completion: nil)
//            return false
//        }
//      return true
//    }
    
    
}
