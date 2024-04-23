//
//  UserBannerModel.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 04/03/24.
//

import Foundation


class UserBannerModel {
    var updatedAt = ""
    var id = ""
    var createdAt = ""
    var link = ""
    var image = ""
    
    init(){
        
    }
    
    init(data:JSON) {
        self.updatedAt = data[BannerKeys.updatedAt.rawValue].stringValue
        self.id = data[BannerKeys.id.rawValue].stringValue
        self.createdAt = data[BannerKeys.createdAt.rawValue].stringValue
        self.link = data[BannerKeys.link.rawValue].stringValue
        self.image = data[BannerKeys.image.rawValue].stringValue
    }
 
  }
enum BannerKeys:String{
    case updatedAt = "updatedAt"
    case id = "_id"
    case createdAt = "createdAt"
    case link = "link"
    case image = "image"
}
