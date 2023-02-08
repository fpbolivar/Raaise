//
//  CommentDataModel.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 09/12/22.
//
//MARK: - Data Model for a Single Comment

import Foundation
class CommentDataModel{
    var comment = ""
    var id = ""
    var username = ""
    var commentprofileImage = ""
    var replies = [ReplyDataModel]()
    var userId = ""
    var commentTime = ""
    var viewReply = false
    
    init(){
        
    }
    init(data:JSON){
        self.comment = data[CommentKeys.comment.rawValue].stringValue
        self.id = data[CommentKeys.id.rawValue].stringValue
        self.username = data["commentBy"]["userName"].stringValue
        self.commentprofileImage = data["commentBy"]["profileImage"].stringValue
        data["replyId"].forEach { (message,json) in
            self.replies.append(ReplyDataModel(data: json))
        }
        self.userId = data["commentBy"]["_id"].stringValue
        self.commentTime = data["createdAt"].stringValue
    }
    init(data2:JSON){
        self.comment = data2["data"]["comment"].stringValue
        self.id = data2["data"]["_id"].stringValue
        self.username = data2["commentUserDetails"]["userName"].stringValue
        self.commentprofileImage = data2["commentUserDetails"]["profileImage"].stringValue
        self.commentTime = data2["data"]["createdAt"].stringValue
        print("commnetTime",self.commentTime)
        self.userId = AuthManager.currentUser.id
    }
}
class ReplyDataModel{
    var reply = ""
    var id = ""
    var username = ""
    var profileImage = ""
    var userId = ""
    var replyTime = ""
    
    init(){
        
    }
    init(data:JSON){
        self.reply = data["reply"].stringValue
        self.id = data["_id"].stringValue
        self.profileImage = data["replyBy"]["profileImage"].stringValue
        self.username = data["replyBy"]["userName"].stringValue
        self.userId = data["replyBy"]["_id"].stringValue
        //self.replyTime = data["replyBy"]["createdAt"].stringValue
        self.replyTime = data["createdAt"].stringValue
    }
    init(data2:JSON){
        self.reply = data2["reply"].stringValue
        self.userId = data2["replyBy"].stringValue
        self.id = data2["_id"].stringValue
        self.profileImage = AuthManager.currentUser.profileImage
        self.username = AuthManager.currentUser.userName
        self.replyTime = data2["createdAt"].stringValue
        print("REPLYTOCOMMENT",AuthManager.currentUser.profileImage,AuthManager.currentUser.userName)
    }
}
enum CommentKeys:String{
    case comment = "comment"
    case id = "_id"
}
