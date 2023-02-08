//
//  EditProfileVideoVC.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 12/12/22.
//

import UIKit

class EditProfileVideoVC: UIViewController {
    var post : Post?
    var delegate:EditVideoProtocol?
    override func viewDidLoad() {
        super.viewDidLoad()
    
    }
    
    //MARK: - Delete Action
    @IBAction func deleteBtn(_ sender: Any) {
        print(post?.slug)
        deletePost()
    }
    func deletePost(){
        let alert = UIAlertController(title: "Raaise App", message: "Are you sure you want to Delete the video?", preferredStyle: .alert)
        let logOutAction = UIAlertAction(title: "Delete", style: .destructive){action in
            if(!(Constant.check_Internet?.isReachable)!){
                AlertView().showInternetErrorAlert(delegate: self)
                return
            }
            guard let slug = self.post?.slug else{
                AlertView().showAlert(message: "NIL PARAM", delegate: self, pop: false)
                return
            }
            let param = ["slug":slug]
            let pvc = self.presentingViewController
            DataManager.deletePost(delegate: self, param: param) {
                DispatchQueue.main.async {
                    self.dismiss(animated: true){
                        self.delegate?.videoDeleted()
                    }
                }
            }
            
        }
        let cancelAction = UIAlertAction(title: "Cancel", style: .cancel){action in
            self.dismiss(animated: true)
        }
        alert.addAction(cancelAction)
        alert.addAction(logOutAction)
        self.present(alert,animated: true)
    
    }
    //MARK: - Edit Action
    @IBAction func editBtn(_ sender: Any) {
        print(post?.slug)
        self.dismiss(animated: true){
            self.delegate?.videoEdited()
        }
    }
    @IBAction func dismiss(_ sender: Any) {
        self.dismiss(animated: true)
    }

    
}
//MARK: - Edit Video Protocols
protocol EditVideoProtocol{
    func videoDeleted()
    func videoEdited()
}
