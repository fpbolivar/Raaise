//
//  VisitProfileVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 11/12/22.
//

import UIKit

class VisitProfileVC: BaseControllerVC {
    var userDetails : UserProfileData?
    var userVideos = [Post]()
    var userVideoData: [String] = []
    var isFollowing = false
    var id = ""
    var isAccountDeleted = false
    var delegate:VisitProfileDelegate?
    @IBOutlet weak var backImage: UIImageView!
    @IBOutlet weak var collectionView: UICollectionView!
    override func viewDidLoad() {
        super.viewDidLoad()
        hideNavbar()
        backImage.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(popBack)))
        if(!(Constant.check_Internet?.isReachable)!){
            AlertView().showInternetErrorAlert(delegate: self)
            return
        }
        getUserProfile(){
            DispatchQueue.main.async {
                self.setupCollectioView()
                self.delegateCollectioView()
            }
        }
        getUserProfileVideos(){
            DispatchQueue.main.async {
                self.setupCollectioView()
                self.delegateCollectioView()
            }
        }
        // Do any additional setup after loading the view.
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
            self.isFollowing = json["data"]["follow"].boolValue
            if json["statusCode"].intValue == 400{
                self.isAccountDeleted = true
            }
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
        collectionView.register(UINib(nibName:ProfileSliderView.identifier, bundle: nil), forSupplementaryViewOfKind: UICollectionView.elementKindSectionHeader, withReuseIdentifier: ProfileSliderView.identifier)
        collectionView.register(UINib(nibName:VisitVCHeader.identifier, bundle: nil), forSupplementaryViewOfKind: UICollectionView.elementKindSectionHeader, withReuseIdentifier: VisitVCHeader.identifier)
        collectionView.register(UINib(nibName: ProfileVideoItemCell.identifier, bundle: nil), forCellWithReuseIdentifier: ProfileVideoItemCell.identifier)
    }
    func delegateCollectioView(){
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.reloadData()
    }
    
    @IBAction func settingsBtnClicked(_ sender: Any) {
        let popUp = ReportBtnPopUp()
        popUp.blockDelegate = self
        popUp.forBlock = true
        popUp.modalTransitionStyle = .crossDissolve
        popUp.modalPresentationStyle = .overCurrentContext
        self.tabBarController?.present(popUp, animated: true)
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
                headerView.setData(data: self.userDetails ?? UserProfileData(),isAccountDeleted: self.userDetails?.isDeleted ?? true)
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
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, referenceSizeForHeaderInSection section: Int) -> CGSize {
        switch section {
        case 0:
            if id == AuthManager.currentUser.id{
                return CGSize.init(width: ScreenSize.Width, height: 267)
            }else{
                return CGSize.init(width: ScreenSize.Width, height: 300)
            }
        case 1:
            return CGSize.init(width: ScreenSize.Width, height: 30)
        default:
            return .zero
        }
    }
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }

    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return userVideos.count
        //userVideoData.count
    }

    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: ProfileVideoItemCell.identifier, for: indexPath) as!  ProfileVideoItemCell
        //cell.updateCell(withImg: userVideoData[indexPath.row])
        cell.updateCellData(data: userVideos[indexPath.row])
        return cell
    }

    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let wFrame = collectionView.frame.width
        let itemWidth = (wFrame/3) - 2//( wFrame - CGFloat(Int(wFrame) % 3)) / 3.0 - 1.0
        let itemHeight =  itemWidth * 1.3
        return CGSize.init(width: itemWidth, height: itemHeight)
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return 2
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
        return 2    
    }
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let vc = ViewVideoVC()
        vc.changeDelegate = self
        vc.data = self.userVideos
        vc.selectedRow = indexPath.row
        vc.fromProfileId = self.userDetails?.id ?? ""
        vc.visitingProfile = true
        vc.delegate = self
        self.navigationController?.pushViewController(vc, animated: true)
    }

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
