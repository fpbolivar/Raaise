//
//  AuthManager.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 24/11/22.
//

import Foundation
import UIKit
//MARK: - Auth Api Functions
class AuthManager{
    static var currentUser = UserProfileData()
    class func invalidToken(){
        UserDefaultHelper.removeAllData()
        let vc  = LoginVC()
        UIApplication.keyWin!.rootViewController = vc
        UIApplication.keyWin!.makeKeyAndVisible()
    }
    class func loginApi(delegate:UIViewController,param:[String:String],completion:@escaping()->Void){
        delegate.pleaseWait()
        print("LOGIN",param)
        APIManager.postService(url: URLHelper.LOGIN_URL, parameters: param) { json, error, status in
            delegate.clearAllNotice()
            if let error = error{
                print("LOGIN",error)
                AlertView().showAlert(message: error.localizedDescription, delegate:delegate, pop: false)
                return
            }else{
                if let jsonData = json{
                    print("LOGIN",jsonData,status)
                    if jsonData["status"].intValue == 200{
                        UserDefaultHelper.setAccessToken(value: jsonData["token"].stringValue)
                        print("TOKENNNN",UserDefaultHelper.getAccessToken())
                        completion()
                    }else{
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate:delegate, pop: false)
                    }
                }
            }
        }
    }
    class func signInApi(delegate:UIViewController,param:[String:String],completion:@escaping()->Void){
        delegate.pleaseWait()
        print("SIGNIN",param)
        APIManager.postService(url: URLHelper.SIGNIN_URL, parameters: param) { json, error, status in
            delegate.clearAllNotice()
            if let error = error{
                print("SIGNIN",error)
                AlertView().showAlert(message: error.localizedDescription, delegate:delegate, pop: false)
                return
            }else{
                if let jsonData = json{
                    print("SIGNIN",jsonData,status)
                    if jsonData["status"].intValue == 200{
                        UserDefaultHelper.setAccessToken(value: jsonData["token"].stringValue)
                        print("TOKENNNN",UserDefaultHelper.getAccessToken())
                        completion()
                    }else{
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate:delegate, pop: false)
                    }
                }
            }
        }
    }
    class func getProfileApi(delegate:UIViewController,needLoader:Bool = true,completion:@escaping()->Void){
        needLoader ? delegate.pleaseWait() : print("NOLOADER")
        APIManager.getService(url: URLHelper.GET_PROFILE_URL) { json, error, status in
            needLoader ? delegate.clearAllNotice() : print("NOLOADER")
            if let error = error{
                print("PROFILE",error)
                AlertView().showAlert(message: error.localizedDescription, delegate:delegate, pop: false)
                return
            }else{
                if let jsonData = json{
                    print("PROFILE",jsonData,status)
                    if jsonData["status"].intValue == 200{
                        
                        let user = UserProfileData(data: jsonData["data"])
                        AuthManager.currentUser = user
                        //AuthManager.currentUser.unReadNotificationCount = jsonData[ApiKeys.unReadNotificationCount.rawValue].intValue
                        print("PROFILE",AuthManager.currentUser.id)
                        completion()
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate:delegate, pop: false)
                    }
                }
            }
        }
    }
    class func changePasswordApi(delegate:UIViewController,param:[String:String],completion:@escaping()->Void){
        delegate.pleaseWait()
        APIManager.postService(url: URLHelper.CHANGE_PASSWORD, parameters: param) { json, error, status in
            delegate.clearAllNotice()
            if let error = error{
                print("CHANGEPASSWORD",error)
                AlertView().showAlert(message: error.localizedDescription, delegate:delegate, pop: false)
                return
            }else{
                if let jsonData = json{
                    print("CHANGEPASSWORD",jsonData,status)
                    if jsonData["status"].intValue == 200{
                        completion()
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate:delegate, pop: false)
                    }
                }
            }
        }
    }
    class func updateUserProfileApi(delegate:UIViewController, param:[String:String], imageChanged:Bool = false,image:[String:UIImage]? = nil,completion: @escaping()->Void){
        delegate.pleaseWait()
        if imageChanged{
            APIManager.MultipartService(url: URLHelper.GET_UPDATE_URL, parameters: param, image_is_Selected: true,images: image) { progress in
                print("IMAGEUPLOADPROG",progress)
            } completionHandler: { json, error, status in
                delegate.clearAllNotice()
                if let error = error{
                    print("UPDATEPROFILE",error)
                    AlertView().showAlert(message: error.localizedDescription, delegate:delegate, pop: false)
                    return
                }else{
                    if let jsonData = json{
                        print("UPDATEPROFILE",jsonData,status)
                        if jsonData["statusCode"].intValue == 200{
                            completion()
                        }else{
                            if jsonData["statusCode"].intValue == 401{
                                AuthManager.invalidToken()
                            }
                            AlertView().showAlert(message: jsonData["message"].stringValue, delegate:delegate, pop: false)
                        }
                    }
                }
            }
        }else{
            APIManager.postService(url: URLHelper.GET_UPDATE_URL, parameters: param) { json, error, status in
                delegate.clearAllNotice()
                if let error = error{
                    print("UPDATEPROFILE",error)
                    AlertView().showAlert(message: error.localizedDescription, delegate:delegate, pop: false)
                    return
                }else{
                    if let jsonData = json{
                        print("UPDATEPROFILE",jsonData,status)
                        if jsonData["statusCode"].intValue == 200{
                            completion()
                        }else{
                            if jsonData["statusCode"].intValue == 401{
                                AuthManager.invalidToken()
                            }
                            AlertView().showAlert(message: jsonData["message"].stringValue, delegate:delegate, pop: false)
                        }
                    }
                }
            }
        }
    }
    class func appleSignIn(delegate:UIViewController, param:[String:String], completion: @escaping()->Void){
        delegate.pleaseWait()
        APIManager.postService(url: URLHelper.APPLE_LOGIN_URLx, parameters: param) { json, error, status in
            delegate.clearAllNotice()
            if let error = error{
                print("apllelogoin",error)
                AlertView().showAlert(message: error.localizedDescription, delegate:delegate, pop: false)
                return
            }else{
                if let jsonData = json{
                    print("apllelogoin",jsonData,status)
                    if jsonData["status"].intValue == 200{
                        UserDefaultHelper.setAccessToken(value: jsonData["token"].stringValue)
                        print("TOKENNNN",UserDefaultHelper.getAccessToken())
                        completion()
                    }else{
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate:delegate, pop: false)
                    }
                }
            }
        }
    }
    class func facebookLoginApi(delegate:UIViewController, param:[String:String], completion: @escaping()->Void){
        delegate.pleaseWait()
        APIManager.postService(url: URLHelper.FB_LOGIN_URLx, parameters: param) { json, error, status in
            delegate.clearAllNotice()
            if let error = error{
                print("FBLOGIN",error)
                AlertView().showAlert(message: error.localizedDescription, delegate:delegate, pop: false)
                return
            }else{
                if let jsonData = json{
                    print("FBLOGIN",jsonData,status)
                    if jsonData["status"].intValue == 200{
                        UserDefaultHelper.setAccessToken(value: jsonData["token"].stringValue)
                        print("TOKENNNN",UserDefaultHelper.getAccessToken())
                        completion()
                    }else{
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate:delegate, pop: false)
                    }
                }
            }
        }
    }
    class func forgetPasswordApi(delegate:UIViewController, param:[String:String], completion: @escaping(String)->Void){
        delegate.pleaseWait()
        APIManager.postService(url: URLHelper.FORGOT_PASSWORD, parameters: param) { json, error, status in
            delegate.clearAllNotice()
            if let error = error{
                print("FORGETPASSWORD",error)
                AlertView().showAlert(message: error.localizedDescription, delegate:delegate, pop: false)
                return
            }else{
                if let jsonData = json{
                    print("FORGETPASSWORD",jsonData,status)
                    if jsonData["statusCode"].intValue == 200{
                        completion(jsonData["otp"].stringValue)
                    }else{
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate:delegate, pop: false)
                    }
                }
            }
        }
    }
    class func verifyOtp(delegate:UIViewController, param: [String:String],completion: @escaping(String)->Void){
        delegate.pleaseWait()
        APIManager.postService(url: URLHelper.VERIFY_OTP, parameters: param) { json, error, status in
            delegate.clearAllNotice()
            if let error = error{
                print("VERIFYOTP",error)
                AlertView().showAlert(message: error.localizedDescription, delegate:delegate, pop: false)
                return
            }else{
                print("VERIFYOTP",json,status)
                if let jsonData = json{
                    if jsonData["statusCode"].intValue == 200{
                        print("VERIFYOTP",jsonData["token"].stringValue)
                        completion(jsonData["token"].stringValue)
                    }else{
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate: delegate, pop: false)
                    }
                }
            }
        }
    }
    class func resetPasswordApi(delegate:UIViewController, param: [String:String],completion: @escaping()->Void){
        print("RESETPASSWORD",param)
        delegate.pleaseWait()
        APIManager.postService(url: URLHelper.RESET_PASSWORD, parameters: param) { json, error, status in
            delegate.clearAllNotice()
            if let error = error{
                print("RESETPASSWORD",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("RESETPASSWORD",json,status)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        completion()
                    }else{
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate: delegate, pop: false)
                    }
                }
            }
        }
    }
    class func deactivateAccount(delegate:UIViewController, param: [String:String],completion: @escaping()->Void){
        delegate.pleaseWait()
        APIManager.postService(url: URLHelper.DEACTIVATE_URL, parameters: param) { json, error, status in
            delegate.clearAllNotice()
            if let error = error{
                print("DEACTIVATEACCOUNT",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("DEACTIVATEACCOUNT",json,status)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        UserDefaultHelper.removeAllData()
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
    class func deleteAccount(delegate:UIViewController, param: [String:String],completion:@escaping()->Void){
        delegate.pleaseWait()
        APIManager.getService(url: URLHelper.DELETE_ACCOUNT_URL) { json, error, status in
            delegate.clearAllNotice()
            if let error = error{
                print("DELETEACCOUNT",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("DELETEACCOUNT",json,status)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        UserDefaultHelper.removeAllData()
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
    class func logoutApi(delegate:UIViewController, param:[String:String],completion:@escaping()->Void){
        delegate.pleaseWait()
        APIManager.postService(url: URLHelper.LOGOUT_URL, parameters: param) { json, error, status in
            if let error = error{
                print("LOGOUT",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("LOGOUT",json,status)
                if let jsonData = json{
                    if jsonData["statusCode"].intValue == 200{
                        UserDefaultHelper.removeAllData()
                        completion()
                    }else{
                        if jsonData["statusCode"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate: delegate, pop: false)
                    }
                }
            }
        }
    }
    class func notificationsApi(delegate:UIViewController,param:[String:Bool],completion:@escaping()->Void){
        APIManager.postServiceWithBoolParams(url: URLHelper.NOTIFICATIONS_URL, parameters: param) { json, error, status in
            //delegate.clearAllNotice()
            if let error = error{
                print("NOTOFICATION",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("NOTOFICATION",json,status)
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
    class func uploadVideoData(delegate:UIViewController,param:[String:Any],resourcesVideo:[String : URL],resourcesImage:[String : UIImage],completion:@escaping()->Void,progress:@escaping(Int)->Void){
        
        APIManager.MultipartVideoImageService(url: URLHelper.UPLOAD_VIDEO_URL, parameters: param, image_is_Selected: true,images:resourcesImage, video_is_Selected: true, resourcesVideo: resourcesVideo) { progressValue in
            print(progressValue)
            progress(progressValue)
        } completionHandler: { json, error, statusCode in
            if let error = error{
                print("UPLOADVIDEO",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("UPLOADVIDEO",json,statusCode)
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
    class func googleLoginApi(delegate:UIViewController,param:[String:String],completion:@escaping()->Void){
        delegate.pleaseWait()
        APIManager.postService(url: URLHelper.GOOGLE_LOGIN_URLx, parameters: param) { json, error, statusCode in
            delegate.clearAllNotice()
            if let error = error{
                print("GOOGLESIGNIN",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("GOOGLESIGNIN",json,statusCode)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        UserDefaultHelper.setAccessToken(value: jsonData["token"].stringValue)
                        print("TOKENNNN",UserDefaultHelper.getAccessToken())
                        completion()
                    }else{
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate: delegate, pop: false)
                    }
                }
            }
        }
    }
    class func postCommentApi(delegate:UIViewController,param:[String:String],completion:@escaping(JSON)->Void){
        APIManager.postService(url: URLHelper.POST_COMMENTS_URL, parameters: param) { json, error, statusCode in
            if let error = error{
                print("POSTCOMMENTS",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("POSTCOMMENTS",json,statusCode)
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
    class func editCommentApi(delegate:UIViewController,param:[String:String],completion:@escaping(JSON)->Void){
        APIManager.postService(url: URLHelper.EDIT_COMMENTS_URL, parameters: param) { json, error, statusCode in
            if let error = error{
                print("EDITCOMMENTS",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("EDITCOMMENTS",json,statusCode)
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
    class func editReplyApi(delegate:UIViewController,param:[String:String],completion:@escaping(JSON)->Void){
        APIManager.postService(url: URLHelper.EDIT_REPLY_URL, parameters: param) { json, error, statusCode in
            if let error = error{
                print("EDITREply",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("EDITREply",json,statusCode)
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
    class func deleteCommentApi(delegate:UIViewController,param:[String:String],completion:@escaping()->Void){
        APIManager.postService(url: URLHelper.DELETE_COMMENTS_URL, parameters: param) { json, error, statusCode in
            if let error = error{
                print("DELETECOMMENTS",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("DELTECOMMENTS",json,statusCode)
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
    class func deletereplyApi(delegate:UIViewController,param:[String:String],completion:@escaping()->Void){
        APIManager.postService(url: URLHelper.DELETE_REPLY_URL, parameters: param) { json, error, statusCode in
            if let error = error{
                print("DELETEREply",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("DELETEREply",json,statusCode)
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
    class func postReply(delegate:UIViewController,param:[String:String],completion:@escaping(JSON)->Void){
        APIManager.postService(url: URLHelper.POST_REPLY_URL, parameters: param) { json, error, statusCode in
            if let error = error{
                print("POSTREPLY",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("POSTREPLY",json,statusCode)
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
    class func addBankDetails(delegate:UIViewController,param:[String:String],completion:@escaping()->Void){
        delegate.pleaseWait()
        APIManager.postService(url: URLHelper.ADD_BANK_DETAILS_URL, parameters: param) { json, error, statusCode in
            delegate.clearAllNotice()
            if let error = error{
                print("ADDBANKDETAILS",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("ADDBANKDETAILS",json,statusCode)
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
    class func videoViewApi(param:[String:String],onError:@escaping(String)->Void,completion:@escaping()->Void){
        APIManager.postService(url: URLHelper.VIDEO_COUNT_URL, parameters: param) { json, error, statusCode in
            if let error = error{
                print("VIDEOVIEW",error)
                onError(error.localizedDescription)
            }else{
                print("VIDEOVIEW",json,json)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        completion()
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
    class func addCardApi(delegate:UIViewController,param:[String:String],completion:@escaping()->Void){
        APIManager.postService(url: URLHelper.ADD_CARD_URL, parameters: param) { json, error, statusCode in
            delegate.clearAllNotice()
            if let error = error{
                print("ADDCARD",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("ADDCARD",json,statusCode)
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
    class func makePaymentWithCardId(delegate:UIViewController,param:[String:String],completion:@escaping()->Void,onError:@escaping()->Void){
        
        APIManager.postService(url: URLHelper.PAYMENT_URL, parameters: param) { json, error, statusCode in
            delegate.clearAllNotice()
            if let error = error{
                print("PAYMENT",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
            }else{
                print("PAYMENT",json,statusCode)
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        completion()
                    }else if jsonData["status"].intValue == 422 {
                        onError()
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
    class func deleteCardApi(delegate:UIViewController,param:[String:String],completion:@escaping()->Void){
        APIManager.postService(url: URLHelper.DELETE_CARD_URL, parameters: param) { json, error, statusCode in
            if let error = error{
                print("DELETECARD",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("DELETECARD",json,statusCode)
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
    class func setDefaultCard(delegate:UIViewController,param:[String:String],completion:@escaping()->Void){
        APIManager.postService(url: URLHelper.SET_DEFAULT_CARD_URL, parameters: param) { json, error, statusCode in
            delegate.clearAllNotice()
            if let error = error{
                print("SETDEFAULTCARD",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("SETDEFAULTCARD",json,statusCode)
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
    class func shareVideoToChatApi(delegate:UIViewController,param:[String:String],completion:@escaping(JSON)->Void){
        APIManager.postService(url: URLHelper.SHARE_VIDEO_TO_CHAT_URL, parameters: param) { json, error, statusCode in
            if let error = error{
                print("SHAREVIDEO",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("SHAREVIDEO",json,statusCode)
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
    class func claimDonationApi(delegate:UIViewController,param:[String:String],completion:@escaping()->Void){
        delegate.pleaseWait()
        APIManager.postService(url: URLHelper.CLAIM_DONATIONx, parameters: param) {
            json, error, statusCode in
            delegate.clearAllNotice()
            if let error = error{
                print("CLAIM",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
            }else{
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
    class func blockUser(delegate:UIViewController,param:[String:String],completion:@escaping()->Void){
        delegate.pleaseWait()
        APIManager.postService(url: URLHelper.BLOCK_USER, parameters: param) {
            json, error, statusCode in
            delegate.clearAllNotice()
            if let error = error{
                print("BLOCK",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
            }else{
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
    class func joinLiveRoom(delegate:UIViewController,param:[String:String],completion:@escaping()->Void){
        delegate.pleaseWait()
        APIManager.postService(url: URLHelper.JOIN_LIVE_ROOM, parameters: param) {
            json, error, statusCode in
            delegate.clearAllNotice()
            if let error = error{
                print("JOIN_LIVE_ROOM",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
            }else{
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
    class func leaveLiveRoom(delegate:UIViewController,param:[String:String],completion:@escaping()->Void){
        delegate.pleaseWait()
        APIManager.postService(url: URLHelper.LEAVE_LIVE_ROOM, parameters: param) {
            json, error, statusCode in
            delegate.clearAllNotice()
            if let error = error{
                print("LEAVE_LIVE_ROOM",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
            }else{
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
    class func updateLiveRoomApi(delegate:UIViewController, param:[String:String], image:[String:UIImage]? = nil,completion: @escaping(JSON)->Void){
        delegate.pleaseWait()
        
        APIManager.MultipartService(url: URLHelper.UPDATE_LIVE_ROOM, parameters: param, image_is_Selected: true,images: image) { progress in
            print("IMAGEUPLOADPROG",progress)
        } completionHandler: { json, error, status in
            delegate.clearAllNotice()
            if let error = error{
                print("updateLiveRoomApi",error)
                AlertView().showAlert(message: error.localizedDescription, delegate:delegate, pop: false)
                return
            }else{
                if let jsonData = json{
                    print("updateLiveRoomApi",jsonData,status)
                    if jsonData["status"].intValue == 200{
                        completion(jsonData)
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate:delegate, pop: false)
                    }
                }
            }
        }
    }
    class func createLiveRoomApi(delegate:UIViewController, param:[String:String], image:[String:UIImage]? = nil,completion: @escaping()->Void){
        delegate.pleaseWait()
        //CHANGE IMAGE IS SELECTED TO TRUE WHEN IMAGE IMPLEMENTATION DONE
        APIManager.MultipartService(url: URLHelper.CREATE_LIVE_ROOM, parameters: param, image_is_Selected: false,images: image) { progress in
            print("IMAGEUPLOADPROG",progress)
        } completionHandler: { json, error, status in
            delegate.clearAllNotice()
            if let error = error{
                print("createLiveRoomApi",error)
                AlertView().showAlert(message: error.localizedDescription, delegate:delegate, pop: false)
                return
            }else{
                if let jsonData = json{
                    print("createLiveRoomApi",jsonData,status)
                    if jsonData["status"].intValue == 200{
                        completion()
                    }else{
                        if jsonData["status"].intValue == 401{
                            AuthManager.invalidToken()
                        }
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate:delegate, pop: false)
                    }
                }
            }
        }
    }
    class func sendMessageApi(delegate:UIViewController,param:[String:String],completion:@escaping()->Void){
        //delegate.pleaseWait()
        APIManager.postService(url: URLHelper.SEND_MESSAGE_URL, parameters: param) {
            json, error, statusCode in
            //delegate.clearAllNotice()
            if let error = error{
                print("SEND_MESSAGE_URL",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
            }else{
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
    class func updatePrivacy(delegate:UIViewController,param:[String:String],completion:@escaping()->Void){
        delegate.pleaseWait()
        APIManager.postService(url: URLHelper.UPDATE_PRIVACY_URL, parameters: param) {
            json, error, statusCode in
            delegate.clearAllNotice()
            if let error = error{
                print("UPDATEPRIVACY",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
            }else{
                if let jsonData = json{
                    if jsonData["status"].intValue == 200{
                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate: delegate, pop: false)
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
}
