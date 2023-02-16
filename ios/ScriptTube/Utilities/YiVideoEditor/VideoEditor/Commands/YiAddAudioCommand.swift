
import Foundation
import AVFoundation

class YiAddAudioCommand: NSObject, YiVideoEditorCommandProtocol {
    weak var videoData: YiVideoEditorData?
    var audioAsset: AVAsset
    var startingAt: CGFloat
    var trackDuration: CGFloat
    init(videoData: YiVideoEditorData, audioAsset: AVAsset, startingAt: CGFloat?, trackDuration: CGFloat?) {
        self.videoData = videoData
        self.audioAsset = audioAsset
        self.startingAt = startingAt ?? 0
        self.trackDuration = trackDuration ?? CGFloat.greatestFiniteMagnitude
        super.init()
    }

    func execute() {
//        if CMTimeGetSeconds(audioAsset.duration) < CMTimeGetSeconds(videoData?.asset.duration ?? CMTime.zero) {
//            print("AUDIOASSETLESSAUDIOASSETLESS")
//            let mixComposition = AVMutableComposition()
//
//                let videoDuration = videoData?.asset.duration
//                let audioDuration = audioAsset.duration
//
//                let videoTrack = mixComposition.addMutableTrack(withMediaType: .video, preferredTrackID: kCMPersistentTrackID_Invalid)
//                let audioTrack = mixComposition.addMutableTrack(withMediaType: .audio, preferredTrackID: kCMPersistentTrackID_Invalid)
//
//                do {
//                    try videoTrack?.insertTimeRange(CMTimeRangeMake(start: CMTime.zero, duration: videoDuration!), of: (videoData?.asset.tracks(withMediaType: .video)[0])!, at: CMTime.zero)
//
//                    let loops = Int(ceil(CMTimeGetSeconds(videoDuration!) / CMTimeGetSeconds(audioDuration)))
//                    let audioDurationPerLoop = audioDuration
//                    var audioDurationAdded: CMTime = CMTime.zero
//                    for i in 0..<loops {
//                        let startTime = CMTimeAdd(audioDurationAdded, CMTime.zero)
//                        let audioDurationRemaining = CMTimeSubtract(videoDuration!, audioDurationAdded)
//                        let audioDurationToBeAdded = audioDurationRemaining > audioDuration ? audioDuration : audioDurationRemaining
//                        try audioTrack?.insertTimeRange(CMTimeRangeMake(start: CMTime.zero, duration: audioDurationToBeAdded), of: audioAsset.tracks(withMediaType: .audio)[0], at: startTime)
//                        audioDurationAdded = CMTimeAdd(audioDurationAdded, audioDurationToBeAdded)
//                    }

//                    let exporter = AVAssetExportSession(asset: mixComposition, presetName: AVAssetExportPresetHighestQuality)
//                    let tempDirectoryURL = URL(fileURLWithPath: NSTemporaryDirectory(), isDirectory: true)
//                    let outputURL = tempDirectoryURL.appendingPathComponent("mergedVideoWithAudio.mp4")
//                    try? FileManager.default.removeItem(at: outputURL)
//
//                    exporter?.outputURL = outputURL
//                    exporter?.outputFileType = .mp4
//                    exporter?.exportAsynchronously(completionHandler: {
//                        if exporter?.status == .completed {
//                            print("Exported merged video with audio to: \(outputURL)")
//                        } else {
//                            print("Failed to export merged video with audio: \(exporter?.error.debugDescription ?? "unknown error")")
//                        }
//                    })

                    //return outputURL

//                } catch {
//                    print("Failed to merge video with audio: \(error.localizedDescription)")
//                    //return nil
//                }
//        }else{
            print("AUDIOASSETMORE")
            var track: AVAssetTrack?

            if audioAsset.tracks(withMediaType: .audio).count != 0 {
                track = audioAsset.tracks(withMediaType: .audio).last
            } else {
                return
            }
            let audioCompositionTrack = videoData?.composition?.addMutableTrack(withMediaType: .audio, preferredTrackID: kCMPersistentTrackID_Invalid)
            let videoDuration = videoData?.videoCompositionTrack?.timeRange.duration
            let startTime = CMTime(seconds: Double(startingAt), preferredTimescale: videoDuration?.timescale ?? CMTimeScale(0.0))
            let trackDurationTime = CMTime(seconds: Double(trackDuration), preferredTimescale: videoDuration?.timescale ?? CMTimeScale(0.0))
            if CMTimeCompare(videoDuration ?? CMTime.zero, startTime) == -1 {
                return
            }
            
            let availableTrackDuration = CMTimeSubtract(videoDuration ?? CMTime.zero, CMTime(seconds: Double(startingAt), preferredTimescale: videoDuration?.timescale ?? CMTimeScale(0.0)))
            var duration: CMTime?
            if CMTimeCompare(availableTrackDuration, track?.timeRange.duration ?? CMTime.zero) == -1 {
                duration = availableTrackDuration
            } else {
                duration = track?.timeRange.duration
            }
            
            if CMTimeCompare(trackDurationTime, duration ?? CMTime.zero) == -1 {
                duration = trackDurationTime
            }
            let timeRange = CMTimeRange(start: CMTime.zero, duration: duration ?? CMTime.zero)
            do {
                if let track = track {
                    try audioCompositionTrack?.insertTimeRange(timeRange, of: track, at: startTime)
                }
            } catch {
            
            }
        //}
    }
   

}
