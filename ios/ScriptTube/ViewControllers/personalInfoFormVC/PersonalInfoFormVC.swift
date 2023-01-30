//
//  PersonalInfoFormVC.swift
//  ScriptTube
//
//  Created by Dilpreet Singh on 11/17/22.
//

import UIKit
import PhotosUI
class PersonalInfoFormVC: BaseControllerVC {
    @IBOutlet weak var updateLbl: UILabel!
    @IBOutlet weak var cameraImage: UIImageView!
    @IBOutlet weak var profileImage: UIImageView!
    @IBOutlet weak var  descriptionLbl:UILabel!
    @IBOutlet weak var  submitBtn:UIButton!
    @IBOutlet weak var  emailTF:UITextField!
    @IBOutlet weak var  mobileTF:UITextField!
    @IBOutlet weak var  nameTF:UITextField!
    var imageChanged = false
    override func viewDidLoad() {
        super.viewDidLoad()
        setfonts()
        cameraImage.layer.cornerRadius = cameraImage.frame.height / 2
        addNavBar(headingText:"Personal Information",redText:"Information")
        if AuthManager.currentUser.email == ""{
            emailTF.isEnabled = true
            emailTF.textColor = .white
        }
        // Do any additional setup after loading the view.
    }
    func gotoUsernameForm(){
        let vc = UserNameFormVC()
        self.navigationController?.pushViewController(vc, animated: true)
    }
    func setfonts(){
        cameraImage.image = UIImage(systemName: "camera")
        //?.addPadding(-50)
        cameraImage.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(photoOptions)))
        emailTF.overrideUserInterfaceStyle = .light
        mobileTF.overrideUserInterfaceStyle = .light
        nameTF.overrideUserInterfaceStyle = .light
        descriptionLbl.font = AppFont.FontName.regular.getFont(size: AppFont.pX10)
        submitBtn.titleLabel?.font = AppFont.FontName.medium.getFont(size: AppFont.pX17)
        updateLbl.font = AppFont.FontName.medium.getFont(size: AppFont.pX18)
        emailTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        nameTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        mobileTF.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        paddingTF(tf:emailTF);
        paddingTF(tf:mobileTF);
        paddingTF(tf:nameTF);
        setPlaceholder()
    }
    @objc func photoOptions(){
        print("jhfhsjhdb")
        AlertView().alertCameraGallery(msg: "Select Option", delegate: self) { action in
            if action == .camera{
                self.openCamera()
            }else if action == .photo{
                self.openGallery()
            }else{
                
            }
        }
    }
    func openCamera(){
        if UIImagePickerController.isSourceTypeAvailable(.camera){
            let picker = UIImagePickerController()
            picker.sourceType = .camera
            picker.delegate = self
            self.present(picker,animated: true)
        }else{
            AlertView().showCameraNotAvailableAlert(delegate:  self, pop: false)
        }
    }
    func openGallery(){
        if #available(iOS 14, *) {
                    // using PHPickerViewController
                    var config = PHPickerConfiguration(photoLibrary: PHPhotoLibrary.shared())
                    config.selectionLimit = 1
                    config.filter = .images
                    config.preferredAssetRepresentationMode = .current
                    let picker = PHPickerViewController(configuration: config)
                    picker.delegate = self
                    DispatchQueue.main.async {
                        self.present(picker, animated: true, completion: nil)
                    }
        } else {
            if UIImagePickerController.isSourceTypeAvailable(.photoLibrary){
                let picker = UIImagePickerController()
                picker.sourceType = .photoLibrary
                picker.delegate = self
                self.present(picker,animated: true)
            }else{
                
            }
        }

    }
    func paddingTF(tf:UITextField,value:Int=20){
        tf.paddingLeftRightTextField(left: CGFloat(value), right: CGFloat(value))
    }
    func updateProfileApi(){
        let param = ["name":nameTF.text ?? "","phoneNumber":mobileTF.text ?? ""]
        AuthManager.updateUserProfileApi(delegate: self, param: param,imageChanged:self.imageChanged,image: ["image":self.profileImage.image ?? UIImage()]) {
            DispatchQueue.main.async {
                //self.gotoUsernameForm()
                ToastManager.successToast(delegate: self, msg: "Profile Updated Successfully")
                //self.navigationController?.popViewController(animated: true)
            }
        }
    }
    func setPlaceholder(){
//        mobileTF.placeholder = "Phone Number"
//        nameTF.placeholder = "Name"
//        emailTF.placeholder = "Email"
//        
        nameTF.attributedPlaceholder = NSAttributedString(string: "Name",attributes: [.foregroundColor: UIColor.lightGray])
        mobileTF.attributedPlaceholder = NSAttributedString(string: "Phone Number",attributes: [.foregroundColor: UIColor.lightGray])
        emailTF.attributedPlaceholder = NSAttributedString(string: "Email Address",attributes: [.foregroundColor: UIColor.lightGray])
        
        mobileTF.layer.cornerRadius = 10
        nameTF.layer.cornerRadius = 10
        emailTF.layer.cornerRadius = 10
        
        mobileTF.text = AuthManager.currentUser.phoneNumber
        nameTF.text = AuthManager.currentUser.name
        emailTF.text = AuthManager.currentUser.email
        profileImage.layer.cornerRadius = profileImage.frame.height / 2
        profileImage.loadImgForProfile(url: AuthManager.currentUser.profileImage)
        //loadImg(url: AuthManager.currentUser.profileImage)
    }
    func checkValidations(){
        if (nameTF.text!.isEmpty){
            ToastManager.errorToast(delegate: self, msg: LocalStrings.emptyName)
            return
        }
//        else if(emailTF.text!.isEmpty){
//            ToastManager.errorToast(delegate: self, msg: LocalStrings.emptyEmail)
//            return
//        }
        else if (!Validator.isValidEmail(email: emailTF.text ?? "" ) && AuthManager.currentUser.email.isEmpty){
            ToastManager.errorToast(delegate: self, msg: LocalStrings.validEmail)
            return
        }
        else if (mobileTF.text!.isEmpty){
            ToastManager.errorToast(delegate: self, msg: LocalStrings.emptyMobile)
            return
        }else if (mobileTF.text!.count < 8 || mobileTF.text!.count > 10){
            ToastManager.errorToast(delegate: self, msg: LocalStrings.validMobile)
            return
        }else{
            if(!(Constant.check_Internet?.isReachable)!){
                AlertView().showInternetErrorAlert(delegate: self)
                return
            }
            updateProfileApi()
        }
    }

    @IBAction func submitAction(_ sender: AnyObject) {
        checkValidations()
    }

}
extension PersonalInfoFormVC: UIImagePickerControllerDelegate,UINavigationControllerDelegate{
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
        self.profileImage.image = image
        imageChanged = true
      //  let data =  image.jpegData(compressionQuality: 0.8)
       // self.imageUpload(data: data!)
    }
    @objc func image(_ image: UIImage, didFinishSavingWithError error: Error?, contextInfo: UnsafeRawPointer) {
        if let error = error {
            print("Not Stored")
        }

    }
}
extension PersonalInfoFormVC:PHPickerViewControllerDelegate{
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
                        self.profileImage.image = UIImage(data: imageData)
                        self.imageChanged = true
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
extension PHAsset {
static func getURL(ofPhotoWith mPhasset: PHAsset, completionHandler : @escaping ((_ responseURL : URL?) -> Void)) {
            if mPhasset.mediaType == .image {
                let options: PHContentEditingInputRequestOptions = PHContentEditingInputRequestOptions()
                options.canHandleAdjustmentData = {(adjustmeta: PHAdjustmentData) -> Bool in
                    return true
                }
                mPhasset.requestContentEditingInput(with: options, completionHandler: { (contentEditingInput, info) in
                    if let fullSizeImageUrl = contentEditingInput?.fullSizeImageURL {
                        completionHandler(fullSizeImageUrl)
                    } else {
                        completionHandler(nil)
                    }
                })
            } else if mPhasset.mediaType == .video {
                let options: PHVideoRequestOptions = PHVideoRequestOptions()
                options.version = .original
                PHImageManager.default().requestAVAsset(forVideo: mPhasset, options: options, resultHandler: { (asset, audioMix, info) in
                    if let urlAsset = asset as? AVURLAsset {
                        let localVideoUrl = urlAsset.url
                        completionHandler(localVideoUrl)
                    } else {
                        completionHandler(nil)
                    }
                })
            }
            
        }
}
