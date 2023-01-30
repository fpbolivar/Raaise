package com.raaise.android.ApiManager.ApiModels;

public class Payment_AddCardModel {
    public String name;
    public String number;
    public int exp_month;
    public int exp_year;
    public String cvc;
    public int status;
    public String message;
    public Errors errors;
    public Payment_AddCardModel(String name, String number, int exp_month, int exp_year, String cvc) {
        this.name = name;
        this.number = number;
        this.exp_month = exp_month;
        this.exp_year = exp_year;
        this.cvc = cvc;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Errors getErrors() {
        return errors;
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
