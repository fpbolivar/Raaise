//
//  CircularImageView.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 25/04/23.
//

import Foundation
import UIKit
@IBDesignable
class DesignableImageView: UIImageView {

    // Define the corner radius value for the image view
    @IBInspectable var cornerRadius: CGFloat = 0 {
        didSet {
            layer.cornerRadius = cornerRadius
            layer.masksToBounds = true
        }
    }

    override func prepareForInterfaceBuilder() {
        super.prepareForInterfaceBuilder()
        setupView()
    }

    override func awakeFromNib() {
        super.awakeFromNib()
        setupView()
    }

    private func setupView() {
        layer.cornerRadius = cornerRadius
        layer.masksToBounds = true
    }

}
