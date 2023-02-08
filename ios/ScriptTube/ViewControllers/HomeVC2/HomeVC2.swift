//
//  HomeVC2.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 22/12/22.
//

import UIKit

class HomeVC2: BaseControllerVC,UIScrollViewDelegate {
    //MARK: - Vairables
    var xPage = 0
    var page = 1
    var page2 = 1
    var data = [Post]()
    var items: [URL] = []
    var data2 = [Post]()
    var items2: [URL] = []
    var table2Setup = false
    var forYouTap: UITapGestureRecognizer!
    var followingTap: UITapGestureRecognizer!
    var visibleCell: HomeTableViewCell?
    var shareVideoLink:String = ""
    var viewIsVisisble = false
    //MARK: - UI Components
    @IBOutlet weak var noVidLbl1: UILabel!
    @IBOutlet weak var noVidLbl: UILabel!
    @IBOutlet weak var spinner1: UIActivityIndicatorView!
    @IBOutlet weak var spinner2: UIActivityIndicatorView!
    @IBOutlet weak var bellIcon: UIImageView!
    @IBOutlet weak var followingUnderline: UIView!
    @IBOutlet weak var forYouunderline: UIView!
    @IBOutlet weak var forYouLbl: UILabel!
    @IBOutlet weak var followingLbl: UILabel!
    @IBOutlet weak var tableView2: UITableView!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var notificationCountView:CardView!
    @IBOutlet weak var notificationCountLbl:UILabel!
    override func viewDidLoad() {
        super.viewDidLoad()
        hideNavbar()
        setup()
        
        let param = ["limit":"10","page":"\(page)"]
        getHomePageVideosApi(withParams: param){
            if self.data.isEmpty{
                self.spinner1.stopAnimating()
                self.noVidLbl1.isHidden = false
            }else{
                self.setUpTable()
            }
        }
        self.scrollView.delegate = self
        // Do any additional setup after loading the view.
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        getNotificationCount()
        self.tabBarController?.tabBar.isHidden = false
        viewIsVisisble = true
        xPage == 1 ? checkPlay(table2: true) : checkPlay()
    }
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        viewIsVisisble = false
        xPage == 1 ? checkPause(table2: true) : checkPause()
    }
    //MARK: - Api Methods
    func getNotificationCount(){
        DataManager.getUnreadNotificationCount(delegate: self) {
            DispatchQueue.main.async {
                if AuthManager.currentUser.unReadNotificationCount == 0{
                    self.notificationCountView.isHidden = true
                }else{
                    self.notificationCountView.isHidden = false
                    self.notificationCountLbl.text = "\(AuthManager.currentUser.unReadNotificationCount)"
                }
            }
        }
    }
    func getHomePageVideosApi(withParams param:[String:String],table2:Bool = false,completion:@escaping()->Void){
        print("MYPARAMS",param)
        if(!(Constant.check_Internet?.isReachable)!){
            AlertView().showInternetErrorAlert(delegate: self)
            return
        }
        DataManager.getHomePageVideos(delegate: self, param: param) { json in
            json["data"].forEach { (message,data) in
                print("HOMEDATA",data)
                if table2{
                    guard let url = URL(string:UserDefaultHelper.getBaseUrl()+data["videoLink"].stringValue) else{return}
                    self.data2.append(Post(data: data))
                    print("DOwnlaod URl",url)
                    self.items2.append(url)
                    self.spinner2.stopAnimating()
                }else{
                    guard let url = URL(string:UserDefaultHelper.getBaseUrl()+data["videoLink"].stringValue) else{return}
                    self.data.append(Post(data: data))
                    print("DOwnlaod URl",url)
                    self.items.append(url)
                    self.spinner1.stopAnimating()
                }
            }
            completion()
        }
    }
    @objc func scrollToFollowing(sender:UITapGestureRecognizer){
        
        if sender == forYouTap{
            self.scrollView.scrollRectToVisible(CGRect(x: 0, y: 0, width: scrollView.frame.width, height: scrollView.frame.height), animated: true)
            forYouunderline.isHidden = false
            followingUnderline.isHidden = true
        }else{
            self.scrollView.scrollRectToVisible(CGRect(x: scrollView.frame.width, y: 0, width: scrollView.frame.width, height: scrollView.frame.height), animated: true)
            forYouunderline.isHidden = true
            followingUnderline.isHidden = false
        }
    }
    @objc func gotoNotification(){
        let vc = NotificationVC()
        self.navigationController?.pushViewController(vc, animated: true)
    }
    //MARK: - Setup
    func setup(){
        let tbc = self.tabBarController as! MainTabBarVC
        tbc.thisDelegate = self
        forYouTap = UITapGestureRecognizer(target: self, action: #selector(scrollToFollowing))
        followingTap = UITapGestureRecognizer(target: self, action: #selector(scrollToFollowing))
        bellIcon.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(gotoNotification)))
        notificationCountView.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(gotoNotification)))
        followingLbl.addGestureRecognizer(followingTap)
        forYouLbl.addGestureRecognizer(forYouTap)
        followingLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        forYouLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX16)
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
            self.viewIsVisisble ? self.check() : print("")
            
        }
    }
    func setupTable2(){
        DispatchQueue.main.async {
            self.tableView2.tableFooterView = UIView()
            self.tableView2.isPagingEnabled = true
            self.tableView2.contentInsetAdjustmentBehavior = .never
            self.tableView2.showsVerticalScrollIndicator = false
            self.tableView2.separatorStyle = .none
            self.tableView2.register(UINib(nibName: HomeTableViewCell.identifier, bundle: nil), forCellReuseIdentifier: HomeTableViewCell.identifier)
            self.tableView2.delegate = self
            self.tableView2.dataSource = self
            self.tableView2.prefetchDataSource = self
            self.tableView2.reloadData()
            self.viewIsVisisble ? self.check(table2: true) : print("")
        }
    }
    //MARK: - Scrolling Managing Methods
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        guard let _ = scrollView as? UITableView else{
            let page = scrollView.contentOffset.x / scrollView.frame.size.width
            xPage = Int(page)
            if xPage == 1{
                if table2Setup{
                    check(table2: true)
                }else{
                    let param = ["type":"following","limit":"10","page":"\(page2)"]
                    getHomePageVideosApi(withParams: param,table2: true) {
                        if !self.data2.isEmpty{
                            self.setupTable2()
                            self.table2Setup = true
                            self.noVidLbl.isHidden = true
                        }else{
                            self.spinner2.stopAnimating()
                            self.noVidLbl.isHidden = false
                        }
                    }
                }
            }
            
            return
        }
        let page = scrollView.contentOffset.y / scrollView.frame.size.height
        
     }
    func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        if !decelerate {
            print("sjhcjasncjks2")
            if xPage == 1{
                
                check(table2: true)
                checkPause()
            }else{
                
                check()
                checkPause(table2: true)
            }
        }
    }
    func scrollViewDidEndScrollingAnimation(_ scrollView: UIScrollView) {
        print("sjhcjasncjks")
        if xPage == 1{
            followingLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX16)
            forYouLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
            check(table2: true)
            checkPause()
        }else{
            followingLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
            forYouLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX16)
            check()
            checkPause(table2: true)
        }
    }
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        print("sjhcjasncjks1")
        if xPage == 1{
            forYouunderline.isHidden = true
            followingUnderline.isHidden = false
            followingLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX16)
            forYouLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
            check(table2: true)
            checkPause()
        }else{
            check()
            checkPause(table2: true)
            forYouunderline.isHidden = false
            followingUnderline.isHidden = true
            followingLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
            forYouLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX16)
        }
        guard let _ = scrollView as? UITableView else{return}
        let height = scrollView.frame.size.height
        let contentYOffset = scrollView.contentOffset.y
        let distanceFromBottom = scrollView.contentSize.height - contentYOffset
        if distanceFromBottom == height{
            print("helloajsk")
            print("You reached end of the table")
            if xPage == 1{
                page2 = page2 + 1
                var param = [String:String]()
                param = ["type":"following","limit":"10","page":"\(page2)"]
                getHomePageVideosApi(withParams: param,table2: true){
                    DispatchQueue.main.async {
                        self.tableView2.reloadData()
                        self.check(table2: true)
                    }
                }
            }else{
                page = page + 1
                var param = [String:String]()
                param = ["limit":"10","page":"\(page)"]
                getHomePageVideosApi(withParams: param){
                    DispatchQueue.main.async {
                        self.tableView.reloadData()
                        self.check()
                    }
                }
            }
        }
    }
    //MARK: - Video Playing/Downloading Methods
    func check(table2:Bool = false) {
        print("forpage",xPage)
        checkPreload(table2: table2)
        checkPlay(table2: table2)
    }
    func checkPreload(table2:Bool = false) {
        if table2{
            guard let lastRow = tableView2.indexPathsForVisibleRows?.last?.row else { return }
            
            let urls = items2
                .suffix(from: min(lastRow + 1, items2.count))
                .prefix(2)
            
            VideoPreloadManager.shared.set(waiting: Array(urls))
        }else{
            guard let lastRow = tableView.indexPathsForVisibleRows?.last?.row else { return }
            
            let urls = items
                .suffix(from: min(lastRow + 1, items.count))
                .prefix(2)
            
            VideoPreloadManager.shared.set(waiting: Array(urls))
        }
    }
    
    func checkPlay(table2:Bool = false) {
        var visibleCells = [HomeTableViewCell]()
        if table2{
            visibleCells = tableView2.visibleCells.compactMap { $0 as? HomeTableViewCell }
            guard visibleCells.count > 0 else { return }
            
            let visibleFrame = CGRect(x: 0, y: tableView2.contentOffset.y, width: tableView2.bounds.width, height: tableView2.bounds.height)

            let visibleCell = visibleCells
                .filter { visibleFrame.intersection($0.frame).height >= $0.frame.height / 2 }
                .first
            visibleCell?.play()
            visibleCell?.checkFollow()
            self.visibleCell = visibleCell
        }else{
            visibleCells = tableView.visibleCells.compactMap { $0 as? HomeTableViewCell }
            guard visibleCells.count > 0 else { return }
            
            let visibleFrame = CGRect(x: 0, y: tableView.contentOffset.y, width: tableView.bounds.width, height: tableView.bounds.height)

            let visibleCell = visibleCells
                .filter { visibleFrame.intersection($0.frame).height >= $0.frame.height / 2 }
                .first
            visibleCell?.play()
            visibleCell?.checkFollow()
            self.visibleCell = visibleCell
        }
       
        
    }
    func checkPause(table2:Bool = false){
        print("forpage",xPage)
        var visibleCells = [HomeTableViewCell]()
        if table2{
            visibleCells = tableView2.visibleCells.compactMap { $0 as? HomeTableViewCell }
            guard visibleCells.count > 0 else { return }
            let visibleFrame = CGRect(x: 0, y: tableView2.contentOffset.y, width: tableView2.bounds.width, height: tableView2.bounds.height)
            let visibleCell = visibleCells
                .filter { visibleFrame.intersection($0.frame).height >= $0.frame.height / 2 }
                .first
            visibleCell?.pause()
        }else{
            visibleCells = tableView.visibleCells.compactMap { $0 as? HomeTableViewCell }
            guard visibleCells.count > 0 else { return }
            let visibleFrame = CGRect(x: 0, y: tableView.contentOffset.y, width: tableView.bounds.width, height: tableView.bounds.height)
            let visibleCell = visibleCells
                .filter { visibleFrame.intersection($0.frame).height >= $0.frame.height / 2 }
                .first
            visibleCell?.pause()
        }
        
    }
}
//MARK: -Table View Delegates
extension HomeVC2: UITableViewDelegate, UITableViewDataSource, UITableViewDataSourcePrefetching{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if tableView == self.tableView{
            return self.data.count
        }else{
            return self.data2.count
        }
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: HomeTableViewCell.identifier, for: indexPath) as! HomeTableViewCell
        cell.delegate = self
        if tableView == self.tableView{
            cell.configure(post: data[indexPath.row])
        }else{
            
            cell.configure(post: data2[indexPath.row])
        }
        
        return cell
    }

    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return tableView.frame.height
    }


    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        // If the cell is the first cell in the tableview, the queuePlayer automatically starts.
        // If the cell will be displayed, pause the video until the drag on the scroll view is ended
        if let cell = cell as? HomeTableViewCell{
            print("INDEXPATY1",indexPath.row)
            cell.pauseImgView.alpha = 0
        }
    }

    func tableView(_ tableView: UITableView, didEndDisplaying cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        // Pause the video if the cell is ended displaying
        tableView == self.tableView ? print("table1") : print("table2")
        if let cell = cell as? HomeTableViewCell {
            cell.pause()
            
            print("INDEXPATY2",indexPath.row)
        }
    }

    func tableView(_ tableView: UITableView, prefetchRowsAt indexPaths: [IndexPath]) {
       
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        print("thisVideois",indexPath.row)
    }
}
//MARK: - Delegate for camera opening
extension HomeVC2:MainTabbarVCDelegate{
    func cameraOpened() {
        xPage == 1 ? checkPause(table2: true) : checkPause()
    }
}
//MARK: - Navigation from Home page Delegates
extension HomeVC2:HomeCellNavigationDelegate{
    func gotoUserProfileOfSupporter(withUser user: DonationUserModel) {
        let vc = VisitProfileVC()
        vc.id = user.id
        self.navigationController?.pushViewController(vc, animated: true)
    }
    func clickedFollowBtn(forUser id: String, isFollowing: Bool) {
        let sameUser = self.data.filter { post in
            return post.userDetails?.id == id
        }
        print("SAMEUSERVUEI",sameUser.count)
        sameUser.forEach { post in
            post.isFollow = isFollowing
        }
        let sameUser2 = self.data2.filter { post in
            return post.userDetails?.id == id
        }
        print("SAMEUSERVUEI2",sameUser2.count,isFollowing)
        sameUser2.forEach { post in
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
    func gotoUserProfile(withUser user:UserProfileData,isFollowing:Bool) {
        let vc = VisitProfileVC()
        vc.id = user.id
        vc.delegate = self
        self.navigationController?.pushViewController(vc, animated: true)
    }
    func shareVideo(withUrl  url:String,id:String) {
        let vc = SharePopUp()
        vc.videoId = id
        vc.delegate = self
        vc.modalPresentationStyle = .popover
        self.shareVideoLink = url
        self.tabBarController?.present(vc, animated: true)
    }
    func reportVideo(withId id:String,isReported: Bool) {
        let popUp = ReportBtnPopUp()
        popUp.btnDelegate = self
        popUp.isvideoReported = isReported
        popUp.videoId = id
        popUp.delegate = self
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
//MARK: - Donation Delegate
extension HomeVC2:DonationPopupDelegate{
    func donationSuccess() {
        let vc = PaymentSuccessVC()
        vc.modalTransitionStyle = .coverVertical
        vc.modalPresentationStyle = .overCurrentContext
        self.tabBarController?.present(vc, animated: true)
    }
    
    func donateBtnClicked(post:Post,amt:String) {
        let vc = PaymemtSavedCardListVC()
        vc.videoId = post.id
        vc.donateTo = post.userDetails!.id
        vc.amount = amt
        vc.forPayment = true
        self.navigationController?.pushViewController(vc, animated: true)
    }
}
//MARK: - Comment Delegate
extension HomeVC2:CommentPopUpDelegate{
    func dismissAfterComment(numberOfComments num: String) {
        guard let cell = visibleCell else{
            return
        }
        cell.post?.videoCommentCount = num
        cell.commentCountLbl.text = Int(num)?.shorten()
    }
    
    func dismissToVisitProfile(withId id: String) {
        let vc = VisitProfileVC()
        vc.id = id
        self.navigationController?.pushViewController(vc, animated: true)
    }
}
//MARK: - Navigation Delegates
extension HomeVC2:VisitProfileDelegate{
    func followActionChanged(withId id: String, isFollowing: Bool) {
        let sameUser = self.data.filter { post in
            return post.userDetails?.id == id
        }
        
        sameUser.forEach { post in
            post.isFollow = isFollowing
        }
        let sameUser2 = self.data2.filter { post in
            return post.userDetails?.id == id
        }
        
        sameUser2.forEach { post in
            post.isFollow = isFollowing
        }
    }
}

//MARK: - Sharing Video Delegates
extension HomeVC2:SharePopUpDelegate{
    func newShareCount(count: String,otherUser:UserProfileData,slug:String) {
        guard let cell = visibleCell else{
            return
        }
        cell.post?.videoShareCount = count
        cell.shareCountLbl.text = Int(count)?.shorten()
        let vc = ChatVC()
        vc.chatSlug = slug
        vc.otherUser = otherUser
        self.navigationController?.pushViewController(vc, animated: true)
    }
    func shareToOtherApp() {
        self.dismiss(animated: true)
        DispatchQueue.main.async {
            self.pleaseWait()
        }
        let vid  = AudioVideoMerger()
        vid.downloadVideoToCameraRoll(videoUrl: self.shareVideoLink) { url in
            //MARK: - Insert App Url from App Store
            if let name = URL(string: LocalStrings.APP_URL), !name.absoluteString.isEmpty {
                let objectsToShare = [url]
                let activityVC = UIActivityViewController(activityItems: objectsToShare, applicationActivities: nil)
                DispatchQueue.main.async {
                    self.clearAllNotice()
                    self.present(activityVC, animated: true, completion: nil)
                }
                
            } else {
                AlertView().showAlert(message: "ERROR in sharing", delegate: self, pop: false)
            }
        }
    }
}
//MARK: - Report Video Delegate
extension HomeVC2:ReportVideoDelegate{
    func videoReported() {
        ToastManager.successToast(delegate: self, msg: "Report Submit Succesfully")
        guard let cell = visibleCell else{
            return
        }
        cell.post?.isReported = true
    }
}
extension HomeVC2:ReportBtnDelegate{
    func alreadyReportedVideo() {
        ToastManager.errorToast(delegate: self, msg: "Video Already Reported")
    }
}
