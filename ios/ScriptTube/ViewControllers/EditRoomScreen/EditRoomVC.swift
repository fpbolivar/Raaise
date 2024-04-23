//
//  EditRoomVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 25/04/23.
//

import UIKit

protocol UpdateRoom{
    func roomUpdateSuccess(newData:LiveRoomDataModel)
}
 
class EditRoomVC: BaseControllerVC {

    @IBOutlet weak var cameraImg: DesignableImageView!
    @IBOutlet weak var roomImage: DesignableImageView!
    @IBOutlet weak var descriptionLbl: UILabel!
    @IBOutlet weak var updateLbl: UILabel!
    @IBOutlet weak var titleLbl: UILabel!
    @IBOutlet weak var descriptionTf: UITextField!
    @IBOutlet weak var nameTf: UITextField!
    var roomData = LiveRoomDataModel()
    var updateDelegate: UpdateRoom?
    override func viewDidLoad() {
        super.viewDidLoad()
        addNavBar(headingText: "Edit Room", redText: "Room")
        setup()
        setupData()
        // Do any additional setup after loading the view.
    }
    func setupData(){
        nameTf.text = roomData.title
        descriptionTf.text = roomData.description
        roomImage.loadImg(url: roomData.logo)
    }
    func setup(){
        updateLbl.font = AppFont.FontName.medium.getFont(size: AppFont.pX18)
        descriptionLbl.font = AppFont.FontName.medium.getFont(size: AppFont.pX14)
        titleLbl.font = AppFont.FontName.medium.getFont(size: AppFont.pX14)
        paddingTF(tf:descriptionTf)
        paddingTF(tf:nameTf)
        descriptionTf.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        nameTf.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        descriptionTf.layer.cornerRadius = 10
        nameTf.layer.cornerRadius = 10
        nameTf.attributedPlaceholder = NSAttributedString(string: "Room Name",attributes: [.foregroundColor: UIColor.lightGray])
        descriptionTf.attributedPlaceholder = NSAttributedString(string: "Room Description",attributes: [.foregroundColor: UIColor.lightGray])
        cameraImg.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(openImageSelector)))
    }
    func paddingTF(tf:UITextField,value:Int=20){
        tf.paddingLeftRightTextField(left: CGFloat(value), right: CGFloat(value))
    }
    @objc func openImageSelector(){
        AlertView().alertCameraGallery(msg: "Select Option", delegate: self) { actionType in
            if actionType == .photo{
                self.imagePickerDelegate = self
                self.openGallery()
              
            }
            if actionType == .camera{
                self.imagePickerDelegate = self
                self.openCamera()
            }
        }
    }
    
    @IBAction func updateBtnClicked(_ sender: Any) {
        checkValidations()
    }
    func checkValidations(){
        if nameTf.text!.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty{
            AlertView().showAlert(message: "Enter Room Name", delegate: self, pop: false)
            return
        }
        if descriptionTf.text!.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty{
            AlertView().showAlert(message: "Enter Room Description", delegate: self, pop: false)
            return
        }
        let param = ["title":nameTf.text!.trimmingCharacters(in: .whitespacesAndNewlines),
                     "description":descriptionTf.text!.trimmingCharacters(in: .whitespacesAndNewlines),
                     "slug":roomData.slug]
        AuthManager.updateLiveRoomApi(delegate: self, param: param,image: ["logo":roomImage.image!]) {newData in
            self.roomData = LiveRoomDataModel(json: newData["updatedData"])
            self.updateDelegate?.roomUpdateSuccess(newData: self.roomData)
            AlertView().showAlert(message: "Room Updated Successfully", delegate: self, pop: true)
        }
    }
}
extension EditRoomVC:ImagePickerDelegate{
    func didSelectImage(image: UIImage, imgType: ImageType) {
        roomImage.image = image
    }
    
//    func didSelectImage(image: UIImage) {
//       roomImage.image = image
//    }
}
