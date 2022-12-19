//
//  TabbarController.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/18/22.
//


import Foundation
import UIKit

class TabBarController:  UITabBarController  {
    
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
        UITabBarItem.appearance().titlePositionAdjustment = UIOffset(horizontal: 0, vertical: -5)
        UITabBarItem.appearance().setTitleTextAttributes([.font:AppFont.FontName.bold.getFont(size: AppFont.pX10)], for: .normal)
        tabBar.barTintColor = .black
        tabBar.isTranslucent = true
        tabBar.unselectedItemTintColor = .gray
//        self.extendedLayoutIncludesOpaqueBars = false
//        self.edgesForExtendedLayout = .bottom
        tabBar.tintColor = .white
       
        homeViewController = HomeVC()
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
            tabBarItem.title = tabBarItemTitle[index]
            if index == 2 {
                // Media Button
                tabBarItem.title = ""
                tabBarItem.imageInsets = UIEdgeInsets(top: -6, left: 0, bottom: -6, right: 0)
                
            }else{
                tabBarItem.imageInsets = UIEdgeInsets(top: -3, left: 0, bottom: 3, right: 0)
            }
        }
     
    }
    override func viewWillAppear(_ animated: Bool) {
        
    }
//    override var selectedViewController: UIViewController? { // Mark 2
//        didSet {
//            guard let viewControllers = viewControllers else { return }
//        }
//
//
//    }
//    override var selectedIndex: Int { // Mark 1
//
//        didSet {
//            print("SSELECTED INDEX")
//            guard let selectedViewController = viewControllers?[selectedIndex] else { return }
//            self.selectedViewController = selectedViewController
//        }
//    }

    
}
extension TabBarController : UITabBarControllerDelegate{
    func tabBarController(_ tabBarController: UITabBarController, didSelect viewController: UIViewController) {
      print(#function)
    }
    func tabBarController(_ tabBarController: UITabBarController, shouldSelect viewController: UIViewController) -> Bool {
        print(#function)
        return true
    }
}
