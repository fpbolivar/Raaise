//
//  UserDefaultHelper.swift
//  FraudsterFresh
//
//  Created by Dilpreet Singh on 8/3/22.
//

import Foundation
class UserDefaultHelper{
    //MARK:- USERDEFAULTS KEYS
    static let UD_UserID  : String = "UD_UserID"
    static let UD_Device_Token  : String = "UD_Device_Token"
    static let UD_API_Token  : String = "UD_API_Token"
    static let UD_UserEmail  : String = "UD_UserEmail"
    static let UD_InstallationID : String = "UD_InstallationID"
    //MARK:SET METHOD OF USERDEFAULTS DATA
    static func setDeviceToken(value:String){
        getUserDefault().set(value, forKey: UserDefaultHelper.UD_Device_Token)
    }

    static func setAccessToken(value:String){
        getUserDefault().set(value, forKey: UserDefaultHelper.UD_API_Token)
    }

    static func getAccessToken()->String{
        print(UserDefaultHelper.UD_API_Token)
        let token = getUserDefault().string(forKey: UserDefaultHelper.UD_API_Token) ?? ""
        print("Constant token",token)
        if token != ""{
            return token
            //return "Bearer \(token)"
        }
        return token

    }
    static func removeAllData(){
        getUserDefault().removeObject(forKey: UserDefaultHelper.UD_API_Token)
        //getUserDefault().removeObject(forKey: UserDefaultHelper.UD_UserEmail)

    }
    static func installationID()->String{
        let UD_InstallationID =   getUserDefault().string(forKey: UserDefaultHelper.UD_InstallationID) ?? ""
        return UD_InstallationID

    }
    static func setInstallationID(value:String){
        getUserDefault().set(value, forKey: UserDefaultHelper.UD_InstallationID)
    }

    static func setDevice_Token(value:String){
        getUserDefault().set(value, forKey: UserDefaultHelper.UD_Device_Token)
    }

    static func getDevice_Token()->String{
        let UD_Device_Token =   getUserDefault().string(forKey: UserDefaultHelper.UD_Device_Token) ?? ""

        return UD_Device_Token

    }
    static func setEmail(value:String){
        getUserDefault().set(value, forKey: UserDefaultHelper.UD_UserEmail)
    }
    static func getEmail()->String{
        let UD_Device_Token =   getUserDefault().string(forKey: UserDefaultHelper.UD_UserEmail) ?? ""

        return UD_Device_Token

    }
    static func syncronize(){
        getUserDefault().synchronize()
    }
    static  func getUserDefault()->UserDefaults{
        return UserDefaults.standard
        //return UserDefaults(suiteName: Constant.appGroupIdentifier)!
    }
}
