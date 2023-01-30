package com.raaise.android.ApiManager.ApiModels;

public class ReadSingleNotificationModel {
    public String notificationId;
    public int status;
    public String message;
    public ReadSingleNotificationModel(String notificationId) {
        this.notificationId = notificationId;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
