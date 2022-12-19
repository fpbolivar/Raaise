package com.scripttube.android.ScriptTube.ApiManager.ApiModels;

public class GoogleLoginModel {
    public String token;
    public String deviceType;

    public GoogleLoginModel(String token, String deviceType) {
        this.token = token;
        this.deviceType = deviceType;
    }

    public int status;
    public String message;
    public Errors errors;

    public Errors getErrors() {
        return errors;
    }

    public String getToken() {
        return token;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
    public class Errors {
        public String message;
        public String param;

        public String getMessage() {
            return message;
        }

        public String getParam() {
            return param;
        }
    }
}
