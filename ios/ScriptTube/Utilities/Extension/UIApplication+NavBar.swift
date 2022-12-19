//
//  UIApplication+NavBar.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/17/22.
//

import Foundation
import UIKit
extension UIViewController{

    func hideNavbar(){
        self.navigationController?.setNavigationBarHidden(true, animated: true)
    }
    func showNavbar(){
        self.navigationController?.setNavigationBarHidden(false, animated: true)
    }
}
