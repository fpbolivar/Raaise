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
    @IBOutlet weak var backImage: UIImageView!
    @IBOutlet weak var collectionView: UICollectionView!
    override func viewDidLoad() {
        super.viewDidLoad()
        hideNavbar()
        backImage.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(popBack)))
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
    @objc func popBack(){
        self.navigationController?.popViewController(animated: true)
    }
    func getUserProfile(completion:@escaping()->Void){
        let param = ["userIdentity":id]
        DataManager.getOtherUserProfile(delegate: self, param: param) { json in
            self.userDetails = UserProfileData(data: json["data"])
            self.isFollowing = json["data"]["follow"].boolValue
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
        let vc = SettingVC()
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
}
extension VisitProfileVC:UICollectionViewDelegate,UICollectionViewDataSource, UICollectionViewDelegateFlowLayout{
    func collectionView(_ collectionView: UICollectionView,
                        viewForSupplementaryElementOfKind kind: String,
                        at indexPath: IndexPath) -> UICollectionReusableView {

        switch kind {

            case UICollectionView.elementKindSectionHeader:
            if indexPath.section == 0{
                let headerView = collectionView.dequeueReusableSupplementaryView(ofKind: kind, withReuseIdentifier: VisitVCHeader.identifier, for: indexPath) as! VisitVCHeader
                headerView.setData(data: self.userDetails ?? UserProfileData())
                headerView.delegate = self
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
            return CGSize.init(width: ScreenSize.Width, height: 300)
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
        return userVideoData.count
    }

    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: ProfileVideoItemCell.identifier, for: indexPath) as!  ProfileVideoItemCell
        cell.updateCell(withImg: userVideoData[indexPath.row])
        return cell
    }

    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let wFrame = collectionView.frame.width
        let itemWidth = (wFrame/3)//( wFrame - CGFloat(Int(wFrame) % 3)) / 3.0 - 1.0
        let itemHeight =  itemWidth * 1.3
        return CGSize.init(width: itemWidth, height: itemHeight)
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return 0
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
        return 0
    }
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let vc = ViewVideoVC()
        vc.data = self.userVideos
        vc.selectedRow = indexPath.row
        self.navigationController?.pushViewController(vc, animated: true)
    }

}
extension VisitProfileVC:UserHeaderReusableViewProtocol{
    func gotoFollowersListVC(isForFollowing: Bool) {
        //
    }
}
