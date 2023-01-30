package com.raaise.android.ApiManager.ApiModels;

import java.util.List;

public class VideoCommentModel {
    public String videoId;
    public String comment;
    public int status;
    public String message;
    public Data data;

    public VideoCommentModel(String videoId, String comment) {
        this.videoId = videoId;
        this.comment = comment;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
        public String comment;
        public String videoId;
        public List<String> replyId;
        public String commentBy;
        public String _id;
        public String createdAt;
        public String updatedAt;
        public int __v;

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

//        public String getReplyId() {
//            return replyId;
//        }
//
//        public void setReplyId(String replyId) {
//            this.replyId = replyId;
//        }


        public List<String> getReplyId() {
            return replyId;
        }

        public void setReplyId(List<String> replyId) {
            this.replyId = replyId;
        }

        public String getCommentBy() {
            return commentBy;
        }

        public void setCommentBy(String commentBy) {
            this.commentBy = commentBy;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }
    }
}
