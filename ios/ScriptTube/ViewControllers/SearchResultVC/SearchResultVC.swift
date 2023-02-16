//
//  SearchResultVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 23/12/22.
//

import UIKit

class SearchResultVC: BaseControllerVC {
    //MARK: - UI Components
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var seachTf: UITextField!
    @IBOutlet weak var noResultLbl:UILabel!
    
    //MARK: - Variables
    var resultType:ResultType!
    var selectedIndex: IndexPath? = nil
    var audioListData:[AudioDataModel] = []
    var userList:[UserProfileData] = []
    var userVideos:[Post] = []
    var searchParam:[String:String]!
    var isPlaying:Bool = false
    var searchText = ""
    var page = 1
    var searchMore = true
    var oldSize = 0
    var tableSetup = false
    var collectionSetup = false
    var selectedCell:SelectionCell!
    override func viewDidLoad() {
        super.viewDidLoad()
        hideNavbar()
        collectionView.register(UINib(nibName: ProfileVideoItemCell.identifier, bundle: nil), forCellWithReuseIdentifier: ProfileVideoItemCell.identifier)
        tableView.register(UINib(nibName: SelectionCell.identifier, bundle: nil), forCellReuseIdentifier: SelectionCell.identifier)
        addNavBar(headingText: "", redText: "",type: .smallNavBarOnlyBack)
        seachTf.paddingLeftRightTextField(left: 35, right: 0)
        seachTf.layer.cornerRadius = 10
        seachTf.text = searchText
        seachTf.delegate = self
        self.pleaseWait()
        setup(withParam: searchParam){
            
        }
        // Do any additional setup after loading the view.
    }
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        guard let selectedCell = selectedCell else{return}
        selectedCell.pause()
    }
    //MARK: - Api Methods
    func getAudioApi(param:[String:String],completion:@escaping()->Void){
        print("SerachParam",param)
        DataManager.globalSearchApi(delegate: self, param: param) { json in
            json["data"]["audios"].forEach { (message,data) in
                self.audioListData.append(AudioDataModel(data: data))
            }
            completion()
        }
    }
    func getVideoApi(param:[String:String],completion:@escaping()->Void){
        print("SerachParam",param)
        DataManager.globalSearchApi(delegate: self, param: param) { json in
            json["data"]["posts"].forEach { (message,data) in
                self.userVideos.append(Post(data: data))
            }
            if self.oldSize == self.userVideos.count{
                self.searchMore = false
            }
            self.oldSize = self.userVideos.count
            completion()
        }
    }
    func userListApi(param:[String:String],completion:@escaping()->Void){
        print("SerachParam",param)
        DataManager.globalSearchApi(delegate: self, param: param) { json in
            json["data"]["users"].forEach { (message,data) in
                self.userList.append(UserProfileData(data: data))
            }
            
            completion()
        }
    }
    //MARK: - Setup
    func setup(withParam param:[String:String],completion:@escaping()->Void){
        switch resultType{
        case.audio:
            seachTf.attributedPlaceholder = NSAttributedString(string: "Search Music",attributes: [.foregroundColor: UIColor.lightGray])
            collectionView.isHidden = true
            tableView.isHidden = false
            getAudioApi(param: param) {
                DispatchQueue.main.async {
                    self.tableSetup ? self.tableView.reloadData() : self.setupTable()
                    self.clearAllNotice()
                    completion()
                }
            }
            break
        case.post:
            seachTf.attributedPlaceholder = NSAttributedString(string: "Search Posts",attributes: [.foregroundColor: UIColor.lightGray])
            collectionView.isHidden = false
            tableView.isHidden = true
            getVideoApi(param: param) {
                DispatchQueue.main.async {
                    self.setupCollectionView()
                    //self.collectionView.reloadData()
                    self.clearAllNotice()
                    completion()
                }
            }
            break
        case.users:
            seachTf.attributedPlaceholder = NSAttributedString(string: "Search Users",attributes: [.foregroundColor: UIColor.lightGray])
            collectionView.isHidden = true
            tableView.isHidden = false
            userListApi(param: param) {
                DispatchQueue.main.async {
                    self.tableSetup ? self.tableView.reloadData() : self.setupTable()
                    self.clearAllNotice()
                    completion()
                }
            }
            break
        case .none:
            print("alo")
        }
    }
    func setupTable(){
        tableView.delegate = self
        tableView.dataSource = self
        tableView.reloadData()
        tableSetup = true
    }
    func setupCollectionView(){
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.reloadData()
        collectionSetup = true
    }
    //MARK: - Pagination
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        let height = scrollView.frame.size.height
        let contentYOffset = scrollView.contentOffset.y
        let distanceFromBottom = scrollView.contentSize.height - contentYOffset
        if distanceFromBottom == height{
            print("helloajsk")
            print("You reached end of the table")
            page = page + 1
            if resultType == .audio{
                let param = ["search":seachTf.text!,"limit":"10","page":"\(page)","type":"audio"]
                getAudioApi(param: param) {
                    DispatchQueue.main.async {
                        self.tableView.reloadData()
                    }
                }
            }else if resultType == .users{
                let param = ["search":seachTf.text!,"limit":"10","page":"\(page)","type":"user"]
                userListApi(param: param) {
                    DispatchQueue.main.async {
                        self.tableView.reloadData()
                    }
                }
            }
        }
    }
}
//MARK: - Table View Delegate
extension SearchResultVC:UITableViewDelegate,UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if resultType == .users{
            return userList.count
        }else if resultType == .audio{
            return audioListData.count
        }else{
            return 0
        }
    }
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if resultType == .audio{
            let vc = TryAudioVC()
            vc.audioData = audioListData[indexPath.row]
            self.navigationController?.pushViewController(vc, animated: true)
        }else if resultType == .users{
            let vc = VisitProfileVC()
            vc.id = userList[indexPath.row].id
            self.navigationController?.pushViewController(vc, animated: true)
        }
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 70
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if resultType == .audio{
            let cell =  tableView.dequeueReusableCell(withIdentifier: SelectionCell.identifier, for: indexPath) as! SelectionCell
            cell.profileImg.contentMode = .scaleAspectFit
            cell.selectionStyle = .none
                cell.playAudio = { url in
                    self.selectedCell = cell
                    if self.selectedIndex == nil{
                        self.selectedIndex = indexPath
                        if self.isPlaying{
                            cell.pause()
                            cell.playBtn.setImage(UIImage(systemName: "play.fill"), for: .normal)
                        }else{
                            cell.play()
                            cell.playBtn.setImage(UIImage(systemName: "pause.fill"), for: .normal)
                        }
                    }else if self.selectedIndex == indexPath{
                        if self.isPlaying{
                            cell.pause()
                            cell.playBtn.setImage(UIImage(systemName: "play.fill"), for: .normal)
                        }else{
                            
                            cell.play()
                            cell.playBtn.setImage(UIImage(systemName: "pause.fill"), for: .normal)
                        }
                        self.selectedIndex = nil
                    }else{
                        
                        let cell2 = tableView.cellForRow(at: self.selectedIndex!) as! SelectionCell
                            cell2.pause()
                            cell.play()
                            cell2.playBtn.setImage(UIImage(systemName: "play.fill"), for: .normal)
                            cell.playBtn.setImage(UIImage(systemName: "pause.fill"), for: .normal)
                        self.selectedIndex = indexPath
                    }
                    self.isPlaying = !self.isPlaying
                }
                cell.updateCellForAudio(data: self.audioListData[indexPath.row])
                return cell
        }else if resultType == .users{
            let cell = tableView.dequeueReusableCell(withIdentifier: SelectionCell.identifier, for: indexPath) as! SelectionCell
            cell.userList(data: self.userList[indexPath.row])
            cell.selectionStyle = .none
            return cell
        }else{
            return UITableViewCell()
        }
    }
}

//MARK: - Collection View Delegate
extension SearchResultVC:UICollectionViewDelegate,UICollectionViewDelegateFlowLayout,UICollectionViewDataSource{
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
        vc.selectedRow = indexPath.row
        self.navigationController?.pushViewController(vc, animated: true)
    }
    //MARK: - CollectionView Pagination
    func collectionView(_ collectionView: UICollectionView, willDisplay cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        if indexPath.row == userVideos.count - 2 && searchMore{
            self.page = page + 1
            let param = ["search":seachTf.text!,"limit":"10","page":"\(page)","type":"post"]
            getVideoApi(param: param) {
                DispatchQueue.main.async {
                    self.collectionView.reloadData()
                }
            }
        }
    }
}
//MARK: - Search Delegate
extension SearchResultVC:UITextFieldDelegate{
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
            NSObject.cancelPreviousPerformRequests(
            withTarget: self,
            selector: #selector(getHintsFromTextField),
            object: textField)
        self.perform(
            #selector(getHintsFromTextField),
            with: textField,
            afterDelay: 0.5)
        return true
    }
    @objc func getHintsFromTextField(textField: UITextField) {
        print("Hints for textField: \(textField.text)")
        var param = [String:String]()
        self.searchMore = true
        switch resultType{
        case .audio:
            audioListData = []
            param = ["search":seachTf.text!,"limit":"10","page":"1","type":"audio"]
            break
        case .post:
            userVideos = []
            param = ["search":seachTf.text!,"limit":"10","page":"1","type":"post"]
            break
        case .users:
            userList = []
            param = ["search":seachTf.text!,"limit":"10","page":"1","type":"user"]
            break
        case .none:
            param = searchParam
            break
        }
        setup(withParam: param){
            DispatchQueue.main.async {
                if self.userList.isEmpty && self.userVideos.isEmpty && self.audioListData.isEmpty{
                    self.noResultLbl.isHidden = false
                }else{
                    self.noResultLbl.isHidden = true
                }
            }
        }
    }
}
