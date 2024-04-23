//
//  UserPostsVC.swift
//  ScriptTube
//
//  Created by VivaJiva  on 04/03/24.
//

import UIKit

class UserPostsVC: BaseControllerVC {
    
    var delegate:VisitProfileDelegate?
    @IBOutlet weak var collectionView: UICollectionView!
    var userVideos = [Post]()
    var userVideoData: [String] = []
    var userIdentity = ""
    var userDetails : UserProfileData?
    
    //Date:: 06, Mar 2024
//    var userDetails : UserProfileData?
//    var delegate:VisitProfileDelegate?
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        
        getUserProfileVideos(){
            DispatchQueue.main.async {
                self.setupCollectioView()
                self.delegateCollectioView()
            }
        }
    }
    
    


    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */
    
    func getUserProfileVideos(completion:@escaping()->Void){
        let param = ["userIdentity":userIdentity,"page":"1","limit":"10"]
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
        collectionView.register(UINib(nibName: ProfileVideoItemCell.identifier, bundle: nil), forCellWithReuseIdentifier: ProfileVideoItemCell.identifier)
    }
    func delegateCollectioView(){
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.reloadData()
    }

}

//MARK: - Collection View Delegate
extension UserPostsVC:UICollectionViewDelegate,UICollectionViewDataSource, UICollectionViewDelegateFlowLayout{
    func collectionView(_ collectionView: UICollectionView,
                        viewForSupplementaryElementOfKind kind: String,
                        at indexPath: IndexPath) -> UICollectionReusableView {

        
        return UICollectionReusableView.init()
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, referenceSizeForHeaderInSection section: Int) -> CGSize {
        return .zero
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
extension UserPostsVC:ViewVideoChangeDelegate{
    func videoChange(post:Post,isLiked: Bool) {
        self.delegate?.videoLiked(post:post,isLiked: isLiked)
    }
}
extension UserPostsVC: ViewVideoFromProfile {
    func followedFromProfileVideo(isFollowing: Bool) {
        
    }
    
}
