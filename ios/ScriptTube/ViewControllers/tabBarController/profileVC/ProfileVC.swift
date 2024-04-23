//
//  ProfileVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/21/22.
//

import UIKit

class ProfileVC: UIViewController {
    @IBOutlet weak var progress: CircleProgress!
    @IBOutlet weak var uploadView: CardView!
    @IBOutlet weak var  collectionView:UICollectionView!
    @IBOutlet weak var applyForView: UIView!
    

    var refreshControl = UIRefreshControl()
    var customView: CustomRefreshControl!
    var userVideoData: [String] = []
    var userVideos = [Post]()
    var needLoader = true
    override func viewDidLoad() {
        super.viewDidLoad()
        hideNavbar()
        progress.layer.cornerRadius = progress.frame.height / 2
        progress.forgroundColor = .white
        setupUI()
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        print("UPLPADSTATUS",VideoUploadStatus.isUploadingVar)
        self.uploadView.isHidden = !VideoUploadStatus.isUploadingVar
        VideoUploadStatus.progress = { prog in
           
            self.progress.progress = CGFloat(Double(prog)/100.00)
            print("UPLPADSTATUS",VideoUploadStatus.isUploadingVar,prog)
        }
        VideoUploadStatus.isUploading = { isUploading in
            self.uploadView.isHidden = !isUploading
        }
        if(!(Constant.check_Internet?.isReachable)!){
            AlertView().showInternetErrorAlert(delegate: self)
            return
        }
        getProfileApi(needLoader: needLoader)
        needLoader = false
        self.tabBarController?.tabBar.isHidden = false
        
        
    }
    func addRefreshControl() {

        guard let customView = Bundle.main.loadNibNamed("RefreshContents", owner: nil, options: nil) else {
            return
        }

        guard let refreshView = customView[0] as? CustomRefreshControl else {
            return
        }


        refreshView.frame = refreshControl.frame
        self.customView = refreshView
        refreshControl.addSubview(refreshView)

        refreshControl.tintColor = UIColor.clear
        refreshControl.backgroundColor = UIColor.clear

        refreshControl.addTarget(self, action: #selector(refresh), for: .valueChanged)
        if #available(iOS 10.0, *) {
            collectionView.refreshControl = refreshControl
        } else {
            collectionView.addSubview(refreshControl)
        }
    }
    func setupUI() {
        applyForView.isUserInteractionEnabled = true
        applyForView.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(blockUser)))
        
        if AuthManager.currentUser.isVerified == true {
            applyForView.isHidden = true
        } else {
            applyForView.isHidden = false
        }
    }
    
    @objc func blockUser(){
        applyForVerificationApi() { }
    }
    func applyForVerificationApi(completion:@escaping()->Void){
        let param = ["isUserVerified":"true"]
        DataManager.applyForVerification(delegate: self, param: param) { json in
           
            if json["statusCode"].intValue == 400{
                
            }
            if json["status"].intValue == 200{
                print("Status is 200")
                ToastManager.successToast(delegate: self, msg: json["message"].stringValue)
            }
            
            completion()
        }
    }
    
    @objc func refresh(){
        refreshControl.beginRefreshing()
        self.customView.spinner.startAnimating()
        getProfileApi(needLoader: false)
    }
    //MARK: - Api Methods
    func getProfileVideos(needloader:Bool,completion:@escaping()->Void){
        needloader ? self.pleaseWait() : print("NOLOADER")
        DataManager.getUserVideos(delegate: self) { jsonData in
            self.userVideoData = []
            self.userVideos = []
            jsonData["data"].forEach { (message,data) in
                self.userVideoData.append(data["videoImage"].stringValue)
                self.userVideos.append(Post(data: data))
            }
            needloader ? self.clearAllNotice() : print("NOLOADER")
            completion()
            guard let _ = self.customView else{return}
            self.refreshControl.endRefreshing()
            self.customView.spinner.stopAnimating()
            
        }
    }
    func getProfileApi(needLoader:Bool){
        getProfileVideos(needloader:needLoader){
            AuthManager.getProfileApi(delegate: self,needLoader: needLoader) {
                DispatchQueue.main.async {
                    self.setupCollectioView()
                    self.delegateCollectioView()
                }
            }
        }
    }
    //MARK: - Setup
    func setupCollectioView(){
        collectionView.register(UINib(nibName:ProfileSliderView.identifier, bundle: nil), forSupplementaryViewOfKind: UICollectionView.elementKindSectionHeader, withReuseIdentifier: ProfileSliderView.identifier)
        collectionView.register(UINib(nibName:UserHeaderReusableView.identifier, bundle: nil), forSupplementaryViewOfKind: UICollectionView.elementKindSectionHeader, withReuseIdentifier: UserHeaderReusableView.identifier)
        collectionView.register(UINib(nibName: ProfileVideoItemCell.identifier, bundle: nil), forCellWithReuseIdentifier: ProfileVideoItemCell.identifier)
        self.addRefreshControl()
    }
    func delegateCollectioView(){
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.reloadData()
    }
    @IBAction func settingAction(_ sender: AnyObject) {
        let vc = SettingVC()
        self.navigationController?.pushViewController(vc, animated: true)
    }
}
//MARK: - Collection View Delegate
extension ProfileVC:UICollectionViewDelegate,UICollectionViewDataSource, UICollectionViewDelegateFlowLayout{
    func collectionView(_ collectionView: UICollectionView,
                        viewForSupplementaryElementOfKind kind: String,
                        at indexPath: IndexPath) -> UICollectionReusableView {

        switch kind {

            case UICollectionView.elementKindSectionHeader:
            if indexPath.section == 0{
                let headerView = collectionView.dequeueReusableSupplementaryView(ofKind: kind, withReuseIdentifier: UserHeaderReusableView.identifier, for: indexPath) as! UserHeaderReusableView
                headerView.setData(data: AuthManager.currentUser)
                headerView.delegate = self
//                headerView.profileImgView.layer.cornerRadius = headerView.profileImgView.frame.height / 2
//                headerView.profileImgView.layer.borderWidth = 3
//                headerView.profileImgView.layer.borderColor = UIColor(named: "bgColor")?.cgColor
//                headerView.profileImgView.clipsToBounds = true
//                headerView.profileImgView.layer.masksToBounds = true
                headerView.backgroundColor = UIColor.blue
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
            return CGSize.init(width: ScreenSize.Width, height: 320)
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
    }

    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: ProfileVideoItemCell.identifier, for: indexPath) as! ProfileVideoItemCell
        cell.updateCellData(data: userVideos[indexPath.row])
        return cell
    }

    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let wFrame = collectionView.frame.width
        let itemWidth = (wFrame/3) - 2
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
        vc.data = self.userVideos
        vc.visitingProfile = false
        vc.selectedRow = indexPath.row
        vc.fromProfileId = AuthManager.currentUser.id
        self.navigationController?.pushViewController(vc, animated: true)
    }

}
class ProfileCollectionViewCell: UICollectionViewCell {

    override init(frame: CGRect) {
        super.init(frame: frame)
        backgroundColor = UIColor.gray.withAlphaComponent(0.5)
    }

    required init?(coder aDecoder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
}
extension ProfileVC:UserHeaderReusableViewProtocol{
    func gotoFollowersListVC(isForFollowing: Bool) {
        let vc = FollowListVC()
        vc.isFollowingList = isForFollowing
        self.navigationController?.pushViewController(vc, animated: true)
    }
}
