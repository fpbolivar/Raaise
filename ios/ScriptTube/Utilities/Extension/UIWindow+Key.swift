//
//  UIWindow+Key.swift
//  FraudsterFresh
//
//  Created by Dilpreet Singh on 7/9/22.
//

import Foundation
import UIKit

extension UIApplication {
    static var keyWin: UIWindow? {
        if #available(iOS 13, *) {
            return UIApplication.shared.windows.first { $0.isKeyWindow }
        } else {
            return UIApplication.shared.keyWindow
        }
    }
}
