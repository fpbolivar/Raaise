//
//  ToastManager.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 24/11/22.
//
import Foundation
import UIKit
class ToastManager{
  static  func errorToast(delegate:UIViewController,msg:String){
        FraudsterToast(msg, state: .error,location: .top, sender: delegate).show(.average)
    }
    static  func successToast(delegate:UIViewController,msg:String){
        FraudsterToast(msg, state: .success,location: .top, sender: delegate).show(.average)
    }
    static  func dismissAll(delegate:UIViewController){
        FraudsterToast.dismiss(sender: delegate, animated: true)
    }
}

