package com.raaise.android.ApiManager.ApiModels;

public class UnReadNotificationCountModel {
    public int status;
    public String message;
    public int notificationUnreadCount;

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

    public int getNotificationUnreadCount() {
        return notificationUnreadCount;
    }

    public void setNotificationUnreadCount(int notificationUnreadCount) {
        this.notificationUnreadCount = notificationUnreadCount;
    }
}
