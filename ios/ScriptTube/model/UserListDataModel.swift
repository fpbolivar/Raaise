//
//  UserListDataModel.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 14/12/22.
//

import Foundation
class UserListDataModel{
    var id = ""
    var userName = ""
    var name = ""
    var userId = ""
    var count = ""
    var image = ""
    
    init(){
        
    }
    init(data:JSON,isForFollowing:Bool){
        var selectType = ""
        self.id = data[UserListKeys.id.rawValue].stringValue
        if isForFollowing{
            selectType = UserListKeys.followTo.rawValue
        }else{
            selectType = UserListKeys.followedBy.rawValue
        }
        
        self.userName = data[selectType][UserListKeys.userName.rawValue].stringValue
        self.name = data[selectType][UserListKeys.name.rawValue].stringValue
        self.userId = data[selectType][UserListKeys.id.rawValue].stringValue
        self.count = data[selectType][UserListKeys.count.rawValue].stringValue
        self.image = data[selectType][UserListKeys.profileImage.rawValue].stringValue
        print("alosskd",selectType,self.userName,self.name,self.userId,self.count)
    }
}
enum UserListKeys:String{
    case id = "_id"
    case userName = "userName"
    case name = "name"
    case count = "followersCount"
    case followTo = "followTo"
    case followedBy = "followedBy"
    case profileImage = "profileImage"
}
