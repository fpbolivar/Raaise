//
//  SwiftNotice.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 25/11/22.
//

import Foundation
import UIKit

extension UIViewController {

    func notice(_ text: String, type: NoticeType, autoClear: Bool = true){
        SwiftNotice.showNoticeWithText(type, text: text, autoClear: autoClear)
    }

    func pleaseWait() {
        SwiftNotice.wait()
        self.view.isUserInteractionEnabled = false

        NotificationCenter.default.addObserver(self, selector: #selector(self.rotatedForLoader), name: UIDevice.orientationDidChangeNotification, object: nil)

        //  UIDevice.orientationDidChangeNotification
    }

    func noticeOnlyText(_ text: String) {
        SwiftNotice.showText(text)
    }
    //    @objc func clearAllNotice2() {
    //        DispatchQueue.main.async(execute: {
    //            self.view.isUserInteractionEnabled = true
    //            SwiftNotice.clear()
    //
    //        })
    //
    //    }
    func clearAllNotice() {
        DispatchQueue.main.async(execute: {
            self.view.isUserInteractionEnabled = true
            SwiftNotice.clear()

        })

    }

    @objc func rotatedForLoader(){
        SwiftNotice.updateView()
        self.setNeedsStatusBarAppearanceUpdate()

    }

}

enum NoticeType{
    case success
    case error
    case info
}
extension UIWindow{
    static var keyWin: UIWindow {
        if #available(iOSApplicationExtension 13.0, *) {
            return (SceneDelegate.shared?.window!)!
        } else {
            return UIApplication.shared.keyWindow!
            // Fallback on earlier versions
        }
    }
}

class SwiftNotice: NSObject {

    static var mainViews = Array<UIView>()
    static let rv =   UIWindow.keyWin//(UIApplication.shared.keyWindow?.subviews.first)! as UIView

    static func clear() {
        for i in mainViews {
            i.removeFromSuperview()
        }
    }
    static func updateView(){
        for i in mainViews {
            i.center = rv.center
        }

    }
    static func wait() {


        let mainView = UIView(frame: CGRect(x: 0, y: 0, width: UIScreen.main.bounds.width , height:  UIScreen.main.bounds.height))
        //UIView(frame: CGRect(x: 0, y: 0, width: 6000 , height: 6000))

        mainView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha: 0.4)


        var myView = UIView()
        var lbl = UILabel()
        myView.frame.size = CGSize(width: 200, height: 100)



        let ai = UIActivityIndicatorView()
        ai.frame = CGRect(x: 0, y:0, width: 36, height: 36)
        ai.color = UIColor.white
        ai.startAnimating()
        ai.center = myView.center
        myView.addSubview(ai)
        lbl.text = "Loading"
        lbl.textAlignment = .center
        lbl.textColor = .white
        lbl.frame=CGRect(x: 0, y:ai.frame.maxY, width: 200, height: 20)
        myView.addSubview(lbl)

        myView.center = mainView.center
        mainView.addSubview(myView)

        //   ai.center = mainView.center




        // mainView.addSubview(ai)

        mainView.center = rv.center
        rv.addSubview(mainView)

        mainViews.append(mainView)
        //

    }
    @objc func clean (){



    }
    static func showText(_ text: String) {
        let frame = CGRect(x: 0, y: 0, width: 200, height: 60)
        let mainView = UIView(frame: frame)
        mainView.layer.cornerRadius = 12
        mainView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha: 0.8)

        let label = UILabel(frame: frame)
        label.text = text
        label.font = UIFont.systemFont(ofSize: 13)
        label.textAlignment = NSTextAlignment.center
        label.textColor = UIColor.white
        mainView.addSubview(label)

        mainView.center = rv.center
        rv.addSubview(mainView)

        mainViews.append(mainView)
    }

    static func showNoticeWithText(_ type: NoticeType,text: String, autoClear: Bool) {

        let mainView = UIView(frame: CGRect(x: 0, y: 0, width: 90, height: 90))
        mainView.layer.cornerRadius = 10
        mainView.backgroundColor = UIColor(red:0, green:0, blue:0, alpha: 0.7)

        var image = UIImage()
        switch type {
            case .success:
                image = SwiftNoticeSDK.imageOfCheckmark
                break
            case .error:
                image = SwiftNoticeSDK.imageOfCross
                break
            case .info:
                image = SwiftNoticeSDK.imageOfInfo
                break
        }
        let checkmarkView = UIImageView(image: image)
        checkmarkView.frame = CGRect(x: 27, y: 15, width: 36, height: 36)
        mainView.addSubview(checkmarkView)

        let label = UILabel(frame: CGRect(x: 0, y: 60, width: 90, height: 16))
        label.font = UIFont.systemFont(ofSize: 13)
        label.textColor = UIColor.white
        label.text = text
        label.textAlignment = NSTextAlignment.center
        mainView.addSubview(label)

        mainView.center = rv.center
        rv.addSubview(mainView)

        mainViews.append(mainView)

        if autoClear {
            let selector = #selector(SwiftNotice.hideNotice(_:))
            self.perform(selector, with: mainView, afterDelay: 3)
        }
    }

    @objc static func hideNotice(_ sender: AnyObject) {
        if sender is UIView {
            sender.removeFromSuperview()
        }
    }
}

class SwiftNoticeSDK {
    struct Cache {
        static var imageOfCheckmark: UIImage?
        static var imageOfCross: UIImage?
        static var imageOfInfo: UIImage?
    }
    class func draw(_ type: NoticeType) {
        let checkmarkShapePath = UIBezierPath()

        // draw circle
        checkmarkShapePath.move(to: CGPoint(x: 36, y: 18))
        checkmarkShapePath.addArc(withCenter: CGPoint(x: 18, y: 18), radius: 17.5, startAngle: 0, endAngle: CGFloat(M_PI*2), clockwise: true)
        checkmarkShapePath.close()

        switch type {
            case .success: // draw checkmark
                checkmarkShapePath.move(to: CGPoint(x: 10, y: 18))
                checkmarkShapePath.addLine(to: CGPoint(x: 16, y: 24))
                checkmarkShapePath.addLine(to: CGPoint(x: 27, y: 13))
                checkmarkShapePath.move(to: CGPoint(x: 10, y: 18))
                checkmarkShapePath.close()
                break
            case .error: // draw X
                checkmarkShapePath.move(to: CGPoint(x: 10, y: 10))
                checkmarkShapePath.addLine(to: CGPoint(x: 26, y: 26))
                checkmarkShapePath.move(to: CGPoint(x: 10, y: 26))
                checkmarkShapePath.addLine(to: CGPoint(x: 26, y: 10))
                checkmarkShapePath.move(to: CGPoint(x: 10, y: 10))
                checkmarkShapePath.close()
                break
            case .info:
                checkmarkShapePath.move(to: CGPoint(x: 18, y: 6))
                checkmarkShapePath.addLine(to: CGPoint(x: 18, y: 22))
                checkmarkShapePath.move(to: CGPoint(x: 18, y: 6))
                checkmarkShapePath.close()

                UIColor.white.setStroke()
                checkmarkShapePath.stroke()

                let checkmarkShapePath = UIBezierPath()
                checkmarkShapePath.move(to: CGPoint(x: 18, y: 27))
                checkmarkShapePath.addArc(withCenter: CGPoint(x: 18, y: 27), radius: 1, startAngle: 0, endAngle: CGFloat(M_PI*2), clockwise: true)
                checkmarkShapePath.close()

                UIColor.white.setFill()
                checkmarkShapePath.fill()
                break

                //        default:
                //            break
        }

        UIColor.white.setStroke()
        checkmarkShapePath.stroke()
    }
    class var imageOfCheckmark: UIImage {
        if (Cache.imageOfCheckmark != nil) {
            return Cache.imageOfCheckmark!
        }
        UIGraphicsBeginImageContextWithOptions(CGSize(width: 36, height: 36), false, 0)

        SwiftNoticeSDK.draw(NoticeType.success)

        Cache.imageOfCheckmark = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return Cache.imageOfCheckmark!
    }
    class var imageOfCross: UIImage {
        if (Cache.imageOfCross != nil) {
            return Cache.imageOfCross!
        }
        UIGraphicsBeginImageContextWithOptions(CGSize(width: 36, height: 36), false, 0)

        SwiftNoticeSDK.draw(NoticeType.error)

        Cache.imageOfCross = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return Cache.imageOfCross!
    }
    class var imageOfInfo: UIImage {
        if (Cache.imageOfInfo != nil) {
            return Cache.imageOfInfo!
        }
        UIGraphicsBeginImageContextWithOptions(CGSize(width: 36, height: 36), false, 0)

        SwiftNoticeSDK.draw(NoticeType.info)

        Cache.imageOfInfo = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return Cache.imageOfInfo!
    }



}


