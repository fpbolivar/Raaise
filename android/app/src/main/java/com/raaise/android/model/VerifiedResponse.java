package com.raaise.android.model;

public class VerifiedResponse {

        public int status;
        public String message;
        public Data data;
        public boolean isUserVerified;

    public VerifiedResponse(boolean isUserVerified) {
        this.isUserVerified = isUserVerified;
    }

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

    public class Data {

        public String userGoogleId;
        public String userFaceBookId;
        public boolean emailNotification;
        public boolean pushNotification;
        public int followersCount;
        public int videoCount;
        public String followingCount;
        public String _id;
        public String userName;
        public String email;
        public String phoneNumber;
        public String deviceType;
        public String loginType;
        public String profileImage;
        public String coverImage;

        public boolean isBlock;
        public boolean isActive;
        public String createdAt;
        public String updatedAt;
        public int __v;
        public String otp;
        public String name;
        public String shortBio;
        public String donatedAmount;
        public boolean isVerified;

        public String getCoverImage() {
            return coverImage;
        }

        public void setCoverImage(String coverImage) {
            this.coverImage = coverImage;
        }

        public int getVideoCount() {
            return videoCount;
        }

        public String getDonatedAmount() {
            return donatedAmount;
        }

        public String getUserGoogleId() {
            return userGoogleId;
        }

        public String getUserFaceBookId() {
            return userFaceBookId;
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

        public String getFollowingCount() {
            return followingCount;
        }

        public String get_id() {
            return _id;
        }

        public String getUserName() {
            return userName;
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

        public String getCreatedAt() {
            return createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public int get__v() {
            return __v;
        }

        public String getOtp() {
            return otp;
        }

        public String getName() {
            return name;
        }

        public String getShortBio() {
            return shortBio;
        }

        public boolean isVerified() {
            return isVerified;
        }
    }

}
