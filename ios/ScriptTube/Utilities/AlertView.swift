//
//  AlertView.swift
//  SriptTube
//
//  Created by Code Optimal Solutions Ios on 21/11/22.
//

import UIKit

class AlertView: NSObject {

    static let instance: AlertView = {
    
        return AlertView()
    }()
    static let title = "SripTube"
    typealias alertHandler = (Any?) -> Void
    
    
}
enum ActionType{
    case yes
    case nothanks
    case submit
    case camera
    case friend
    case social
    case photo
    case cancel
}
extension AlertView {
    
    func waitLoader() {
                    let alert = UIAlertController(title: nil, message: "Please wait...", preferredStyle: .alert)
                    let loadingIndicator = UIActivityIndicatorView(frame: CGRect(x: 10, y: 5, width: 50, height: 50))
                    loadingIndicator.hidesWhenStopped = true
                    loadingIndicator.style = UIActivityIndicatorView.Style.gray
                    loadingIndicator.startAnimating();
                    alert.view.addSubview(loadingIndicator)
        getWindow()?.rootViewController?.present(alert, animated: true, completion: nil)
        }

    func dismissLoader() {
        getWindow()?.rootViewController?.dismiss(animated: false, completion: nil)
    }
    
    func datePicker(message: String, delegate: UIViewController , pop:Bool,dateLimit:Date,startDateEnable:Bool,handler:@escaping(String,Date)->(),clearhandler:@escaping()->()) {
        print(startDateEnable)
            print(dateLimit)
        let datePicker = UIDatePicker()
        datePicker.datePickerMode = .date
        if #available(iOS 13.4, *) {
            datePicker.preferredDatePickerStyle = .wheels
        } else {
            //datePicker.datePickerStyle = .wheels
            // Fallback on earlier versions
        }
        
        let calendar = Calendar.current
        var components = DateComponents()
        components.calendar = calendar

        if startDateEnable{
//        let maxDate = calendar.date(byAdding: components, to: dateLimit)!
//        datePicker.maximumDate = maxDate
        }else{
//            let minDate = calendar.date(byAdding: components, to: dateLimit)!
//            datePicker.minimumDate = minDate
        }
        let alert = UIAlertController(title: message, message: "\n\n\n\n\n\n\n\n\n\n\n", preferredStyle: .actionSheet)
        
        datePicker.frame = CGRect(x: 5, y: 20, width: delegate.view.frame.width - 20, height: 220)
        alert.view.addSubview(datePicker)
        
        
        
        let ok = UIAlertAction(title: "Confirm", style: .default) { (action) in
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "dd-MM-yyyy"
            
            let dateString = dateFormatter.string(from: datePicker.date)
            
            print(dateString)
            handler(dateString,datePicker.date)
           
        }
        let clear = UIAlertAction(title: "Clear & Dismiss", style: .default) { (action) in
            
            clearhandler()
           
        }
        let cancel = UIAlertAction(title: "Cancel", style: .cancel, handler: nil)
        
        alert.addAction(ok)
        alert.addAction(clear)
        alert.addAction(cancel)
        
        delegate.present(alert, animated: true, completion: nil)
        
    }
    
    
    func timePicker2(message: String, delegate: UIViewController , pop:Bool,dateLimit:Date,startDateEnable:Bool,handler:@escaping(String)->()) {
        print(startDateEnable)
            print(dateLimit)
        let datePicker = UIDatePicker()
        datePicker.datePickerMode = .time
        
        

//        let calendar = Calendar.current
//        var components = DateComponents()
//        components.calendar = calendar
//
//        if startDateEnable{
//        let maxDate = calendar.date(byAdding: components, to: dateLimit)!
//        datePicker.maximumDate = maxDate
//        }else{
//            let minDate = calendar.date(byAdding: components, to: dateLimit)!
//            datePicker.minimumDate = minDate
//        }
        let alert = UIAlertController(title: message, message: "\n\n\n\n\n\n\n\n\n\n\n", preferredStyle: .actionSheet)
        
        datePicker.frame = CGRect(x: 5, y: 20, width: delegate.view.frame.width - 20, height: 220)
        alert.view.addSubview(datePicker)
        
        
        
        let ok = UIAlertAction(title: "Confirm", style: .default) { (action) in
            let dateFormatter = DateFormatter()
            dateFormatter.dateFormat = "hh:mm a"
            
            let dateString = dateFormatter.string(from: datePicker.date)
            
            print(dateString)
            handler(dateString)
           
        }
        
        let cancel = UIAlertAction(title: "Cancel", style: .cancel, handler: nil)
        
        alert.addAction(ok)
        alert.addAction(cancel)
        
        delegate.present(alert, animated: true, completion: nil)
        
    }
    
    func timePicker_With_Limitation(message: String,previousTime:String ,delegate: UIViewController , pop:Bool,timeLimit:Date?,startTimeEnable:Bool,handler:@escaping(String)->()) {
            print(startTimeEnable)
                print(timeLimit)
            let datePicker = UIDatePicker()
        datePicker.datePickerMode = .time
          //  datePicker.minuteInterval = 30
            //  datePicker.locale = Locale(identifier: "en_GB")

         
        
        let calendar = Calendar.current
        var dateComponents = calendar.dateComponents([.day, .month, .year], from: Date())
        
        if !startTimeEnable{
            
            
           let Shour =  previousTime.split(separator: ":")
            dateComponents.setValue(Int(Shour[0]) ?? 9, for: .hour)
            
            
        let minDate = Calendar.current.date(from: dateComponents)
        dateComponents.setValue(24, for: .hour)
        let maxDate = Calendar.current.date(from: dateComponents)

       datePicker.minimumDate = minDate
        datePicker.maximumDate = maxDate
            
            
            
        }
      

            let alert = UIAlertController(title: message, message: "\n\n\n\n\n\n\n\n\n\n\n", preferredStyle: .actionSheet)
            
            datePicker.frame = CGRect(x: 5, y: 20, width: delegate.view.frame.width - 20, height: 220)
            alert.view.addSubview(datePicker)
            
            
            
            let ok = UIAlertAction(title: "Confirm", style: .default) { (action) in
                let dateFormatter = DateFormatter()
              // dateFormatter.dateFormat =  "HH:mm"
                dateFormatter.dateFormat = "hh:mm a"
//
//                let min = dateFormatter.date(from: "9:00")      //createing min time
//                let max = dateFormatter.date(from: "21:00") //creating max time
//
//                print(min)
//
//
//                datePicker.minimumDate = min  //setting min time to picker
//                datePicker.maximumDate = max
                let dateString = dateFormatter.string(from: datePicker.date)
                
                print(dateString)
                handler(dateString)
               
            }
            
            let cancel = UIAlertAction(title: "Cancel", style: .cancel, handler: nil)
            
            alert.addAction(ok)
            alert.addAction(cancel)
            
            delegate.present(alert, animated: true, completion: nil)
            
        }
    func showAlert(message: String, delegate: UIViewController , pop:Bool) {
        
        let alert = UIAlertController(title:AlertView.self.title, message: NSLocalizedString(message, comment: ""), preferredStyle: UIAlertController.Style.alert)
        alert.addAction(UIAlertAction(title: NSLocalizedString("OK", comment: ""), style: UIAlertAction.Style.default, handler: {(alert: UIAlertAction!) in
            if pop == true
            {
                delegate.navigationController?.popViewController(animated: true)

            }
        }))
        delegate.present(alert, animated: true, completion: nil)
    }
    func showAlertWithCompletion(message: String, delegate: UIViewController , pop:Bool,completion:@escaping ()->Void) {
        
        let alert = UIAlertController(title:AlertView.self.title, message: NSLocalizedString(message, comment: ""), preferredStyle: UIAlertController.Style.alert)
        alert.addAction(UIAlertAction(title: NSLocalizedString("OK", comment: ""), style: UIAlertAction.Style.default, handler: {(alert: UIAlertAction!) in
            if pop == true
            {
                delegate.navigationController?.popViewController(animated: true)

            }
            completion()
        }))
        delegate.present(alert, animated: true, completion: nil)
    }

    func showCameraNotAvailableAlert(delegate: UIViewController , pop:Bool) {

        let alert = UIAlertController(title:AlertView.self.title, message: NSLocalizedString("Camera not available on device.", comment: ""), preferredStyle: UIAlertController.Style.alert)
        alert.addAction(UIAlertAction(title: NSLocalizedString("OK", comment: ""), style: UIAlertAction.Style.default, handler: {(alert: UIAlertAction!) in
            if pop == true
            {
                delegate.navigationController?.popViewController(animated: true)

            }
        }))
        delegate.present(alert, animated: true, completion: nil)
    }
    func enterTenDigits(delegate: UIViewController , pop:Bool) {

        let alert = UIAlertController(title:AlertView.self.title, message: NSLocalizedString("Please Enter 10 digits number", comment: ""), preferredStyle: UIAlertController.Style.alert)
        alert.addAction(UIAlertAction(title: NSLocalizedString("OK", comment: ""), style: UIAlertAction.Style.default, handler: {(alert: UIAlertAction!) in
            if pop == true
            {
                delegate.navigationController?.popViewController(animated: true)

            }
        }))
        delegate.present(alert, animated: true, completion: nil)
    }
    func enterPhoneNumber(delegate: UIViewController , pop:Bool) {

        let alert = UIAlertController(title:AlertView.self.title, message: NSLocalizedString("Please Enter Phone Number", comment: ""), preferredStyle: UIAlertController.Style.alert)
        alert.addAction(UIAlertAction(title: NSLocalizedString("OK", comment: ""), style: UIAlertAction.Style.default, handler: {(alert: UIAlertAction!) in
            if pop == true
            {
                delegate.navigationController?.popViewController(animated: true)

            }
        }))
        delegate.present(alert, animated: true, completion: nil)
    }
    func emptyTextFieldAlert(delegate: UIViewController , pop:Bool) {

        let alert = UIAlertController(title:AlertView.self.title, message: NSLocalizedString("Field cannot be empty", comment: ""), preferredStyle: UIAlertController.Style.alert)
        alert.addAction(UIAlertAction(title: NSLocalizedString("OK", comment: ""), style: UIAlertAction.Style.default, handler: {(alert: UIAlertAction!) in
            if pop == true
            {
                delegate.navigationController?.popViewController(animated: true)

            }
        }))
        delegate.present(alert, animated: true, completion: nil)
    }
    func userDoesNotExists(delegate: UIViewController , pop:Bool) {

        let alert = UIAlertController(title:AlertView.self.title, message: NSLocalizedString("User Does Not Exist", comment: ""), preferredStyle: UIAlertController.Style.alert)
        alert.addAction(UIAlertAction(title: NSLocalizedString("Create Account", comment: ""), style: UIAlertAction.Style.default, handler: {(alert: UIAlertAction!) in
            let vc = SignUpVC()
            delegate.view.window?.rootViewController = vc
            delegate.view.window?.makeKeyAndVisible()
            if pop == true
            {
                delegate.navigationController?.popViewController(animated: true)

            }
        }))
        alert.addAction(UIAlertAction(title: NSLocalizedString("Cancel", comment: ""), style: UIAlertAction.Style.destructive, handler: {(alert: UIAlertAction!) in
            if pop == true
            {
                delegate.navigationController?.popViewController(animated: true)

            }
        }))
        delegate.present(alert, animated: true, completion: nil)
    }
    func userAlreadyExists(delegate: UIViewController , pop:Bool) {

        let alert = UIAlertController(title:AlertView.self.title, message: NSLocalizedString("Phone Number Already in use.", comment: ""), preferredStyle: UIAlertController.Style.alert)
        alert.addAction(UIAlertAction(title: NSLocalizedString("OK", comment: ""), style: UIAlertAction.Style.default, handler: {(alert: UIAlertAction!) in
            if pop == true
            {
                delegate.navigationController?.popViewController(animated: true)

            }
        }))
        delegate.present(alert, animated: true, completion: nil)
    }
    func wrongOtpAlert(delegate: UIViewController , pop:Bool) {

        let alert = UIAlertController(title:AlertView.self.title, message: NSLocalizedString("Wrong OTP", comment: ""), preferredStyle: UIAlertController.Style.alert)
        alert.addAction(UIAlertAction(title: NSLocalizedString("OK", comment: ""), style: UIAlertAction.Style.default, handler: {(alert: UIAlertAction!) in
            if pop == true
            {
                delegate.navigationController?.popViewController(animated: true)

            }
        }))
        delegate.present(alert, animated: true, completion: nil)
    }
    func invalidPhoneNumber(delegate: UIViewController , pop:Bool) {

        let alert = UIAlertController(title:AlertView.self.title, message: NSLocalizedString("Wrong OTP", comment: ""), preferredStyle: UIAlertController.Style.alert)
        alert.addAction(UIAlertAction(title: NSLocalizedString("OK", comment: ""), style: UIAlertAction.Style.default, handler: {(alert: UIAlertAction!) in
            if pop == true
            {
                delegate.navigationController?.popViewController(animated: true)

            }
        }))
        delegate.present(alert, animated: true, completion: nil)
    }
    func showAlertShareReferalLink(message: String, delegate: UIViewController,completion:@escaping (ActionType)->Void ) {
        
        let alert = UIAlertController(title:AlertView.self.title, message: NSLocalizedString(message, comment: ""), preferredStyle: UIAlertController.Style.alert)
        alert.addAction(UIAlertAction(title:"Share", style: UIAlertAction.Style.default, handler: {(alert: UIAlertAction!) in
            completion(.yes)
        }))
        alert.addAction(UIAlertAction(title:"No, thanks", style: UIAlertAction.Style.destructive, handler: {(alert: UIAlertAction!) in
            completion(.nothanks)
        }))
        delegate.present(alert, animated: true, completion: nil)
    }
    func showAlert_On_OK_Handler(message: String, delegate: UIViewController,completion:@escaping (ActionType)->Void ) {

        let alert = UIAlertController(title:AlertView.self.title, message: NSLocalizedString(message, comment: ""), preferredStyle: UIAlertController.Style.alert)
        alert.addAction(UIAlertAction(title:"OK", style: UIAlertAction.Style.default, handler: {(alert: UIAlertAction!) in
            completion(.yes)
        }))

        delegate.present(alert, animated: true, completion: nil)
    }
    func showAlertGotoSetting(message: String, delegate: UIViewController,completion:@escaping (ActionType)->Void ) {
        
        let alert = UIAlertController(title:AlertView.self.title, message: NSLocalizedString(message, comment: ""), preferredStyle: UIAlertController.Style.alert)
        alert.addAction(UIAlertAction(title:"Settings", style: UIAlertAction.Style.default, handler: {(alert: UIAlertAction!) in
            completion(.submit)
        }))
        alert.addAction(UIAlertAction(title:"Cancel", style: UIAlertAction.Style.destructive, handler: {(alert: UIAlertAction!) in
            completion(.cancel)
        }))
        delegate.present(alert, animated: true, completion: nil)
    }
    
    func showInvitePop(delegate: UIViewController,completion:@escaping (ActionType)->Void ) {
        
        let alert = UIAlertController(title:AlertView.self.title, message: "Invite others to FOMO and get a free month of Premium if they join Premium.", preferredStyle: UIAlertController.Style.alert)
        alert.addAction(UIAlertAction(title:"Invite", style: UIAlertAction.Style.default, handler: {(alert: UIAlertAction!) in
            completion(.yes)
        }))
        alert.addAction(UIAlertAction(title:"Later", style: UIAlertAction.Style.destructive, handler: {(alert: UIAlertAction!) in
            completion(.nothanks)
        }))
        
        
        getWindow()?.rootViewController?.present(alert, animated: true, completion: nil)
    }
    func alertCameraGallery(msg:String,delegate: UIViewController,completion:@escaping (ActionType)->Void ) {

        let alert = UIAlertController(title:AlertView.self.title, message: msg, preferredStyle: UIAlertController.Style.alert)
        alert.addAction(UIAlertAction(title:"Camera", style: UIAlertAction.Style.default, handler: {(alert: UIAlertAction!) in
            completion(.camera)
        }))
        alert.addAction(UIAlertAction(title:"Photo Library", style: UIAlertAction.Style.default, handler: {(alert: UIAlertAction!) in
            completion(.photo)
        }))
        alert.addAction(UIAlertAction(title:"Cancel", style: UIAlertAction.Style.destructive, handler: {(alert: UIAlertAction!) in
            completion(.nothanks)
        }))
        delegate.present(alert, animated: true, completion: nil)
    }
    func alertShareSheet(msg:String="Share With",delegate: UIViewController,completion:@escaping (ActionType)->Void ) {

        let alert = UIAlertController(title:AlertView.self.title, message: msg, preferredStyle: UIAlertController.Style.actionSheet)
        alert.addAction(UIAlertAction(title:"Friend", style: UIAlertAction.Style.default, handler: {(alert: UIAlertAction!) in
            completion(.friend)
        }))
        alert.addAction(UIAlertAction(title:"Social App", style: UIAlertAction.Style.default, handler: {(alert: UIAlertAction!) in
            completion(.social)
        }))
        alert.addAction(UIAlertAction(title:"Cancel", style: UIAlertAction.Style.destructive, handler: {(alert: UIAlertAction!) in
            completion(.nothanks)
        }))
        delegate.present(alert, animated: true, completion: nil)
    }
    func alertDelete(msg:String="Do you want to delete the Nip?",delegate: UIViewController,completion:@escaping (ActionType)->Void ) {

        let alert = UIAlertController(title:AlertView.self.title, message: msg, preferredStyle: UIAlertController.Style.actionSheet)
        alert.addAction(UIAlertAction(title:"Delete", style: UIAlertAction.Style.destructive, handler: {(alert: UIAlertAction!) in
            completion(.yes)
        }))
        alert.addAction(UIAlertAction(title:"Cancel", style: UIAlertAction.Style.default, handler: {(alert: UIAlertAction!) in
            completion(.cancel)
        }))
        delegate.present(alert, animated: true, completion: nil)
    }
    func alertPurchasedPlan(msg:String,delegate: UIViewController,completion:@escaping (ActionType)->Void ) {

        let alert = UIAlertController(title:AlertView.self.title, message: msg, preferredStyle: UIAlertController.Style.alert)
        alert.addAction(UIAlertAction(title:"OK", style: UIAlertAction.Style.default, handler: {(alert: UIAlertAction!) in
            completion(.yes)
        }))

        delegate.present(alert, animated: true, completion: nil)
    }
    func showComingSoonAlert(delegate: UIViewController) {
        
        let alert = UIAlertController(title: AlertView.self.title, message: NSLocalizedString("Coming Soon", comment: ""), preferredStyle: UIAlertController.Style.alert)
        alert.addAction(UIAlertAction(title: NSLocalizedString("OK", comment: ""), style: UIAlertAction.Style.default, handler: nil))
        delegate.present(alert, animated: true, completion: nil)
    }
    
    
    func showErrorAlert(delegate: UIViewController) {
        
        let alert = UIAlertController(title: AlertView.self.title, message: NSLocalizedString("Something went wrong. Please try again later.", comment: ""), preferredStyle: UIAlertController.Style.alert)
//         let alert = UIAlertController(title: NSLocalizedString("Veteranresurs.se", comment: ""), message: NSLocalizedString("Connection Lost. Please try again later.", comment: ""), preferredStyle: UIAlertControllerStyle.alert)
        alert.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: nil))
        delegate.present(alert, animated: true, completion: nil)
    }
    func showInternetErrorAlert(delegate: UIViewController) {
            
            let alert = UIAlertController(title: AlertView.self.title, message: NSLocalizedString("Connection has been lost.Please check your internet connection.", comment: ""), preferredStyle: UIAlertController.Style.alert)
    //         let alert = UIAlertController(title: NSLocalizedString("Veteranresurs.se", comment: ""), message: NSLocalizedString("Connection Lost. Please try again later.", comment: ""), preferredStyle: UIAlertControllerStyle.alert)
            alert.addAction(UIAlertAction(title: "OK", style: UIAlertAction.Style.default, handler: nil))
        DispatchQueue.main.async {
            delegate.present(alert, animated: true, completion: nil)
        }
        }
    func showRequestErrorAlert(delegate: UIViewController) {
        
        let alert = UIAlertController(title: AlertView.self.title, message: NSLocalizedString("Request_Error", comment: ""), preferredStyle: UIAlertController.Style.alert)
        alert.addAction(UIAlertAction(title: NSLocalizedString("OK", comment: ""), style: UIAlertAction.Style.default, handler: nil))
        delegate.present(alert, animated: true, completion: nil)
    }
    
    func showAlertWithMessage(message: String, buttonNames:[String], delegate: UIViewController) {
        
        let alert = UIAlertController(title:AlertView.self.title, message: message, preferredStyle: UIAlertController.Style.alert)
        
        for btnName in buttonNames {
            
            alert.addAction(UIAlertAction(title: btnName as String, style: UIAlertAction.Style.default, handler: nil))
        }
        
        delegate.present(alert, animated: true, completion: nil)
    }
    func getWindow()->UIWindow?{
        return UIApplication.keyWin
        //AppDelegate.shared?.window
    }
}
//extension UIApplication {
//    static var keyWin: UIWindow? {
//        if #available(iOS 13, *) {
//            return UIApplication.shared.windows.first { $0.isKeyWindow }
//        } else {
//            return UIApplication.shared.keyWindow
//        }
//    }
//}

