//
//  CommentPopUp.swift
//  ScriptTube
//
//  Created by Code Optimal Solutions Ios on 06/12/22.
//

import UIKit
protocol CommentPopUpDelegate{
    func dismissToVisitProfile(withId id:String)
    func dismissAfterComment(numberOfComments num:String)
}
class CommentPopUp: UIViewController {
    @IBOutlet weak var replyToLbl: UILabel!
    @IBOutlet weak var replyToView: UIView!
    @IBOutlet weak var totalCommentsLbl: UILabel!
    @IBOutlet weak var tableView:UITableView!
    @IBOutlet weak var commentTf:UITextField!
    var videoId:String = ""
    var commentId:String?
    var replyId:String?
    var page = 1
    var numberOfComments = ""
    var commentData : [CommentDataModel] = []
    var delegate:CommentPopUpDelegate?
    var replyIndex:IndexPath?
    var isEdit = false
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
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        delegate?.dismissAfterComment(numberOfComments: "\(self.commentData.count)")
    }
    //MARK: - Setup
    func setData(withNumberOfComments num :String){
        DispatchQueue.main.async {
            self.totalCommentsLbl.text = "\(num) comments"
        }
    }
    func setupTable(){
        DispatchQueue.main.async {
            self.tableView.register(UINib(nibName: CommentTableViewCell.identifier, bundle: nil), forCellReuseIdentifier: CommentTableViewCell.identifier)
            self.tableView.register(UINib(nibName: ReplyTblCell.identifier, bundle: nil), forCellReuseIdentifier: ReplyTblCell.identifier)
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
        if !commentTf.text!.trimmingCharacters(in: .whitespaces).isEmpty{
            if  !isEdit && commentId == nil{
                postComment()
            }else if isEdit && commentId != nil{
                postEditComment()
            }
            else if isEdit && commentId == nil{
                postEditReply()
            }else{
                postReply()
            }
            self.commentTf.text = ""
            self.replyToView.isHidden = true
            
        }
    }
    //MARK: - Posting Comment & Reply Api
    func postEditReply(){
        let param = ["replyId":self.replyId!,"reply":commentTf.text!]
        print("REPLYTEdit",AuthManager.currentUser.profileImage,AuthManager.currentUser.userName)
        AuthManager.editReplyApi(delegate: self, param: param) { data in
            print("REPLYTEdit",data)
            let i = self.commentData.firstIndex { comment in
                
                return comment.replies.contains { reply in
                    if reply.id == self.replyId{
                        
                    }
                    return reply.id == self.replyId
                }
           }
            
           guard let index = i else{return}
            let j = self.commentData[index].replies.firstIndex { reply in
                return reply.id == self.replyId
            }
            guard let replyIndex = j else{return}
            self.commentData[index].replies[replyIndex].reply = data["data"]["reply"].stringValue
           self.tableView.reloadSections(IndexSet(integer: index), with: .fade)
           self.commentId = nil
           self.replyId = nil
            self.isEdit = false
        }
    }
    func postEditComment(){
        let param = ["commentId":self.commentId!,"comment":commentTf.text!]
        print("REPLYTOCOMMENT",AuthManager.currentUser.profileImage,AuthManager.currentUser.userName)
        AuthManager.editCommentApi(delegate: self, param: param) { data in
            print("EDITEDCOMMNMET",data)
             let i = self.commentData.firstIndex { comment in
                 print(comment.id,self.commentId)
                return comment.id == self.commentId
            }
            guard let index = i else{return}
            self.commentData[index].comment = data["data"]["comment"].stringValue
            self.tableView.reloadSections(IndexSet(integer: index), with: .fade)
            self.commentId = nil
            self.replyId = nil
            self.isEdit = false
        }
    }
    func postReply(){
        let param = ["commentId":self.commentId!,"reply":commentTf.text!]
        print("REPLYTOCOMMENT",AuthManager.currentUser.profileImage,AuthManager.currentUser.userName)
        AuthManager.postReply(delegate: self, param: param) { data in
            let cell = self.tableView.cellForRow(at: self.replyIndex!) as! CommentTableViewCell
            cell.commentData.replies.append(ReplyDataModel(data2: data["data"]))
            DispatchQueue.main.async {
                self.tableView.reloadSections([self.replyIndex!.section], with: .none)
            }
            self.isEdit = false
            self.commentId = nil
        }
    }
    func postComment(){
        let param = ["videoId":self.videoId,"comment":commentTf.text!]
        print("EMOJEEE",commentTf.text!)
        AuthManager.postCommentApi(delegate: self, param: param) { data in
            let newComment = CommentDataModel(data2: data)
            self.commentData.append(newComment)
            DispatchQueue.main.async {
                self.tableView.reloadData()
                self.totalCommentsLbl.text = "\(self.commentData.count) comments"
            }
            self.isEdit = false
            self.commentId = nil
        }
    }
    //MARK: - Get Chat Data Api
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
//MARK: - Table View Delegates
extension CommentPopUp:UITableViewDelegate,UITableViewDataSource{
    func numberOfSections(in tableView: UITableView) -> Int {
        return commentData.count
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        let sec = commentData[section]
        if sec.viewReply{
            return sec.replies.count + 1
        }else{
            return 1
        }
    }
    func tableView(_ tableView: UITableView, heightForFooterInSection section: Int) -> CGFloat {
        return 30
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.row == 0{
            let cell = tableView.dequeueReusableCell(withIdentifier: CommentTableViewCell.identifier, for: indexPath) as! CommentTableViewCell
            cell.setup(data: commentData[indexPath.section])
            cell.showReplyView = {
                self.replyIndex = indexPath
            }
            cell.showReplyTable = {
                self.commentData[indexPath.section].viewReply = !self.commentData[indexPath.section].viewReply
                UIView.setAnimationsEnabled(false)
                self.tableView.beginUpdates()
                self.tableView.reloadSections([indexPath.section], with: .none)
                self.tableView.endUpdates()
                UIView.setAnimationsEnabled(true)
                self.replyIndex = indexPath
            }
            cell.delegate = self
            if self.commentData[indexPath.section].viewReply{
                cell.separatorInset =  UIEdgeInsets(top: 0, left: 0, bottom: 0, right: .greatestFiniteMagnitude)
            }else{
                cell.separatorInset = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: 0)
            }
            return cell
        }else{
            
            let cell = tableView.dequeueReusableCell(withIdentifier: ReplyTblCell.identifier, for: indexPath) as! ReplyTblCell
            cell.delegate = self
            cell.setup(data: commentData[indexPath.section].replies[indexPath.row - 1])
            if commentData[indexPath.section].replies.count == indexPath.row {
                cell.separatorInset = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: 0)
            }else{
                cell.separatorInset =  UIEdgeInsets(top: 0, left: 0, bottom: 0, right: .greatestFiniteMagnitude)
            }
            return cell
        }
        
    }
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
    }
    //MARK: - Pagination
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        let height = scrollView.frame.size.height
        let contentYOffset = scrollView.contentOffset.y
        let distanceFromBottom = scrollView.contentSize.height - contentYOffset

        if distanceFromBottom == height && commentData.count >= 10{
            
            page = page + 1
            getCommentsApi {
                self.tableView.reloadData()
            }
        }
    }
}
//MARK: - Comment & Reply Delegates
extension CommentPopUp:CommentCellDelegate,ReplyDelegate{
    func editReply(replyData: ReplyDataModel) {
        self.commentTf.text = replyData.reply
        //commentTf.becomeFirstResponder()
        self.replyToView.isHidden = false
        self.replyToLbl.text = "Edit Reply: \(replyData.reply)"
        self.isEdit = true
        self.replyId = replyData.id
    }
    
    func deleteReply(replyData: ReplyDataModel) {
        let param = ["replyId":replyData.id]
        
        AuthManager.deletereplyApi(delegate: self, param: param) {
            let i = self.commentData.firstIndex { comment in
                
                return comment.replies.contains { reply in
                    if reply.id == replyData.id{
                        
                    }
                    return reply.id == replyData.id
                }
           }
           guard let index = i else{return}
            self.commentData[index].replies.removeAll { reply in
                return reply.id == replyData.id
            }
            self.tableView.reloadSections(IndexSet(integer: index), with: .none)
            self.isEdit = false
            self.commentId = nil
        }
    }
    
    func deleteComment(commentData: CommentDataModel) {
        
        self.replyToView.isHidden = true
        if isEdit{
            commentTf.text = ""
        }
        let param = ["videoId":self.videoId,"commentId":commentData.id]
        AuthManager.deleteCommentApi(delegate: self, param: param) {
            let i = self.commentData.firstIndex { comment in
                print(comment.id,commentData.id)
                return comment.id == commentData.id
           }
           guard let index = i else{return}
            self.commentData.remove(at: index)
            self.tableView.deleteSections(IndexSet(integer: index), with: .fade)
           
            self.commentId = nil
        }
    }
    
    func editComment(commentData: CommentDataModel) {
        self.commentTf.text = commentData.comment
        //commentTf.becomeFirstResponder()
        self.replyToView.isHidden = false
        self.replyToLbl.text = "Edit Comment: \(commentData.comment)"
        self.commentId = commentData.id
        self.isEdit = true
    }
    
    func gotoProfile(withId id: String) {
        dismiss(animated: true){
            self.delegate?.dismissToVisitProfile(withId: id)
        }
    }
    
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
