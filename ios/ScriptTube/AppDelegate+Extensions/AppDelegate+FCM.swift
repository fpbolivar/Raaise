//
//  AppDelegate+FCM.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 26/12/22.
//

import Foundation
import FirebaseMessaging
import UserNotifications
import FirebaseCore

extension AppDelegate:UNUserNotificationCenterDelegate{
    func fcmMessagingSetup(application:UIApplication){
        FirebaseApp.configure()
        if #available(iOS 10.0, *) {
            // For iOS 10 display notification (sent via APNS)
            UNUserNotificationCenter.current().delegate = self
            
            let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
            UNUserNotificationCenter.current().requestAuthorization(
                options: authOptions,
                completionHandler: { _, _ in }
            )
        } else {
            let settings: UIUserNotificationSettings =
            UIUserNotificationSettings(types: [.alert, .badge, .sound], categories: nil)
            application.registerUserNotificationSettings(settings)
        }
        application.applicationIconBadgeNumber = 0
        application.registerForRemoteNotifications()
        
        Messaging.messaging().delegate = self
        Messaging.messaging().token { token, error in
            if let error = error {
                print("Error fetching FCM registration token: \(error)")
            } else if let token = token {
                
                print("FCM registration token: \(token)")
                
                UserDefaultHelper.setDeviceToken(value: token)
                
            }
            
        }
    }
//    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
//        print("Registered for Apple Remote Notifications")
//        Messaging.messaging().setAPNSToken(deviceToken, type: .unknown)
//    }
}
extension AppDelegate: MessagingDelegate{
    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        print("Firebase registration token: \(String(describing: fcmToken))")
        
        let dataDict: [String: String] = ["token": fcmToken ?? ""]
        NotificationCenter.default.post(
            name: Notification.Name("FCMToken"),
            object: nil,
            userInfo: dataDict
        )
    }
    static func screenOpenWhenClickNotification(param:[AnyHashable : Any])
    {
        
    }
    func application(application: UIApplication,
                     didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
      Messaging.messaging().apnsToken = deviceToken
    }
    
}

extension AppDelegate{
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                willPresent notification: UNNotification) async
    -> UNNotificationPresentationOptions {
        let userInfo = notification.request.content.userInfo
        
        print(userInfo)
        //        if let notificationType = userInfo["notificationType"] as? String{
        //            if notificationType == "chat"{
        //                let channelID = userInfo["channelID"] as? String ?? ""
        //                if NotificationManager.instance.channelID == channelID{
        //                    return [[.badge]]
        //                }
        //            }}
        
        return [[.alert, .sound]]
    }
    
    
    @available(iOS 10.0, *)
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        
        
        completionHandler()
    }
    func application(_ application: UIApplication,
                     didReceiveRemoteNotification userInfo: [AnyHashable : Any],
                     fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        
        // Perform background operation
        print(userInfo)
        completionHandler(.newData)
    }
    
    
}

