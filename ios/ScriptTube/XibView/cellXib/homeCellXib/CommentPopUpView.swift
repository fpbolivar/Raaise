//
//  CommentPopUpView.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 24/11/22.
//

import UIKit

class CommentPopUpView: UIView {
    var totalSlidingDistance = CGFloat()
    var panGesture : UIPanGestureRecognizer!
    @IBOutlet weak var addCommentTf: UITextField!
    @IBOutlet weak var popUpView: UIView!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var commentsLbl: UILabel!
    class func instanceFromNib() -> UIView {
        return UINib(nibName: "CommentPopUpView", bundle: nil).instantiate(withOwner: nil, options: nil)[0] as! UIView
    }
    override class func awakeFromNib() {
        super.awakeFromNib()
        
    }
    @IBAction func closeBtnClicked(_ sender: Any) {
        dismiss()
    }
    @objc func dismiss(){
        UIView.animate(withDuration: 0.25, delay: 0.0, options: .curveEaseIn, animations: {
            self.frame.origin.y = ScreenSize.Height
        }) { finished in
            self.removeFromSuperview()
        }
    }
    @objc func animatePopUpView(sender: UIPanGestureRecognizer){
        let transition = sender.translation(in: popUpView)
        // Rules: PopupView cannot go over the min Y, only dismiss when the gesture velocity exceeds 300
        switch sender.state {
        case .began, .changed:
            if totalSlidingDistance <= 0 && transition.y < 0 {return} //Only allow swipe down or up to the minY of PopupView
            if self.frame.origin.y + transition.y >= 0 {
                self.frame.origin.y += transition.y
                sender.setTranslation(.zero, in: popUpView)
                totalSlidingDistance += transition.y
            }

        case .ended:
            //Pan gesture ended
            if sender.velocity(in: popUpView).y > 300{
                dismiss()
            } else if totalSlidingDistance >= 0{
                UIView.animate(withDuration: 0.2, delay: 0, options: [.curveEaseOut],
                               animations: {
                                self.frame.origin.y -= self.totalSlidingDistance
                                self.layoutIfNeeded()
                }, completion: nil)
            }
            tableView.isUserInteractionEnabled = true
            totalSlidingDistance = 0
        default:
            UIView.animate(withDuration: 0.2, delay: 0, options: [.curveEaseOut],
                           animations: {
                            self.frame.origin.y -= self.totalSlidingDistance
                            self.layoutIfNeeded()
            }, completion: nil)
            tableView.isUserInteractionEnabled = true
            totalSlidingDistance = 0
        }
    }
    func setup(){
        //self.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(dismiss)))
        popUpView.clipsToBounds = true
        popUpView.layer.cornerRadius = 10
        popUpView.layer.maskedCorners = [.layerMaxXMinYCorner,.layerMinXMinYCorner]
        panGesture = UIPanGestureRecognizer(target: self, action: #selector(animatePopUpView(sender:)))
        self.popUpView.addGestureRecognizer(panGesture)
        addCommentTf.paddingLeftRightTextField(left: 25, right: 0)
        addCommentTf.attributedPlaceholder = NSAttributedString(string: "Add Comment...",attributes: [.foregroundColor:UIColor.darkGray])
        addCommentTf.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        commentsLbl.font = AppFont.FontName.italic.getFont(size: AppFont.pX13)
        tableView.register(UINib(nibName: CommentTableViewCell.identifier, bundle: nil), forCellReuseIdentifier: CommentTableViewCell.identifier)
        tableView.dataSource = self
        tableView.delegate = self
    }
    @objc func show(){
        // Add CommentPopUpView in the front of the current window
        guard let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
          let sceneDelegate = windowScene.delegate as? SceneDelegate
        else {
          return
        }
        sceneDelegate.window?.addSubview(self)
        
        UIView.animate(withDuration: 0.25, delay: 0.0, options: .curveEaseOut, animations: {
            self.frame.origin.y = 0
        }) { finished in
            
        }
    }
//    @objc func show2(view:UIView){
//        // Add CommentPopUpView in the front of the current window
//        guard let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
//          let sceneDelegate = windowScene.delegate as? SceneDelegate
//        else {
//          return
//        }
//        view.addSubview(self)
//        //sceneDelegate.window?.addSubview(self)
//        
//        UIView.animate(withDuration: 0.25, delay: 0.0, options: .curveEaseOut, animations: {
//            self.frame.origin.y = 0
//        }) { finished in
//            
//        }
//    }

}
extension CommentPopUpView:UITableViewDelegate,UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 20
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: CommentTableViewCell.identifier, for: indexPath) as! CommentTableViewCell
        return cell
    }
}

struct ScreenSize{
    ///Width: *Screen width*
    static let Width = UIScreen.main.bounds.width
    ///Height: *Screen Height*
    static let Height = UIScreen.main.bounds.height

}
