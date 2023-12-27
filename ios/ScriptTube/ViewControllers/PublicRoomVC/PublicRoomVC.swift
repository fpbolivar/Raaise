//
//  PublicRoomVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 25/04/23.
//

import UIKit

class PublicRoomVC: BaseControllerVC {
    @IBOutlet weak var backBtn: UIButton!
    var liveRoomListDataAll:[LiveRoomDataModel] = []
    var liveRoomListData:[LiveRoomDataModel] = []
    var nonLiveRoomListData:[LiveRoomDataModel] = []
    @IBOutlet weak var addBtn: UIButton!
    @IBOutlet weak var titleLbl: UILabel!
    var roomPage = 1
    var index = 0
    var hideTabbar = false
    @IBOutlet weak var segment: UISegmentedControl!
    var needLoader = true
    @IBOutlet weak var collectionView: UICollectionView!
    override func viewDidLoad() {
        super.viewDidLoad()
        titleLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX20)
        if hideTabbar{
//            addNavBar(headingText: "Public Rooms to join", redText: "",type: .smallNavBarOnlyBack)
            addBtn.isHidden = true
        }else{
//            addNavBar(headingText: "Public Rooms to join", redText: "",type: .onlyTopTitle)
            backBtn.isHidden = true
        }
        
        collectionView.register(UINib(nibName: RoomCell.identifier, bundle: nil), forCellWithReuseIdentifier: RoomCell.identifier)
        collectionView.delegate = self
        collectionView.dataSource = self
        
        
        // Do any additional setup after loading the view.
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        self.tabBarController?.tabBar.isHidden = hideTabbar
        let roomParam = ["page":"\(roomPage)","limit":"30","query":""]
        getPublicRoomListApi(param: roomParam,needLoader: needLoader) {
            DispatchQueue.main.async {
                self.collectionView.reloadData()
                self.needLoader = false
                
            }
        }
    }
    @IBAction func addNewRoomClicked(_ sender: Any) {
        let vc = CreateRoomVC()
        
        self.navigationController?.pushViewController(vc, animated: true)
    }
    @IBAction func segmentChanged(_ sender: UISegmentedControl) {
        self.index = sender.selectedSegmentIndex
        collectionView.reloadData()
    }
    func getPublicRoomListApi(param:[String:String],needLoader:Bool,completion:@escaping()->Void){
        DataManager.getPublicRoomListAPI(delegate: self, param: param,needLoader: needLoader) { json in
            
            var liveRoomData:[LiveRoomDataModel] = []
            json["data"].forEach { (message,data) in
                print("MESAGE",message)
                liveRoomData.append(LiveRoomDataModel(json: data))
            }
            if self.roomPage == 1{
                self.liveRoomListDataAll = liveRoomData
                self.liveRoomListData = self.liveRoomListDataAll.filter({$0.isOnline == "1"})
                self.nonLiveRoomListData = self.liveRoomListDataAll.filter({$0.isOnline == "0"})
            }else{
                self.liveRoomListDataAll.append(contentsOf: liveRoomData)
                self.liveRoomListData.append(contentsOf: self.liveRoomListDataAll.filter({$0.isOnline == "1"}))
                self.nonLiveRoomListData.append(contentsOf: self.liveRoomListDataAll.filter({$0.isOnline == "0"}))
            }
           
            completion()
        }
    }
    @IBAction func backBtnClicked(_ sender: Any) {
        self.navigationController?.popViewController(animated: true)
    }
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        
        if let _ = scrollView as? UICollectionView{
            let height = scrollView.frame.size.height
            let contentYOffset = scrollView.contentOffset.y
            let distanceFromBottom = scrollView.contentSize.height - contentYOffset
            
            if distanceFromBottom == height{
                roomPage = roomPage + 1
                let roomParam = ["page":"\(roomPage)","limit":"30","query":""]
                getPublicRoomListApi(param: roomParam,needLoader: false) {
                    DispatchQueue.main.async {
                        self.collectionView.reloadData()
                       
                    }
                }
            }
        }
    }
}
extension PublicRoomVC:UICollectionViewDelegate,UICollectionViewDelegateFlowLayout,UICollectionViewDataSource{
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        if index == 0{
            return liveRoomListData.count
        }else{
            return nonLiveRoomListData.count
        }
       
    }
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: RoomCell.identifier, for: indexPath) as! RoomCell
        if index == 0{
            cell.updateCell(data: liveRoomListData[indexPath.row])
//            collectionView.reloadData()
        }else{
            cell.updateCell(data: nonLiveRoomListData[indexPath.row])
            //collectionView.reloadData()
        }
        
        return cell
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: collectionView.frame.width / 2 - 5, height: collectionView.frame.width / 2)
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        return 10
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
        return 5
    }
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let vc = RoomDetailVC()
        vc.isJoin = true
        if index == 0{
            vc.roomData = liveRoomListData[indexPath.row]
        }else{
            vc.roomData = nonLiveRoomListData[indexPath.row]
        }
        
        self.navigationController?.pushViewController(vc, animated: true)
    }
}
