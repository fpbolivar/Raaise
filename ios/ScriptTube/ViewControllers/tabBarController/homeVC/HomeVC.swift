//
//  HomeVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/21/22.
//

import UIKit
import Photos

class HomeVC: UIViewController {
    @IBOutlet weak var forYouunderline: UIView!
    @IBOutlet weak var forYouLbl: UILabel!
    @IBOutlet weak var bellIcon: UIImageView!
    @IBOutlet weak var followingUnderline: UIView!
    @IBOutlet weak var followingLbl: UILabel!
    @IBOutlet weak var  tableView:UITableView!
    var data = [Post]()
    var forYouTap: UITapGestureRecognizer!
    var followingTap: UITapGestureRecognizer!
    var oldAndNewIndices = (0,0)
    var page = 1
    var followingPage = 1
    var items: [URL] = []
    @objc dynamic var currentIndex = 0
    override func viewDidLoad() {
        super.viewDidLoad()
        hideNavbar()
        
        let param = ["limit":"4","page":"\(page)"]
        getHomePageVideosApi(withParams: param){
            self.setUpTable()
        }
        let tbc = self.tabBarController as! MainTabBarVC
        tbc.thisDelegate = self
        //makeUnderLine(forFollowing: true)
        forYouTap = UITapGestureRecognizer(target: self, action: #selector(selectTap))
        followingTap = UITapGestureRecognizer(target: self, action: #selector(selectTap))
        bellIcon.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(gotoNotification)))
        followingLbl.addGestureRecognizer(followingTap)
        forYouLbl.addGestureRecognizer(forYouTap)
        followingLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX16)
        forYouLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX16)
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.tabBarController?.tabBar.isHidden = false
        checkPlay()
    }
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
//        check()
    }
    func getHomePageVideosApi(withParams param:[String:String],completion:@escaping()->Void){
        print("MYPARAMS",param)
        DataManager.getHomePageVideos(delegate: self, param: param) { json in
            json["data"].forEach { (message,data) in
                print("HOMEDATA",data)
                guard let url = URL(string:data["videoLink"].stringValue) else{return}
                self.data.append(Post(data: data))
                self.items.append(url)
            }
            
            completion()
            print("POSTS",self.data.count)
//            DispatchQueue.main.async {
//                self.tableView.reloadData()
//            }
        }
    }
    @objc func selectTap(sender: UITapGestureRecognizer){
        self.pleaseWait()
        forYouunderline.isHidden = !forYouunderline.isHidden
        followingUnderline.isHidden = !followingUnderline.isHidden
        //self.data = []
        data.removeAll()
        print(data.count)
        followingPage = 1
        page = 1
        var param = [String:String]()
        if sender == followingTap{
            param = ["type":"following","limit":"4","page":"\(followingPage)"]
        }else{
            param = ["limit":"4","page":"\(page)"]
        }
        getHomePageVideosApi(withParams: param){
        self.tableView.reloadData()
            //self.setUpTable()
            self.clearAllNotice()
        }
    }
    @objc func gotoNotification(){
        print("ahsghjasgdhja")
        let vc = NotificationVC()
        self.navigationController?.pushViewController(vc, animated: true)
    }
    func makeUnderLine(forFollowing:Bool){
        if forFollowing{
            let underlineAttriString = NSMutableAttributedString(string: "Following")
            let range1 = ("Following" as NSString).range(of: "Fol")
            underlineAttriString.addAttribute(.underlineStyle, value: NSUnderlineStyle.thick.rawValue, range: range1)
            underlineAttriString.addAttribute(.underlineColor, value: UIColor.white, range: range1)
            underlineAttriString.addAttribute(NSAttributedString.Key.foregroundColor, value: UIColor.white, range: range1)
            followingLbl.attributedText = underlineAttriString
            forYouLbl.text = "For You"
        }else{
            let underlineAttriString = NSMutableAttributedString(string: "For You")
            let range1 = ("For You" as NSString).range(of: "For")
            underlineAttriString.addAttribute(.underlineStyle, value: NSUnderlineStyle.thick.rawValue, range: range1)
            underlineAttriString.addAttribute(.underlineColor, value: UIColor.white, range: range1)
            underlineAttriString.addAttribute(NSAttributedString.Key.foregroundColor, value: UIColor.white, range: range1)
            forYouLbl.attributedText = underlineAttriString
            followingLbl.text = "Following"
        }

    }
    func setUpTable(){
        DispatchQueue.main.async {
            self.tableView.tableFooterView = UIView()
            self.tableView.isPagingEnabled = true
            self.tableView.contentInsetAdjustmentBehavior = .never
            self.tableView.showsVerticalScrollIndicator = false
            self.tableView.separatorStyle = .none
            self.tableView.register(UINib(nibName: HomeTableViewCell.identifier, bundle: nil), forCellReuseIdentifier: HomeTableViewCell.identifier)
            self.tableView.delegate = self
            self.tableView.dataSource = self
            self.tableView.prefetchDataSource = self
            self.tableView.reloadData()
            self.check()
        }
        
    }
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        checkPause()
    }
    
}

// MARK: - Table View Extensions
extension HomeVC: UITableViewDelegate, UITableViewDataSource, UITableViewDataSourcePrefetching{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.data.count
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: HomeTableViewCell.identifier, for: indexPath) as! HomeTableViewCell
        cell.delegate = self
        cell.configure(post: data[indexPath.row])
        return cell
    }

    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return tableView.frame.height
    }


    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        // If the cell is the first cell in the tableview, the queuePlayer automatically starts.
        // If the cell will be displayed, pause the video until the drag on the scroll view is ended
        if let cell = cell as? HomeTableViewCell{
            oldAndNewIndices.1 = indexPath.row
            currentIndex = indexPath.row
//            if indexPath.row == data.count - 1{
//                print("IJSJKAKOSM")
//            }
            print("INDEXPATY1",indexPath.row)
            //cell.play()
            cell.pauseImgView.alpha = 0
        }
    }

    func tableView(_ tableView: UITableView, didEndDisplaying cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        // Pause the video if the cell is ended displaying
        if let cell = cell as? HomeTableViewCell {
            cell.pause()
            print("INDEXPATY2",indexPath.row)
        }
        if indexPath.row == data.count - 4{
            print("siudbcguhdsvbcjh",indexPath.row,data.count - 4)
            page = page + 1
            var param = [String:String]()
            if forYouunderline.isHidden{
                followingPage = followingPage + 1
                param = ["type":"following","limit":"4","page":"\(followingPage)"]
            }else{
                param = ["limit":"4","page":"\(page)"]
            }
            getHomePageVideosApi(withParams: param){
                self.tableView.reloadData()
                self.check()
            }
        }
    }

    func tableView(_ tableView: UITableView, prefetchRowsAt indexPaths: [IndexPath]) {
       
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        print("thisVideois",indexPath.row)
    }
}
// MARK: - ScrollView Extension
extension HomeVC: UIScrollViewDelegate {
    func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        if !decelerate { check() }
    }
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        check()
    }
    func check() {
        checkPreload()
        checkPlay()
    }
    func checkPreload() {
        guard let lastRow = tableView.indexPathsForVisibleRows?.last?.row else { return }
        
        let urls = items
            .suffix(from: min(lastRow + 1, items.count))
            .prefix(2)
        
        VideoPreloadManager.shared.set(waiting: Array(urls))
    }
    
    func checkPlay() {
        let visibleCells = tableView.visibleCells.compactMap { $0 as? HomeTableViewCell }
        
        guard visibleCells.count > 0 else { return }
        
        let visibleFrame = CGRect(x: 0, y: tableView.contentOffset.y, width: tableView.bounds.width, height: tableView.bounds.height)

        let visibleCell = visibleCells
            .filter { visibleFrame.intersection($0.frame).height >= $0.frame.height / 2 }
            .first
        
        visibleCell?.play()
    }
    func checkPause(){
        let visibleCells = tableView.visibleCells.compactMap { $0 as? HomeTableViewCell }
        guard visibleCells.count > 0 else { return }
        let visibleFrame = CGRect(x: 0, y: tableView.contentOffset.y, width: tableView.bounds.width, height: tableView.bounds.height)
        let visibleCell = visibleCells
            .filter { visibleFrame.intersection($0.frame).height >= $0.frame.height / 2 }
            .first
        visibleCell?.pause()
    }
}

// MARK: - Navigation Delegate
extension HomeVC: HomeCellNavigationDelegate {
   
    
//    func downloadVideo(url:Str){
//        
//    }
    func goTiTryAudio(withId audio :AudioDataModel) {
        let vc = TryAudioVC()
        //vc.audioId = id
        vc.audioData = audio
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    func errorOnLike(withMessage message: String) {
        AlertView().showAlert(message: message, delegate: self, pop: false)
    }
    
    func showComments(id:String, numberOfComments num: String) {
        let popup = CommentPopUp()
        popup.delegate = self
        popup.videoId = id
        popup.numberOfComments = num
        popup.modalPresentationStyle = .pageSheet
        self.tabBarController?.present(popup, animated: true)
    }
    func gotoUserProfile(withUser user:UserProfileData,isFollowing:Bool) {
        let vc = VisitProfileVC()
//        vc.isFollowing = isFollowing
//        vc.userDetails = user
        vc.id = user.id
        self.navigationController?.pushViewController(vc, animated: true)
    }
    func shareVideo(withUrl  url:String) {
        DispatchQueue.main.async {
            self.pleaseWait()
        }
        
        let vid  = AudioVideoMerger()
        vid.downloadVideoToCameraRoll(videoUrl: url) { url in
            print("URLLLLL",url)
            if let name = URL(string: "https://itunes.apple.com/us/app/myapp/idxxxxxxxx?ls=1&mt=8"), !name.absoluteString.isEmpty {
                let objectsToShare = [url]
                let activityVC = UIActivityViewController(activityItems: objectsToShare, applicationActivities: nil)
                DispatchQueue.main.async {
                    self.clearAllNotice()
                    self.present(activityVC, animated: true, completion: nil)
                }
                
            } else {
                // show alert for not available
                AlertView().showAlert(message: "ERROR in sharing", delegate: self, pop: false)
            }
            //            DispatchQueue.main.async {
            //                PHPhotoLibrary.shared().performChanges({
            //                   PHAssetChangeRequest.creationRequestForAssetFromVideo(atFileURL: url)
            //                }) { saved, error in
            //                if saved {
            //                    print("Saved")
            //                }
            //                }
            //            }
            //        }
            //        var videoPath:URL? = nil
            //        let videoImageUrl = url
            //
            //        DispatchQueue.global(qos: .background).async {
            //            if let url = URL(string: videoImageUrl),
            //                let urlData = NSData(contentsOf: url) {
            //                let documentsPath = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)[0];
            //                let date =  Date().millisecondsSince1970
            //                let filePath="\(documentsPath)/\(date).mp4"
            //                videoPath = URL(fileURLWithPath: filePath)
            //                DispatchQueue.main.async {
            //                    urlData.write(toFile: filePath, atomically: true)
            //                    PHPhotoLibrary.shared().performChanges({
            //                        PHAssetChangeRequest.creationRequestForAssetFromVideo(atFileURL: URL(fileURLWithPath: filePath))
            //                    }) { completed, error in
            //                        if completed {
            //                            print("Video is saved!")
            
            //                        }
            //                    }
            //                }
            //            }
            //        }
        }
        
    }
    func reportVideo(withId id:String) {
        //AlertView().showAlert(message: "Report is not implemented yet", delegate: self, pop: false)
        let popUp = ReportBtnPopUp()
        //ReportVideoVC()
        popUp.videoId = id
        popUp.modalTransitionStyle = .crossDissolve
        popUp.modalPresentationStyle = .overCurrentContext
        self.tabBarController?.present(popUp, animated: true)
    }
    
    func donationPopUp() {
        let popUp = DonationPopUpViewController()
        popUp.delegate = self
        popUp.modalTransitionStyle = .crossDissolve
        popUp.modalPresentationStyle = .overCurrentContext
        self.tabBarController?.present(popUp, animated: true)
    }
    func navigateToTryAudio() {
        let vc = TryAudioVC()
        self.navigationController?.pushViewController(vc, animated: true)
    }
}
extension HomeVC: DonationPopupDelegate{
    func donateBtnClicked() {
        let vc = PaymemtSavedCardListVC()
        self.navigationController?.pushViewController(vc, animated: true)
    }
}
extension HomeVC:CommentPopUpDelegate{
    func dismissToVisitProfile(withId id: String) {
        let vc = VisitProfileVC()
        //vc.isFollowing = isFollowing
        //vc.userDetails = user
        vc.id = id
        self.navigationController?.pushViewController(vc, animated: true)
    }
}
extension HomeVC:MainTabbarVCDelegate{
    func cameraOpened() {
        checkPause()
    }
}
