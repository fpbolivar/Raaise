//
//  PlayerView.swift
//  ScripTube
//
//  Created by Dilpreet Singh on 12/6/22.
//  Copyright © 2022 Code Optimal Solutions Pvt Ltd. All rights reserved.
//

import Foundation
import AVFoundation
//"http://techslides.com/demos/sample-videos/small.mp4"
class PlayerView{
    //let cach =
    static func getPlayer(frame:CGRect,url:String = "")->AVPlayerLayer{
        var player: AVPlayer!

        let url = URL(string:url)!
        let playerItem = CachingPlayerItem(url: url)

        player = AVPlayer(playerItem: playerItem)

        player.automaticallyWaitsToMinimizeStalling = false
        player.play()
        player.volume = 1.0
        let playerLayer = AVPlayerLayer(player:player)
        playerLayer.frame = frame
        playerLayer.videoGravity = .resizeAspectFill
        NotificationCenter.default.addObserver(forName: NSNotification.Name.AVPlayerItemDidPlayToEndTime, object: nil, queue: nil) { notification in
            player.seek(to: CMTime.zero)
            player.play()
        }
        return playerLayer
    }
    

}

