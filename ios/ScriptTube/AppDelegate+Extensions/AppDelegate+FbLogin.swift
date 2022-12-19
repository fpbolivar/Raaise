//
//  AppDelegate+FbLogin.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 25/11/22.
//

import Foundation
import FBSDKCoreKit
import GoogleSignIn

extension AppDelegate{
    
    func application(
            _ app: UIApplication,
            open url: URL,
            options: [UIApplication.OpenURLOptionsKey : Any] = [:]
        ) -> Bool {
            ApplicationDelegate.shared.application(
                app,
                open: url,
                sourceApplication: options[UIApplication.OpenURLOptionsKey.sourceApplication] as? String,
                annotation: options[UIApplication.OpenURLOptionsKey.annotation]
            )
            var handled: Bool

            handled = GIDSignIn.sharedInstance.handle(url)
            if handled {
              return true
            }

            // Handle other custom URL types.

            // If not handled by this app, return false.
            return false
        }
}
