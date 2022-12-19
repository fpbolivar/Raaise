package com.scripttube.android.ScriptTube.ApiManager.ApiModels;

public class UserFollowUnfollowModel {
    public String followerTo;
    public int status;
    public int followersCount;
    public int followingCount;
    public String message;
    public boolean isFollowed;

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public UserFollowUnfollowModel(String followerTo) {
        this.followerTo = followerTo;
    }

    public String getFollowerTo() {
        return followerTo;
    }

    public void setFollowerTo(String followerTo) {
        this.followerTo = followerTo;
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

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }
}
