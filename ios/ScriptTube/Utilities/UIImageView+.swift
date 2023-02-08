//
//  UIImageView+.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 25/11/22.
//

import Foundation
import UIKit
//MARK: - Cache Images
extension SDImageFormat {
    public static let BPG: SDImageFormat = SDImageFormat(rawValue: 11)
}
extension UIImageView{

    func loadImg(url:String){
        let  url1 = UserDefaultHelper.getBaseUrl() + url
        guard let imageUrL = URL(string: url1) else {
            self.image =  UIImage(named:"placeholder")!
            return
        }
        self.sd_setImage(with: imageUrL, placeholderImage: UIImage(named:"placeholder")!, options: [.highPriority], progress: nil) { image, error, s, sf in
        }
    }
    func loadImgForProfile(url:String){
        let  url1 = UserDefaultHelper.getBaseUrl() + url
        guard let imageUrL = URL(string: url1) else {
            self.image =  UIImage(named:"profile_placeholder")!
            return
        }
        self.sd_setImage(with: imageUrL, placeholderImage: UIImage(named:"profile_placeholder")!, options: [.highPriority], progress: nil) { image, error, s, sf in
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
