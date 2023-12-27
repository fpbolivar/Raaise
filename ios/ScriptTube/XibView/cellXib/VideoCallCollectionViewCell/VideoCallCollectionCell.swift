//
//  VideoCallCollectionCell.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 27/04/23.
//

import UIKit
import VideoSDKRTC
import WebRTC
import AVFoundation
class VideoCallCollectionCell: UICollectionViewCell {
    static var identifier = "VideoCallCollectionCell"
    @IBOutlet weak var videoView: RTCMTLVideoView!
    @IBOutlet weak var nameLbl: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        videoView.contentMode = .scaleAspectFill
        
        videoView.layer.borderWidth = 5
        videoView.layer.borderColor = UIColor.darkGray.cgColor
        videoView.clipsToBounds = true
        videoView.transform = CGAffineTransformMakeScale(-1.0, 1.0)
    }

}
@IBDesignable
class Circle_View:UIView{
    @IBInspectable var shadowColor:UIColor = UIColor.lightGray
    @IBInspectable var shadowOffSetWidth:CGFloat = 0
    @IBInspectable var shadowOffSetHeight:CGFloat = 0
    @IBInspectable var shadowOpacity:Float = 0.2
    @IBInspectable var borderColor:UIColor? = UIColor.lightGray
    @IBInspectable var borderWidth:CGFloat = 0
    @IBInspectable var shadowRadius:CGFloat = 0
    
    @IBInspectable open var circle_View : Bool = false
    override func layoutSubviews() {
        super.layoutSubviews()
        
        if(circle_View){
            self.layer.cornerRadius = self.frame.size.height / 2
            
        }
        
        self.layer.borderColor = borderColor?.cgColor
        self.layer.borderWidth = borderWidth
        self.layer.shadowColor = shadowColor.cgColor
        self.layer.shadowRadius = shadowRadius
        self.layer.shadowOffset = CGSize(width: shadowOffSetWidth, height: shadowOffSetHeight)
        let  shadow_path = UIBezierPath(roundedRect: bounds, cornerRadius: self.frame.size.height / 2)
        self.layer.shadowPath = shadow_path.cgPath
        self.layer.shadowOpacity = shadowOpacity
        
//        self.layer.cornerRadius = self.frame.size.height / 2
//        self.layer.shadowColor = shadowColor.cgColor
//        self.layer.shadowRadius = shadowRadius
//        self.layer.shadowOffset = CGSize(width: shadowOffSetWidth, height: shadowOffSetHeight)
        
    }
    
    
    
    
    
}
