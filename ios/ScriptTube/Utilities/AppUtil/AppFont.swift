//
//  AppFont.swift
//  FraudsterFresh
//
//  Created by Dilpreet Singh on 7/8/22.
//

import Foundation
import UIKit
class AppFont{
    static var fontName = "NotoSans-"
    enum FontName:String{
        case regular = "Regular"
        case medium =  "Medium"
        case semiBold = "SemiBold"
        case bold =  "Bold"
        case italic = "Italic"
        case light =  "Light"

        var style: String {
            switch self {
                case .regular:
                    return self.rawValue
                case .medium:
                    return self.rawValue
                default:
                    return self.rawValue
            }
        }
        func getFont(size: CGFloat = 17.0) -> UIFont {
            let style = self.style
            let font = UIFont(name: AppFont.fontName + style, size: size) ?? UIFont(name: AppFont.fontName, size: size) ?? UIFont.systemFont(ofSize: size)
            return font
        }

    }
    static var pX13:CGFloat = 13
    static var pX12:CGFloat = 12
    static var pX11:CGFloat = 11
    static var pX10:CGFloat = 10
    static var pX14:CGFloat = 14
    static var pX15:CGFloat = 15
    static var pX16:CGFloat = 16
    static var pX17:CGFloat = 17
    static var pX18:CGFloat = 18
    static var pX19:CGFloat = 19
    static var pX20:CGFloat = 20
    static var pX22:CGFloat = 22
    static var pX25:CGFloat = 25
    static var pX27:CGFloat = 27
    static var pX30:CGFloat = 20
    static var pXNavBar:CGFloat = 22
}


extension UILabel {
    @objc var substituteFontName : String {
        get {
            return self.font.fontName;
        }
        set {
            let fontNameToTest = self.font.fontName.lowercased();
            var fontName = newValue;
            if fontNameToTest.range(of: "bold") != nil {
                fontName += "-Bold";
            } else if fontNameToTest.range(of: "medium") != nil {
                fontName += "-Medium";
            } else if fontNameToTest.range(of: "light") != nil {
                fontName += "-Light";
            } else if fontNameToTest.range(of: "ultralight") != nil {
                fontName += "-UltraLight";
            }
            self.font = UIFont(name: fontName, size: self.font.pointSize)
        }
    }
    @objc var medium : String {
        get {
            return self.font.fontName;
        }
        set {
            let fontNameToTest = self.font.fontName.lowercased();
            var fontName = newValue;
            if fontNameToTest.range(of: "bold") != nil {
                fontName += "-Bold";
            } else if fontNameToTest.range(of: "medium") != nil {
                fontName += "-Medium";
            } else if fontNameToTest.range(of: "light") != nil {
                fontName += "-Light";
            } else if fontNameToTest.range(of: "ultralight") != nil {
                fontName += "-UltraLight";
            }
            self.font = UIFont(name: fontName, size: self.font.pointSize)
        }
    }
}
