//
//  Extension + UIApplication.swift
//  ScriptTube
//
//  Created by CODE OPTIMAL SOLUTIONS on 10/04/24.
//

import Foundation
extension UIApplication {

    //MARK: GET TOP MOST CONTROLLER 
    static func topViewController(_ viewController: UIViewController? = UIApplication.keyWin?.rootViewController) -> UIViewController? {
        if let nav = viewController as? UINavigationController {
            return topViewController(nav.visibleViewController)
        }
        if let tab = viewController as? UITabBarController {
            if let selected = tab.selectedViewController {
                return topViewController(selected)
            }
        }
        if let presented = viewController?.presentedViewController {
            return topViewController(presented)
        }

        return viewController
    }
}
