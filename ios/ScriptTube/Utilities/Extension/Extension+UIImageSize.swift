//
//  Extension+UIImageSize.swift
//  turnip
//
//  Created by Dilpreet Singh on 9/23/22.
//

import Foundation
import UIKit

extension UIImageView{
}
extension UIImage {
    func addPadding(_ padding: CGFloat) -> UIImage {
        let alignmentInset = UIEdgeInsets(top: -padding, left: -padding,
                                          bottom: -padding, right: -padding)
        return withAlignmentRectInsets(alignmentInset)
    }
    func addPadding(_ top: CGFloat,_ bottom: CGFloat,_ left: CGFloat,_ right: CGFloat) -> UIImage {
        let alignmentInset = UIEdgeInsets(top: -top, left: -left,
                                          bottom: -bottom, right: -right)
        return withAlignmentRectInsets(alignmentInset)
    }
}
extension UIImage {

    func with(_ insets: UIEdgeInsets) -> UIImage {
        let targetWidth = size.width + insets.left + insets.right
        let targetHeight = size.height + insets.top + insets.bottom
        let targetSize = CGSize(width: targetWidth, height: targetHeight)
        let targetOrigin = CGPoint(x: insets.left, y: insets.top)
        let format = UIGraphicsImageRendererFormat()
        format.scale = scale
        let renderer = UIGraphicsImageRenderer(size: targetSize, format: format)
        return renderer.image { _ in
            draw(in: CGRect(origin: targetOrigin, size: size))
        }.withRenderingMode(renderingMode)
    }

}
