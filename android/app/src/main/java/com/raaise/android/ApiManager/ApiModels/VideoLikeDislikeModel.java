package com.raaise.android.ApiManager.ApiModels;

public class VideoLikeDislikeModel {
    public String slug;
    public int status;
    public int videoCount;
    public String message;
    public boolean isLike;

    public VideoLikeDislikeModel(String slug) {
        this.slug = slug;
    }

    public int getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(int videoCount) {
        this.videoCount = videoCount;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
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

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }
}
