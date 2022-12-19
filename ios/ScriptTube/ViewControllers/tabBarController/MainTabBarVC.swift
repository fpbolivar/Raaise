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
//    override var selectedViewController: UIViewController? { // Mark 2
//        didSet {
//
//            guard let viewControllers = viewControllers else { return }
//            for viewController in viewControllers {
//                print(viewController.tabBarItem.tag)
//            }
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
    override func viewDidLoad() {
        super.viewDidLoad()
        tabBar.tintColor = UIColor.theme
        tabBar.unselectedItemTintColor = .white
        tabBar.barTintColor = .black
        
//        tabBar.tintColor = UIColor.theme
//        tabBar.unselectedItemTintColor = .black
//        tabBar.barTintColor = .white
        tabBar.backgroundColor = .black
        delegate = self
        // Do any additional setup after loading the view.
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
            //tabBarItem.title = tabBarItemTitle[index]
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
    }
   func tabBarController(_ tabBarController: UITabBarController, shouldSelect viewController: UIViewController) -> Bool {
        print(#function)
       if viewController.isKind(of: AddMediaVC.self) {
           let vc =  AddMediaVC()
           let navigationController = UINavigationController(rootViewController: vc)
           //BaseNavigationController.init(rootViewController: vc)
           navigationController.modalPresentationStyle = .overFullScreen
           thisDelegate?.cameraOpened()
           self.present(navigationController, animated: true, completion: nil)
           return false
       }
     return true
    }
}

