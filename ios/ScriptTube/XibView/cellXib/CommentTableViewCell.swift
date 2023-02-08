//
//  CommentTableViewCell.swift
//  KD Tiktok-Clone
//
//  Created by Sam Ding on 9/10/20.
//  Copyright © 2020 Kaishan. All rights reserved.
//

import UIKit

protocol CommentCellDelegate{
    func replyToComment(withId id:String,name:String)
    func openProfile(withId id:String)
}

class CommentTableViewCell: UITableViewCell {
   
    @IBOutlet weak var viewReplyView: UIView!
    @IBOutlet weak var viewReplyIcon: UIImageView!
    @IBOutlet weak var replyTableView: ContentSizedTableView!
    @IBOutlet weak var viewReplyLbl: UILabel!
    @IBOutlet weak var profileImg: UIImageView!
    @IBOutlet weak var timeLbl: UILabel!
    @IBOutlet weak var nameLbl: UILabel!
    @IBOutlet weak var commentLbl: UILabel!
    var commentData = CommentDataModel()
    var showReplyTable: (()->Void)? = nil
    var showReplyView: (()->Void)? = nil
    var delegate:CommentCellDelegate?
    static var identifier = "CommentTableViewCell"
    override func awakeFromNib() {
        super.awakeFromNib()
        selectionStyle = .none
        
        profileImg.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(gotoProfileTap)))
        nameLbl.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(gotoProfileTap)))
        profileImg.layer.cornerRadius = profileImg.frame.height / 2
        replyTableView.register(UINib(nibName: ReplyTblCell.identifier, bundle: nil), forCellReuseIdentifier: ReplyTblCell.identifier)
        
    }
    override func prepareForReuse() {
        super.prepareForReuse()
        self.viewReplyIcon.isHidden = false
        self.viewReplyView.isHidden = false
    }

    @objc func gotoProfileTap(){
        delegate?.openProfile(withId: commentData.userId)
    }
    @IBAction func replyBtn(_ sender: Any) {
        showReplyView!()
        delegate?.replyToComment(withId: self.commentData.id, name: self.commentData.username)
    }
    //MARK: - Setup & Update Data
    func setup(data:CommentDataModel){
        self.commentData = data
        self.commentLbl.text = data.comment
        self.nameLbl.text = data.username
        self.profileImg.loadImgForProfile(url: data.commentprofileImage)
        if commentData.replies.count == 0{
            self.viewReplyIcon.isHidden = true
            self.viewReplyView.isHidden = true
        }
        if commentData.replies.count > 1{
            self.viewReplyLbl.text = "View Replies(\(commentData.replies.count))"
        }else{
            self.viewReplyLbl.text = "View Reply(\(commentData.replies.count))"
        }
        let dateFormatter = DateFormatter()
        dateFormatter.locale = Locale(identifier: "en_US_POSIX")
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        let date = dateFormatter.date(from: data.commentTime)
        let currentDate = Date()
        if currentDate.offsetFrom(date: date!) == ""{
            self.timeLbl.text = "Just Now"
        }else{
            self.timeLbl.text = currentDate.offsetFrom(date: date!)
        }
        viewReplyLbl.addGestureRecognizer(UITapGestureRecognizer(target: self, action: #selector(showReplies)))
        if !self.commentData.viewReply{
            if self.commentData.replies.count > 1{
                self.viewReplyLbl.text = "View Replies(\(self.commentData.replies.count))"
            }else{
                self.viewReplyLbl.text = "View Reply(\(self.commentData.replies.count))"
            }
        }else{
            if self.commentData.replies.count > 1{
                self.viewReplyLbl.text = "Hide Replies(\(self.commentData.replies.count))"
            }else{
                self.viewReplyLbl.text = "Hide Reply(\(self.commentData.replies.count))"
            }
        }
    }
    @objc func showReplies(){
        showReplyTable!()
    }
}
//MARK: - Table View Delegates
extension CommentTableViewCell:UITableViewDelegate,UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.commentData.replies.count
    }
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: ReplyTblCell.identifier, for: indexPath) as! ReplyTblCell
        cell.delegate = self
        cell.setup(data: self.commentData.replies[indexPath.row])
        if indexPath.row == self.commentData.replies.count - 1{
            cell.separatorInset = UIEdgeInsets(top: 0, left: 0, bottom: 0, right: .greatestFiniteMagnitude)
        }
        return cell
    }
}
//MARK: - Reply Delegate
extension CommentTableViewCell:ReplyDelegate{
    func gotoProfile(withId id: String) {
        delegate?.openProfile(withId: id)
    }
}
