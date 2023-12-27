//
//  VideoCallCell.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 26/04/23.
//

import UIKit
import VideoSDKRTC
import WebRTC
import AVFoundation
class VideoCallCell: UITableViewCell {
    static var identifier = "VideoCallCell"
    @IBOutlet weak var videoView: RTCMTLVideoView!
    @IBOutlet weak var nameLbl: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
        videoView.contentMode = .scaleAspectFit
        videoView.layer.cornerRadius = 10
        videoView.layer.borderWidth = 5
        videoView.layer.borderColor = UIColor.darkGray.cgColor
        videoView.clipsToBounds = true
        videoView.transform = CGAffineTransformMakeScale(-1.0, 1.0)
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}
