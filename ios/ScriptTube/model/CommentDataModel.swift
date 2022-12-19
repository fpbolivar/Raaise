//
//  CommentDataModel.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 09/12/22.
//

import Foundation
class CommentDataModel{
    var comment = ""
    var id = ""
    var username = ""
    var commentprofileImage = ""
    var replies = [ReplyDataModel]()
    var userId = ""
    var commentTime = ""
    
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
}
class ReplyDataModel{
    var reply = ""
    var id = ""
    var username = ""
    var profileImage = ""
    var userId = ""
    
    init(){
        
    }
    init(data:JSON){
        self.reply = data["reply"].stringValue
        self.id = data["_id"].stringValue
        self.profileImage = data["replyBy"]["profileImage"].stringValue
        self.username = data["replyBy"]["userName"].stringValue
        self.userId = data["replyBy"]["_id"].stringValue
    }
}
enum CommentKeys:String{
    case comment = "comment"
    case id = "_id"
}
