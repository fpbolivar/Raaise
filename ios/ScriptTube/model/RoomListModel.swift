//
//  RoomListModel.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 25/04/23.
//

import Foundation
class LiveRoomDataModel{
    var id = ""
    var slug = ""
    var title = ""
    var description = ""
    var scheduleDateTime = ""
    var logo = ""
    var roomId = ""
    var token = ""
    var hostId = ""
    var isOnline = ""
    var members = [RoomMemberModel]()
    
    init(){
        
    }
    init(json:JSON){
        self.id = json["_id"].stringValue
        self.slug = json["slug"].stringValue
        self.title = json["title"].stringValue
        self.description = json["description"].stringValue
        self.scheduleDateTime = json["scheduleDateTime"].stringValue
        self.logo = json["logo"].stringValue
        self.roomId = json["roomId"].stringValue
        self.token = json["token"].stringValue
        self.isOnline = json["isOnline"].stringValue
        self.hostId = json["hostId"]["_id"].stringValue
        json["memberIds"].forEach { (_,data) in
            self.members.append(RoomMemberModel(json: data))
        }
        print("MMEBERCOUNT",members.count)
    }
}


class RoomMemberModel{
    var id = ""
    var username = ""
    var email = ""
    var phone = ""
    var profileImg = ""
    init(){
        
    }
    init(json:JSON){
        self.id = json["_id"].stringValue
        self.username = json["userName"].stringValue
        self.email = json["email"].stringValue
        self.phone = json["phoneNumber"].stringValue
        self.profileImg = json["profileImage"].stringValue
    }
}
