//
//  BaseControllerVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/18/22.
//

import Foundation
import UIKit
import PhotosUI
import Photos


protocol ImagePickerDelegate{
    func didSelectImage(image:UIImage,imgType:ImageType)
}
    
class BaseControllerVC:UIViewController,UIImagePickerControllerDelegate, UINavigationControllerDelegate{
    var navView:NavigationBar!
    var imagePickerDelegate:ImagePickerDelegate!
    var  type:NavBarType = .largeNavBarOnlyBack
    var imgType: ImageType = .profile
    
    var headingText: String? {
        
        didSet { print("Name was changed.")
            headingtitleSet()
        }
    }
    var redText: String? {
        
        didSet { print("Name was changed.")
            headingtitleSet()
        }
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        self.hideNavbar()
        navView =  NavigationBar.instanceFromNib() as? NavigationBar
        
    }
    func addNavBar(
        headingText:String,
        leftAction:Selector?=nil,
        redText:String,
        type:NavBarType = .largeNavBarOnlyBack,
        addNewCardSelector:Selector? = nil,
        addNewCardSelectorTitle:String = "Add New Card",
        color:UIColor? = UIColor(
            named: "BackgroundColor"
        )){
        print(addNewCardSelectorTitle)
        self.type = type
        //self.
        self.headingText = headingText
        self.redText = redText
        addNavBar(type:type,leftAction: leftAction,addNewCardSelector: addNewCardSelector,addNewCardSelectorTitle:addNewCardSelectorTitle, color: color)
    }
    private func addNavBar(style:NavBarStyle?=nil,title:String="",type:NavBarType = .largeNavBarOnlyBack,leftAction:Selector?=nil,deleteSelector: Selector? = nil,addNewCardSelector:Selector? = nil,addNewCardSelectorTitle:String, color:UIColor? = UIColor(named: "BackgroundColor")){
        
        
        navView.leftIcon.isUserInteractionEnabled = true
        navView.backgroundColor = color//UIColor(named: "bgColor")
        
        titleSet()
        if let cardAction = addNewCardSelector{
            navView.addNewCardBtn.addTarget(self, action: cardAction, for: .touchUpInside)
        }
        navView.translatesAutoresizingMaskIntoConstraints = false
        //navView.leftIcon.image = UIImage(named: "ic_back")!.addPadding(0, 0, 20, 0)
        navView.leftIcon.image = UIImage(named: "ic_new_back")!.addPadding(0, 0, 20, 0)
        navView.rigthBtn.isHidden = true
        if let leftAction = leftAction{
            navView.leftIcon.addGestureRecognizer(UITapGestureRecognizer(target: self, action: leftAction))
        }else{
            navView.leftIcon.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(backAction)))
        }
        self.view.addSubview(navView)
        navView.topAnchor.constraint(equalTo: self.view.safeAreaLayoutGuide.topAnchor, constant: 0).isActive = true
        navView.leadingAnchor.constraint(equalTo: self.view.leadingAnchor, constant: 0).isActive = true
        navView.trailingAnchor.constraint(equalTo: self.view.trailingAnchor, constant: 0).isActive = true
        if .largeNavBarOnlyBack == type{
            navView.heightAnchor.constraint(equalToConstant: 80).isActive = true
        }
        if .largeNavBarOnlyBackWithRightBtn == type{
            navView.heightAnchor.constraint(equalToConstant: 80).isActive = true
            navView.rigthBtn.isHidden = false
        }
        if .smallNavBarOnlyBack == type{
            navView.heightAnchor.constraint(equalToConstant: 60).isActive = true
        }
        if .onlyTopTitle == type{
            navView.heightAnchor.constraint(equalToConstant: 60).isActive = true
            navView.rigthBtn.isHidden = false
            navView.leftIcon.isHidden = true
        }
        if .leaveRoom == type{
            navView.heightAnchor.constraint(equalToConstant: 60).isActive = true
            navView.rigthBtn.isHidden = false
            navView.rigthBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: 12)
            navView.leftIcon.isHidden = false
            navView.rigthBtn.setTitle("Leave Room", for: .normal)
            navView.rigthBtn.setTitleColor(UIColor.red, for: .normal)
            if let rightAction = addNewCardSelector{
                navView.rigthBtn.addTarget(self, action: rightAction, for: .touchUpInside)
            }
        }
        if .joinRoom == type{
            navView.heightAnchor.constraint(equalToConstant: 60).isActive = true
            navView.rigthBtn.isHidden = false
            navView.rigthBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: 12)
            navView.leftIcon.isHidden = false
            navView.rigthBtn.setTitle("Join Room", for: .normal)
            navView.rigthBtn.setTitleColor(UIColor.appButtonColor, for: .normal)
            if let rightAction = addNewCardSelector{
                navView.rigthBtn.addTarget(self, action: rightAction, for: .touchUpInside)
            }
        }
        if type == .addNewCard{
            navView.heightAnchor.constraint(equalToConstant: 80).isActive = true
            navView.addNewCardBtn.isHidden = false
            //navView.addNewCardBtn.setTitle(addNewCardSelectorTitle, for: .normal)
            navView.addNewCardBtn.setBtnGradientText(colors: [UIColor(named: "Gradient1") ?? .black, UIColor(named: "Gradient2") ?? .white], labelText: addNewCardSelectorTitle)
            navView.addNewCardBtn.setTitle("", for: .normal)

        }
        if type == .addRoom{
            navView.heightAnchor.constraint(equalToConstant: 80).isActive = true
            navView.addNewCardBtn.isHidden = false
            navView.addNewCardBtn.backgroundColor = UIColor.appButtonColor
            navView.addNewCardBtn.layer.cornerRadius = 7
            navView.addNewCardBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: 12)
            navView.addNewCardBtn.titleEdgeInsets = UIEdgeInsets.zero
            navView.addNewCardBtn.setTitleColor(UIColor.white, for: .normal)
            navView.addNewCardBtn.setTitle("  + Room  ", for: .normal)
        }
        if type == .filter{
            navView.heightAnchor.constraint(equalToConstant: 80).isActive = true
            navView.addNewCardBtn.isHidden = false
            navView.addNewCardBtn.setTitle(addNewCardSelectorTitle, for: .normal)
            navView.addNewCardBtn.setImage(UIImage(named: "ic_filter"), for: .normal)
            navView.addNewCardBtn.tintColor = .white
        }
        
    }
    
    private  func titleSet(){
        
    }
    private  func headingtitleSet(){
        navView.title.text =  ""
        //navView.title.text =  "Setting"
        navView.headingLbl.text =  ""
        if .largeNavBarOnlyBack == type || .largeNavBarOnlyBackWithRightBtn == type || .addNewCard == type   || .filter == type || .addRoom == type{
            
            navView.headingLbl.adjustsFontSizeToFitWidth = true
            navView.headingLbl.font =  AppFont.FontName.regular.getFont(size: AppFont.pX20)
            navView.title.font =  AppFont.FontName.regular.getFont(size: AppFont.pX20)
            let customType = ActiveType.custom(pattern:   redText ?? "")
            navView.headingLbl.enabledTypes.append(customType)
            navView.headingLbl.textColor = UIColor.white
            navView.headingLbl.underLineEnable = false
            navView.headingLbl.text =  "    " + (headingText ?? "")
            
            navView.headingLbl.customColor[customType] = UIColor.new_theme
            navView.headingLbl.customSelectedColor[customType] = UIColor.gray
        }
        if .onlyTopTitle == type{
            navView.title.adjustsFontSizeToFitWidth = true
            navView.title.font =  AppFont.FontName.regular.getFont(size: AppFont.pX20)
           
            navView.title.textColor = UIColor.white
            
            navView.title.text = headingText ?? ""
        }
        if .smallNavBarOnlyBack == type || .leaveRoom == type || .joinRoom == type{
            navView.title.adjustsFontSizeToFitWidth = true
            navView.title.font =  AppFont.FontName.regular.getFont(size: AppFont.pX20)
            navView.title.text =  self.headingText
        }
        
        
    }
    func hideCustomNavBar(){
        self.navView.isHidden = true
    }
    func unhideCustomNavBar(navigationBar: NavigationBar){
        navigationBar.isHidden = false
    }
    @objc func gotoProfile(){
        
    }
    @objc  func gotoFriendChatList(){
        
    }
    @objc  func gotoSetting(){
        
    }
    @objc  func backAction(){
        self.navigationController?.popViewController(animated: false)
    }
    
    @objc func openGallery(){
        debugPrint(imgType)
        
        if UIImagePickerController.isSourceTypeAvailable(.photoLibrary){
            let picker = UIImagePickerController()
            picker.sourceType = .photoLibrary
            picker.delegate = self
            self.present(picker,animated: true)}
            
//        if #available(iOS 14, *) {
//            // using PHPickerViewController
//            var config = PHPickerConfiguration(photoLibrary: PHPhotoLibrary.shared())
//            config.selectionLimit = 1
//            config.filter = .images
//            config.preferredAssetRepresentationMode = .current
//            let picker = PHPickerViewController(configuration: config)
//            picker.delegate = self
//            DispatchQueue.main.async {
//                self.present(picker, animated: true, completion: nil)
//            }
//        } else {
//            if UIImagePickerController.isSourceTypeAvailable(.photoLibrary){
//                let picker = UIImagePickerController()
//                picker.sourceType = .photoLibrary
//                picker.delegate = self
//                self.present(picker,animated: true)
//            }else{
//                
//            }
//        }
    }
    @objc func openCamera(){
        if UIImagePickerController.isSourceTypeAvailable(.camera){
            let picker = UIImagePickerController()
            picker.sourceType = .camera
            picker.delegate = self
            self.present(picker,animated: true)
        }else{
            AlertView().showCameraNotAvailableAlert(delegate:  self, pop: false)
        }
    }
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        picker.dismiss(animated: true)
    }
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        
        guard let image = info[UIImagePickerController.InfoKey.originalImage] as? UIImage else{
            return
        }
        if picker.sourceType == .camera{
            UIImageWriteToSavedPhotosAlbum(image, self, #selector(image(_:didFinishSavingWithError:contextInfo:)), nil)
        }
        picker.dismiss(animated: true)
        
        imagePickerDelegate.didSelectImage(image: image,imgType: imgType)
        
        //  let data =  image.jpegData(compressionQuality: 0.8)
        // self.imageUpload(data: data!)
    }
    @objc func image(_ image: UIImage, didFinishSavingWithError error: Error?, contextInfo: UnsafeRawPointer) {
        if let error = error {
            print("Not Stored")
        }
        
    }
}
extension BaseControllerVC:PHPickerViewControllerDelegate{
    @available(iOS 14, *)
    
    func picker(_ picker: PHPickerViewController, didFinishPicking results: [PHPickerResult]) {
        UIWindow.keyWin.rootViewController?.dismiss(animated: true, completion: nil)
        print("didFinishPicking",results)
        guard !results.isEmpty else { return }
        // request image urls
        let identifier = results.compactMap(\.assetIdentifier)
        let fetchResult = PHAsset.fetchAssets(withLocalIdentifiers: identifier, options: nil)
        let count = fetchResult.count
        fetchResult.enumerateObjects {(asset, index, stop) in
            
            PHAsset.getURL(ofPhotoWith: asset) { (url) in
                if let url = url {
                    // got image url
                    do {
                        let imageData = try Data(contentsOf: url)
                        self.imagePickerDelegate.didSelectImage(image: UIImage(data: imageData) ?? UIImage(), imgType: self.imgType)
                        
                    } catch {
                        print("\(#function) Error loading image : \(error)")
                    }
                } else {
                    // show error
                    print(" ERROR didFinishPicking")
                }
            }
        }
    }
}
