package com.raaise.android.ApiManager.ApiModels;

public class Payment_AddUpdateBankDetailsModel {
    public String accountHolderName;
    public String accountId;
    public long routingNumber;
    public long bankPhone;
    public String city;
    public String state;
    public long postalCode;
    public String address;
    public int status;
    public String message;
    public Errors errors;
    public Payment_AddUpdateBankDetailsModel(String accountHolderName, String accountId, long routingNumber, long bankPhone, String city, String state, long postalCode, String address) {
        this.accountHolderName = accountHolderName;
        this.accountId = accountId;
        this.routingNumber = routingNumber;
        this.bankPhone = bankPhone;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.address = address;
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
