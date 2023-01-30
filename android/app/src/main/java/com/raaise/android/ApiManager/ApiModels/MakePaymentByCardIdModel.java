package com.raaise.android.ApiManager.ApiModels;

public class MakePaymentByCardIdModel {
    public int amount;
    public String donateTo;
    public String videoId;
    public int status;
    public String message;
    public Errors errors;
    public MakePaymentByCardIdModel(int amount, String donateTo, String videoId) {
        this.amount = amount;
        this.donateTo = donateTo;
        this.videoId = videoId;
    }

    public Errors getErrors() {
        return errors;
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
