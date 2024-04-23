//
//  ShareVideoModel.swift
//  ScriptTube
//
//  Created by CODE OPTIMAL SOLUTIONS on 10/04/24.
//

import Foundation
enum ShareVideoOn{
    case whatsapp
    case instagram
    case facebook
    case other
}
class ShareVideoModel {
    var img : UIImage?
    var title = ""
    var shareOn  : ShareVideoOn = .other
    init(shareOn: ShareVideoOn){
        switch shareOn {
        case .whatsapp:
            title = "Whatsapp"
            self.img  = UIImage(named: "ic_whatsapp")
        case .instagram:
            title = "Instagram"
            self.img  = UIImage(named: "ic_instagram")
        case .facebook:
            title = "Facebook"
            self.img  = UIImage(named: "ic_facebook")
        case .other:
            title = "Other"
            self.img = UIImage(named: "ic_other")
        }
    }
}
