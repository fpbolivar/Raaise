//
//  AudioVideoMerger.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 01/12/22.
//

import UIKit
import AVFoundation
import AVKit
import AssetsLibrary
import Photos

class AudioVideoMerger{
    private var observation : NSKeyValueObservation?
    var onprogress: ((Double)->Void)!

    func downloadVideoToCameraRoll(videoUrl:String,completion:@escaping(URL)->Void){
        if let audioUrl = URL(string: videoUrl) {
            DispatchQueue.global(qos: .background).async {
                if let url = URL(string: videoUrl),
                    let urlData = NSData(contentsOf: url) {
                    let documentsPath = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)[0];
                    let filePath="\(documentsPath)/\(Date().millisecondsSince1970).mp4"
                    DispatchQueue.main.async {
                        urlData.write(toFile: filePath, atomically: true)
                        self.addWatermarkOnly(videoURL: URL(fileURLWithPath: filePath)) { output in
                            completion(output)
                        }
                       // completion(URL(fileURLWithPath: filePath))
//                        PHPhotoLibrary.shared().performChanges({
//                            PHAssetChangeRequest.creationRequestForAssetFromVideo(atFileURL: URL(fileURLWithPath: filePath))
//                        }) { completed, error in
//                            if completed {
//                                print("Video is saved!")
//                                completion(URL(fileURLWithPath: filePath))
//                            }
//                        }
                    }
                }
            }
            
            
//            let manager = Session.default
//            let destination: DownloadRequest.Destination = { a,c   in
//
//                       var destinationPath = FileManager.default.urls(for:.documentDirectory, in: .userDomainMask)[0]
//
//                var url = destinationPath.appendingPathComponent("ScripTube/Videos")
//                url.appendPathComponent("\(Date().millisecondsSince1970)")
//
//                        return (url, [.removePreviousFile,.createIntermediateDirectories])
//                    }
//            manager.session.configuration.timeoutIntervalForRequest = 30
//            manager.download(audioUrl,to: destination).downloadProgress { progress in
//                print(progress.fractionCompleted)
//            }.response { response in
//                completion(response.fileURL!)
//                print("ALOO",response.response)
//                print("ALOO1",response.fileURL)
//                print("ALOO2",response)
//            }
        }
    }
    func downloadAudio(audioUrl:String,completion:@escaping(URL)->Void){
        if let audioUrl = URL(string: audioUrl) {
            let documentsDirectoryURL =  FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first!
            
            // lets create your destination file url
            let destinationUrl = documentsDirectoryURL.appendingPathComponent(audioUrl.lastPathComponent)
            print(destinationUrl)
            
            // to check if it exists before downloading it
            if FileManager.default.fileExists(atPath: destinationUrl.path) {
                print("The file already exists at path")
                completion(destinationUrl)
                

                
                
                // if the file doesn't exist
            } else {

                let manager = Session.default
                let destination = DownloadRequest.suggestedDownloadDestination()
                manager.session.configuration.timeoutIntervalForRequest = 30
                manager.download(audioUrl, to:destination).downloadProgress { progress in
                    print("Progress",progress.fractionCompleted)
                    self.onprogress(progress.fractionCompleted)
                }.response { downloadUrl in
                    print("audiodonwloadurl",downloadUrl)
                    completion(downloadUrl.fileURL!)
                }
            }
        }
    }
    deinit{
        observation?.invalidate()
    }

    func addWatermarkOnly(videoURL: URL,completion:@escaping(URL)->Void){
        let date = Date().millisecondsSince1970
        let filePath:String = NSHomeDirectory() + "/Documents/output.mp4"
        let exportUrl = URL(fileURLWithPath: filePath)
        let layer = self.createVideoLayer()
        let videoEditor = YiVideoEditor(videoURL: videoURL)
        videoEditor.rotate(rotateDegree: .rotateDegree90)
        videoEditor.addLayer(layer: layer)
        videoEditor.export(exportURL: exportUrl) { [weak self] (session) in
            print("donenne",session.outputFileType)
            if session.status == .completed {
//                print("donenne",session.outputURL?.appendingPathExtension("mp4"))
//                do{
//                    let ans = try session.outputURL?.resourceValues(forKeys:[.fileSizeKey]).fileSize
//                    print("VIDEOSIZE",ans)
//                    let bcf = ByteCountFormatter()
//                         bcf.allowedUnits = [.useMB] // optional: restricts the units to MB only
//                         bcf.countStyle = .file
//                    let string = bcf.string(fromByteCount: Int64(ans!))
//                    print("VIDEOSIZE",string)
//                }catch{
//
//                }
                
                completion(session.outputURL!)

            }
        }
    }
    func editVideo(videoURL: URL,audioUrl:URL,pickedVideo:Bool = false,completion:@escaping(URL)->Void) -> Void {
        let date = Date().millisecondsSince1970
        let filePath:String = NSHomeDirectory() + "/Documents/output.mp4"
        let exportUrl = URL(fileURLWithPath: filePath)
       
        let audioUrl = audioUrl
        
//        guard let audioUrl = Bundle.main.url(forResource: "applause-01", withExtension: "mp3") else {
//            return
//        }
        removeAudioFromVideo(videoURL.path){ url in
            let audioAsset = AVURLAsset(url: audioUrl)
            let videoAsset = AVURLAsset(url: videoURL)
            let VideoTime = videoAsset.duration
            let audioTime = audioAsset.duration
            let vdtime = CGFloat(CMTimeGetSeconds(VideoTime))
            let adtime = CGFloat(CMTimeGetSeconds(audioTime))
            let layer = self.createVideoLayer(forWatermark: false)
            let videoEditor = YiVideoEditor(videoURL: url!)
            videoEditor.rotate(rotateDegree: .rotateDegree90)
            //pickedVideo ? videoEditor.rotate(rotateDegree: .rotateDegree360) : videoEditor.rotate(rotateDegree: .rotateDegree90)
            
            print("VIDEOURLAFTERAUDIOREMOVE",url)
            
                 
                //   videoEditor.crop(cropFrame: CGRect(x: 10, y: 10, width: 300, height: 200))
            videoEditor.addLayer(layer: layer)
            videoEditor.addAudio(asset: audioAsset, startingAt: 0, trackDuration: vdtime)
            print("VIDEOSIZE2",videoEditor.videoData.videoComposition?.renderSize.height,videoEditor.videoData.videoComposition?.renderSize.width)
            videoEditor.export(exportURL: exportUrl) { [weak self] (session) in
//                guard let `self` = self else {
//                    return
//                }
                print("donenne",session.outputFileType)
                if session.status == .completed {
                    //print("donenne",session.outputURL?.appendingPathExtension("mp4"))
                    do{
                        let ans = try session.outputURL?.resourceValues(forKeys:[.fileSizeKey]).fileSize
                        print("VIDEOSIZE",ans)
                        let bcf = ByteCountFormatter()
                             bcf.allowedUnits = [.useMB] // optional: restricts the units to MB only
                             bcf.countStyle = .file
                        let string = bcf.string(fromByteCount: Int64(ans!))
                        print("VIDEOSIZE",string)
                    }catch{
                        
                    }
                    
                    completion(session.outputURL!)

                }
            }
        }
    }
    func createVideoLayer(forWatermark:Bool = true) -> CALayer {
        let layer = CALayer()
        if forWatermark{
            let myImage = UIImage(named: "watermark")?.cgImage
            layer.contents = myImage
        }
        layer.backgroundColor = UIColor.clear.cgColor
        layer.frame = CGRect(x: 10, y: 10, width: 100, height: 50)
        return layer
    }
    func removeAudioFromVideo(_ videoPath: String,completion: @escaping (URL?)->Void){
        let initPath1: String = videoPath
        let composition = AVMutableComposition()
        let inputVideoPath: String = initPath1
        let sourceAsset = AVURLAsset(url: URL(fileURLWithPath: inputVideoPath), options: nil)
        let compositionVideoTrack: AVMutableCompositionTrack? = composition.addMutableTrack(withMediaType: AVMediaType.video, preferredTrackID: kCMPersistentTrackID_Invalid)
        let sourceVideoTrack: AVAssetTrack? = sourceAsset.tracks(withMediaType: AVMediaType.video)[0]
        let x: CMTimeRange = CMTimeRangeMake(start: CMTime.zero, duration: sourceAsset.duration)
        _ = try? compositionVideoTrack!.insertTimeRange(x, of: sourceVideoTrack!, at: CMTime.zero)
        if FileManager.default.fileExists(atPath: initPath1) {
            try? FileManager.default.removeItem(atPath: initPath1)
        }
        let url = URL(fileURLWithPath: initPath1)
        let exporter = AVAssetExportSession(asset: composition, presetName: AVAssetExportPresetHighestQuality)
        exporter?.outputURL = url
        exporter?.outputFileType = .mp4
//        completion(exporter!.outputURL!)
        exporter?.exportAsynchronously(completionHandler: {() -> Void in
            completion(exporter!.outputURL!)
        })
    }
}
extension Date {
    var millisecondsSince1970:Int64 {
        Int64((self.timeIntervalSince1970 * 1000.0).rounded())
    }
    
    init(milliseconds:Int64) {
        self = Date(timeIntervalSince1970: TimeInterval(milliseconds) / 1000)
    }
}
