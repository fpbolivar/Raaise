//
//  Extension+UITableView.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/21/22.
//

import Foundation
import UIKit
final class ContentSizedTableView: UITableView {
    override var contentSize: CGSize {
        didSet {
            invalidateIntrinsicContentSize()
        }
    }
    override var intrinsicContentSize: CGSize {
        layoutIfNeeded()
        return CGSize(width: UIView.noIntrinsicMetric, height: contentSize.height)
    }
}
