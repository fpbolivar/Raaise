//
//  AuthManager.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 24/11/22.
//

import Foundation
import UIKit
class AuthManager{
    static var currentUser = UserProfileData()
    class func invalidToken(){
        UserDefaultHelper.removeAllData()
        let vc  = MainTabBarVC()
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
        //delegate.pleaseWait()
        needLoader ? delegate.pleaseWait() : print("NOLOADER")
        APIManager.getService(url: URLHelper.GET_PROFILE_URL) { json, error, status in
            //delegate.clearAllNotice()
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
                        if jsonData["status"].intValue == 200{
                            completion()
//                            AuthManager.getProfileApi(delegate: delegate,needLoader: false) {
//                                completion()
//                            }
                        }else{
                            if jsonData["status"].intValue == 401{
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
                        if jsonData["status"].intValue == 200{
                            completion()
//                            AuthManager.getProfileApi(delegate: delegate,needLoader: false) {
//                                completion()
//                            }
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
                        //AlertView().showAlert(message: jsonData["otp"].stringValue, delegate:delegate, pop: false)
                       // ToastManager.successToast(delegate: delegate, msg: jsonData["otp"].stringValue)
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
//        APIManager.postService(url: URLHelper.DELETE_ACCOUNT_URL, parameters: param) { json, error, status in
//            delegate.clearAllNotice()
//            if let error = error{
//                print("DELETEACCOUNT",error)
//                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
//                return
//            }else{
//                print("DELETEACCOUNT",json,status)
//                if let jsonData = json{
//                    if jsonData["status"].intValue == 200{
//                        UserDefaultHelper.removeAllData()
//                        completion()
//                    }else{
//                        AlertView().showAlert(message: jsonData["message"].stringValue, delegate: delegate, pop: false)
//                    }
//                }
//            }
//        }
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
        //delegate.pleaseWait()
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
    class func uploadVideoData(delegate:UIViewController,param:[String:Any],resourcesVideo:[String : URL],resourcesImage:[String : UIImage],completion:@escaping()->Void){
        
        APIManager.MultipartVideoImageService(url: URLHelper.UPLOAD_VIDEO_URL, parameters: param, image_is_Selected: true,images:resourcesImage, video_is_Selected: true, resourcesVideo: resourcesVideo) { progressValue in
            print(progressValue)
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
    class func postCommentApi(delegate:UIViewController,param:[String:String],completion:@escaping()->Void){
        APIManager.postService(url: URLHelper.POST_COMMENTS_URL, parameters: param) { json, error, statusCode in
            if let error = error{
                print("POSTCOMMENTS",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("POSTCOMMENTS",json,statusCode)
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
    class func postReply(delegate:UIViewController,param:[String:String],completion:@escaping()->Void){
        APIManager.postService(url: URLHelper.POST_REPLY_URL, parameters: param) { json, error, statusCode in
            if let error = error{
                print("POSTREPLY",error)
                AlertView().showAlert(message: error.localizedDescription, delegate: delegate, pop: false)
                return
            }else{
                print("POSTREPLY",json,statusCode)
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
}
