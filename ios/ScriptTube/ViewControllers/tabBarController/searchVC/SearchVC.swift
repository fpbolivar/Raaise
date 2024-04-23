//
//  SearchVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/21/22.
//

import UIKit

class SearchVC: BaseControllerVC {
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var searchTf: UITextField!
    @IBOutlet weak var backImage: UIImageView!
    @IBOutlet weak var noResultLbl:UILabel!
    var result = SearchResultModel()
    var searchParam:[String:String]!
    var hideTabbar = false

    override func viewDidLoad() {
        super.viewDidLoad()
        hideNavbar()
        setup()
        self.setuptable()
        searchTf.delegate = self
        //Date:: 07, Mar 2024 - here we change the type
        addNavBar(headingText: "Search", redText: "",
                  type: .onlyTopTitle,
                  color: UIColor(named: "bgColor"))
        tableView.register(UINib(nibName: SearchCell.identifier, bundle: nil), forCellReuseIdentifier: SearchCell.identifier)
    
        if hideTabbar{
//            addNavBar(headingText: "Public Rooms to join", redText: "",type: .smallNavBarOnlyBack)
            backImage.isHidden = true
        }else{
//            addNavBar(headingText: "Public Rooms to join", redText: "",type: .onlyTopTitle)
            backImage.isHidden = true
        }
        // Do any additional setup after loading the view.
    }
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.tabBarController?.tabBar.isHidden = hideTabbar
    }
    //MARK: - Api method
    func searchApi(withText text:[String:String],completion:@escaping()->Void){
        DataManager.globalSearchApi(delegate: self, param: text) { json in
            print("JSon",text,json)
            let resultData = SearchResultModel(data: json["data"])
            self.result = resultData
            completion()
        }
    }
    //MARK: - Setup
    func setup(){
        searchTf.layer.cornerRadius = 10
        searchTf.overrideUserInterfaceStyle = .light
        backImage.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(backAction)))
        //searchTf.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        backImage.image = UIImage(named: "ic_back")?.addPadding(0, 0, 20, 0)
        searchTf.paddingLeftRightTextField(left: 35, right: 0)
        searchTf.attributedPlaceholder = NSAttributedString(string: "Search Something",attributes: [.foregroundColor: UIColor.lightGray])
    }
    func setuptable(){
        DispatchQueue.main.async {
            self.tableView.delegate = self
            self.tableView.dataSource = self
            self.tableView.reloadData()
        }
    }
   
    @objc override func backAction(){
        self.navigationController?.popViewController(animated: true)
    }
}
//MARK: - Table View Delegate
extension SearchVC: UITableViewDelegate,UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 3
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: SearchCell.identifier, for: indexPath) as! SearchCell
        cell.delegate = self
        switch indexPath.row{
        case 0:
            cell.setupSearchCell(withType: .users, data: result.userResult)
            cell.collectionViewHeight.constant = cell.collectionView.collectionViewLayout.collectionViewContentSize.height
            break
        case 1:
            cell.setupSearchCell(withType: .audio, data: result.audioResult)
            cell.collectionViewHeight.constant = cell.collectionView.collectionViewLayout.collectionViewContentSize.height
            break
        case 2:
            cell.setupSearchCell(withType: .post, data: result.postResult)
            cell.collectionViewHeight.constant = cell.collectionView.collectionViewLayout.collectionViewContentSize.height
            break
        default:
            print("INDEX OUT OF RANGE")
            AlertView().showAlert(message: "INDEX OUT OF RANGE", delegate: self, pop: false)
        }
        return cell
    }
    @objc func getHintsFromTextField(textField: UITextField) {
        
        if !textField.text!.isEmpty{
           
            let param = ["search":textField.text!,"limit":"5","page":"1"]
            searchParam = param
            searchApi(withText: param){
                DispatchQueue.main.async {
                    self.tableView.reloadData()
                    if textField.text!.isEmpty{
                        self.tableView.isHidden = true
                        self.noResultLbl.isHidden = true
                    }else{
                        self.noResultLbl.isHidden = !self.result.resultIsEmpty()
                        self.tableView.isHidden = false
                    }
                }
            }
        }else{
            self.tableView.isHidden = true
        }
    }
}

//MARK: - Search Delegate
extension SearchVC:UITextFieldDelegate{
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
}
//MARK: - Navigation Delegate
extension SearchVC:SelectedSearchItemDelegate{
    func openProfileWithId(id: String) {
        let vc = VisitProfileVC()
        vc.id = id
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    func tryAudioWithAudio(audio: AudioDataModel) {
        let vc = TryAudioVC()
        vc.audioData = audio
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    func viewVideo(posts: [Post], selectedIndex: Int) {
        let vc = ViewVideoVC()
        vc.data = posts
        vc.selectedRow = selectedIndex
        self.navigationController?.pushViewController(vc, animated: true)
    }
    
    func gotoResultPage(resultType: ResultType) {
        let vc = SearchResultVC()
        vc.resultType = resultType
        
        vc.searchText = searchTf.text!
        
        switch resultType{
        case .post:
            
            vc.searchParam = ["search":searchTf.text!,"limit":"10","page":"1","type":"post"]
            break
        case .users:
           
            vc.searchParam = ["search":searchTf.text!,"limit":"10","page":"1","type":"user"]
            break
        case .audio:
            
            vc.searchParam = ["search":searchTf.text!,"limit":"10","page":"1","type":"audio"]
            break
        }
        self.navigationController?.pushViewController(vc, animated: true)
    }
}
