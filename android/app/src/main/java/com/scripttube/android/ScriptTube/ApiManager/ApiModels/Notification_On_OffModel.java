package com.scripttube.android.ScriptTube.ApiManager.ApiModels;

public class Notification_On_OffModel {
    public int status;
    public String message;
    public boolean emailNotification;
    public boolean pushNotification;

    public Notification_On_OffModel() {

    }

    public Notification_On_OffModel(boolean emailNotification, boolean pushNotification) {
        this.emailNotification = emailNotification;
        this.pushNotification = pushNotification;
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

    public boolean isEmailNotification() {
        return emailNotification;
    }

    public void setEmailNotification(boolean emailNotification) {
        this.emailNotification = emailNotification;
    }

    public boolean isPushNotification() {
        return pushNotification;
    }

    public void setPushNotification(boolean pushNotification) {
        this.pushNotification = pushNotification;
    }
}
