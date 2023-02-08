//
//  AppDelegate.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 08/11/22.
//

import UIKit
import FacebookCore
import GoogleSignIn

@main
class AppDelegate: UIResponder, UIApplicationDelegate {
   static let signInConfig = GIDConfiguration(clientID: "201557307033-958382ttchrur82u65n689l6ubhd0kn8.apps.googleusercontent.com")


    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        fcmMessagingSetup(application: application)
        IQKeyboardManager.shared.enable = true
        IQKeyboardManager.shared.enableAutoToolbar = true
        IQKeyboardManager.shared.shouldShowToolbarPlaceholder = false
        IQKeyboardManager.shared.previousNextDisplayMode = .alwaysHide
        
        ///Facebook Login
        ApplicationDelegate.shared.application(
            application,
            didFinishLaunchingWithOptions:
            launchOptions
        )
        
        ///Googole Sign In
        GIDSignIn.sharedInstance.restorePreviousSignIn { user, error in
          if error != nil || user == nil {
            // Show the app's signed-out state.
              print("GOOGLE SIGN IN ERROR",error?.localizedDescription as Any)
          } else {
              print("GOOGLE SIGN IN COMPLETE",user as Any)
            // Show the app's signed-in state.
          }
        }
        return true
    }

    // MARK: UISceneSession Lifecycle

    func application(_ application: UIApplication, configurationForConnecting connectingSceneSession: UISceneSession, options: UIScene.ConnectionOptions) -> UISceneConfiguration {
        // Called when a new scene session is being created.
        // Use this method to select a configuration to create the new scene with.
        return UISceneConfiguration(name: "Default Configuration", sessionRole: connectingSceneSession.role)
    }

    func application(_ application: UIApplication, didDiscardSceneSessions sceneSessions: Set<UISceneSession>) {
        // Called when the user discards a scene session.
        // If any sessions were discarded while the application was not running, this will be called shortly after application:didFinishLaunchingWithOptions.
        // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
    }


}

