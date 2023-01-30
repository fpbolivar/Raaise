//
//  Extension+UIView.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/21/22.
//

import Foundation
import UIKit
// MARK: - UIViews
extension UIView {
    func makeRounded(color: UIColor, borderWidth: CGFloat) {
        self.layer.borderWidth = borderWidth
        self.layer.masksToBounds = false
        self.layer.borderColor = color.cgColor
        self.layer.cornerRadius = self.frame.height / 2
        self.clipsToBounds = true
    }
}
extension Int {
    /**
     * Shorten the number to *thousand* or *million*
     * - Returns: the shorten number and the suffix as *String*
     */
    func shorten() -> String{
        let number = Double(self)
        let thousand = number / 1000
        let million = number / 1000000
        if million >= 1.0 {
            return "\(round(million*10)/10)M"
        }
        else if thousand >= 1.0 {
            return "\(round(thousand*10)/10)K"
        }
        else {
            return "\(self)"
        }
    }
}
extension Double{
     func shortAmt() -> String{
        let number = self
        print("NUMBERRR",number)
        let thousand = number / 1000.00
        let million = number / 1000000.00
        if million >= 1.0 {
          print(String(format: "%.2f", million))
            return String(format: "%.2fM", million)
            //"\(million * 100 / 100.00)M"
        }
        else if thousand >= 1.0 {
            print(String(format: "%.2f", thousand))
            return String(format: "%.2fK", thousand)
            //"\(thousand * 100 / 100.00)K"
        }
        else {
            print(String(format: "%.2f", self))
            return String(format: "%.2f", self)
            //"\(self)"
        }
    }
}
