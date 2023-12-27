//
//  BottomVideoVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 27/04/23.
//

import UIKit
import VideoSDKRTC
import WebRTC
import AVFoundation
class BottomVideoVC: MeetingVC {
    @IBOutlet weak var videoView: RTCMTLVideoView!
    override func viewDidLoad() {
        
           
        
    }
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        if let currentVideoTrack = self.participants[0].streams.first(where: {$1.kind == .state(value: .video)})?.value.track as? RTCVideoTrack {
            currentVideoTrack.add(videoView)
        }
    }
    
}
