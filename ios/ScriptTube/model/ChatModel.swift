//
//  ChatModel.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 29/12/22.
//
//MARK: - Data Model for Single Chat Message
import Foundation
class ChatModel{
    var message = ""
    var id = ""
    var otherUser = ChatUserModel()
    var messageType:MessageType!
    var chatHistoryTime: Int64 = 0
    var videoImage = ""
    var videoSlug = ""
    
    init(){
        
    }
    init(data:JSON){
        if AuthManager.currentUser.id  == data["receiverId"]["_id"].stringValue{
            self.otherUser = ChatUserModel(data: data["senderId"])
        }else{
            self.otherUser = ChatUserModel(data: data["receiverId"])
            self.otherUser.sender = true
        }
        self.message = data[ChatKeys.message.rawValue].stringValue
        self.id = data[ChatKeys.id.rawValue].stringValue
        self.chatHistoryTime = data[ChatKeys.chatHistoryTime.rawValue].int64Value
        let type = data[ChatKeys.messageType.rawValue].stringValue
        setMessageType(text: type)
        self.videoImage = data[ChatKeys.videoId.rawValue][ChatKeys.videoImage.rawValue].stringValue
        self.videoSlug = data[ChatKeys.videoId.rawValue][ChatKeys.videoSlug.rawValue].stringValue
    }
    func setMessageType(text:String){
        if text == "Text"{
            self.messageType = .text
        }else{
            self.messageType = .video
        }
    }
}
enum ChatKeys:String{
    case message = "message"
    case chatHistoryTime = "chatHistoryTime"
    case messageType = "messageType"
    case id = "_id"
    case videoId = "videoId"
    case videoImage = "videoImage"
    case videoSlug = "slug"
}
enum MessageType{
    case text
    case video
}


class ChatUserModel{
    var id = ""
    var userName = ""
    var profileImage = ""
    var sender = false
    init(){
        
    }
    init(data:JSON){
        self.id = data["_id"].stringValue
        self.userName = data["userName"].stringValue
        self.profileImage = data["profileImage"].stringValue
    }
}
