//
//  Validators.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 24/11/22.
//

import Foundation
class Validator{
    static func isValidEmail(email:String) -> Bool {
         print("validate emilId: \(email)")
         let emailRegEx = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}"
         let emailTest = NSPredicate(format:"SELF MATCHES %@", emailRegEx)
         let result = emailTest.evaluate(with: email)
         return result
    }
    static  func isValidPassword(value:String) -> Bool {
        print("validate value: \(value)")
        // Alternative Regexes

        // 8 characters. One uppercase. One Lowercase. One number.
        // static let regex = "^(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[a-z]).{8,}$"
        //
        // no length. One uppercase. One lowercae. One number.
        // static let regex = "^(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[a-z]).*?$"

        /// Regular express string to be used in validation.
        let regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[#$@!%&*?])[A-Za-z[0-9]#$@!%&*?]{8,}$"
        let passRegEx = regex
        let passTest = NSPredicate(format:"SELF MATCHES %@", passRegEx)
        let result = passTest.evaluate(with: value)
        return result
    }
}
