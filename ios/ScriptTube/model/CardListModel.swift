//
//  CardListModel.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 19/12/22.
//

import Foundation

class CardListModel{
    var id = ""
    var object = ""
    var brand = ""
    var country = ""
    var funding = ""
    var last4 = ""
    
    init(){
        
    }
    init(data:JSON){
        self.id = data["id"].stringValue
        self.object = data["object"].stringValue
        self.brand = data["brand"].stringValue
        self.funding = data["funding"].stringValue
        self.last4 = data["last4"].stringValue
        self.country = data["country"].stringValue
    }
}
