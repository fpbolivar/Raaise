package com.scripttube.android.ScriptTube.ApiManager.ApiModels;

public class VerifyOtpModel {
    public String email;
    public String otp;
    public int statusCode;
    public String message;
    public String token;
    public Error errors;

    public Error getErrors() {
        return errors;
    }

    public void setErrors(Error errors) {
        this.errors = errors;
    }

    public VerifyOtpModel(String email, String otp) {
        this.email = email;
        this.otp = otp;
    }

    public String getEmail() {
        return email;
    }

    public String getOtp() {
        return otp;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }
    public class Error {
        public String message;
        public String param;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getParam() {
            return param;
        }

        public void setParam(String param) {
            this.param = param;
        }
    }

}
