package com.raaise.android.ApiManager.ApiModels;

public class DeleteCardModel {
    public String cardId;
    public int status;
    public String message;
    public Error errors;
    public DeleteCardModel(String cardId) {
        this.cardId = cardId;
    }

    public Error getErrors() {
        return errors;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public class Error {
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
