//
//  ChatChannelModel.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 28/12/22.
//
//MARK: - Data Model of Single Chat Channel 
import Foundation
class ChatChannelModel{
    var id = ""
    var slug = ""
    var senderId = ""
    var otherUserId = ""
    var otherUserName = ""
    var profileImage = ""
    var lastMessage = ""
    var chatTime: Int64 = 0
    var unreadMsgCount = ""
    var otherUser = UserProfileData()
    
    init(){
        
    }
    init(data:JSON){
        if AuthManager.currentUser.id  == data["receiverId"]["_id"].stringValue{
            self.otherUser = UserProfileData(data: data["senderId"])
        }else{
            self.otherUser = UserProfileData(data: data["receiverId"])
        }
        self.chatTime = data[ChatChannelKeys.chatTime.rawValue].int64Value
        self.id = data[ChatChannelKeys.id.rawValue].stringValue
        self.slug = data[ChatChannelKeys.slug.rawValue].stringValue
        self.senderId = data[ChatChannelKeys.senderId.rawValue][ChatChannelKeys.id.rawValue].stringValue
        self.otherUserId = data[ChatChannelKeys.senderId.rawValue][ChatChannelKeys.id.rawValue].stringValue
        self.otherUserName = data[ChatChannelKeys.senderId.rawValue][ChatChannelKeys.otherUserName.rawValue].stringValue
        self.profileImage = data[ChatChannelKeys.senderId.rawValue][ChatChannelKeys.profileImage.rawValue].stringValue
        self.lastMessage = data[ChatChannelKeys.lastMessage.rawValue].stringValue
        self.unreadMsgCount = data[ChatChannelKeys.messageUnReadCount.rawValue].stringValue
    }
}
enum ChatChannelKeys:String{
    case id = "_id"
    case slug = "slug"
    case senderId = "senderId"
    case receiverId = "receiverId"
    case otherUserName = "userName"
    case profileImage = "profileImage"
    case lastMessage = "lastMessage"
    case chatTime = "chatTime"
    case unreadMsgCount = "readCount"
    case messageUnReadCount = "messageUnReadCount"
}

extension Int64 {
    func getDateStringFromMillisecond2() -> String {
       let millisecond =  Double(self)
        let date = Date(timeIntervalSince1970: TimeInterval(millisecond))
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US_POSIX")
        dateFormatter.timeZone = TimeZone.current
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        return dateFormatter.string(from: date)
    }
    func getDateStringFromMillisecond() -> String {
       let millisecond =  Double(self)
        let date = Date(timeIntervalSince1970: TimeInterval(millisecond))
        let dateFormatter = DateFormatter()
        dateFormatter.timeZone = TimeZone.current
        dateFormatter.dateFormat = "MMM dd,yyyy h:mm a"
        return dateFormatter.string(from: date)
    }
    func getTime()->String{
        let millisecond =  Double(self)
         let date = Date(timeIntervalSince1970: TimeInterval(millisecond))
         let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US")
         dateFormatter.timeZone = TimeZone.current
        dateFormatter.dateFormat = "h:mm a"
         return dateFormatter.string(from: date)
    }
}
