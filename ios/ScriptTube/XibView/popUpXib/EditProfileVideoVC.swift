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
    
        // Do any additional setup after loading the view.
    }
    
  
    @IBAction func deleteBtn(_ sender: Any) {
        print(post?.slug)
        deletePost()
    }
    @IBAction func editBtn(_ sender: Any) {
        print(post?.slug)
        self.dismiss(animated: true){
            self.delegate?.videoEdited()
        }
    }
    @IBAction func dismiss(_ sender: Any) {
        self.dismiss(animated: true)
    }
    func deletePost(){
        guard let slug = post?.slug else{
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
    
}
protocol EditVideoProtocol{
    func videoDeleted()
    func videoEdited()
}
//enum EditVideoAction{
//    case delete
//    case edit
//}
