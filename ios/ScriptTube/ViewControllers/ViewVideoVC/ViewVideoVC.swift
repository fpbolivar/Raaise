//
//  ViewVideoVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 08/12/22.
//

import UIKit

class ViewVideoVC: BaseControllerVC {
    @IBOutlet weak var menuImage: UIImageView!
    @IBOutlet weak var backImage: UIImageView!
    @IBOutlet weak var tableView: UITableView!
    var data = [Post]()
    var items: [URL] = []
    var selectedRow = 0
    var selectedIndexPath: IndexPath!
    var visitingProfile = true
    var visiblePost: Post?
    override func viewDidLoad() {
        super.viewDidLoad()
        backImage.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(popBack)))
        menuImage.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(openMenu)))
        hideNavbar()
        //addNavBar(headingText: "User Videos", redText: "Videos")
        setupTableView()
        // Do any additional setup after loading the view.
    }
    @objc func popBack(){
        self.navigationController?.popViewController(animated: true)
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
        selectedIndexPath = IndexPath(row: selectedRow, section: 0)
        DispatchQueue.main.async {
            self.tableView.scrollToRow(at: self.selectedIndexPath, at: .top, animated: false)
        }
        
    }
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        check()
    }
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        checkPause()
    }
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
        }
        
        
    }
}
extension ViewVideoVC:UITableViewDelegate,UITableViewDataSource,UITableViewDataSourcePrefetching{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.data.count
    }

    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: HomeTableViewCell.identifier, for: indexPath) as! HomeTableViewCell
        cell.delegate = self
        cell.configure(post: data[indexPath.row])
        
        visitingProfile ? print("SHOWDONATION") : cell.viewProfileVideo()
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
    }

    func tableView(_ tableView: UITableView, prefetchRowsAt indexPaths: [IndexPath]) {
       
    }
}
extension ViewVideoVC:UIScrollViewDelegate{
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
    }
}
extension ViewVideoVC:HomeCellNavigationDelegate{
    
    
    func goTiTryAudio(withId audio :AudioDataModel) {
        //
    }
    
    func showComments(id: String, numberOfComments num: String) {
        let popup = CommentPopUp()
        popup.delegate = self
        popup.videoId = id
        popup.numberOfComments = num
        popup.modalPresentationStyle = .pageSheet
        self.tabBarController?.present(popup, animated: true)
    }
    
    func donationPopUp() {
        //
    }
    
    func navigateToTryAudio() {
        //
    }
    
    func reportVideo(withId id: String) {
        let popUp = ReportBtnPopUp()
        popUp.videoId = id
        popUp.modalTransitionStyle = .crossDissolve
        popUp.modalPresentationStyle = .overCurrentContext
        self.tabBarController?.present(popUp, animated: true)
    }
    
    func shareVideo(withUrl  url:String) {
        if let name = URL(string: "https://itunes.apple.com/us/app/myapp/idxxxxxxxx?ls=1&mt=8"), !name.absoluteString.isEmpty {
          let objectsToShare = [name]
          let activityVC = UIActivityViewController(activityItems: objectsToShare, applicationActivities: nil)
          self.present(activityVC, animated: true, completion: nil)
        } else {
            AlertView().showAlert(message: "ERROR in sharing", delegate: self, pop: false)
        }
    }
    
    func gotoUserProfile(withUser user:UserProfileData,isFollowing:Bool) {
        //
    }
    
    
    
    func errorOnLike(withMessage message: String) {
        AlertView().showAlert(message: message, delegate: self, pop: false)
    }
}
extension ViewVideoVC:CommentPopUpDelegate{
    func dismissToVisitProfile(withId id: String) {
        let vc = VisitProfileVC()
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
