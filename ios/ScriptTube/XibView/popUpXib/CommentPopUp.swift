//
//  CommentPopUp.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 06/12/22.
//

import UIKit
protocol CommentPopUpDelegate{
    func dismissToVisitProfile(withId id:String)
}
class CommentPopUp: UIViewController {
    @IBOutlet weak var replyToLbl: UILabel!
    @IBOutlet weak var replyToView: UIView!
    @IBOutlet weak var totalCommentsLbl: UILabel!
    @IBOutlet weak var tableView:UITableView!
    @IBOutlet weak var commentTf:UITextField!
    var videoId:String = ""
    var commentId:String?
    var page = 1
    var numberOfComments = ""
    var commentData : [CommentDataModel] = []
    var delegate:CommentPopUpDelegate?
    override func viewDidLoad() {
        super.viewDidLoad()
        commentTf.layer.borderWidth = 0.5
        commentTf.layer.cornerRadius = 10
        commentTf.layer.borderColor = UIColor.lightGray.cgColor
        commentTf.paddingLeftRightTextField(left: 25, right: 0)
        commentTf.attributedPlaceholder = NSAttributedString(string: "Add Comment...",attributes: [.foregroundColor:UIColor.darkGray])
        commentTf.font = AppFont.FontName.regular.getFont(size: AppFont.pX14)
        setData(withNumberOfComments: numberOfComments)
        getCommentsApi(){
            self.setupTable()
        }
        // Do any additional setup after loading the view.
    }
    func setupTable(){
        DispatchQueue.main.async {
            self.tableView.register(UINib(nibName: CommentTableViewCell.identifier, bundle: nil), forCellReuseIdentifier: CommentTableViewCell.identifier)
            self.tableView.dataSource = self
            self.tableView.delegate = self
            self.tableView.reloadData()
        }
    }
    @IBAction func closeBtnClicked(sender:Any){
        self.dismiss(animated: true)
    }
    @IBAction func closeReplyBtn(_ sender: Any) {
        replyToView.isHidden = true
        self.commentId = nil
    }
    @IBAction func postCommentBtn(_ sender: Any) {
        if !commentTf.text!.isEmpty{
            if commentId == nil{
                postComment()
            }else{
                postReply()
                
            }
            self.commentTf.text = ""
        }else{
            //AlertView().showAlert(message: "dd", delegate: self, pop: false)
        }
    }
    func postReply(){
        self.tableView.isScrollEnabled = false
        let param = ["commentId":self.commentId!,"reply":commentTf.text!]
        AuthManager.postReply(delegate: self, param: param) {
            self.commentData = []
            self.getCommentsApi {
                DispatchQueue.main.async {
                    self.setupTable()
                    self.tableView.isScrollEnabled = true
                }
            }
        }
    }
    func postComment(){
        self.tableView.isScrollEnabled = false
        let param = ["videoId":self.videoId,"comment":commentTf.text!]
        AuthManager.postCommentApi(delegate: self, param: param) {
            self.commentData = []
            self.getCommentsApi {
                DispatchQueue.main.async {
                    self.tableView.reloadData()
                }
            }
        }
    }
    func setData(withNumberOfComments num :String){
        DispatchQueue.main.async {
            self.totalCommentsLbl.text = "\(num) Comments"
        }
    }
    func getCommentsApi(completion:@escaping()->Void){
        let param = ["videoId":videoId,"limit":"10","page":"\(page)"]
        DataManager.getVideoComments(param: param) { errorMessage in
            AlertView().showAlert(message: errorMessage, delegate: self, pop: false)
        } completion: { json in
            json["data"].forEach { (message,data) in
                print("EACHRES",data)
                self.commentData.append(CommentDataModel(data: data))
            }
            print("CommentCount",self.commentData.count)
            
            completion()
        }
    }
}
extension CommentPopUp:UITableViewDelegate,UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return commentData.count
    }
    func tableView(_ tableView: UITableView, heightForFooterInSection section: Int) -> CGFloat {
        return 30
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: CommentTableViewCell.identifier, for: indexPath) as! CommentTableViewCell
        cell.setup(data: commentData[indexPath.row])
        cell.showReplyTable = {
            //cell.replyTableView.reloadData()
            self.tableView.performBatchUpdates(nil)
            //self.tableView.beginUpdates()
           // print()
            
        }
        cell.delegate = self
        return cell
    }
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        if indexPath.row == self.commentData.count - 2{
 
        }
    }
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        let height = scrollView.frame.size.height
        let contentYOffset = scrollView.contentOffset.y
        let distanceFromBottom = scrollView.contentSize.height - contentYOffset

        if distanceFromBottom + 50 < height && commentData.count >= 10{
            print("You reached end of the table")
            page = page + 1
            getCommentsApi {
                self.tableView.reloadData()
            }
        }
    }
}

extension CommentPopUp:CommentCellDelegate{
    func openProfile(withId id: String) {
        dismiss(animated: true){
            self.delegate?.dismissToVisitProfile(withId: id)
        }
        
    }
    
    func replyToComment(withId id: String, name: String) {
        self.replyToView.isHidden = false
        self.replyToLbl.text = "Reply To: \(name)"
        self.commentTf.becomeFirstResponder()
        self.commentId = id
    }
    
    
}
