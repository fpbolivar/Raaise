//
//  GlobalManager.swift
//  ScriptTube
//
//  Created by CODE OPTIMAL SOLUTIONS on 10/04/24.
//

import Foundation
class GlobalManager{
    ///***Functions
    
    //MARK: SHARE VIDEO BY OPENING SHEET
    static func shareVideoByOpeningSheet(link:String){
        if let vc = UIApplication.topViewController(){
            vc.dismiss(animated: true)
            DispatchQueue.main.async {
                vc.pleaseWait()
            }
            let vid  = AudioVideoMerger()
            vid.downloadVideoToCameraRoll(videoUrl: link) { url in
                //MARK: - Insert App Url from App Store
                if let name = URL(string: LocalStrings.APP_URL), !name.absoluteString.isEmpty {
                    let objectsToShare = [url]
                    let activityVC = UIActivityViewController(activityItems: objectsToShare, applicationActivities: nil)
                    DispatchQueue.main.async {
                        vc.clearAllNotice()
                        vc.present(activityVC, animated: true, completion: nil)
                    }
                    
                } else {
                    vc.clearAllNotice()
                    AlertView().showAlert(message: "ERROR in sharing", delegate: vc, pop: false)
                }
            }
        }
    }
    
}
