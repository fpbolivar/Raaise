package com.raaise.android.ApiManager.ApiModels;

public class CreateNewPasswordModel {
    public String newpassword;
    public String token;
    public int status;
    public String message;
    public CreateNewPasswordModel(String newpassword, String token) {
        this.newpassword = newpassword;
        this.token = token;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
}
