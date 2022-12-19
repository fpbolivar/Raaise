//
//  Post.swift
//  KD Tiktok-Clone
//
//  Created by Sam Ding on 9/8/20.
//  Copyright © 2020 Kaishan. All rights reserved.
//

import Foundation
import UIKit
import AVFoundation

//struct Post: Codable{
//    var id: String
//    var video: String
//    var videoURL: URL?
//    var videoFileExtension: String?
//    var videoHeight: Int
//    var videoWidth: Int
//    var autherID: String
//    var autherName: String
//    var caption: String
//    var music: String
//    var likeCount: Int
//    var shareCount: Int
//    var commentID: String
//
//
//    enum CodingKeys: String, CodingKey {
//        case id
//        case video
//        case videoURL
//        case videoFileExtension
//        case videoHeight
//        case videoWidth
//        case autherID = "author"
//        case autherName
//        case caption
//        case music
//        case likeCount
//        case shareCount
//        case commentID
//    }
//
//    init(id: String, video: String, videoURL: URL? = nil, videoFileExtension: String? = nil, videoHeight: Int, videoWidth: Int, autherID: String, autherName: String, caption: String, music: String, likeCount: Int, shareCount: Int, commentID: String) {
//        self.id = id
//        self.video = video
//        self.videoURL = videoURL ?? URL(fileURLWithPath: "")
//        self.videoFileExtension = videoFileExtension ?? "mp4"
//        self.videoHeight = videoHeight
//        self.videoWidth = videoWidth
//        self.autherID = autherID
//        self.autherName = autherName
//        self.caption = caption
//        self.music = music
//        self.likeCount = likeCount
//        self.shareCount = shareCount
//        self.commentID = commentID
//    }
//
//    init(dictionary: [String: Any]) {
//        id = dictionary["id"] as? String ?? ""
//        video = dictionary["video"] as? String ?? ""
//        let urlString = dictionary["videoURL"] as? String ?? ""
//        videoURL = URL(string: urlString)
//        videoFileExtension = dictionary["videoFileExtension"] as? String ?? ""
//        videoHeight = dictionary["videoHeight"] as? Int ?? 0
//        videoWidth = dictionary["videoWidth"] as? Int ?? 0
//        autherID = dictionary["author"] as? String ?? ""
//        autherName = dictionary["autherName"] as? String ?? ""
//        caption = dictionary["caption"] as? String ?? ""
//        music = dictionary["music"] as? String ?? ""
//        likeCount = dictionary["likeCount"] as? Int ?? 0
//        shareCount = dictionary["shareCount"] as? Int ?? 0
//        commentID = dictionary["commentID"] as? String ?? ""
//    }
//
//
//    var dictionary: [String: Any] {
//        let data = (try? JSONEncoder().encode(self)) ?? Data()
//        return (try? JSONSerialization.jsonObject(with: data, options: [.mutableContainers, .allowFragments]) as? [String: Any]) ?? [:]
//    }
//
//}


class Post{
    var id = ""
    var userDetails: UserProfileData?
    var audioDetails:AudioDataModel?
    var videoCaption = ""
    var videoLink = ""
    var donationAmount = ""
    var videoImage = ""
    var slug = ""
    var videoLikeCount = ""
    var videoCommentCount = ""
    var videoShareCount = ""
    var isFollow = false
    var isLiked = false
    
    init(){
        
    }
    init(data:JSON){
        self.id = data[PostKeys.id.rawValue].stringValue
        self.userDetails = UserProfileData(data: data[PostKeys.userDetails.rawValue])
        self.videoCaption = data[PostKeys.videoCaption.rawValue].stringValue
        self.videoLink = data[PostKeys.videoLink.rawValue].stringValue
        self.donationAmount = data[PostKeys.donationAmount.rawValue].stringValue
        self.videoImage = data[PostKeys.videoImage.rawValue].stringValue
        self.slug = data[PostKeys.slug.rawValue].stringValue
        self.videoLikeCount = data[PostKeys.videoLikeCount.rawValue].stringValue
        self.videoCommentCount = data[PostKeys.videoCommentCount.rawValue].stringValue
        self.videoShareCount = data[PostKeys.videoShareCount.rawValue].stringValue
        self.isFollow = data[PostKeys.isFollow.rawValue].boolValue
        self.isLiked = data[PostKeys.isLiked.rawValue].boolValue
        self.audioDetails = AudioDataModel(data: data[PostKeys.audioDetails.rawValue])
    }
}
enum PostKeys:String{
    case id = "_id"
    case userDetails = "userId"
    case videoCaption = "videoCaption"
    case videoLink = "videoLink"
    case donationAmount = "donationAmount"
    case videoImage = "videoImage"
    case slug = "slug"
    case videoLikeCount = "videolikeCount"
    case videoCommentCount = "videoCommentCount"
    case videoShareCount = "videoShareCount"
    case isFollow = "isFollow"
    case isLiked = "isLiked"
    case audioDetails = "audioId"
}
