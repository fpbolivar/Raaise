//
//  UserProfileData.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 25/11/22.
//

import Foundation
class UserProfileData{
    var id = ""
    var userName = ""
    var name = ""
    var email = ""
    var phoneNumber = ""
    var shortBio = ""
    var profileImage = ""
    var isVerified = false
    var followersCount = ""
    var followingCount = ""
    var videoCount = ""
    var emailNotification = false
    var pushNotification = false
    var accountId = ""
    var routingNumber = ""
    var bankPhone = ""
    var city = ""
    var state = ""
    var postalCode = ""
    var address = ""
    var userPosts = [Post]()
    var userPostsImages = [String]()
    
    init(){
        
    }
    init(data:JSON){
        self.id = data[ApiKeys.id.rawValue].stringValue
        self.userName = data[ApiKeys.userName.rawValue].stringValue
        self.name = data[ApiKeys.name.rawValue].stringValue
        self.email = data[ApiKeys.email.rawValue].stringValue
        self.phoneNumber = data[ApiKeys.phoneNumber.rawValue].stringValue
        self.shortBio = data[ApiKeys.shortBio.rawValue].stringValue
        self.profileImage = data[ApiKeys.profileImage.rawValue].stringValue
        self.isVerified = data[ApiKeys.isVerified.rawValue].boolValue
        self.followingCount = data[ApiKeys.followingCount.rawValue].stringValue
        self.followersCount = data[ApiKeys.followersCount.rawValue].stringValue
        self.videoCount = data[ApiKeys.videoCount.rawValue].stringValue
        self.emailNotification = data[ApiKeys.emailNotification.rawValue].boolValue
        self.pushNotification = data[ApiKeys.pushNotification.rawValue].boolValue
        self.accountId = data[ApiKeys.accountId.rawValue].stringValue
        self.routingNumber = data[ApiKeys.routingNumber.rawValue].stringValue
        self.bankPhone = data[ApiKeys.bankPhone.rawValue].stringValue
        self.city = data[ApiKeys.city.rawValue].stringValue
        self.state = data[ApiKeys.state.rawValue].stringValue
        self.postalCode = data[ApiKeys.postalCode.rawValue].stringValue
        self.address = data[ApiKeys.address.rawValue].stringValue
    }
}
enum ApiKeys:String{
    case id = "_id"
    case name = "name"
    case userName = "userName"
    case email = "email"
    case phoneNumber = "phoneNumber"
    case shortBio = "shortBio"
    case profileImage = "profileImage"
    case isVerified = "isVerified"
    case followingCount = "followingCount"
    case followersCount = "followersCount"
    case videoCount = "videoCount"
    case emailNotification = "emailNotification"
    case pushNotification = "pushNotification"
    case accountId = "accountId"
    case routingNumber = "routingNumber"
    case bankPhone = "bankPhone"
    case city = "city"
    case state = "state"
    case postalCode = "postalCode"
    case address = "address"
}
