//
//  UIView+Child.swift
//  FraudsterFresh
//
//  Created by Dilpreet Singh on 7/15/22.
//

import UIKit

public extension UIView {

    func configureChildView(childView: UIView) {
        self.addSubview(childView)
        self.constrainViewEqual(holderView: self, view: childView)
    }

    func constrainViewEqual(holderView: UIView, view: UIView) {
        view.translatesAutoresizingMaskIntoConstraints = false
        let pinTop = NSLayoutConstraint(item: view,
                                        attribute: .top,
                                        relatedBy: .equal,
                                        toItem: holderView,
                                        attribute: .top,
                                        multiplier: 1.0,
                                        constant: 0.0)
        let pinBottom = NSLayoutConstraint(item: view,
                                           attribute: .bottom,
                                           relatedBy: .equal,
                                           toItem: holderView,
                                           attribute: .bottom,
                                           multiplier: 1.0,
                                           constant: 0.0)
        let pinLeft = NSLayoutConstraint(item: view,
                                         attribute: .left,
                                         relatedBy: .equal,
                                         toItem: holderView,
                                         attribute: .left,
                                         multiplier: 1.0,
                                         constant: 0.0)
        let pinRight = NSLayoutConstraint(item: view,
                                          attribute: .right,
                                          relatedBy: .equal,
                                          toItem: holderView,
                                          attribute: .right,
                                          multiplier: 1.0,
                                          constant: 0.0)

        holderView.addConstraints([pinTop, pinBottom, pinLeft, pinRight])
    }
    func anchor(
        to view: UIView? = nil,
        top: NSLayoutYAxisAnchor? = nil,
        leading: NSLayoutXAxisAnchor? = nil,
        trailing: NSLayoutXAxisAnchor? = nil,
        bottom: NSLayoutYAxisAnchor? = nil,
        centerX: NSLayoutXAxisAnchor? = nil,
        centerY: NSLayoutYAxisAnchor? = nil,
        padding: UIEdgeInsets = .zero,
        size: CGSize = .zero) {
            translatesAutoresizingMaskIntoConstraints = false

            if let view = view { view.addSubview(self) }

            if let top = top { topAnchor.constraint(equalTo: top, constant: padding.top).isActive = true }
            if let leading = leading { leadingAnchor.constraint(equalTo: leading, constant: padding.left).isActive = true }
            if let trailing = trailing { trailingAnchor.constraint(equalTo: trailing, constant: -padding.right).isActive = true }
            if let bottom = bottom { bottomAnchor.constraint(equalTo: bottom, constant: -padding.bottom).isActive = true }

            if let centerX = centerX { centerXAnchor.constraint(equalTo: centerX).isActive = true }
            if let centerY = centerY { centerYAnchor.constraint(equalTo: centerY).isActive = true }

            if size.width != 0 { widthAnchor.constraint(equalToConstant: size.width).isActive = true }
            if size.height != 0 { heightAnchor.constraint(equalToConstant: size.height).isActive = true }
        }
}
