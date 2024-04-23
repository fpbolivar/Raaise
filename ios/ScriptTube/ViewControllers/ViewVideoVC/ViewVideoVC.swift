//
//  ViewVideoVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 08/12/22.
//

import UIKit
import Photos
protocol ViewVideoErrorDelegate{
    func popAndShowError(error:String)
}
protocol ViewVideoChangeDelegate{
    func videoChange(post:Post,isLiked:Bool)
}
class ViewVideoVC: BaseControllerVC {
    @IBOutlet weak var menuImage: UIImageView!
    @IBOutlet weak var backImage: UIImageView!
    @IBOutlet weak var tableView: UITableView!
    var errorDelegate:ViewVideoErrorDelegate?
    var shareVideoLink:String = ""
    var visibleCell: HomeTableViewCell?
    var data = [Post]()
    var items: [URL] = []
    var selectedRow = 0
    var selectedIndexPath: IndexPath!
    var visitingProfile = true
    var visiblePost: Post?
    var fromProfileId = ""
    var slug: String? = nil
    var changeDelegate:ViewVideoChangeDelegate?
    var delegate:ViewVideoFromProfile?
    
    //Date:: 09, Apr 2024 - variable for the comment table view
    var selTableViewRowIndexForComment : Int = -1
    var isGetCommentApiCalled : Bool = false
    var post: Post?
    var commentData : [CommentDataModel] = []
    var commentPage = 1
    //---
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //Date:: 09, Apr 2024 - Notification Center add Observer
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShow(_:)), name: UIResponder.keyboardWillShowNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillHide(_:)), name: UIResponder.keyboardWillHideNotification, object: nil)
        
        backImage.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(popBack)))
        menuImage.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(openMenu)))
        hideNavbar()
        menuImage.isHidden = visitingProfile
        if let slug = slug{
            self.pleaseWait()
            getVideoData(slug: slug){
                DispatchQueue.main.async {
                    self.setupTableView()
                    self.clearAllNotice()
                }
            }
        }else{
            self.tableView.isHidden = true
            setupTableView()
        }
    }
    @objc func popBack(){
        self.navigationController?.popViewController(animated: false)
    }
    @objc func openMenu(){
        let vc = EditProfileVideoVC()
        vc.post = self.visiblePost
        vc.delegate = self
        vc.modalTransitionStyle = .crossDissolve
        vc.modalPresentationStyle = .overCurrentContext
        self.present(vc, animated: true)
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.tabBarController?.tabBar.isHidden = true
    }
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        check()
    }
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        checkPause()
    }
    //MARK: - API Methods
    func getVideoData(slug:String,completion:@escaping()->Void){
        let param = ["slug":slug]
        DataManager.getSingleVideoDetail(delegate: self, param: param) { json in
            let obj = Post(data2: json["data"])
            self.data.append(obj)
            self.data.append(obj)
            guard let url = URL(string: UserDefaultHelper.getBaseUrl()+obj.videoLink) else{return}
            self.items.append(url)
            completion()
        } onError: { error in
            self.navigationController?.popViewController(animated: true)
            self.errorDelegate?.popAndShowError(error: error)
        }
    }
    //MARK: - Setup
    func setupTableView(){
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
            if self.slug == nil{
                self.selectedIndexPath = IndexPath(row: self.selectedRow, section: 0)
                    self.tableView.scrollToRow(at: self.selectedIndexPath, at: .none, animated: true)
                if self.selectedRow == 0{
                    self.tableView.isHidden = false
                }
            }
            self.check()
        }
        
        
    }
    //Date:: 09, Apr 2024 - when user tap on the comment send Button
    @objc func commentButtonTapped(_ sender: UIButton) {

        self.view.endEditing(true)
        selTableViewRowIndexForComment = sender.tag
        
        debugPrint("Sel index \(selTableViewRowIndexForComment)")
        var indexPath = IndexPath(row: sender.tag, section: 0)
        
        debugPrint("index path Inner \(indexPath)")
        debugPrint("index path row Inner \(indexPath.row)")
        
        if let homeTableViewCell = tableView.cellForRow(at: indexPath) as? HomeTableViewCell {
            print("CommentTf text \(homeTableViewCell.commentTf.text ?? "")")
            
            if homeTableViewCell.commentTf.text != ""
            {
                post = data[indexPath.row]
                print(post?.id ?? "")
                postComment(videoId: post?.id ?? "",
                            comment: homeTableViewCell.commentTf.text ?? "")
                homeTableViewCell.commentTf.text = ""
                homeTableViewCell.commentData = self.commentData
                homeTableViewCell.commentTblView.reloadData()
            }
        }
        
    }
    //Date:: 09, Apr 2024 - post api call
    func postComment(videoId: String, comment: String){
        isGetCommentApiCalled = true
        let param = ["videoId":videoId,"comment":comment]
        print("EMOJEEE",comment, param)
        AuthManager.postCommentApi(delegate: self, param: param) { retData in
            let indexPath = IndexPath(row: self.selTableViewRowIndexForComment , section: 0)
            
            //self.tableView.reloadRows(at: [indexPath], with: .top)
            
            //let indexPath = IndexPath(row: self.selTableViewRowIndexForComment , section: 0)
            self.post = self.data[indexPath.row]
            
            DispatchQueue.main.async {
                print("api Call in postComment ")
                self.getCommentsApi(post: self.post!, rowID: indexPath.row) {
                    let cell = self.tableView.cellForRow(at: indexPath) as! HomeTableViewCell
                    self.commentData = self.commentData.reversed()
                    cell.commentData = self.commentData
                    cell.commentTblView.reloadData()
                    if self.commentData.count > 0{
                        cell.updateTableContentInset()
                        self.scrollToBottom(tblComments:cell.commentTblView)
                    }
                    self.isGetCommentApiCalled = false
                }
            }
        }
    }
    func getCommentsApi(post: Post, rowID: Int, completion:@escaping()->Void){
        debugPrint("Api getCommentsApi called for row: \(rowID)")
        self.commentData = []
        let param = ["videoId":post.id,"limit":"1000","page":"\(commentPage)"]
        
        debugPrint("Input: \(param)")
        DataManager.getVideoComments(param: param) { errorMessage in
            AlertView().showAlert(message: errorMessage, delegate: self, pop: false)
        } completion: { json in
            json["data"].forEach { (message,data) in
                //print("EACHRES",data)
                self.commentData.append(CommentDataModel(data: data))
            }
            //print("CommentCount",self.commentData.count)
            debugPrint("Comment array count with api called for row", self.commentData.count, rowID)
            completion()
        }
    }

}
//MARK: - Table View Delegate
extension ViewVideoVC:UITableViewDelegate,UITableViewDataSource,UITableViewDataSourcePrefetching{
    func scrollViewDidEndScrollingAnimation(_ scrollView: UIScrollView) {
        print("lalaoals")
        self.tableView.isHidden = false
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.data.count
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: HomeTableViewCell.identifier, for: indexPath) as! HomeTableViewCell
        cell.delegate = self
        cell.configure(post: data[indexPath.row])
        //Date:: 09, Apr 2024 - here we set the index path and tags
        post = data[indexPath.row]
        cell.ibSentBtn.tag = indexPath.row
        cell.ibSentBtn.addTarget(self, action:#selector(commentButtonTapped(_:)), for: .touchUpInside)
        cell.commentTf.tag = indexPath.row
        cell.commentTf.delegate = self
        //--
        
        return cell
    }

    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return tableView.frame.height
    }
    //Date:: 09, Apr 2024 - table view content scroll to bottom
    func scrollToBottom(tblComments:UITableView){
        DispatchQueue.main.async {
            let indexPath = IndexPath(row: self.commentData.count-1, section: 0)
            debugPrint("Sel index path row", indexPath.row)
            debugPrint("Selected Comment array count", self.commentData.count)
            if indexPath.row < self.commentData.count
            {
                tblComments.scrollToRow(at: indexPath, at: .bottom, animated: false)
            }
            
        }
    }
    //----

    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        if let cell = cell as? HomeTableViewCell{
            print("INDEXPATY1",indexPath.row)
            cell.pauseImgView.alpha = 0
        }
    }

    func tableView(_ tableView: UITableView, didEndDisplaying cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        if let cell = cell as? HomeTableViewCell {
            cell.pause()
            print("INDEXPATY2",indexPath.row)
        }
    }

    func tableView(_ tableView: UITableView, prefetchRowsAt indexPaths: [IndexPath]) {
       
    }
}
//MARK: - Scrolling Methods
extension ViewVideoVC:UIScrollViewDelegate{
    func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        if !decelerate { check() }
    }
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        check()
    }
    //MARK: - Video Play & Preload Methods
    func check() {
        if(!(Constant.check_Internet?.isReachable)!){
            AlertView().showInternetErrorAlert(delegate: self)
            return
        }
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
        self.visibleCell = visibleCell
        visiblePost = visibleCell?.post
    }
    func checkPause(){
        let visibleCells = tableView.visibleCells.compactMap { $0 as? HomeTableViewCell }
        guard visibleCells.count > 0 else { return }
        let visibleFrame = CGRect(x: 0, y: tableView.contentOffset.y, width: tableView.bounds.width, height: tableView.bounds.height)
        let visibleCell = visibleCells
            .filter { visibleFrame.intersection($0.frame).height >= $0.frame.height / 2 }
            .first
        visibleCell?.pause()
        self.visibleCell = visibleCell
    }
}
//MARK: - Video Cell Navigaton Delegates
extension ViewVideoVC:HomeCellNavigationDelegate{
    func onLikeVideo(post: Post, isLike: Bool) {
        changeDelegate?.videoChange(post:post,isLiked: isLike)
    }
    
    func clickedFollowBtn(forUser id: String, isFollowing: Bool) {
        let sameUser = self.data.filter { post in
            return post.userDetails?.id == id
        }
        print("SAMEUSERVUEI",sameUser.count)
        sameUser.forEach { post in
            post.isFollow = isFollowing
        }
        visitingProfile ? delegate?.followedFromProfileVideo(isFollowing: isFollowing) : print("HAHAH")
    }
    
    func viewCountError(error: String) {
        AlertView().showAlert(message: error, delegate: self, pop: false)
    }
    
    
    
    func goTiTryAudio(withId audio :AudioDataModel) {
        let vc = TryAudioVC()
        vc.audioData = audio
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    func showComments(id: String, numberOfComments num: String) {
        let popup = CommentPopUp()
        popup.delegate = self
        popup.videoId = id
        popup.numberOfComments = num
        popup.modalPresentationStyle = .popover
        self.tabBarController?.present(popup, animated: true)
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
    }
    
    func reportVideo(withId id: String,isReported:Bool) {
        let popUp = ReportBtnPopUp()
        popUp.videoId = id
        popUp.btnDelegate = self
        popUp.isvideoReported = isReported
        popUp.delegate = self
        popUp.modalTransitionStyle = .crossDissolve
        popUp.modalPresentationStyle = .overCurrentContext
        self.tabBarController?.present(popUp, animated: true)
    }
    
    func shareVideo(withUrl  url:String,id:String) {
        let vc = SharePopUp()
        vc.videoId = id
        vc.delegate = self
        vc.modalPresentationStyle = .popover
        self.shareVideoLink = url
        self.tabBarController?.present(vc, animated: true)
    }
    func gotoUserProfileOfSupporter(withUser user: DonationUserModel) {
        let vc = VisitProfileVC()
        vc.id = user.id
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    func gotoUserProfile(withUser user:UserProfileData,isFollowing:Bool) {
        if fromProfileId == user.id{
            self.navigationController?.popViewController(animated: true)
        }else{
            let vc = VisitProfileVC()
            vc.id = user.id
            self.navigationController?.pushViewController(vc, animated: true)
        }
    }
    
    
    
    func errorOnLike(withMessage message: String) {
        AlertView().showAlert(message: message, delegate: self, pop: false)
    }
}
extension ViewVideoVC:CommentPopUpDelegate{
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


extension ViewVideoVC:EditVideoProtocol{
    func videoDeleted() {
        self.navigationController?.popToRootViewController(animated: true)
    }
    
    func videoEdited() {
        let vc = AddPostVC()
        vc.editPost = true
        vc.slug = self.visiblePost?.slug
        vc.oldCaption = self.visiblePost?.videoCaption
        vc.thumbnailImageString = self.visiblePost?.videoImage
        self.navigationController?.pushViewController(vc, animated: true)
    }
}
extension ViewVideoVC:DonationPopupDelegate{
    func donateBtnClicked(post: Post, amt: String) {
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
extension ViewVideoVC:SharePopUpDelegate{
    func newShareCount(count: String,otherUser:UserProfileData,slug:String) {
        guard let cell = visibleCell else{
            return
        }
        cell.post?.videoShareCount = count
        //cell.shareCountLbl.text = Int(count)?.shorten()
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
            //MARK: - Add App Url here
            if let name = URL(string: LocalStrings.APP_URL), !name.absoluteString.isEmpty {
                let objectsToShare = [url]
                let activityVC = UIActivityViewController(activityItems: objectsToShare, applicationActivities: nil)
                DispatchQueue.main.async {
                    self.clearAllNotice()
                    self.present(activityVC, animated: true, completion: nil)
                }
                
            } else {
                // show alert for not available
                AlertView().showAlert(message: "Error in sharing Video", delegate: self, pop: false)
            }
        }
    }
    func saveVideoToGallery(url:URL){
        DispatchQueue.main.async {
            PHPhotoLibrary.shared().performChanges({
                PHAssetChangeRequest.creationRequestForAssetFromVideo(atFileURL: url)
            }) { saved, error in
                if saved {
                    print("Saved")
                }
            }
        }
    }
}
extension ViewVideoVC:ReportVideoDelegate{
    func videoReported() {
        ToastManager.successToast(delegate: self, msg: "Video Reported Successfully")
        guard let cell = visibleCell else{
            return
        }
        cell.post?.isReported = true
        //cell.reportBtn.isHidden = true
    }
}
extension ViewVideoVC:ReportBtnDelegate{
    func alreadyReportedVideo() {
        ToastManager.errorToast(delegate: self, msg: "Video Already Reported")
    }
}
protocol ViewVideoFromProfile{
    func followedFromProfileVideo(isFollowing:Bool)
}
//Date:: 09, Apr 2024
extension ViewVideoVC {
    @objc func keyboardWillShow(_ notification: Notification) {
    }

    @objc func keyboardWillHide(_ notification: Notification) {

        //tableView.setBottomInset(to: 0.0)
//        let edgeInset = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: 0)
//
//        tableView.contentInset = edgeInset
//        tableView.scrollIndicatorInsets = edgeInset
    }
    
    @objc func closeKeyboardView(){
        self.view.endEditing(true)
    }
}
//Date:: 09, Apr 2024 - set the table view scroll after user end editing.
extension ViewVideoVC:UITextFieldDelegate{
    
    func textFieldDidBeginEditing(_ textField: UITextField) {

    }
    
    func textFieldDidEndEditing(_ textField: UITextField) {

        let indexPath = IndexPath(row: textField.tag , section: 0)
        self.tableView.scrollToRow(at: indexPath, at: .middle, animated: true)
        
    }
}
