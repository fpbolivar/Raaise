//
//  Post.swift 
//MARK: - Data Model for Single Post

import Foundation
import UIKit
import AVFoundation

class Post{
    var id = ""
    var userDetails: UserProfileData?
    var audioDetails:AudioDataModel?
    var topSupportersList = [DonationUserModel]()
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
    var isViewed :Bool? = true
    var isDonation = false
    var videoViewCount = ""
    var isReported = false
    var createdAt = ""
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
        if audioDetails == nil{
            print("sahcbsjhcbsjhcbh")
        }else{
            print("sahcbsjhcbsjhcbh22")
        }
        self.createdAt = data[PostKeys.createdAt.rawValue].stringValue
        self.isViewed = data[PostKeys.isViewed.rawValue].boolValue
        self.isDonation = data[PostKeys.isDonation.rawValue].boolValue
        self.isReported = data[PostKeys.isReported.rawValue].boolValue
        self.videoViewCount = data[PostKeys.videoViewCount.rawValue].stringValue
        data[PostKeys.donationUsers.rawValue].forEach { (_,json) in
            self.topSupportersList.append(DonationUserModel(data: json))
        }
        print("LISTTTTSb", self.topSupportersList.count)
    }
    init(data2:JSON){
        self.id = data2["getVideo"][PostKeys.id.rawValue].stringValue
        self.userDetails = UserProfileData(data: data2["getVideo"][PostKeys.userDetails.rawValue])
        self.videoCaption = data2["getVideo"][PostKeys.videoCaption.rawValue].stringValue
        self.videoLink = data2["getVideo"][PostKeys.videoLink.rawValue].stringValue
        self.donationAmount = data2["getVideo"][PostKeys.donationAmount.rawValue].stringValue
        self.videoImage = data2["getVideo"][PostKeys.videoImage.rawValue].stringValue
        self.slug = data2["getVideo"][PostKeys.slug.rawValue].stringValue
        self.videoLikeCount = data2["getVideo"][PostKeys.videoLikeCount.rawValue].stringValue
        self.videoCommentCount = data2["getVideo"][PostKeys.videoCommentCount.rawValue].stringValue
        self.videoShareCount = data2["getVideo"][PostKeys.videoShareCount.rawValue].stringValue
        self.isFollow = data2[PostKeys.isFollow.rawValue].boolValue
        self.isLiked = data2[PostKeys.isLiked.rawValue].boolValue
        self.audioDetails = AudioDataModel(data: data2["getVideo"][PostKeys.audioDetails.rawValue])
        if audioDetails == nil{
            print("sahcbsjhcbsjhcbh")
        }else{
            print("sahcbsjhcbsjhcbh22")
        }
        self.isViewed = data2[PostKeys.getVideo.rawValue][PostKeys.isViewed.rawValue].boolValue
        self.isDonation = data2[PostKeys.getVideo.rawValue][PostKeys.isDonation.rawValue].boolValue
        self.isReported = data2[PostKeys.getVideo.rawValue][PostKeys.isReported.rawValue].boolValue
        self.videoViewCount = data2[PostKeys.getVideo.rawValue][PostKeys.videoViewCount.rawValue].stringValue
        data2[PostKeys.getVideo.rawValue][PostKeys.donationUsers.rawValue].forEach { (_,json) in
            self.topSupportersList.append(DonationUserModel(data: json))
        }
        print("LISTTTTSb", self.topSupportersList.count)
    }
    func getDictOfObject(){
        
    }
}
enum PostKeys:String{
    case id = "_id"
    case userDetails = "userId"
    case videoCaption = "videoCaption"
    case videoLink = "videoLink"
    case donationAmount = "totalDanotionAmount"
    case videoImage = "videoImage"
    case slug = "slug"
    case videoLikeCount = "videolikeCount"
    case videoCommentCount = "videoCommentCount"
    case videoShareCount = "videoShareCount"
    case isFollow = "isFollow"
    case isLiked = "isLiked"
    case audioDetails = "audioId"
    case isViewed = "isViewed"
    case videoViewCount = "videoViewCount"
    case isDonation = "isDonation"
    case isReported = "isReported"
    case donationUsers = "donationUsers"
    case getVideo = "getVideo"
    case createdAt = "createdAt"
}
