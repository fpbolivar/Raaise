package com.scripttube.android.ScriptTube.ApiManager.ApiModels;

public class DeactivateAccountModel {
    public int status;
    public String message;

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
