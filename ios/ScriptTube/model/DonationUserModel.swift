//
//  DonationUserModel.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 01/02/23.
//

import Foundation
class DonationUserModel{
    var id = ""
    var name = ""
    var profileImage = ""
    var credit = ""
    init(){
        
    }
    init(data:JSON){
        self.id = data["id"].stringValue
        self.name = data["name"].stringValue
        self.profileImage = data["profileImage"].stringValue
        self.credit = data["credit"].stringValue
    }
}
