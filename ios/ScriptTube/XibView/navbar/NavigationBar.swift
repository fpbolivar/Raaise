//
//  NavigationBar.swift
//  turnip
//
//  Created by Dilpreet Singh on 9/27/22.
//

import Foundation
import UIKit

class NavigationBar: UIView {
    @IBOutlet weak var addNewCardBtn: UIButton!
    @IBOutlet weak var deleteIcon: UIImageView!
    @IBOutlet weak var leftIcon: UIImageView!
    @IBOutlet weak var msgIcon: UIImageView!
    @IBOutlet weak var title: UILabel!
    @IBOutlet weak var headingLbl: ActiveLabel!
    @IBOutlet weak var rigthBtn: UIButton!

    @IBOutlet weak var contactIcon: UIImageView!
    class func instanceFromNib() -> UIView {
        return UINib(nibName: "NavigationBar", bundle: nil).instantiate(withOwner: nil, options: nil)[0] as! UIView
    }
}
