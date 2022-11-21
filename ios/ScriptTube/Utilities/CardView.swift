//
//  CardView.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 09/11/22.
//

import Foundation
import UIKit
class CardView:UIView{

    @IBInspectable var cornerRadius:CGFloat = 0
    @IBInspectable var shadowColor:UIColor = UIColor.lightGray
    @IBInspectable var shadowOffSetWidth:CGFloat = 0
    @IBInspectable var shadowOffSetHeight:CGFloat = 0
    @IBInspectable var shadowOpacity:Float = 0.2
    @IBInspectable var borderColor:UIColor? = UIColor.lightGray
    @IBInspectable var borderWidth:CGFloat = 0
    @IBInspectable var shadowRadius:CGFloat = 0

    override func layoutSubviews() {
        super.layoutSubviews()
      
        self.layer.cornerRadius = cornerRadius
        self.layer.borderColor = borderColor?.cgColor
        self.layer.borderWidth = borderWidth
        self.layer.shadowColor = shadowColor.cgColor
        self.layer.shadowRadius = shadowRadius
        self.layer.shadowOffset = CGSize(width: shadowOffSetWidth, height: shadowOffSetHeight)
        let  shadow_path = UIBezierPath(roundedRect: bounds, cornerRadius: cornerRadius)
       self.layer.shadowPath = shadow_path.cgPath
        self.layer.shadowOpacity = shadowOpacity

    }
    
    
}
