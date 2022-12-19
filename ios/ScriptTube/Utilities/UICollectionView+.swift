//
//  UICollectionView+.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 23/11/22.
//

import Foundation
import UIKit
class AutoSizedCollectionView: UICollectionView {

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

