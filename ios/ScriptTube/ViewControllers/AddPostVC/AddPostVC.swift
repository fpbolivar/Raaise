//
//  AddPostVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 28/11/22.
//

import UIKit

class AddPostVC: BaseControllerVC {
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
    override func viewDidLoad() {
        super.viewDidLoad()
        hideNavbar()
        if editPost{
            setupForEditPost()
        }else{
            thumbnailImgView.image = thumbnailimage
        }
        categoryTf.layer.cornerRadius = 10
        categoryTf.paddingLeftRightTextField(left: 25, right: 0)
        addNavBar(headingText: "New Post", redText: "Post")
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
        setupChooseArticleDropDown()
        // Do any additional setup after loading the view.
    }
    func setupChooseArticleDropDown() {
        chooseArticleDropDown.anchorView = categoryTf
        chooseArticleDropDown.bottomOffset = CGPoint(x: 0, y: categoryTf.bounds.height + 10)
        chooseArticleDropDown.backgroundColor = UIColor(named: "TFcolor")
        chooseArticleDropDown.textColor = .white
        chooseArticleDropDown.selectionBackgroundColor = .darkGray
        chooseArticleDropDown.dataSource = ["test1","test2","test3","test4"]

        // Action triggered on selection
        chooseArticleDropDown.selectionAction = { [weak self] (index, item) in
            print("SELECTEDVAL",item)
            self?.categoryTf.text = item
        }

//        chooseArticleDropDown.multiSelectionAction = { [weak self] (indices, items) in
//            print("Muti selection action called with: \(items)")
//            if items.isEmpty {
//                self?.chooseArticleButton.setTitle("", for: .normal)
//            }
//        }

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
    func editPostApi(){
        let param = ["slug":slug,"videoCaption":captionTv.text] as! [String:String]
        DataManager.editPostApi(delegate: self, param: param) {
            DispatchQueue.main.async {
                self.navigationController?.popToRootViewController(animated: true)
                self.tabBarController?.dismiss(animated: true)
            }
        }
    }
    @IBAction func categoryBtn(_ sender: Any) {
        //categoryTableView.isHidden = !categoryTableView.isHidden
        chooseArticleDropDown.show()
    }
    @IBAction func submitBtnClicked(_ sender: Any) {
        if editPost{
            editPostApi()
        }else{
            addpostApi()
        }
        
    }
    @IBAction func donationSwitchChanged(_ sender: UISwitch) {
        stack.isHidden = !sender.isOn
    }
    func addpostApi(){
        self.pleaseWait()
        do{
             videoData = try Data(contentsOf: videoUrl)
            imageData = thumbnailimage.jpegData(compressionQuality: 0.8)!
            
        }catch{
            AlertView().showAlert(message: error.localizedDescription, delegate: self, pop: false)
        }
        
        let param = ["videoCaption":captionTv.text!,"isDonation":optionsSwitch.isOn,"donationAmount":donationTf.text ?? "0","audioId":selectedAudioId,"categoryId":"639ac2e41b797deae7ef7efc"] as [String : Any]
        AuthManager.uploadVideoData(delegate: self, param: param, resourcesVideo: ["video" : videoUrl], resourcesImage: ["image":self.thumbnailimage]) {
            self.clearAllNotice()
            DispatchQueue.main.async {
                self.navigationController?.popToRootViewController(animated: true)
                self.tabBarController?.dismiss(animated: true)
            }
        }
    }
}
extension AddPostVC:UICollectionViewDelegate,UICollectionViewDataSource,UICollectionViewDelegateFlowLayout{
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return amounts.count
    }
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: AmountCell.identifier, for: indexPath) as! AmountCell
        cell.updateCell(withAmt: amounts[indexPath.row])
        return cell
    }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: (self.collectionView.bounds.width / 5) - 5, height: (self.collectionView.bounds.height - 15))
    }
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        let cell = collectionView.cellForItem(at: indexPath) as! AmountCell
        cell.selectedCell()
    }
    func collectionView(_ collectionView: UICollectionView, didDeselectItemAt indexPath: IndexPath) {
        let cell = collectionView.cellForItem(at: indexPath) as! AmountCell
        cell.unselectedCell()
    }
}
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
