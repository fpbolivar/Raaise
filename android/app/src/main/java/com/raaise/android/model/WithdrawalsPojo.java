package com.raaise.android.model;

import java.util.ArrayList;

public class WithdrawalsPojo {
    public int status;
    public String message;
    public ArrayList<WithdrawalData> data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<WithdrawalData> getData() {
        return data;
    }

    public class WithdrawalData{
        public String _id;
        public String userId;
        public String status;
        public String amount;
        public String videoId;
        public ArrayList<String> walletId;
        public String updatedAt;
        public String withdrawalStatus;

        public String getUpdatedAt() {
            return updatedAt;
        }

        public ArrayList<String> getWalletId() {
            return walletId;
        }

        public String get_id() {
            return _id;
        }

        public String getUserId() {
            return userId;
        }

        public String getStatus() {
            return status;
        }

        public String getAmount() {
            return amount;
        }

        public String getVideoId() {
            return videoId;
        }

        public String getWithdrawalStatus() {
            return withdrawalStatus;
        }
    }

}
