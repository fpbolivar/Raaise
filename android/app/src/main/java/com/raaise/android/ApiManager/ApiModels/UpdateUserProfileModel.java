package com.raaise.android.ApiManager.ApiModels;

public class UpdateUserProfileModel {
    public String name;
    public String userName;
    public String phoneNumber;
    public String shortBio;
    public Error errors;
    public int statusCode;
    public String message;
    public Data data;

    public UpdateUserProfileModel() {

    }

    public UpdateUserProfileModel(String name, String userName, String phoneNumber, String shortBio) {
        this.name = name;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.shortBio = shortBio;
    }


    public UpdateUserProfileModel(String name, String phoneNumber) {
        this.name = name;

        this.phoneNumber = phoneNumber;
    }

    public UpdateUserProfileModel(String shortBio) {
        this.shortBio = shortBio;
    }

    public Error getErrors() {
        return errors;
    }

    public void setErrors(Error errors) {
        this.errors = errors;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getShortBio() {
        return shortBio;
    }

    public void setShortBio(String shortBio) {
        this.shortBio = shortBio;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Error {
        public String message;
        public String param;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getParam() {
            return param;
        }

        public void setParam(String param) {
            this.param = param;
        }
    }

    public class Data {
        public String _id;
        public String userName;
        public String name;
        public String email;
        public String deviceType;
        public String loginType;
        public String profileImage;
        public boolean isBlock;
        public boolean isActive;
        public String userGoogleId;
        public String userFaceBookId;
        public String shortBio;
        public String createdAt;
        public String updatedAt;
        public String __v;
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

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getLoginType() {
            return loginType;
        }

        public void setLoginType(String loginType) {
            this.loginType = loginType;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public boolean isBlock() {
            return isBlock;
        }

        public void setBlock(boolean block) {
            isBlock = block;
        }

        public boolean isActive() {
            return isActive;
        }

        public void setActive(boolean active) {
            isActive = active;
        }

        public String getUserGoogleId() {
            return userGoogleId;
        }

        public void setUserGoogleId(String userGoogleId) {
            this.userGoogleId = userGoogleId;
        }

        public String getUserFaceBookId() {
            return userFaceBookId;
        }

        public void setUserFaceBookId(String userFaceBookId) {
            this.userFaceBookId = userFaceBookId;
        }

        public String getShortBio() {
            return shortBio;
        }

        public void setShortBio(String shortBio) {
            this.shortBio = shortBio;
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

        public String get__v() {
            return __v;
        }

        public void set__v(String __v) {
            this.__v = __v;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }
    }


}
