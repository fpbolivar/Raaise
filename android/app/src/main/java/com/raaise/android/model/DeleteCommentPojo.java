package com.raaise.android.model;

public class DeleteCommentPojo {

    public String videoId;
    public String commentId;

    public DeleteCommentPojo(String videoId, String commentId) {
        this.videoId = videoId;
        this.commentId = commentId;
    }
}
