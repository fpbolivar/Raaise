//
//  NotificationDataModel.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 26/12/22.
//

import Foundation

class NotificationDataModel{
    var id = ""
    var type : NotificationType!
    var name = ""
    var profileImage = ""
    var message = ""
    var slug = ""
    var isRead = false
    var time = ""
    init(){
        
    }
    init(data:JSON){
        self.id = data[NotificationKeys.id.rawValue].stringValue
        setNotificationType(text: data[NotificationKeys.type.rawValue].stringValue)
       // self.type =
        self.name = data[NotificationKeys.name.rawValue].stringValue
        self.profileImage = data[NotificationKeys.profileImage.rawValue].stringValue
        self.message = data[NotificationKeys.message.rawValue].stringValue
        self.slug = data[NotificationKeys.slug.rawValue].stringValue
        self.isRead = data[NotificationKeys.isRead.rawValue].boolValue
        self.time = data[NotificationKeys.time.rawValue].stringValue
    }
    func setNotificationType(text:String){
        if text == "like-video" || text == "comment-video" || text == "comment-reply" || text == "payment-donate"{
            self.type = .userVideo
        }else if (text == "payment-withdraw"){
            self.type = .paymentWithdraw
        }else if ( text == "get-amount"){
            self.type = .getAmount
        }else if(text == "add-video"){
            self.type = .videoAdded
        }else if (text == "send-now" || text == "schedule"){
            self.type = .admin
        }else if text == "user-follow"{
            self.type = .userFollow
        }
    }
}
enum NotificationKeys:String{
    case id = "id"
    case type = "type"
    case name = "fromUserName"
    case profileImage = "fromProfileImage"
    case message = "message"
    case slug = "slug"
    case isRead = "isRead"
    case time = "createdAt"
}

enum NotificationType{
    case videoAdded
    case userVideo
    case admin
    case paymentWithdraw
    case getAmount
    case userFollow
}
