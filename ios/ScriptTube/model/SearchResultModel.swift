//
//  SearchResultModel.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 23/12/22.
//
//MARK: - Data Model for Search Result
import Foundation
class SearchResultModel{
    var audioResult:[AudioDataModel] = []
    var postResult:[Post] = []
    var userResult:[UserProfileData] = []
    
    init(){
        
    }
    init(data:JSON){
        
        data["audios"].forEach { (message,json) in
            self.audioResult.append(AudioDataModel(data: json))
        }
        data["posts"].forEach { (message,json) in
            self.postResult.append(Post(data: json))
        }
        data["users"].forEach { (message,json) in
            self.userResult.append(UserProfileData(data: json))
        }
    }
    func resultIsEmpty()->Bool{
        return audioResult.isEmpty && postResult.isEmpty && userResult.isEmpty
    }
}
enum ResultType{
    case audio
    case users
    case post
}
