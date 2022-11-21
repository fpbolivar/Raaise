//
//  Extension+UIImageView.swift
//  FraudsterFresh
//
//  Created by Dilpreet Singh on 8/3/22.
//

import Foundation
import UIKit

extension UIImage{
    static  func isChecked(value:Bool)->UIImage{
        if value{
            return UIImage(named: "checked")!
        }else{
            return UIImage(named: "uncheck")!
        }
    }

}
extension UIImageView{
    func circleView(){
        self.layer.cornerRadius = self.frame.size.height / 2
        self.layer.masksToBounds = true
    }

}
extension UIImage{
   static func eyeImage(value:Bool) -> UIImage {
        if value{
            return UIImage(named: "eyeOn")!

        }else{
            return UIImage(named: "eyeOff")!
        }
    }
}
