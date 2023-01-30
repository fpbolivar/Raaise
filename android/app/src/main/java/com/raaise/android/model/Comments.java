package com.raaise.android.model;

public class Comments {
    public String commentatorName;
    public String comment;
    public int likedCount;
    public Comments(String commentatorName, String comment, int likedCount) {
        this.commentatorName = commentatorName;
        this.comment = comment;
        this.likedCount = likedCount;
    }
}
