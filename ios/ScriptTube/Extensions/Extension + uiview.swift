//
//  Extension + uiview.swift
//  ScriptTube
//
//  Created by CODE OPTIMAL SOLUTIONS on 10/04/24.
//

import Foundation
extension UIView  {
    //MARK: ADD TO THE CENTER
    ///  This method help to center child view with respect to parent
    /// - Parameters:
    /// - parent: the parent of the view
    /// - padding:  padding to add to the child view if any
    func addToCenter(parent: UIView,padding: UIEdgeInsets = .zero){
        self.anchor(to: parent, centerX: parent.centerXAnchor, centerY: parent.centerYAnchor, padding: padding)
    }
}
