package com.scripttube.android.ScriptTube.model;

public class Comments {
    public String commentatorName;

    public Comments(String commentatorName, String comment, int likedCount) {
        this.commentatorName = commentatorName;
        this.comment = comment;
        this.likedCount = likedCount;
    }

    public String comment;
    public int likedCount;
}
