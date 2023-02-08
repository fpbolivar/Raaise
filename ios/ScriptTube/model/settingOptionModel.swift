//
//  settingOptionModel.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/18/22.
//MARK: - Data Model for Settings Screen

import Foundation
import Foundation
enum SettingType {
    case profile
    case switchBtn
    case logout
    case redLbl
    case none
}
class SettingOptionModal{
    var title = "";
    var type:SettingType = .none;

    var items = [SettingOptionItems]()
    init(title:String,items: [SettingOptionItems],type:SettingType) {
        self.title = title
        self.items = items
        self.type = type
    }
}
class SettingOptionItems{
    var title = "";
    var subtitle = "";
    var type:SettingType = .none;
    init(title:String,subtitle:String,type:SettingType = .profile) {
        self.title = title
        self.subtitle = subtitle
        self.type = type
    }

}

class SettingOptionManager{


    func getList()->[SettingOptionModal]{

        let myprofileSection = SettingOptionModal(title: "MY ACCOUNT", items: [SettingOptionItems(title: "Personal Information", subtitle: ""),SettingOptionItems(title: "Username", subtitle: ""),SettingOptionItems(title: "Short Bio", subtitle: ""),SettingOptionItems(title: "Change Password", subtitle: "")], type: .profile)


        let notificationSection = SettingOptionModal(title: "NOTIFICATIONS", items: [SettingOptionItems(title: "Push Notifications", subtitle: "",type: .switchBtn),SettingOptionItems(title: "Email Notifications", subtitle: "")], type: .switchBtn)

        let paymentSection = SettingOptionModal(title: "PAYMENTS", items: [SettingOptionItems(title: "Payment Methods", subtitle: ""),SettingOptionItems(title: "Support Raised", subtitle: ""),SettingOptionItems(title: "Withdrawals", subtitle: ""),SettingOptionItems(title: "Bank Details", subtitle: "")], type: .profile)

        let aboutSection = SettingOptionModal(title: "ABOUT", items: [SettingOptionItems(title: "Terms of Service", subtitle: ""),SettingOptionItems(title: "Privacy Policy", subtitle: ""),SettingOptionItems(title: "Copyright Policy", subtitle: "")], type: .profile)

        let logoutSection = SettingOptionModal(title: "LOGINS", items: [SettingOptionItems(title: "Deactivate Account", subtitle: ""),SettingOptionItems(title: "Delete Account", subtitle: "",type: .redLbl),SettingOptionItems(title: "Logout", subtitle: "",type: .logout)], type: .profile)

        return [myprofileSection,notificationSection,paymentSection,aboutSection,logoutSection];
    }
}
