//
//  SearchWithListVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 23/11/22.
//

import UIKit
import AVFoundation

class SearchWithListVC: BaseControllerVC {

    var selectedIndex: IndexPath? = nil
    @IBOutlet weak var searchTf: UITextField!
    @IBOutlet weak var tableView: UITableView!
    var isForSelectAudio: Bool = false
    var audioListData: [AudioDataModel]!
    var player: AVPlayer?
    var selectedAudioUrl:((URL,String,String)->Void)? = nil
    var isPlaying:Bool = false
    var videoUrl: URL!
    let vid = AudioVideoMerger()
   
    override func viewDidLoad() {
        super.viewDidLoad()
        hideNavbar()
        searchTf.layer.cornerRadius = 10
        searchTf.overrideUserInterfaceStyle = .light
        setup()
        if isForSelectAudio{
            getAudioListApi(){
                DispatchQueue.main.async {
                    self.setupTableView()
                }
            }
        }
        //if isForSelectAudio{
            addNavBar(headingText:"Select Audio",redText:"Audio")
//        }else{
//            addNavBar(headingText: "Followers List", redText: "List")
//        }
        // Do any additional setup after loading the view.
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.tabBarController?.tabBar.isHidden = true
    }
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        player?.pause()
    }
    func setup(){
        searchTf.paddingLeftRightTextField(left: 35, right: 0)
        
        //if isForSelectAudio{
            searchTf.attributedPlaceholder = NSAttributedString(string: "Search Music",attributes: [.foregroundColor: UIColor.lightGray])
            //searchTf.placeholder = "Search Music"
//        }else{
//            searchTf.attributedPlaceholder = NSAttributedString(string: "Search Users",attributes: [.foregroundColor: UIColor.lightGray])
//            //searchTf.placeholder = "Search Users"
//        }
    }
    func playSound(withUrl url:String){
        
        if isPlaying{
            player?.pause()
        }else{
            guard let url = URL(string: url) else { return }
            let playerItem = CachingPlayerItem(url: url)
            player = AVPlayer(playerItem: playerItem)
            player?.automaticallyWaitsToMinimizeStalling = false
            player?.play()
        }
        isPlaying = !isPlaying
       // player = AVAudioPlayer(pla)
        
//            do {
//                try AVAudioSession.sharedInstance().setCategory(.playback, mode: .default)
//                try AVAudioSession.sharedInstance().setActive(true)
//
//                /* The following line is required for the player to work on iOS 11. Change the file type accordingly*/
//                player = try AVAudioPlayer(contentsOf: url, fileTypeHint: AVFileType.mp3.rawValue)
//
//                /* iOS 10 and earlier require the following line:
//                player = try AVAudioPlayer(contentsOf: url, fileTypeHint: AVFileTypeMPEGLayer3) */
//
//                guard let player = player else { return }
//
//                player.play()
//
//            } catch let error {
//                print(error.localizedDescription)
//            }
    }
    func getAudioListApi(onCompletion:@escaping()->Void){
        DataManager.getAudioList(delegate: self, param: ["userId":AuthManager.currentUser.id]) { data in
            self.audioListData = []
            data["data"].forEach { (string,json) in
                self.audioListData.append(AudioDataModel(data: json))
                print("AUDIOLIST11",json)
            }
            onCompletion()
        }
    }
    func setupTableView(){
        tableView.register(UINib(nibName: SelectionCell.identifier, bundle: nil), forCellReuseIdentifier: SelectionCell.identifier)
        tableView.delegate = self
        tableView.dataSource = self
        tableView.reloadData()
    }
}

extension SearchWithListVC: UITableViewDelegate, UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        //if isForSelectAudio{
            return audioListData.count
//        }else{
//            return 10
//        }
        
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell =  tableView.dequeueReusableCell(withIdentifier: SelectionCell.identifier, for: indexPath) as! SelectionCell
        
        cell.selectionStyle = .none
   //     if isForSelectAudio{
            cell.playAudio = { url in
                if self.selectedIndex == nil{
                    self.selectedIndex = indexPath
                    self.playSound(withUrl: url)
                    if self.isPlaying{
                        cell.playBtn.setImage(UIImage(systemName: "pause.fill"), for: .normal)
                    }else{
                        cell.playBtn.setImage(UIImage(systemName: "play.fill"), for: .normal)
                    }
                }else if self.selectedIndex == indexPath{
                    self.playSound(withUrl: url)
                    if self.isPlaying{
                        cell.playBtn.setImage(UIImage(systemName: "pause.fill"), for: .normal)
                    }else{
                        cell.playBtn.setImage(UIImage(systemName: "play.fill"), for: .normal)
                    }
                    self.selectedIndex = nil
                }else{
                    let cell2 = tableView.cellForRow(at: self.selectedIndex!) as! SelectionCell
                    self.isPlaying = !self.isPlaying
                    if self.isPlaying{
                        cell2.playBtn.setImage(UIImage(systemName: "pause.fill"), for: .normal)
                    }else{
                        cell2.playBtn.setImage(UIImage(systemName: "play.fill"), for: .normal)
                    }
                    self.playSound(withUrl: url)
                    if self.isPlaying{
                        cell.playBtn.setImage(UIImage(systemName: "pause.fill"), for: .normal)
                    }else{
                        cell.playBtn.setImage(UIImage(systemName: "play.fill"), for: .normal)
                    }
                    self.selectedIndex = indexPath
                }
            }
            cell.updateCellForAudio(data: self.audioListData[indexPath.row])
            return cell
//        }else{
//            cell.followerList()
//            return cell
//        }
        
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 70
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let cell = tableView.cellForRow(at: indexPath) as! SelectionCell
        if isForSelectAudio{
            self.pleaseWait()
            let titleString = "\(audioListData[indexPath.row].artistName.localizedCapitalized) - \(audioListData[indexPath.row].songName.localizedCapitalized)"
            vid.downloadAudio(audioUrl: self.audioListData[indexPath.row].audioUrl){ downloadedAudio in
                
                self.vid.editVideo(videoURL: self.videoUrl, audioUrl: downloadedAudio) { outputVideoUrl in
                    self.selectedAudioUrl!(outputVideoUrl,titleString,self.audioListData[indexPath.row].id)
                    DispatchQueue.main.async {
                        self.clearAllNotice()
                        self.navigationController?.popViewController(animated: true)
                    }
                }
                
            }
            vid.onprogress = { progress in
                cell.progressBar.isHidden = false
                cell.progressBar.progress = CGFloat(progress)
            }
        }else{
            let vc = ChatVC()
            self.navigationController?.pushViewController(vc, animated: true)
        }
    }
}
