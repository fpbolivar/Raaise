package com.raaise.android.model;

public class SelectUsersModel {

    String id;
    String profileImage;
    String userName;

    public SelectUsersModel(String id, String profileImage, String userName) {
        this.id = id;
        this.profileImage = profileImage;
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getUserName() {
        return userName;
    }
}
