//
//  UIButtonExtension.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 26/04/23.
//

import Foundation
import UIKit
extension UIButton {
    //MARK:- Animate check mark
    func checkboxAnimation(closure: @escaping () -> Void){
        guard let image = self.imageView else {return}
        self.adjustsImageWhenHighlighted = false
        self.isHighlighted = false
        self.isSelected = !self.isSelected
        self.backgroundColor = self.isSelected ? UIColor.darkGray:UIColor.white
        closure()

    }
}
