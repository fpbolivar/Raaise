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
    var page = 1
    var numberOfComments = ""
    var commentData : [CommentDataModel] = []
    var delegate:CommentPopUpDelegate?
    var replyIndex:IndexPath?
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
            if commentId == nil{
                postComment()
            }else{
                postReply()
                
            }
            self.commentTf.text = ""
            self.replyToView.isHidden = true
            self.commentId = nil
        }else{
            //AlertView().showAlert(message: "dd", delegate: self, pop: false)
        }
    }
    func postReply(){
        //self.tableView.isScrollEnabled = false
        let param = ["commentId":self.commentId!,"reply":commentTf.text!]
        print("REPLYTOCOMMENT",AuthManager.currentUser.profileImage,AuthManager.currentUser.userName)
        AuthManager.postReply(delegate: self, param: param) { data in
//            let reply  = self.commentData.filter { comment in
//                return self.commentId == comment.id
//            }
            let cell = self.tableView.cellForRow(at: self.replyIndex!) as! CommentTableViewCell
            cell.commentData.replies.append(ReplyDataModel(data2: data["data"]))
            //reply.first?.replies.append( ReplyDataModel(data2: data["data"]))
            DispatchQueue.main.async {
               // self.tableView.reloadData()
                self.tableView.reloadSections([self.replyIndex!.section], with: .none)
                //self.tableView.reloadRows(at: [self.replyIndex!], with: .none)
                //cell.replyTableView.reloadData()
            }
            //print("REPLYTOCOMMENT",reply.first?.id,reply.first?.replies.count,self.commentId)
//            self.commentData = []
//            self.getCommentsApi {
//                DispatchQueue.main.async {
//                    self.setupTable()
//                    self.tableView.isScrollEnabled = true
//                }
//            }
        }
    }
    func postComment(){
        //self.tableView.isScrollEnabled = false
        let param = ["videoId":self.videoId,"comment":commentTf.text!]
        print("EMOJEEE",commentTf.text!)
        AuthManager.postCommentApi(delegate: self, param: param) { data in
            let newComment = CommentDataModel(data2: data)
            self.commentData.append(newComment)
            DispatchQueue.main.async {
                self.tableView.reloadData()
                self.totalCommentsLbl.text = "\(self.commentData.count) comments"
            }
//            self.getCommentsApi {
//                DispatchQueue.main.async {
//                    self.tableView.reloadData()
//                }
//            }
        }
    }
    func setData(withNumberOfComments num :String){
        DispatchQueue.main.async {
            self.totalCommentsLbl.text = "\(num) comments"
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
            //cell.tableViewHeight.constant = cell.replyTableView.contentSize.height
            cell.showReplyTable = {
                //cell.replyTableView.reloadData()
                self.commentData[indexPath.section].viewReply = !self.commentData[indexPath.section].viewReply
                UIView.setAnimationsEnabled(false)
                self.tableView.beginUpdates()
                self.tableView.reloadSections([indexPath.section], with: .none)
                self.tableView.endUpdates()
                UIView.setAnimationsEnabled(true)
//                self.tableView.reloadSections([indexPath.section], with: .none)
                self.replyIndex = indexPath
                //self.tableView.performBatchUpdates(nil)
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
            
//            guard let commentCell = tableView.cellForRow(at: IndexPath(row: indexPath.row - 1, section: indexPath.section))
//            else{return cell}
//            commentCell.separatorInset = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: .greatestFiniteMagnitude)
            return cell
        }
        
    }
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        if indexPath.row == self.commentData.count - 2{
 
        }
    }
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        let height = scrollView.frame.size.height
        let contentYOffset = scrollView.contentOffset.y
        let distanceFromBottom = scrollView.contentSize.height - contentYOffset

        if distanceFromBottom == height && commentData.count >= 10{
            print("You reached end of the table")
            page = page + 1
            getCommentsApi {
                self.tableView.reloadData()
            }
        }
    }
}

extension CommentPopUp:CommentCellDelegate,ReplyDelegate{
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
