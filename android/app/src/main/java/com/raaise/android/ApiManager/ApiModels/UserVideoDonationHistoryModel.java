package com.raaise.android.ApiManager.ApiModels;

import java.util.List;

public class UserVideoDonationHistoryModel {
    public String videoId;

    public UserVideoDonationHistoryModel(String videoId) {
        this.videoId = videoId;
    }

    public int status;
    public String message;
    public float totalDanotionAmount;
    public float raisedDonationAmount;
    public float completedDonationAmount;
    public List<Data> data;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public float getTotalDanotionAmount() {
        return totalDanotionAmount;
    }

    public void setTotalDanotionAmount(float totalDanotionAmount) {
        this.totalDanotionAmount = totalDanotionAmount;
    }

    public float getRaisedDonationAmount() {
        return raisedDonationAmount;
    }

    public void setRaisedDonationAmount(float raisedDonationAmount) {
        this.raisedDonationAmount = raisedDonationAmount;
    }

    public float getCompletedDonationAmount() {
        return completedDonationAmount;
    }

    public void setCompletedDonationAmount(float completedDonationAmount) {
        this.completedDonationAmount = completedDonationAmount;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data{
        public String _id;
        public String userId;
        public DonatedBy donatedBy;
        public String videoId;
        public String status;
        public String adminAmount;
        public String userAmount;
        public String amount;
        public String transferId;
        public String createdAt;
        public String updatedAt;
        public int __v;
        public String paymentRequestStatus;

        public DonatedBy getDonatedBy() {
            return donatedBy;
        }

        public void setDonatedBy(DonatedBy donatedBy) {
            this.donatedBy = donatedBy;
        }

        public class DonatedBy{
            public String _id;
            public String userName;
            public String name;
            public String email;
            public String phoneNumber;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getPhoneNumber() {
                return phoneNumber;
            }

            public void setPhoneNumber(String phoneNumber) {
                this.phoneNumber = phoneNumber;
            }
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }



        public String getVideoId() {
            return videoId;
        }

        public void setVideoId(String videoId) {
            this.videoId = videoId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAdminAmount() {
            return adminAmount;
        }

        public void setAdminAmount(String adminAmount) {
            this.adminAmount = adminAmount;
        }

        public String getUserAmount() {
            return userAmount;
        }

        public void setUserAmount(String userAmount) {
            this.userAmount = userAmount;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getTransferId() {
            return transferId;
        }

        public void setTransferId(String transferId) {
            this.transferId = transferId;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
        }

        public String getPaymentRequestStatus() {
            return paymentRequestStatus;
        }

        public void setPaymentRequestStatus(String paymentRequestStatus) {
            this.paymentRequestStatus = paymentRequestStatus;
        }
    }
}
