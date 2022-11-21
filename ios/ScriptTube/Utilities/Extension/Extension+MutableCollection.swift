//
//  Extension+MutableCollection.swift
//  FraudsterFresh
//
//  Created by Code Optimal Solutions Ios on 20/08/22.
//

import Foundation
extension MutableCollection {
    subscript(safe index: Index) -> Element? {
        get {
            return indices.contains(index) ? self[index] : nil
        }
        set(newValue) {
            if let newValue = newValue, indices.contains(index) {
                self[index] = newValue
            }
        }
    }
}
