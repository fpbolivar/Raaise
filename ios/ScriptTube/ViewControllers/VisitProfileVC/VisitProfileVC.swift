//
//  VisitProfileVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 11/12/22.
//

import UIKit

class VisitProfileVC: BaseControllerVC {
    
    @IBOutlet weak var coverImg: UIImageView!
    @IBOutlet weak var profileImg: UIImageView!
    @IBOutlet weak var lblUserName: UILabel!
    @IBOutlet weak var verifiedUserIcon: UIImageView!
    @IBOutlet weak var totalDonatedLbl: UILabel!
    @IBOutlet weak var shortBioLbl: UILabel!
    
    @IBOutlet weak var categoryCollectionView: UICollectionView!
    @IBOutlet weak var categoryHeightCOnst: NSLayoutConstraint!
    @IBOutlet weak var btnAbout: UIButton!
    @IBOutlet weak var btnPosts: UIButton!
    @IBOutlet weak var btnDonation: UIButton!
    @IBOutlet weak var btnFollow: UIButton!
    @IBOutlet var btnCollection: [UIButton]!
    @IBOutlet weak var btnFollowing: UIButton!
    
    @IBOutlet weak var viewLinePost: UIView!
    @IBOutlet weak var viewLineAbout: UIView!
    @IBOutlet weak var viewLineDonation: UIView!
    @IBOutlet weak var viewLineFollower: UIView!
    @IBOutlet weak var viewLineFollowing: UIView!
    
    @IBOutlet weak var lblFollowingBtn: UILabel!
    @IBOutlet weak var lblMessageBtn: UILabel!
    
    @IBOutlet weak var manuScrollview: UIScrollView!
    
    private func btnSelection(sender: UIButton) {
        //self.btnCollection.forEach({$0.isSelected = false})
        //sender.isSelected = true
        viewLinePost.isHidden = true
        viewLineAbout.isHidden = true
        viewLineDonation.isHidden = true
        viewLineFollower.isHidden = true
        viewLineFollowing.isHidden = true
        //UIView.animate(withDuration: 0.5) {
            //self.imgLine.center.x = sender.center.x
            switch sender{
            case self.btnAbout: 
                self.viewLineAbout.isHidden = false
                break
                
            case self.btnDonation:
                self.viewLineDonation.isHidden = false
                break
                
            case self.btnFollow:
                self.viewLineFollower.isHidden = false
                break
                
            case self.btnFollowing:
                self.viewLineFollowing.isHidden = false
                break
                
            default:
                self.viewLinePost.isHidden = false
            }
        //}
    }
    
    @IBOutlet weak var imgLine: UIView!
    @IBOutlet weak var pageContainer: UIView!
    lazy var controllers : [UIViewController] = {
        var controllers = [UIViewController]()
        
        let userPosts = UserPostsVC()
        let userAboutUs = UserAboutUsVC()
        let userDonations = UserDonationListVC()
        let following = FollowListVC()
        let follow = FollowListVC()
        controllers = [userPosts, userAboutUs, userDonations, following,follow]
        return controllers
    }()
    
    var pageController: UIPageViewController!
    var userDetails : UserProfileData?
    var interestCategoryData :[InterestCategoryData] = []
    var donationUserModelData :[DonationUserModel] = []
    var interestCategory: [String] = []
    var userVideos = [Post]()
    var userVideoData: [String] = []
    var isFollowing = false
    var id = ""
    var isAccountDeleted = false
    var delegate:VisitProfileDelegate?
    var followUserDetails = UserProfileData()
    
    @IBOutlet weak var backImage: UIImageView!
    @IBOutlet weak var collectionView: UICollectionView!
    override func viewDidLoad() {
        super.viewDidLoad()
        hideNavbar()
        applyStyle()
        setupFont()
        backImage.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(popBack)))
        if(!(Constant.check_Internet?.isReachable)!){
            AlertView().showInternetErrorAlert(delegate: self)
            return
        }
        getUserProfile(){
            DispatchQueue.main.async {
                
                
                
                self.setupCollectioView()
                self.delegateCollectioView()
                
                self.btnFollow.setTitle("Followers(\(self.userDetails?.followersCount ?? ""))", for: .normal)
                self.btnFollowing.setTitle("Following(\(self.userDetails?.followingCount ?? ""))", for: .normal)
                self.btnPosts.setTitle("Posts(\(self.userDetails?.videoCount ?? ""))", for: .normal)
               
                guard let obj1 = self.controllers.first(where: {$0 is UserAboutUsVC}) as? UserAboutUsVC else { return }
                print(self.userDetails?.shortBio ?? "")
                obj1.bioData = self.userDetails?.shortBio ?? ""
                
                guard let obj2 = self.controllers.first(where: {$0 is UserDonationListVC}) as? UserDonationListVC else { return }
                print("userDetails\(self.donationUserModelData)")
                obj2.userList = self.donationUserModelData
                obj2.totalRaisedDonation = self.userDetails?.donatedAmount ?? ""
                
                
                guard let objFollow = self.controllers.first(where: {$0 is FollowListVC}) as? FollowListVC else { return }
                //print("follower tapped")
                objFollow.isFromProfilePager = true
                objFollow.userName = self.userDetails?.userName
                objFollow.isFollowingList = false
                
                guard let objFollowing = self.controllers.last(where: {$0 is FollowListVC}) as? FollowListVC else { return }
                print("Following tapped")
                objFollowing.isFromProfilePager = true
                objFollowing.userName = self.userDetails?.userName
                objFollowing.isFollowingList = true
                //print(self.userDetails?.shortBio ?? "")
                //obj1.bioData = self.userDetails?.shortBio ?? ""
                
                
            }
        }
        
        self.categoryHeightCOnst.constant = 400
//        getUserProfileVideos(){
//            DispatchQueue.main.async {
//                self.setupCollectioView()
//                self.delegateCollectioView()
//            }
//        }
        // Do any additional setup after loading the view.
    }
    
    func setupUI() {
        if userDetails?.userName != ""{
            lblUserName.text = "@\(userDetails?.userName ?? "")"
        }
        
        setAmountLbl(withAmount: userDetails?.donatedAmount ?? "")
       // profileImg.layer.cornerRadius = self.profileImg.frame.height / 2
        
        profileImg.layer.borderWidth = 3
        profileImg.layer.borderColor = UIColor(named: "bgColor")?.cgColor
        profileImg.layer.cornerRadius = profileImg.frame.height / 2
        profileImg.layer.masksToBounds = true
        
        profileImg.loadImgForProfile(url: userDetails?.profileImage ?? "")
        coverImg.loadImgForCover(url: userDetails?.coverImage ?? "")
        //shortBioLbl.text = userDetails?.shortBio
        self.verifiedUserIcon.isHidden = !(userDetails?.isVerified ?? false)
        
    }
    
//    func setAmountLbl_old(withAmount amt:String){
//        let text = "Total Supported   ($\(amt))"
//        let underlineAttriString = NSMutableAttributedString(string: text)
//        let range1 = (text as NSString).range(of: "($\(amt))")
//             
//             underlineAttriString.addAttribute(NSAttributedString.Key.font, value: AppFont.FontName.bold.getFont(size: AppFont.pX12), range: range1)
//        totalDonatedLbl.attributedText = underlineAttriString
//    }
    func setAmountLbl(withAmount amt: String) {
        let text = "Total Raised   $\(amt)"
        
        if let range1 = text.range(of: "$\(amt)") {
            
            let attributedString = NSMutableAttributedString(string: text)
            
            attributedString.addAttribute(.font, value: AppFont.FontName.regular.getFont(size: AppFont.pX12), range: NSRange(range1, in: text))
            
            let imageAttachment = NSTextAttachment()
            imageAttachment.image = UIImage(named: "ic_coin") // Replace with your image name
            imageAttachment.bounds = CGRect(x: 0, y: -5, width: 17, height: 17) // Adjust the bounds as needed
            //imageAttachment.ali
            
            let imageString = NSAttributedString(attachment: imageAttachment)
            
            attributedString.insert(imageString, at: 16)
            
            // Set the final attributed text to the label
            totalDonatedLbl.attributedText = attributedString
        }
    }
    func setupFont() {
        lblUserName.font = AppFont.FontName.regular.getFont(size: AppFont.pX16)
        totalDonatedLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
       // shortBioLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        lblFollowingBtn.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        lblMessageBtn.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
    }
    
    func applyStyle(){
        
        viewLinePost.backgroundColor = .white
        viewLineAbout.backgroundColor = .white
        viewLineDonation.backgroundColor = .white
        viewLineFollower.backgroundColor = .white
        viewLineFollowing.backgroundColor = .white
        
        
        pageController = UIPageViewController(transitionStyle: .scroll, navigationOrientation: .horizontal, options: nil)
        pageController.dataSource = self
        pageController.delegate = self
        
        addChild(pageController)
        pageContainer.addSubview(pageController.view)
        
        pageController.view.frame = CGRect(x: 0, y: 0, width: pageContainer.frame.width, height: pageContainer.frame.height)
        
        guard let obj = self.controllers.first(where: {$0 is UserPostsVC}) as? UserPostsVC else { return }
        obj.userIdentity = self.id
        
        
        pageController.setViewControllers([controllers[0]], direction: .forward, animated: false)
        
        //imgLine.backgroundColor = .white
        
        self.btnSelection(sender: btnPosts)
    }
    
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.tabBarController?.tabBar.isHidden = true
    }
    @objc func popBack(){
        self.navigationController?.popViewController(animated: true)
    }
    //MARK: - Api Methods
    func getUserProfile(completion:@escaping()->Void){
        let param = ["userIdentity":id]
        DataManager.getOtherUserProfile(delegate: self, param: param) { json in
            self.userDetails = UserProfileData(data: json["data"])
            self.donationUserModelData = []
            debugPrint(json)
            self.isFollowing = json["data"]["follow"].boolValue
            if json["statusCode"].intValue == 400{
                self.isAccountDeleted = true
                
            }
            json["data"]["interestCategoryData"].forEach { (message,data) in
                
                self.interestCategoryData.append(InterestCategoryData(data: data))
            }
            
            //Set collection view height
            var noOfItems = self.interestCategoryData.count/2
            if self.interestCategoryData.count%2 != 0{
                noOfItems += 1
            }
            var height = noOfItems*60
            debugPrint(height)
            self.categoryHeightCOnst.constant = CGFloat(height)
            
            json["data"]["donationUsers"].forEach { (message,data) in
                
                self.donationUserModelData.append(DonationUserModel(data: data))
            }
            
            debugPrint(self.donationUserModelData)
            self.categoryCollectionView.reloadData()
            
            self.setupUI()
            completion()
        }
    }
    func getUserProfileVideos(completion:@escaping()->Void){
        let param = ["userIdentity":id,"page":"1","limit":"10"]
        DataManager.getOtherUserVideos(delegate: self, param: param) { json in
            self.userVideoData = []
            self.userVideos = []
            json["data"].forEach { (message,data) in
                self.userVideoData.append(data["videoImage"].stringValue)
                self.userVideos.append(Post(data: data))
            }
            completion()
        }
    }
    //MARK: - Setup
    func setupCollectioView(){
        let nibName = UINib(nibName: "ChooseInterestCell", bundle: nil)
        self.categoryCollectionView.register(nibName, forCellWithReuseIdentifier: ChooseInterestCell.identifier)
        //Date:: 05, Mar 2024
        
//        collectionView.register(UINib(nibName:ProfileSliderView.identifier, bundle: nil), forSupplementaryViewOfKind: UICollectionView.elementKindSectionHeader, withReuseIdentifier: ProfileSliderView.identifier)
//        collectionView.register(UINib(nibName:VisitVCHeader.identifier, bundle: nil), forSupplementaryViewOfKind: UICollectionView.elementKindSectionHeader, withReuseIdentifier: VisitVCHeader.identifier)
//        collectionView.register(UINib(nibName: ProfileVideoItemCell.identifier, bundle: nil), forCellWithReuseIdentifier: ProfileVideoItemCell.identifier)
    }
    func delegateCollectioView(){
        
        self.categoryCollectionView.delegate = self
        self.categoryCollectionView.dataSource = self
        //Date:: 05, Mar 2024 - old collectionView
//        collectionView.delegate = self
//        collectionView.dataSource = self
//        collectionView.reloadData()
    }
    
    @IBAction func settingsBtnClicked(_ sender: Any) {
        let popUp = ReportBtnPopUp()
        popUp.blockDelegate = self
        popUp.forBlock = true
        popUp.modalTransitionStyle = .crossDissolve
        popUp.modalPresentationStyle = .overCurrentContext
        self.tabBarController?.present(popUp, animated: true)
    }
    
    
    //MARK: - Button Action Methods -
    
    @IBAction func postsTapped(_ sender: UIButton) {
        pageController.setViewControllers([controllers[0]], direction: .reverse, animated: false)
        btnSelection(sender: sender)
    }
    
    @IBAction func aboutUsTapped(_ sender: UIButton) {
        pageController.setViewControllers([controllers[1]], direction: .reverse, animated: false)
        btnSelection(sender: sender)
    }
    
    @IBAction func donationTapped(_ sender: UIButton) {
        pageController.setViewControllers([controllers[2]], direction: .reverse, animated: false)
        btnSelection(sender: sender)
    }
    @IBAction func followerTapped(_ sender: UIButton) {
        pageController.setViewControllers([controllers[3]], direction: .reverse, animated: false)
        btnSelection(sender: sender)
    }
    @IBAction func followingTapped(_ sender: UIButton) {
        pageController.setViewControllers([controllers[4]], direction: .reverse, animated: false)
        btnSelection(sender: sender)
    }
    
    @IBAction func followingBtn(_ sender: UIButton) {
        DataManager.followUnfollowUser(param: ["followerTo":id]) { error in
            print(error)
        } completion: {
            self.userDetails?.follow = !self.userDetails!.follow
            if self.userDetails!.follow{
                var count = Int(self.userDetails!.followersCount) ?? 0
                count = count + 1
                self.userDetails!.followersCount = "\(count)"
                
                //self.followerLbl.text = "Followers \(count)"
                if count > 1{
                    //self.followerLbl.text = "Followers (\(count))"
                }else{
                    //self.followerLbl.text = "Follower (\(count))"
                }
                //self.followerCount.text = "\(count)"
                self.lblFollowingBtn.text = "Following"
            }else{
                var count = Int(self.userDetails!.followersCount) ?? 1
                   count = count - 1
                
                self.userDetails!.followersCount = "\(count)"
                
                self.lblFollowingBtn.text = "Follow"
//
//                if count > 1{
//                    self.followerLbl.text = "Followers (\(count))"
//                }else{
//                    self.followerLbl.text = "Follower (\(count))"
//                }
                //self.followerCount.text = "\(count)"
            }
            //self.btnDelegate?.followBtnClicked(withId: self.id,isFollowing: self.userDetails!.follow)
            self.followBtnClicked(withId: self.userDetails!.id, isFollowing: self.userDetails!.follow)
        }
    }
    
    @IBAction func messageBtn(_ sender: UIButton) {
        //self.btnDelegate?.messageBtnClicked(withId: userDetails!)
        self.messageBtnClicked(withId: userDetails!)
    }
    
}
//MARK: - Collection View Delegate
extension VisitProfileVC:UICollectionViewDelegate,UICollectionViewDataSource, UICollectionViewDelegateFlowLayout{
    func collectionView(_ collectionView: UICollectionView,
                        viewForSupplementaryElementOfKind kind: String,
                        at indexPath: IndexPath) -> UICollectionReusableView {

        switch kind {

            case UICollectionView.elementKindSectionHeader:
            if indexPath.section == 0{
                let headerView = collectionView.dequeueReusableSupplementaryView(ofKind: kind, withReuseIdentifier: VisitVCHeader.identifier, for: indexPath) as! VisitVCHeader
                headerView.setData(data: self.userDetails ?? UserProfileData(),
                                   isAccountDeleted: self.userDetails?.isDeleted ?? true)
                headerView.delegate = self
                headerView.btnDelegate = self
                if isFollowing{
                    headerView.followingBtnLbl.text = "Unfollow"
                }else{
                    headerView.followingBtnLbl.text = "Follow"
                }
                //headerView.backgroundColor = UIColor.blue
                return headerView

            }
            default:
                assert(false, "Unexpected element kind")
        }
        return UICollectionReusableView.init()
    }
//    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, referenceSizeForHeaderInSection section: Int) -> CGSize {
//        switch section {
//        case 0:
//            if id == AuthManager.currentUser.id{
//                //self.profileHeightCOnst.constant = 267
//                return CGSize.init(width: ScreenSize.Width, height: 267)
//            }else{
//                //self.profileHeightCOnst.constant = 400
//                return CGSize.init(width: ScreenSize.Width, height: 400)
//            }
//        case 1:
//            return CGSize.init(width: ScreenSize.Width, height: 30)
//        default:
//            return .zero
//        }
//    }
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }

    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        //return userVideos.count
        return interestCategoryData.count
        //userVideoData.count
    }

    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        //let cell = collectionView.dequeueReusableCell(withReuseIdentifier: ProfileVideoItemCell.identifier, for: indexPath) as!  ProfileVideoItemCell
        //cell.updateCell(withImg: userVideoData[indexPath.row])
        //cell.updateCellData(data: userVideos[indexPath.row])
        let interestCell = collectionView.dequeueReusableCell(withReuseIdentifier: ChooseInterestCell.identifier, for: indexPath) as! ChooseInterestCell
        interestCell.chooseInterestLabel.text = interestCategoryData[indexPath.row].name
        interestCell.chooseInterestImg.loadImg(url:interestCategoryData[indexPath.row].image)
        
        
        return interestCell
        
//        return cell
    }

    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
//        let wFrame = collectionView.frame.width
//        let itemWidth = (wFrame/3) - 2//( wFrame - CGFloat(Int(wFrame) % 3)) / 3.0 - 1.0
//        let itemHeight =  itemWidth * 1.3
//        return CGSize.init(width: itemWidth, height: itemHeight)
        return CGSize(width: (self.categoryCollectionView.bounds.width / 2) - 20,height: (50))
        
    }
//    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
//        return 2
//    }
//    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
//        return 2    
//    }
//    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
//        let vc = ViewVideoVC()
//        vc.changeDelegate = self
//        vc.data = self.userVideos
//        vc.selectedRow = indexPath.row
//        vc.fromProfileId = self.userDetails?.id ?? ""
//        vc.visitingProfile = true
//        vc.delegate = self
//        self.navigationController?.pushViewController(vc, animated: true)
//    }

}
//MARK: - Navigation Delegate
extension VisitProfileVC:UserHeaderReusableViewProtocol{
    func gotoFollowersListVC(isForFollowing: Bool) {
        let vc = FollowListVC()
        vc.userName = self.userDetails?.userName
        vc.isFollowingList = isForFollowing
        self.navigationController?.pushViewController(vc, animated: true)
    }
}
extension VisitProfileVC:VisitProfileBtnDelegate{
    func followBtnClicked(withId id: String,isFollowing:Bool) {
        self.userVideos.forEach { post in
            post.isFollow = isFollowing
        }
        self.delegate?.followActionChanged(withId: id, isFollowing: isFollowing)
    }
    
    func messageBtnClicked(withId id: UserProfileData) {
        let param = ["receiverId":id.id,"senderId":AuthManager.currentUser.id]
        print("CHATSLUGPARAMM",param)
        getchatSlug(param: param) { slug in
            DispatchQueue.main.async {
                let vc = ChatVC()
                vc.otherUser = id
                vc.chatSlug = slug
                self.navigationController?.pushViewController(vc, animated: true)
            }
        }
        
    }
    func getchatSlug(param:[String:String],completion:@escaping(String)->Void){
        DataManager.createCHatSlugApi(delegate: self, param: param) { data in
            completion(data["chatSlug"].stringValue)
            
        }
    }
    
}
extension VisitProfileVC:ViewVideoFromProfile{
    func followedFromProfileVideo(isFollowing: Bool) {
        self.userDetails?.follow = isFollowing
        if let header = collectionView.supplementaryView(forElementKind: UICollectionView.elementKindSectionHeader, at: IndexPath(item: 0, section: 0)) as? VisitVCHeader {
            if self.userDetails!.follow{
                header.followingBtnLbl.text = "Unfollow"
            }else{
                header.followingBtnLbl.text = "Follow"
            }
        }
    }
}
extension VisitProfileVC:BlockUserDelegate{
    func blockUser() {
        let vc = BlockUserPopUp()
        guard let user = self.userDetails else{return}
        vc.username = user.userName
        vc.userId = user.id
        vc.modalTransitionStyle = .crossDissolve
        vc.modalPresentationStyle = .overCurrentContext
        self.present(vc, animated: true)
    }
}

extension VisitProfileVC:ViewVideoChangeDelegate{
    func videoChange(post:Post,isLiked: Bool) {
        self.delegate?.videoLiked(post:post,isLiked: isLiked)
    }
}
//MARK: - Protocol
protocol VisitProfileDelegate{
    func followActionChanged(withId id:String,isFollowing:Bool)
    func videoLiked(post:Post,isLiked:Bool)
}


//MARK: - Page contrller Delegate

extension VisitProfileVC : UIPageViewControllerDataSource , UIPageViewControllerDelegate{
    //MARK:- PageViewController Delegate and DataSource Method
    
    func pageViewController(_ pageViewController: UIPageViewController, viewControllerAfter viewController: UIViewController) -> UIViewController?{
        guard let viewControllerIndex = controllers.firstIndex(of: viewController) else {
            return nil
        }
        
        let nextIndex = viewControllerIndex + 1
        let orderedViewControllersCount = controllers.count
        
        guard orderedViewControllersCount != nextIndex else {
            return nil
        }
        
        guard orderedViewControllersCount > nextIndex else {
            return nil
        }
        
        return controllers[nextIndex]
    }
    
    func pageViewController(_ pageViewController: UIPageViewController, viewControllerBefore viewController: UIViewController) -> UIViewController?{
        guard  let viewControllerIndex = controllers.firstIndex(of: viewController)
            else
        {
            return nil
        }
        
        let previousIndex = viewControllerIndex - 1
        
        guard previousIndex >= 0 else {
            return nil
        }
        
        guard controllers.count > previousIndex else {
            return nil
        }
        
        return controllers[previousIndex]
        
    }
    
    func pageViewController(_ pageViewController: UIPageViewController, didFinishAnimating finished: Bool, previousViewControllers: [UIViewController], transitionCompleted completed: Bool){
        if completed{
            if let index = controllers.firstIndex(of: pageViewController.viewControllers!.first!){
                if index == 0 {
                    self.btnSelection(sender: btnPosts)
                    manuScrollview.setContentOffset(CGPoint(x: 0, y: 0), animated: true)
                }
                else if index == 1 {
                    self.btnSelection(sender: btnAbout)
                    manuScrollview.setContentOffset(CGPoint(x: 0, y: 0), animated: true)
                }
                else if index == 2 {
                    self.btnSelection(sender: btnDonation)
                    manuScrollview.setContentOffset(CGPoint(x: 50, y: 0), animated: true)
                }
                else if index == 3 {
                    self.btnSelection(sender: btnFollow)
                    manuScrollview.setContentOffset(CGPoint(x: 100, y: 0), animated: true)
                }
                else if index == 4 {
                    self.btnSelection(sender: btnFollowing)
                    manuScrollview.setContentOffset(CGPoint(x: 110, y: 0), animated: true)
                }
                
                
            }
        }
    }
}

