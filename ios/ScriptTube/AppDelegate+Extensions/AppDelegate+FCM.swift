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

//MARK: - FIREBASE FCM TOKEN METHODS

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
        return [[.alert, .sound]]
    }
    
    
    @available(iOS 10.0, *)
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        
        
        completionHandler()
    }
    func application(_ application: UIApplication,
                     didReceiveRemoteNotification userInfo: [AnyHashable : Any],
                     fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        
        
        print(userInfo)
        completionHandler(.newData)
    }
    
    
}

extension UIButton {
    func applyGradientToUIButtonTitleLabel(colors: [UIColor], title:String) {
        // Create a transparent UILabel
        // Apply gradient text color to the label\\
        
        self.titleLabel?.font = AppFont.FontName.medium.getFont(size: 12)
        self.titleLabel?.applyGradientColor(colors: colors)
        
        
        // Add the label on top of the button
        //addSubview(label)
    }
    
    func setBtnGradientText(colors: [UIColor], labelText:String) {
        guard let titleLabel = self.titleLabel else {
            return
        }
        
        //            let gradientText = NSMutableAttributedString(string: labelText)
        //            gradientText.addAttribute(.foregroundColor, value: UIColor.red, range: NSRange(location: 0, length: gradientText.length))
        //            gradientText.addAttribute(.foregroundColor, value: UIColor.blue, range: NSRange(location: 0, length: gradientText.length))
        
        let gradientLayer = CAGradientLayer()
        gradientLayer.colors = colors.map { $0.cgColor }
        gradientLayer.startPoint = CGPoint(x: 0.0, y: 1.0)
        
        gradientLayer.endPoint = CGPoint(x: 0.5, y: 1.0)
        //gradientLayer.locations = [0, 1]
        gradientLayer.frame = titleLabel.bounds
        
        
        
        let textMaskLayer = CATextLayer()
        textMaskLayer.frame = gradientLayer.bounds
        textMaskLayer.string = labelText//self.titleLabel?.text
        textMaskLayer.alignmentMode = .left
        textMaskLayer.foregroundColor = UIColor.black.cgColor
        textMaskLayer.font = titleLabel.font
        textMaskLayer.fontSize = titleLabel.font.pointSize
        
        
        
        gradientLayer.mask = textMaskLayer
        
        if let existingLayers = self.layer.sublayers {
            for layer in existingLayers {
                if layer is CAGradientLayer {
                    layer.removeFromSuperlayer()
                }
            }
        }
        
        self.layer.addSublayer(gradientLayer)
    }
}

extension UILabel {
    
    func applyGradientColor(colors: [UIColor]) {
        // Ensure label has text
        guard let labelText = self.text else { return }
        
        // Create gradient layer
        let gradientLayer = CAGradientLayer()
        gradientLayer.frame = bounds
        gradientLayer.colors = colors.map { $0.cgColor }
        gradientLayer.startPoint = CGPoint(x: 0.0, y: 1.0)
        
        gradientLayer.endPoint = CGPoint(x: 0.5, y: 1.0)
        gradientLayer.locations = [0, 1]
        
        
        // Convert gradient layer to image
        UIGraphicsBeginImageContextWithOptions(gradientLayer.bounds.size, false, 0.0)
        if let context = UIGraphicsGetCurrentContext() {
            gradientLayer.render(in: context)
        }
        let gradientImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        
        // Create attributed string with gradient color
        let attributedString = NSMutableAttributedString(string: labelText)
        let textRange = NSMakeRange(0, attributedString.length)
        attributedString.addAttribute(.foregroundColor, value: UIColor(patternImage: gradientImage!), range: textRange)
        
        
        
        // Apply attributed text to label
        self.attributedText = attributedString
    }
    
    
    func applyGradientColorToLabelText(colors: [UIColor]) {
        // Ensure label has text
        guard let labelText = self.text else { return }
        
        // Create gradient layer
        let gradientLayer = CAGradientLayer()
        gradientLayer.frame = bounds
        gradientLayer.colors = colors.map { $0.cgColor }
        gradientLayer.startPoint = CGPoint(x: 0.0, y: 1.0)
        
        gradientLayer.endPoint = CGPoint(x: 0.2, y: 1.0)
        gradientLayer.locations = [0, 1]
        
        
        // Convert gradient layer to image
        UIGraphicsBeginImageContextWithOptions(gradientLayer.bounds.size, false, 0.0)
        if let context = UIGraphicsGetCurrentContext() {
            gradientLayer.render(in: context)
        }
        let gradientImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        
        // Create attributed string with gradient color
        let attributedString = NSMutableAttributedString(string: labelText)
        let textRange = NSMakeRange(0, attributedString.length)
        attributedString.addAttribute(.foregroundColor, value: UIColor(patternImage: gradientImage!), range: textRange)
        
        
        
        // Apply attributed text to label
        self.attributedText = attributedString
    }
}

extension UISwitch {
    
    func applyGradientColorToUISwitch(gradientColors: [UIColor]) {
            // Create a gradient layer
        let gradientLayer = CAGradientLayer()
        gradientLayer.frame = bounds
        gradientLayer.colors = gradientColors.map { $0.cgColor }
        gradientLayer.startPoint = CGPoint(x: 0.0, y: 0.5)
        gradientLayer.endPoint = CGPoint(x: 0.8, y: 0.5)
        gradientLayer.cornerRadius = bounds.height/2
        gradientLayer.masksToBounds = true
        
            // Set the gradient image as the background image for the switch
            layer.insertSublayer(gradientLayer, below: thumbTintColor != nil ? layer.sublayers?.last : nil)
        }
}

//extension UIButton {
//    
//    
//    func applyGradientToTitleLabel(colors: [UIColor]) {
//        // Create a transparent UILabel
//        let label = UILabel(frame: bounds)
//        label.text = titleLabel?.text
//        label.textAlignment = titleLabel?.textAlignment ?? .center
//        label.font = titleLabel?.font ?? UIFont.systemFont(ofSize: 17)
//        label.textColor = .clear
//        
//        // Apply gradient text color to the label
//        label.applyGradient(colors: colors)
//        
//        // Add the label on top of the button
//        addSubview(label)
//    }
//    
//    
//    func setTitleGradientColor(startColor: UIColor, endColor: UIColor, forState state: UIControl.State) {
//        let gradientLayer = CAGradientLayer()
//        gradientLayer.frame = bounds
//        gradientLayer.colors = [startColor.cgColor, endColor.cgColor]
//        gradientLayer.locations = [0.0, 1.0]
//        
//        // Create a mask using the button's titleLabel
//        titleLabel?.layer.mask = gradientLayer
//    }
//}



