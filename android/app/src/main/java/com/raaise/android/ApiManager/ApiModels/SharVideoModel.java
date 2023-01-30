package com.raaise.android.ApiManager.ApiModels;

public class SharVideoModel {
    public String receiverId;
    public String senderId;
    public String videoId;
    public int status;
    public String message;
    public SharVideoModel(String receiverId, String senderId, String videoId) {
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.videoId = videoId;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
