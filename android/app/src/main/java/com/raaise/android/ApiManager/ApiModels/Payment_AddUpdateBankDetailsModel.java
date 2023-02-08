package com.raaise.android.ApiManager.ApiModels;

public class Payment_AddUpdateBankDetailsModel {
    public String accountHolderName;
    public String accountHolderLastName;
    public String dob;
    public String accountId;
    public long routingNumber;
    public long bankPhone;
    public String city;
    public String state;
    public long postalCode;
    public String address;
    public String ssn_last_4;
    public int status;
    public String message;
    public Errors errors;
    public Payment_AddUpdateBankDetailsModel(String accountHolderName, String accountHolderLastName,
                                             String dob, String accountId, long routingNumber, long bankPhone, String city, String state, long postalCode, String address,
                                             String ssn_last_4) {
        this.accountHolderName = accountHolderName;
        this.accountHolderLastName = accountHolderLastName;
        this.dob = dob;
        this.accountId = accountId;
        this.routingNumber = routingNumber;
        this.bankPhone = bankPhone;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.address = address;
        this.ssn_last_4 = ssn_last_4;
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
