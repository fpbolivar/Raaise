package com.raaise.android.ApiManager.ApiModels;

public class ProfileChatModel {
    public String receiverId;
    public String senderId;
    public int status;
    public String message;
    public String chatSlug;
    public ProfileChatModel(String receiverId, String senderId) {
        this.receiverId = receiverId;
        this.senderId = senderId;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getChatSlug() {
        return chatSlug;
    }
}
