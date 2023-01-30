//
//  VideoCategoryModel.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 19/12/22.
//

import Foundation

class VideoCategoryModel{
    var id = ""
    var name = ""
    var image = ""
    
    init(){
        
    }
    init(data:JSON){
        self.id = data["_id"].stringValue
        self.name = data["name"].stringValue
        self.image = data["image"].stringValue
    }
}
