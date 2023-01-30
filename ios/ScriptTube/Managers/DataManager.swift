//
//  DataManager.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 26/11/22.
//

import Foundation
class DataManager{
    class func getsettingsData(delegate:UIViewController,param:[String:String],completion:@escaping(JSON)->Void){
        delegate.pleaseWait()
        APIManager.postService(url: URLHelper.GET_SETTINGS_URL, parameters: param) { json, error, status in
            delegate.clearAllNotice()
            if let error = error{
                print("SETTINGSDATA",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("SETTINGSDATA",json,status)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        completion(jsonData["data"])
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["errors"]["message"].stringValue, delegate: delegate, pop: false)
                    }
                }
            }
        }
    }
    class func getAudioList(delegate:UIViewController,param:[String:String],completion:@escaping(JSON)->Void){
        delegate.pleaseWait()
        APIManager.postService(url: URLHelper.GET_AUDIO_LIST_URL, parameters: param) { json, error, status in
            delegate.clearAllNotice()
            if let error = error{
                print("AUDIOLIST",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
            }else{
                print("AUDIOLIST",json,status)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        completion(jsonData)
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate: delegate, pop: false)
                    }
                }
            }
        }
    }
    class func getUserVideos(delegate:UIViewController,completion:@escaping(JSON)->Void){
        APIManager.getService(url: URLHelper.GET_USER_PROFILE_VIDEOS_URL) { json, error, status in
            if let error = error{
                print("USERVIDEOS",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("USERVIDEOS",json,status)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200 || jsonData["status"].int == 404{
                        completion(jsonData)
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate: delegate, pop: false)
                    }
                }
            }
        }
    }
    class func getHomePageVideos(delegate:UIViewController,param:[String:String],completion:@escaping(JSON)->Void){
        APIManager.postService(url: URLHelper.GET_GLOBAL_VIDEOS_URL, parameters: param) { json, error, status in
            delegate.clearAllNotice()
            if let error = error{
                print("GLOBALVIDEO",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("GLOBALVIDEO",json,status)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200 || jsonData["status"].intValue == 404{
                        completion(jsonData)
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate: delegate, pop: false)
                    }
                }
            }
        }
    }
    class func likeUnlikeVideoApi(param:[String:String],onError:@escaping(String)->Void,completion:@escaping(Int)->Void){
        APIManager.postService(url: URLHelper.VIDEO_LIKE_UNLIKE_URL, parameters: param) { json, error, statusCode in
            if let error = error{
                print("VIDEOLIKEERROR",error)
                onError(error.localizedDescription)
            }else{
                print("VIDEOLIKEERROR",json,statusCode)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        completion(jsonData["videoCount"].intValue)
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        onError(jsonData["message"].stringValue)
                    }
                }
            }
        }
    }
    class func reportVideo(delegate:UIViewController, param:[String:String],completion:@escaping()->Void){
        APIManager.postService(url: URLHelper.VIDEO_REPORT_URL, parameters: param) { json, error, statusCode in
            if let error = error{
                print("REPORTVIDEO",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
            }else{
                print("REPORTVIDEO",json,statusCode)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        completion()
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["message"].stringValue,delegate: delegate, pop: false)
                    }
                }
            }
        }
    }
    class func getVideoComments(param:[String:String],onError:@escaping(String)->Void,completion:@escaping(JSON)->Void){
        APIManager.postService(url: URLHelper.GET_VIDEO_COMMENTS_URL, parameters: param) { json, error, statusCode in
            if let error = error{
                print("VIDEOCOMMENTS",error)
                onError(error.localizedDescription)
            }else{
                print("VIDEOCOMMENTS",json,statusCode)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        completion(jsonData)
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        onError(jsonData["message"].stringValue)
                    }
                }
            }
        }
    }
    class func followUnfollowUser(param:[String:String],onError:@escaping(String)->Void,completion:@escaping()->Void){
        APIManager.postService(url: URLHelper.USER_FOLLOW_UNFOLLOW_URL, parameters: param) { json, error, statusCode in
            if let error = error{
                print("FOLLOWUNFOLLOWUSER",error)
                onError(error.localizedDescription)
            }else{
                print("FOLLOWUNFOLLOWUSER",json,statusCode)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        completion()
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        onError(jsonData["error"]["message"].stringValue)   
                    }
                }
            }
        }
    }
    class func getOtherUserProfile(delegate:UIViewController,param:[String:String],completion:@escaping(JSON)->Void){
        delegate.pleaseWait()
        APIManager.postService(url: URLHelper.GET_USER_PROFILE_BY_ID_URL, parameters: param) { json, error, statusCode in
            delegate.clearAllNotice()
            if let error = error{
                print("GETUSERBYID",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("GETUSERBYID",json,statusCode)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        completion(jsonData)
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate: delegate, pop: false)
                    }
                }
            }
        }
    }
    class func getUserFollowes(delegate:UIViewController,isForFollowing:Bool,param:[String:String],needLoader:Bool,onError:@escaping(String)->Void,completion:@escaping(JSON)->Void){
        needLoader ?  delegate.pleaseWait() : print("noloader")
        let url = isForFollowing ? URLHelper.GET_USER_FOLLOWING_URL : URLHelper.GET_USER_FOLLOWER_URL
        print("FOLLOWERLIST",url)
        APIManager.postService(url: url, parameters: param) { json, error, statusCode in
            delegate.clearAllNotice()
            if let error = error{
                print("FOLLOWERLIST",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                onError(error.localizedDescription)
            }else{
                print("FOLLOWERLIST",json,statusCode)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        completion(jsonData)
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate: delegate, pop: false)
                        onError(jsonData["message"].stringValue)
                    }
                }
            }
        }
    }
    class func getOtherUserFollowes(delegate:UIViewController,isForFollowing:Bool,needLoader:Bool,param:[String:String],onError:@escaping(String)->Void,completion:@escaping(JSON)->Void){
        needLoader ? delegate.pleaseWait() : print("noloader")
        let url = isForFollowing ? URLHelper.GET_OTHER_USER_FOLLOWING_URL : URLHelper.GET_OTHER_USER_FOLLOWER_URL
        print("FOLLOWERLIST",url)
        APIManager.postService(url: url, parameters: param) { json, error, statusCode in
            delegate.clearAllNotice()
            if let error = error{
                print("FOLLOWERLIST",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                onError(error.localizedDescription)
            }else{
                print("FOLLOWERLIST",json,statusCode)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        completion(jsonData)
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate: delegate, pop: false)
                        onError(jsonData["message"].stringValue)
                    }
                }
            }
        }
    }
    class func getOtherUserVideos(delegate:UIViewController,param:[String:String],completion:@escaping(JSON)->Void){
        delegate.pleaseWait()
        APIManager.postService(url: URLHelper.GET_USER_VIDEOS_BY_ID_URL, parameters: param) { json, error, statusCode in
            delegate.clearAllNotice()
            if let error = error{
                print("GETVIDEOBYID",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("GETVIDEOBYID",json,statusCode)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200 || jsonData["status"].intValue == 404{
                        completion(jsonData)
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate: delegate, pop: false)
                    }
                }
            }
        }
    }
    class func getVideoByAudio(delegate:UIViewController,param:[String:String],completion:@escaping(JSON)->Void){
        delegate.pleaseWait()
        print("GETAUDIOVIDEOPARAMS",param)
        APIManager.postService(url: URLHelper.GET_VIDEOS_BY_AUDIO_URL, parameters: param) { json, error, statusCode in
            delegate.clearAllNotice()
            if let error = error{
                print("GETVIDEOBYAUDIO",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("GETVIDEOBYAUDIO",json,statusCode)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        completion(jsonData)
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate: delegate, pop: false)
                    }
                }
            }
        }
    }
    class func getCardListApi(delegate:UIViewController,completion:@escaping(JSON)->Void){
        //delegate.pleaseWait()
        APIManager.getService(url: URLHelper.GET_CARDS_LIST_URL) { json, error, statusCode in
            //delegate.clearAllNotice()
            if let error = error{
                print("GETCARDDETAILS",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("GETCARDDETAILS",json,statusCode)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200 || jsonData["status"].intValue == 404{
                        completion(jsonData)
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate: delegate, pop: false)
                    }
                }
            }
        }
    }
    class func deletePost(delegate:UIViewController,param:[String:String],completion:@escaping()->Void){
        delegate.pleaseWait()
        APIManager.postService(url: URLHelper.DELETE_POST_URL, parameters: param) { json, error, statusCode in
            delegate.clearAllNotice()
            if let error = error{
                print("DELETEVIDEO",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("DELETEVIDEO",json,statusCode)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        completion()
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate: delegate, pop: false)
                    }
                }
            }
        }
    }
    class func editPostApi(delegate:UIViewController,param:[String:String],completion:@escaping()->Void){
        delegate.pleaseWait()
        APIManager.postService(url: URLHelper.EDIT_POST_URL, parameters: param) { json, error, statusCode in
            delegate.clearAllNotice()
            if let error = error{
                print("EDITPOST",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("EDITPOST",json,statusCode)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        completion()
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate: delegate, pop: false)
                    }
                }
            }
        }
    }
    class func getVideoCategory(delegate:UIViewController,completion:@escaping(JSON)->Void){
        APIManager.getService(url: URLHelper.GET_VIDEO_CATEGORY_URL) { json, error, statusCode in
            if let error = error{
                print("GETVIDEOCATEGORY",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("GETVIDEOCATEGORY",json,statusCode)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        completion(jsonData)
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate: delegate, pop: false)
                    }
                }
            }
        }
    }
    class func globalSearchApi(delegate:UIViewController,param:[String:String],completion:@escaping(JSON)->Void){
        APIManager.postService(url: URLHelper.GLOBAL_SEARCH_URL, parameters: param) { json, error, statusCode in
            print("GLOBALSEARCHPARAM",param)
            if let error = error{
                print("SEARCHAPI",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("SEARCHAPI",json,statusCode)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        completion(jsonData)
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate: delegate, pop: false)
                    }
                }
            }
        }
    }
    class func getNotifications(delegate:UIViewController,param:[String:String],completion:@escaping(JSON)->Void){
        APIManager.postService(url: URLHelper.GET_NOTIFICATIONS_URL, parameters: param) { json, error, statusCode in
            if let error = error{
                print("GETNOTIFICATIONS",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("GETNOTIFICATIONS",json,statusCode)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        completion(jsonData)
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate: delegate, pop: false)
                    }
                }
            }
        }
    }
    class func getChatListAPI(delegate:UIViewController,param:[String:String],completion:@escaping(JSON)->Void){
        APIManager.postService(url: URLHelper.GET_CHAT_LIST_URL, parameters: param) { json, error, statusCode in
            if let error = error{
                print("GETCHATLIST",error)
                AlertView().showAlert(message: error.localizedDescription, delegate:delegate, pop:false)
                return
            }else{
                print("GETCHATLIST",json,statusCode)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        completion(jsonData)
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate: delegate, pop: false)
                    }
                }
            }
        }
    }
    class func getChatApi(delegate:UIViewController,param:[String:String],completion:@escaping(JSON)->Void){
        APIManager.postService(url: URLHelper.GET_CHAT_URL, parameters: param) { json, error, statusCode in
            if let error = error{
                print("GETCHAT",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("GETCHAT",json,statusCode)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        completion(jsonData)
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate: delegate, pop: false)
                    }
                }
            }
        }
    }
    class func getSingleVideoDetail(delegate:UIViewController,param:[String:String],completion:@escaping(JSON)->Void){
        APIManager.postService(url: URLHelper.GET_SINGLE_VIDEO_DETAIL_URL, parameters: param) { json, error, statusCode in
            if let error = error{
                print("GETSINGELEVIDEO",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("GETSINGELEVIDEO",json,statusCode)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        completion(jsonData)
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate: delegate, pop: false)
                    }
                }
            }
        }
    }
    class func readNotification(delegate:UIViewController,param:[String:String],completion:@escaping()->Void){
        APIManager.postService(url: URLHelper.READ_NOTIFICATIONS_URL, parameters: param) { json, error, statusCode in
            if let error = error{
                print("READNOTIFICATION",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("READNOTIFICATION",json,statusCode)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        completion()
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate: delegate, pop: false)
                    }
                }
            }
        }
    }
    class func getUnreadChatCount(delegate:UIViewController,completion:@escaping(JSON)->Void){
        APIManager.getService(url: URLHelper.GET_UNREAD_CHAT_COUNT_URL) { json, error, statusCode in
            if let error = error{
                print("GETUNREADCHAT",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("GETUNREADCHAT",json,statusCode)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        completion(jsonData)
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate: delegate, pop: false)
                    }
                }
            }
        }
    }
    class func getUnreadNotificationCount(delegate:UIViewController,completion:@escaping()->Void){
        APIManager.getService(url: URLHelper.GET_UNREAD_NOTIFICATION_COUNT_URL) { json, error, statusCode in
            if let error = error{
                print("GETUNREADNOTIDICSJCNJ",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("GETUNREADNOTIDICSJCNJ",json,statusCode)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        AuthManager.currentUser.unReadNotificationCount = jsonData["notificationUnreadCount"].intValue
                        completion()
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate: delegate, pop: false)
                    }
                }
            }
        }
    }
    class func createCHatSlugApi(delegate:UIViewController,param:[String:String],completion:@escaping(JSON)->Void){
        APIManager.postService(url: URLHelper.CREATE_CHAT_SLUG_URL, parameters: param) { json, error, statusCode in
            if let error = error{
                print("CREATECHATSLUG",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("CREATECHATSLUG",json,statusCode)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        AuthManager.currentUser.unReadNotificationCount = jsonData["notificationUnreadCount"].intValue
                        completion(jsonData)
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate: delegate, pop: false)
                    }
                }
            }
        }
    }
    class func donationRaisedData(delegate:UIViewController,param:[String:String],completion:@escaping(JSON)->Void){
        APIManager.postService(url: URLHelper.DONATION_RAISEDx, parameters: param) { json, error, statusCode in
            if let error = error{
                print("CREATECHATSLUG",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
            }else{
                print("CREATECHATSLUG",json,statusCode)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        completion(jsonData)
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate: delegate, pop: false)
                    }
                }
            }
        }
    }
    class func videoAnalyticsData(delegate:UIViewController,param:[String:String],completion:@escaping(JSON)->Void){
        APIManager.postService(url: URLHelper.VIDEO_ANALYTICSx, parameters: param) { json, error, statusCode in
            if let error = error{
                print("VDIEOPANALUTC",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
            }else{
                print("VDIEOPANALUTC",json,statusCode)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        completion(jsonData)
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate: delegate, pop: false)
                    }
                }
            }
        }
    }
    class func withdrawList(delegate:UIViewController,param:[String:String],completion:@escaping(JSON)->Void){
        APIManager.postService(url: URLHelper.WITHDRAW_LIST, parameters: param) { json, error, statusCode in
            if let error = error{
                print("WIthdrawlList",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
            }else{
                print("WIthdrawlList",json,statusCode)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        completion(jsonData)
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate: delegate, pop: false)
                    }
                }
            }
        }
    }
}
