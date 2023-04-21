package com.raaise.android.model;

public class LiveChatData {

    public String userProfileImage;
    public String userName;
    public String message;

    public LiveChatData(String userProfileImage, String userName, String message) {
        this.userProfileImage = userProfileImage;
        this.userName = userName;
        this.message = message;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public String getUserName() {
        return userName;
    }

    public String getMessage() {
        return message;
    }
}
