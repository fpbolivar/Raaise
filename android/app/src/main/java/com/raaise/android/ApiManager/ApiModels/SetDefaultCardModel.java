package com.raaise.android.ApiManager.ApiModels;

public class SetDefaultCardModel {
    public String cardId;
    public int status;
    public String message;
    public Errors errors;
    public SetDefaultCardModel(String cardId) {
        this.cardId = cardId;
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
