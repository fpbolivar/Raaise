package com.scripttube.android.ScriptTube.ApiManager.ApiModels;

public class FacebookLoginModel {

    public String token;
    public String deviceType;

    public FacebookLoginModel(String token, String deviceType) {
        this.token = token;
        this.deviceType = deviceType;
    }

    public int status;
    public String message;
    public String userName;
    public String email;
//    public String token;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
