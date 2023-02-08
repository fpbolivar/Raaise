//
//  HTMLParse.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 30/11/22.
//

import Foundation
import UIKit

extension UIViewController{
    func convertHtml(htmlString html: String, font: UIFont? = nil, useDocumentFontSize: Bool = true,completion:@escaping(NSAttributedString?)->Void) {
            let options: [NSAttributedString.DocumentReadingOptionKey : Any] = [
                .documentType: NSAttributedString.DocumentType.html,
                .characterEncoding: String.Encoding.utf8.rawValue
            ]

            let data = html.data(using: .utf8, allowLossyConversion: true)

            if let data = data, let attr = try? NSMutableAttributedString(data: data, options: options, documentAttributes: nil){
                let fontSize: CGFloat? = useDocumentFontSize ? nil : font!.pointSize
                let range = NSRange(location: 0, length: attr.length)
                let style = NSMutableParagraphStyle()
                style.lineSpacing = 5
                style.alignment = NSTextAlignment.justified
                attr.addAttribute(.paragraphStyle, value: style, range: NSMakeRange(0, attr.length))
                attr.enumerateAttribute(.foregroundColor, in: range, options: .longestEffectiveRangeNotRequired) {attrib,range,_ in
                
                    
                    if let color = attrib as? UIColor {


                        let sRGB = CGColorSpace(name: CGColorSpace.sRGB)!
                        let myColor  = CGColor(colorSpace: sRGB, components: [0, 0, 0, 1])!

                        if color.cgColor == myColor{
                            attr.addAttribute(.foregroundColor, value: UIColor.white, range: range )
                        }else{
                            attr.addAttribute(.foregroundColor, value:color, range: range )
                        }
                        
                    }
                }
                attr.enumerateAttribute(.font, in: range, options: .longestEffectiveRangeNotRequired) { attrib, range, _ in

                    if let htmlFont = attrib as? UIFont {
                        
                        let traits = htmlFont.fontDescriptor.symbolicTraits
                        var descrip = htmlFont.fontDescriptor.withFamily(htmlFont.familyName)
                        
                        if (traits.rawValue & UIFontDescriptor.SymbolicTraits.traitBold.rawValue) != 0 {
                            descrip = descrip.withSymbolicTraits(.traitBold)!
                        }

                        if (traits.rawValue & UIFontDescriptor.SymbolicTraits.traitItalic.rawValue) != 0 {
                            descrip = descrip.withSymbolicTraits(.traitItalic)!
                        }
                        attr.addAttribute(.foregroundColor, value: UIColor.white, range: range)

                        attr.addAttribute(.font, value: UIFont(descriptor: descrip, size: fontSize ?? htmlFont.pointSize), range: range)
                    }
                }
                return completion(attr)
            }

            let attr1 =    try?   NSMutableAttributedString(data: data ?? Data(html.utf8), options: options, documentAttributes: nil)
        
            return completion(attr1 ?? nil)
            

        }
}
