//
//  LiveRoomChatModel.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 28/04/23.
//

import Foundation
class LiveRoomChatModel{
    var userName = ""
    var image = ""
    var message = ""
    
    init(){
        
    }
    init(data:JSON){
        self.image = data["userProfileImage"].stringValue
        self.userName = data["userName"].stringValue
        self.message = data["message"].stringValue
    }
    init(data2:JSON){
        self.image = data2["senderId"]["profileImage"].stringValue
        self.userName = data2["senderId"]["userName"].stringValue
        self.message = data2["message"].stringValue
    }
}
