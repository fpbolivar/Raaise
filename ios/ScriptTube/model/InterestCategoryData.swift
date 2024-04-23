//
//  InterestCategoryData.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 05/03/24.
//

import Foundation


class InterestCategoryData {
    
    var name = ""
    var image = ""
    var id = ""
    
    init(){
        
    }
    init(data:JSON){
        self.name = data[InterestCategoryKeys.name.rawValue].stringValue
        self.id = data[InterestCategoryKeys.id.rawValue].stringValue
        self.image = data[InterestCategoryKeys.image.rawValue].stringValue
    }
}

enum InterestCategoryKeys:String{
    case name = "name"
    case image = "image"
    case id = "_id"
}

