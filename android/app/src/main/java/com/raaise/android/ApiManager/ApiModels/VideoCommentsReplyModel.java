package com.raaise.android.ApiManager.ApiModels;

public class VideoCommentsReplyModel {
    public String commentId;
    public String reply;
    public int status;
    public String message;
    public Data data;

    public VideoCommentsReplyModel(String commentId, String reply) {
        this.commentId = commentId;
        this.reply = reply;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        public String reply;
        public String replyBy;
        public String commentId;
        public String _id;
        public String createdAt;
        public String updatedAt;
        public int __v;

        public Data(String reply, String replyBy, String commentId, String _id, String createdAt, String updatedAt, int __v) {
            this.reply = reply;
            this.replyBy = replyBy;
            this.commentId = commentId;
            this._id = _id;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.__v = __v;
        }
    }
}
