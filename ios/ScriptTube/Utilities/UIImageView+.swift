//
//  UIImageView+.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 25/11/22.
//

import Foundation
import UIKit
extension SDImageFormat {
    public static let BPG: SDImageFormat = SDImageFormat(rawValue: 11)
}
extension UIImageView{

    func loadImg(url:String){
//       let  url1 = URLHelper.BASE_URL + URLHelper.SEGMENTCLIENT + url
        guard let imageUrL = URL(string: url) else {
            self.image =  UIImage(named:"placeholder")!
            return
        }
        self.sd_setImage(with: imageUrL, placeholderImage: UIImage(named:"placeholder")!, options: [.highPriority], progress: nil) { image, error, s, sf in
            print(image)
        }
    }
    func loadImgForProfile(url:String){
//       let  url1 = URLHelper.BASE_URL + URLHelper.SEGMENTCLIENT + url
        guard let imageUrL = URL(string: url) else {
            self.image =  UIImage(named:"profile_placeholder")!
            return
        }
        self.sd_setImage(with: imageUrL, placeholderImage: UIImage(named:"profile_placeholder")!, options: [.highPriority], progress: nil) { image, error, s, sf in
            print(image)
        }
    }
    func downloadloadImg(url:String,imageCompletion:@escaping(UIImage?)->Void){

        guard let imageUrL = URL(string: url) else {
            self.image =  UIImage(named:"placeholder")!
            imageCompletion(nil)
            return
        }
        self.sd_setImage(with: imageUrL, placeholderImage: UIImage(named:"placeholder")!, options: [.highPriority], progress: nil) { image, error, _, _ in

            if let image = image{
                imageCompletion(image)
            }else{
                imageCompletion(nil)
            }
        }
    }
}
