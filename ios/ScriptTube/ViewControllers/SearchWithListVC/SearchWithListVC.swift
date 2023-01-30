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
    @IBOutlet weak var noResultLbl:UILabel!
    var isForSelectAudio: Bool = false
    var audioListData: [AudioDataModel] = []
    var player: AVPlayer?
    var selectedAudioUrl:((URL,String,String)->Void)? = nil
    var isPlaying:Bool = false
    var videoUrl: URL!
    let vid = AudioVideoMerger()
    var videoPicked = false
    var page = 1
    var asset : AVURLAsset!
    var selectedCell:SelectionCell!
    override func viewDidLoad() {
        super.viewDidLoad()
        hideNavbar()
        searchTf.layer.cornerRadius = 10
        searchTf.overrideUserInterfaceStyle = .light
        setup()
        if isForSelectAudio{
            if(!(Constant.check_Internet?.isReachable)!){
                AlertView().showInternetErrorAlert(delegate: self)
                return
            }
            let param = ["limit":"15","page":"\(page)","type":"audio"]
            getAudioApi(param: param){
                DispatchQueue.main.async {
                    self.setupTableView()
                }
            }
        }
        addNavBar(headingText:"Select Audio",redText:"Audio")
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.tabBarController?.tabBar.isHidden = true
    }
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        player?.pause()
        guard let selectedCell = selectedCell else{return}
        selectedCell.pause()
    }
    func setup(){
        searchTf.paddingLeftRightTextField(left: 35, right: 0)
        searchTf.attributedPlaceholder = NSAttributedString(string: "Search Music",attributes: [.foregroundColor: UIColor.lightGray])
        searchTf.delegate = self
    }
    func playSound(withUrl url:String){
        
        if isPlaying{
            player?.pause()
        }else{
            
            vid.downloadAudio(audioUrl: url) { audioUrl in
                self.asset = AVURLAsset(url: audioUrl)
                
                //let playerItem = AVPlayerItem(url: audioUrl as URL)
                self.player = AVPlayer(asset: self.asset)
                self.player?.play()
//                self.player?.addPeriodicTimeObserver(forInterval: CMTime(seconds: 1, preferredTimescale: CMTimeScale(NSEC_PER_SEC)), queue: DispatchQueue.main, using: { [weak self] (time) in
//                    /// Float(self?.asset.duration.seconds ?? 1)
//                })
                //AVPlayer(playerItem: playerItem)
                self.player?.automaticallyWaitsToMinimizeStalling = false
                
            }

        }
        isPlaying = !isPlaying
    }

    func getAudioApi(param:[String:String],completion:@escaping()->Void){
        print("SerachParam",param)
        DataManager.globalSearchApi(delegate: self, param: param) { json in
            json["data"]["audios"].forEach { (message,data) in
                self.audioListData.append(AudioDataModel(data: data))
            }
            completion()
        }
    }
    func setupTableView(){
        tableView.register(UINib(nibName: SelectionCell.identifier, bundle: nil), forCellReuseIdentifier: SelectionCell.identifier)
        tableView.delegate = self
        tableView.dataSource = self
        tableView.reloadData()
    }
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView){
        let height = scrollView.frame.size.height
        let contentYOffset = scrollView.contentOffset.y
        let distanceFromBottom = scrollView.contentSize.height - contentYOffset
        if distanceFromBottom == height{
            page = page + 1
            let param = ["limit":"10","page":"\(page)","type":"audio"]
            self.audioListData = []
            getAudioApi(param: param) {
                self.tableView.reloadData()
            }
        }
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
                self.selectedCell = cell
                if self.selectedIndex == nil{
                    self.selectedIndex = indexPath
//                    self.playSound(withUrl: url)
                    if self.isPlaying{
                        cell.pause()
                        cell.playBtn.setImage(UIImage(systemName: "play.fill"), for: .normal)
                    }else{
                        cell.play()
                        cell.playBtn.setImage(UIImage(systemName: "pause.fill"), for: .normal)
                    }
                }else if self.selectedIndex == indexPath{
//                    self.playSound(withUrl: url)
                    if self.isPlaying{
                        cell.pause()
                        cell.playBtn.setImage(UIImage(systemName: "play.fill"), for: .normal)
                    }else{
                        
                        cell.play()
                        cell.playBtn.setImage(UIImage(systemName: "pause.fill"), for: .normal)
                    }
                    self.selectedIndex = nil
                }else{
                    print("KALLKALSAKS",self.selectedIndex?.row,indexPath.row,self.isPlaying)
                    let cell2 = tableView.cellForRow(at: self.selectedIndex!) as! SelectionCell
                    
                    //if self.isPlaying{
                        cell2.pause()
                        cell.play()
                        cell2.playBtn.setImage(UIImage(systemName: "play.fill"), for: .normal)
                        cell.playBtn.setImage(UIImage(systemName: "pause.fill"), for: .normal)
                    self.selectedIndex = indexPath
                }
                self.isPlaying = !self.isPlaying
            }
            cell.updateCellForAudio(data: self.audioListData[indexPath.row])
            return cell
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
                
                self.vid.editVideo(videoURL: self.videoUrl, audioUrl: downloadedAudio,pickedVideo: self.videoPicked) { outputVideoUrl in
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
//            let vc = ChatVC()
//            self.navigationController?.pushViewController(vc, animated: true)
        }
    }
}
extension SearchWithListVC:UITextFieldDelegate{
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        //print(textField.text)
        NSObject.cancelPreviousPerformRequests(
            withTarget: self,
            selector: #selector(getHintsFromTextField),
            object: textField)
        self.perform(
            #selector(getHintsFromTextField),
            with: textField,
            afterDelay: 0.5)
        return true
    }
    @objc func getHintsFromTextField(textField: UITextField){
        print("kkkk",textField.text)
        if searchTf.text!.isEmpty{
            let param = ["limit":"10","page":"\(page)","type":"audio"]
            self.audioListData = []
            getAudioApi(param: param) {
                DispatchQueue.main.async {
                    self.noResultLbl.isHidden = true
                    self.tableView.reloadData()
                }
            }
        }else{
            let param = ["search":searchTf.text!,"limit":"10","page":"\(page)","type":"audio"]
            self.audioListData = []
            getAudioApi(param: param) {
                DispatchQueue.main.async {
                    self.tableView.reloadData()
                    self.noResultLbl.isHidden = !self.audioListData.isEmpty
                }
            }
        }
    }
}
