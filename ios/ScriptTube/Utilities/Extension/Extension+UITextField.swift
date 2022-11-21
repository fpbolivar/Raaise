//
//  Extension+UITextField.swift
//  FraudsterFresh
//
//  Created by Dilpreet Singh on 7/28/22.
//

import Foundation
import UIKit
extension UITextField{
    func placeholderColor(color:UIColor=UIColor.lightGray,placeholderText:String){
        self.attributedPlaceholder = NSAttributedString(
            string: placeholderText,
            attributes: [NSAttributedString.Key.foregroundColor:color]
        )
    }
    
    func paddingLeftRightTextField(left:CGFloat=5,right:CGFloat=5){
        let paddingViewLeft: UIView = UIView(frame: CGRect(x: 0, y: 0, width: left, height: self.frame.height))
        let paddingViewRight: UIView = UIView(frame: CGRect(x: 0, y: 0, width: right, height: self.frame.height))
        self.leftView = paddingViewLeft
        self.rightView = paddingViewRight
        self.leftViewMode = .always
        self.rightViewMode = .always
    }
}
