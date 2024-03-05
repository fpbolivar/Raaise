package com.raaise.android.model;

public class CurrentPrivacyResponse {

    public int status;
    public String message;
    public String privacyControl;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getPrivacyControl() {
        return privacyControl;
    }
}
