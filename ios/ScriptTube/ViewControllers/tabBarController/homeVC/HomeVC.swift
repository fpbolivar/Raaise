//
//  HomeVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/21/22.
//MARK: - Unimplemented Screen, Screen Used in This place is HomeVC2

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
    var visibleCell: HomeTableViewCell?
    var swipeLeft = UISwipeGestureRecognizer()
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
        swipeLeft = UISwipeGestureRecognizer(target: self, action: #selector(swipe))
        swipeLeft.direction = .left
        self.view.addGestureRecognizer(swipeLeft)
        let param = ["limit":"10","page":"\(page)"]
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
    @objc func swipe(sender:UISwipeGestureRecognizer){
        if sender.direction == .left{
            print("LEFT")
        }else{
            print("RIGHT")
        }
        
    }
    func getHomePageVideosApi(withParams param:[String:String],completion:@escaping()->Void){
        print("MYPARAMS",param)
        if(!(Constant.check_Internet?.isReachable)!){
            AlertView().showInternetErrorAlert(delegate: self)
            return
        }
        //self.tableView.isUserInteractionEnabled = false
        DataManager.getHomePageVideos(delegate: self, param: param) { json in
            json["data"].forEach { (message,data) in
                print("HOMEDATA",data)
                //self.tableView.isUserInteractionEnabled = true
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
            param = ["type":"following","limit":"10","page":"\(followingPage)"]
        }else{
            param = ["limit":"10","page":"\(page)"]
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
        //checkPause()
    }
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
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
            //cell.checkFollow()
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
//            page = page + 1
//            var param = [String:String]()
//            if forYouunderline.isHidden{
//                followingPage = followingPage + 1
//                param = ["type":"following","limit":"10","page":"\(followingPage)"]
//            }else{
//                param = ["limit":"10","page":"\(page)"]
//            }
//            getHomePageVideosApi(withParams: param){
//                DispatchQueue.main.async {
//                    //self.tableView.isUserInteractionEnabled = false
//                    self.tableView.reloadData()
//                   // self.tableView.isUserInteractionEnabled = true
//                    self.check()
//                }
//            }
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
        let height = scrollView.frame.size.height
        let contentYOffset = scrollView.contentOffset.y
        let distanceFromBottom = scrollView.contentSize.height - contentYOffset
        if distanceFromBottom == height{
            print("helloajsk")
            print("You reached end of the table")
            page = page + 1
            var param = [String:String]()
            if forYouunderline.isHidden{
                followingPage = followingPage + 1
                param = ["type":"following","limit":"10","page":"\(followingPage)"]
            }else{
                param = ["limit":"10","page":"\(page)"]
            }
            getHomePageVideosApi(withParams: param){
                DispatchQueue.main.async {
                    //self.tableView.isUserInteractionEnabled = false
                    self.tableView.reloadData()
                   // self.tableView.isUserInteractionEnabled = true
                    self.check()
                }
            }
        }
        
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
        visibleCell?.checkFollow()
        self.visibleCell = visibleCell
    }
    func checkPause(){
        let visibleCells = tableView.visibleCells.compactMap { $0 as? HomeTableViewCell }
        guard visibleCells.count > 0 else { return }
        let visibleFrame = CGRect(x: 0, y: tableView.contentOffset.y, width: tableView.bounds.width, height: tableView.bounds.height)
        let visibleCell = visibleCells
            .filter { visibleFrame.intersection($0.frame).height >= $0.frame.height / 2 }
            .first
        self.visibleCell = visibleCell
        visibleCell?.pause()
    }
}

// MARK: - Navigation Delegate
extension HomeVC: HomeCellNavigationDelegate {
    func onLikeVideo(post: Post, isLike: Bool) {
        //
    }
    
    
    
    func clickedFollowBtn(forUser id: String, isFollowing: Bool) {
        let sameUser = self.data.filter { post in
            return post.userDetails?.id == id
        }
        print("SAMEUSERVUEI",sameUser.count)
        sameUser.forEach { post in
            post.isFollow = isFollowing
        }
    }
    
    func viewCountError(error: String) {
        AlertView().showAlert(message: error, delegate: self, pop: false)
    }
    func goTiTryAudio(withId audio :AudioDataModel) {
        let vc = TryAudioVC()
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
        popup.modalPresentationStyle = .popover
        self.tabBarController?.present(popup, animated: true)
    }
    func gotoUserProfileOfSupporter(withUser user: DonationUserModel) {
        let vc = VisitProfileVC()
        vc.id = user.id
       // vc.delegate = self
        self.navigationController?.pushViewController(vc, animated: true)
    }
    func gotoUserProfile(withUser user:UserProfileData,isFollowing:Bool) {
        let vc = VisitProfileVC()
//        vc.isFollowing = isFollowing
//        vc.userDetails = user
        vc.id = user.id
        self.navigationController?.pushViewController(vc, animated: true)
    }
    func shareVideo(withUrl  url:String,id:String) {
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
            
        }
        
    }
    func reportVideo(withId id:String,isReported:Bool) {
        let popUp = ReportBtnPopUp()
        popUp.videoId = id
        popUp.modalTransitionStyle = .crossDissolve
        popUp.modalPresentationStyle = .overCurrentContext
        self.tabBarController?.present(popUp, animated: true)
    }
    
    func donationPopUp(post:Post) {
        let popUp = DonationPopUpViewController()
        popUp.delegate = self
        popUp.post = post
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
    func donateBtnClicked(post:Post,amt:String) {
        let vc = PaymemtSavedCardListVC()
        vc.videoId = post.id
        vc.donateTo = post.userDetails!.id
        vc.amount = amt
        vc.forPayment = true
        self.navigationController?.pushViewController(vc, animated: true)
    }
    func donationSuccess() {
        let vc = PaymentSuccessVC()
        vc.modalTransitionStyle = .coverVertical
        vc.modalPresentationStyle = .overCurrentContext
        self.tabBarController?.present(vc, animated: true)
    }
}
extension HomeVC:CommentPopUpDelegate{
    func dismissAfterComment(numberOfComments num: String) {
        guard let cell = visibleCell else{
            return
        }
        cell.post?.videoCommentCount = num
        cell.commentCountLbl.text = Int(num)?.shorten()
    }
    
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
