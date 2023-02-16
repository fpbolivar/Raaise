//
//  ProfileVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/21/22.
//

import UIKit

class ProfileVC: UIViewController {
    @IBOutlet weak var uploadView: CardView!
    @IBOutlet weak var  collectionView:UICollectionView!
    var userVideoData: [String] = []
    var userVideos = [Post]()
    var needLoader = true
    override func viewDidLoad() {
        super.viewDidLoad()
        hideNavbar()
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.uploadView.isHidden = !VideoUploadStatus.isUploadingVar
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
            return CGSize.init(width: ScreenSize.Width, height: 260)
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
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: ProfileVideoItemCell.identifier, for: indexPath) as!  ProfileVideoItemCell
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
