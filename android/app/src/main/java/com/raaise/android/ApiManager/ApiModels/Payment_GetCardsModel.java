package com.raaise.android.ApiManager.ApiModels;

import java.util.List;

public class Payment_GetCardsModel {
    public int status;
    public String message;
    public String user;
    public List<Cards> cards;
    public Errors errors;

    public Payment_GetCardsModel(int status, String message, String user, List<Cards> cards) {
        this.status = status;
        this.message = message;
        this.user = user;
        this.cards = cards;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getUser() {
        return user;
    }

    public List<Cards> getCards() {
        return cards;
    }

    public Errors getErrors() {
        return errors;
    }

    public class Cards {
        public String id;
        public String object;
        public String brand;
        public String country;
        public String funding;
        public int last4;
        public boolean defaultCard;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getObject() {
            return object;
        }

        public void setObject(String object) {
            this.object = object;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getFunding() {
            return funding;
        }

        public void setFunding(String funding) {
            this.funding = funding;
        }

        public int getLast4() {
            return last4;
        }

        public void setLast4(int last4) {
            this.last4 = last4;
        }

        public boolean isDefaultCard() {
            return defaultCard;
        }
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
