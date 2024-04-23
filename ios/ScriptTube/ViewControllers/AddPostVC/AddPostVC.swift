//
//  AddPostVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 28/11/22.
//

import UIKit

class AddPostVC: BaseControllerVC {
    
    @IBOutlet weak var lblChooseTagsForPost: UILabel!
    @IBOutlet weak var categoryCollectionView: UICollectionView!
    @IBOutlet weak var submitLbl: UILabel!
    @IBOutlet weak var switchStack: UIStackView!
    @IBOutlet weak var categoryStack: UIStackView!
    @IBOutlet weak var categoryTableView: ContentSizedTableView!
    @IBOutlet weak var categoryTf: UITextField!
    @IBOutlet weak var stack: UIStackView!
    @IBOutlet weak var donationTf: UITextField!
    @IBOutlet weak var collectionView: UICollectionView!
    @IBOutlet weak var otherAmtLbl: UILabel!
    @IBOutlet weak var optionsSwitch: UISwitch!
    @IBOutlet weak var donationLbl: UILabel!
    @IBOutlet weak var captionTv: CustomTextView!
    @IBOutlet weak var thumbnailImgView: UIImageView!
    let chooseArticleDropDown = DropDown()
    var thumbnailimage: UIImage!
    var thumbnailImageString : String?
    var oldCaption : String?
    let amounts = ["$10","$50","$100","$150"]
    var selectedAudioId = ""
    var videoUrl:URL!
    var videoData = Data()
    var imageData = Data()
    var editPost = false
    var slug:String?
    var category:[VideoCategoryModel] = []
    var selectedCategory:String = ""
    var delegate:AddPostFinishDelegate?
    override func viewDidLoad() {
        super.viewDidLoad()
        hideNavbar()
        setup()
        // Do any additional setup after loading the view.
    }
    //MARK: - Setup
    func setup(){
        
        optionsSwitch.applyGradientColorToUISwitch(gradientColors: [UIColor(named: "Gradient1") ?? .black, UIColor(named: "Gradient2") ?? .white])
        otherAmtLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
        donationLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX12)
        submitLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX18)
        donationTf.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        captionTv.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
       
        if editPost{
            setupForEditPost()
        }else{
            thumbnailImgView.image = thumbnailimage
        }
        categoryTf.layer.cornerRadius = 10
        categoryTf.paddingLeftRightTextField(left: 25, right: 0)
        addNavBar(headingText: "New Post", 
                  redText: "Post",
                  color: UIColor(named: "bgColor"))
        donationTf.paddingLeftRightTextField(left: 25, right: 0)
        
        donationTf.layer.cornerRadius = 10
        donationTf.overrideUserInterfaceStyle = .light
        categoryTableView.register(UINib(nibName: CategoryCell.identifier, bundle: nil), forCellReuseIdentifier: CategoryCell.identifier)
        collectionView.register(UINib(nibName: AmountCell.identifier, bundle: nil), forCellWithReuseIdentifier: AmountCell.identifier)
        collectionView.delegate = self
        collectionView.dataSource = self
        donationTf.attributedPlaceholder = NSAttributedString(string: "Enter Amount",attributes: [.foregroundColor: UIColor.lightGray])
        categoryTableView.delegate = self
        submitLbl.font = AppFont.FontName.bold.getFont(size: AppFont.pX15)
        categoryTableView.dataSource = self
        getVideoCategoryApi {
            DispatchQueue.main.async {
                self.setupChooseArticleDropDown()
            }
        }
        
        let nibName = UINib(nibName: "ChooseInterestCell", bundle: nil)
        self.categoryCollectionView.register(nibName, forCellWithReuseIdentifier: ChooseInterestCell.identifier)
        self.categoryCollectionView.delegate = self
        self.categoryCollectionView.dataSource = self
        
        categoryStack.isHidden = true
        lblChooseTagsForPost.applyGradientColorToLabelText(colors: [UIColor(named: "Gradient1") ?? .black, UIColor(named: "Gradient2") ?? .white])
    }
    func setupChooseArticleDropDown() {
        chooseArticleDropDown.align = .left
        chooseArticleDropDown.anchorView = categoryTf
        chooseArticleDropDown.bottomOffset = CGPoint(x: 0, y: categoryTf.bounds.height + 10)
        chooseArticleDropDown.backgroundColor = UIColor(named: "TFcolor")
        chooseArticleDropDown.textColor = .white
        chooseArticleDropDown.selectionBackgroundColor = .darkGray
        chooseArticleDropDown.dataSource = category.map{$0.name}
        chooseArticleDropDown.selectionAction = { [weak self] (index, item) in
            print("SELECTEDVAL",item)
            self?.categoryTf.text = item
            self?.selectedCategory = self?.category[index].id ?? ""
        }
    }
    func setupForEditPost(){
        categoryStack.isHidden = true
        switchStack.isHidden = true
        stack.isHidden = true
        submitLbl.text = "Update"
        captionTv.text = self.oldCaption
        print(thumbnailImageString)
        thumbnailImgView.loadImg(url: thumbnailImageString ?? "")
    }
    //MARK: - Api Methods
    func editPostApi(){
        let param = ["slug":slug,"videoCaption":captionTv.text.trimmingCharacters(in: .whitespaces)] as! [String:String]
        DataManager.editPostApi(delegate: self, param: param) {
            DispatchQueue.main.async {
                self.navigationController?.popToRootViewController(animated: true)
                self.tabBarController?.dismiss(animated: true)
            }
        }
    }
    func getVideoCategoryApi(completion:@escaping()->Void){
        DataManager.getVideoCategory(delegate: self) { json in
            json["data"].forEach { (message,data) in
                self.category.append(VideoCategoryModel(data: data))
            }
            self.categoryCollectionView.reloadData()
            completion()
        }
    }
    //MARK: - Actions
    @IBAction func categoryBtn(_ sender: Any) {
        chooseArticleDropDown.show()
    }
    @IBAction func submitBtnClicked(_ sender: Any) {
        if(!(Constant.check_Internet?.isReachable)!){
            AlertView().showInternetErrorAlert(delegate: self)
            return
        }
        if editPost{
            editPostApi()
        }else{
            if captionTv.text!.isEmpty{
                ToastManager.errorToast(delegate: self, msg: "Provide a caption for your Video")
                return
            }else if selectedCategory == ""{
                ToastManager.errorToast(delegate: self, msg: "Select an appropriate Category for your Video")
                return
            }else if optionsSwitch.isOn && donationTf.text!.trimmingCharacters(in: .whitespaces).isEmpty{
                ToastManager.errorToast(delegate: self, msg: "Please Enter Amount")
                return
            }
            addpostApi()
        }
        
    }
    @IBAction func donationSwitchChanged(_ sender: UISwitch) {
        stack.isHidden = !sender.isOn
    }
    @objc func updateCounter(){
        self.clearAllNotice()
        self.navigationController?.popToRootViewController(animated: true)
        print("self.clearAllNotice()")
    }
    //MARK: - Add Post Api
    func addpostApi(){
        
        //self.pleaseWait()
        Timer.scheduledTimer(timeInterval: 5.0, target: self, selector: #selector(updateCounter), userInfo: nil, repeats: false)
        do{
             videoData = try Data(contentsOf: videoUrl)
            imageData = thumbnailimage.jpegData(compressionQuality: 0.8)!
            
        }catch{
            AlertView().showAlert(message: error.localizedDescription, delegate: self, pop: false)
        }
        
        let param = ["videoCaption":captionTv.text!.trimmingCharacters(in: .whitespaces),
                     "isDonation":optionsSwitch.isOn,
                     "donationAmount":donationTf.text ?? "0",
                     "audioId":selectedAudioId,
                     "categoryId":selectedCategory] as [String : Any]
        self.navigationController?.popToRootViewController(animated: true)
        self.delegate?.postAdded(status: .start)
        VideoUploadStatus.isUploading?(true)
        VideoUploadStatus.isUploadingVar = true
        DispatchQueue.global(qos: .background).async {
            AuthManager.uploadVideoData(delegate: self, param: param, resourcesVideo: ["video" : self.videoUrl], resourcesImage: ["image":self.thumbnailimage]) {
                print("videouploadedsuccess")
                VideoUploadStatus.isUploading?(false)
                VideoUploadStatus.isUploadingVar = false
                self.clearAllNotice()
                DispatchQueue.main.async {
                    //self.navigationController?.popToRootViewController(animated: true)
                    self.delegate?.postAdded(status: .finish)
                }
            } progress: { progress in
                VideoUploadStatus.progress?(progress)
            }
        }
    }
}
//MARK: - Collection View Delegate
extension AddPostVC:UICollectionViewDelegate,UICollectionViewDataSource,UICollectionViewDelegateFlowLayout{
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        if collectionView == categoryCollectionView {
            return category.count
        }
        return amounts.count
    }
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
         
        if collectionView == categoryCollectionView {
            let interestCell = collectionView.dequeueReusableCell(withReuseIdentifier: ChooseInterestCell.identifier, for: indexPath) as! ChooseInterestCell
            interestCell.chooseInterestLabel.text = category[indexPath.row].name
            interestCell.chooseInterestImg.loadImg(url:category[indexPath.row].image)
            
            if self.selectedCategory == category[indexPath.row].id{
                interestCell.chooseInterestView.backgroundColor = .white
                interestCell.chooseInterestLabel.textColor = .black
            }
            else{
                interestCell.chooseInterestView.backgroundColor = .clear
                interestCell.chooseInterestLabel.textColor = .white
            }
            return interestCell
            
        } else {
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: AmountCell.identifier, for: indexPath) as! AmountCell
            cell.updateCell(withAmt: amounts[indexPath.row])
            return cell
        }
        
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        if collectionView == categoryCollectionView {
            return CGSize(width: (self.categoryCollectionView.bounds.width / 2) - 20,height: (50))
        }
        return CGSize(width: (self.collectionView.bounds.width / 5) - 5, height: (self.collectionView.bounds.height - 15))
    }
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        if collectionView == categoryCollectionView {
            let obj = self.category[indexPath.row]
            
            self.selectedCategory = category[indexPath.row].id
            
            self.categoryCollectionView.reloadData()
        } else {
            let cell = collectionView.cellForItem(at: indexPath) as! AmountCell
            cell.selectedCell()
        }
        
    }
    func collectionView(_ collectionView: UICollectionView, didDeselectItemAt indexPath: IndexPath) {
        if collectionView == categoryCollectionView {
            
            
        } else {
            let cell = collectionView.cellForItem(at: indexPath) as! AmountCell
            cell.unselectedCell()
        }
        
    }
}
//MARK: - Table View Delegate
extension AddPostVC:UITableViewDelegate,UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 10
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: CategoryCell.identifier, for: indexPath) as! CategoryCell
        return cell
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 40
    }
}

//MARK: - Protocol
protocol AddPostFinishDelegate{
    func postAdded(status:UploadStatus)
}
enum UploadStatus{
    case start
    case finish
}
class VideoUploadStatus{
    static var isUploading:((Bool)->Void)?
    static var isUploadingVar = false
    static var progress:((Int)->Void)?
}


