//
//  AudioDataModel.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 27/11/22.
//
//MARK: - Data Model for Single Audio
import Foundation
class AudioDataModel{
    var id = ""
    var slug = ""
    var songName = ""
    var artistName = ""
    var thumbnail = ""
    var audioUrl = ""
    var audioTime = ""
    var genre = ""
    
    init(){
        
    }
    init(data:JSON){
        self.id = data[AudioKeys.id.rawValue].stringValue
        self.slug = data[AudioKeys.slug.rawValue].stringValue
        self.songName = data[AudioKeys.songName.rawValue].stringValue
        self.artistName = data[AudioKeys.artistName.rawValue].stringValue
        self.thumbnail = data[AudioKeys.thumbnail.rawValue].stringValue
        self.audioUrl = data[AudioKeys.audioUrl.rawValue].stringValue
        self.audioTime = data[AudioKeys.audioTime.rawValue].stringValue
        self.genre = data[AudioKeys.genre.rawValue]["name"].stringValue
    }
}
enum AudioKeys:String{
    case id = "_id"
    case slug = "slug"
    case songName = "songName"
    case artistName = "artistName"
    case thumbnail = "Thumbnail"
    case audioUrl = "audio"
    case audioTime = "audioTime"
    case genre = "genreId"
}
