package com.raaise.android.ApiManager;

public class ServerError {
    public String error;

    public ServerError(String error) {
        this.error = error;
    }

    public String getErrorMsg() {
        return error;
    }
}
