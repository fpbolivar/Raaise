package com.raaise.android.ApiManager.ApiModels;

public class GetPublicUserProfileModel {
    public String userIdentity;
    public int status;
    public String message;
    public Data data;
    public GetPublicUserProfileModel(String userIdentity) {
        this.userIdentity = userIdentity;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    public class Data {
        public String deviceToken;
        public String _id;
        public String userName;
        public String name;
        public String email;
        public String phoneNumber;
        public String deviceType;
        public String loginType;
        public String profileImage;
        public boolean isBlock;
        public boolean isActive;
        public String userGoogleId;
        public String userFaceBookId;
        public String donatedAmount;

        public String shortBio;
        public String otp;
        public boolean emailNotification;
        public boolean pushNotification;
        public int followersCount;
        public int followingCount;
        public boolean isVerified;
        public String createdAt;
        public String updatedAt;
        public int __v;
        public int videoCount;
        public boolean follow;

        public String getDonatedAmount() {
            return donatedAmount;
        }

        public boolean isFollow() {
            return follow;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public String get_id() {
            return _id;
        }

        public String getUserName() {
            return userName;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public String getLoginType() {
            return loginType;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public boolean isBlock() {
            return isBlock;
        }

        public boolean isActive() {
            return isActive;
        }

        public String getUserGoogleId() {
            return userGoogleId;
        }

        public String getUserFaceBookId() {
            return userFaceBookId;
        }

        public String getShortBio() {
            return shortBio;
        }

        public String getOtp() {
            return otp;
        }

        public boolean isEmailNotification() {
            return emailNotification;
        }

        public boolean isPushNotification() {
            return pushNotification;
        }

        public int getFollowersCount() {
            return followersCount;
        }

        public int getFollowingCount() {
            return followingCount;
        }

        public boolean isVerified() {
            return isVerified;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public int get__v() {
            return __v;
        }

        public int getVideoCount() {
            return videoCount;
        }
    }
}
