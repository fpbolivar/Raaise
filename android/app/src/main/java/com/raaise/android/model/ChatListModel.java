package com.raaise.android.model;

import java.util.List;

public class ChatListModel {
    public String search;
    public int status;
    public String message;
    public List<Data> data;
    public ChatListModel(String search) {
        this.search = search;
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

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data {
        public String _id;
        public String slug;
        public SenderId senderId;
        public ReceiverId receiverId;
        public String lastMessage;
        public String lastMessageBy;
        public boolean isRead;
        public String file;
        public String messageType;
        public String chatTime;
        public String createdAt;
        public String updatedAt;
        public int __v;
        public String readCount;
        public int messageUnReadCount;

        public String getReadCount() {
            return readCount;
        }

        public void setReadCount(String readCount) {
            this.readCount = readCount;
        }

        public int getMessageUnReadCount() {
            return messageUnReadCount;
        }

        public void setMessageUnReadCount(int messageUnReadCount) {
            this.messageUnReadCount = messageUnReadCount;
        }
        //        public ReadCount readCount;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public SenderId getSenderId() {
            return senderId;
        }

        public void setSenderId(SenderId senderId) {
            this.senderId = senderId;
        }

        public ReceiverId getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(ReceiverId receiverId) {
            this.receiverId = receiverId;
        }

        public String getLastMessage() {
            return lastMessage;
        }

        public void setLastMessage(String lastMessage) {
            this.lastMessage = lastMessage;
        }

        public String getLastMessageBy() {
            return lastMessageBy;
        }

        public void setLastMessageBy(String lastMessageBy) {
            this.lastMessageBy = lastMessageBy;
        }

        public boolean isRead() {
            return isRead;
        }

        public void setRead(boolean read) {
            isRead = read;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getMessageType() {
            return messageType;
        }

        public void setMessageType(String messageType) {
            this.messageType = messageType;
        }

        public String getChatTime() {
            return chatTime;
        }

        public void setChatTime(String chatTime) {
            this.chatTime = chatTime;
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

//        public ReadCount getReadCount() {
//            return readCount;
//        }
//
//        public void setReadCount(ReadCount readCount) {
//            this.readCount = readCount;
//        }

        public class SenderId {
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
            public String shortBio;
            public boolean emailNotification;
            public boolean pushNotification;
            public long followersCount;
            public long followingCount;
            public long videoCount;
            public boolean isVerified;
            public String deviceToken;
            public boolean isDeleted;

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

            public String getShortBio() {
                return shortBio;
            }

            public void setShortBio(String shortBio) {
                this.shortBio = shortBio;
            }

            public boolean isEmailNotification() {
                return emailNotification;
            }

            public void setEmailNotification(boolean emailNotification) {
                this.emailNotification = emailNotification;
            }

            public boolean isPushNotification() {
                return pushNotification;
            }

            public void setPushNotification(boolean pushNotification) {
                this.pushNotification = pushNotification;
            }

            public long getFollowersCount() {
                return followersCount;
            }

            public void setFollowersCount(long followersCount) {
                this.followersCount = followersCount;
            }

            public long getFollowingCount() {
                return followingCount;
            }

            public void setFollowingCount(long followingCount) {
                this.followingCount = followingCount;
            }

            public long getVideoCount() {
                return videoCount;
            }

            public void setVideoCount(long videoCount) {
                this.videoCount = videoCount;
            }

            public boolean isVerified() {
                return isVerified;
            }

            public void setVerified(boolean verified) {
                isVerified = verified;
            }

            public String getDeviceToken() {
                return deviceToken;
            }

            public void setDeviceToken(String deviceToken) {
                this.deviceToken = deviceToken;
            }

            public boolean isDeleted() {
                return isDeleted;
            }

            public void setDeleted(boolean deleted) {
                isDeleted = deleted;
            }
        }

        public class ReceiverId {
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
            public String shortBio;
            public boolean emailNotification;
            public boolean pushNotification;
            public long followersCount;
            public long followingCount;
            public long videoCount;
            public boolean isVerified;
            public String deviceToken;
            public boolean isDeleted;

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

            public String getShortBio() {
                return shortBio;
            }

            public void setShortBio(String shortBio) {
                this.shortBio = shortBio;
            }

            public boolean isEmailNotification() {
                return emailNotification;
            }

            public void setEmailNotification(boolean emailNotification) {
                this.emailNotification = emailNotification;
            }

            public boolean isPushNotification() {
                return pushNotification;
            }

            public void setPushNotification(boolean pushNotification) {
                this.pushNotification = pushNotification;
            }

            public long getFollowersCount() {
                return followersCount;
            }

            public void setFollowersCount(long followersCount) {
                this.followersCount = followersCount;
            }

            public long getFollowingCount() {
                return followingCount;
            }

            public void setFollowingCount(long followingCount) {
                this.followingCount = followingCount;
            }

            public long getVideoCount() {
                return videoCount;
            }

            public void setVideoCount(long videoCount) {
                this.videoCount = videoCount;
            }

            public boolean isVerified() {
                return isVerified;
            }

            public void setVerified(boolean verified) {
                isVerified = verified;
            }

            public String getDeviceToken() {
                return deviceToken;
            }

            public void setDeviceToken(String deviceToken) {
                this.deviceToken = deviceToken;
            }

            public boolean isDeleted() {
                return isDeleted;
            }

            public void setDeleted(boolean deleted) {
                isDeleted = deleted;
            }
        }
//        public class ReadCount{
//            public String _id;
//            public long count;
//
//            public String get_id() {
//                return _id;
//            }
//
//            public void set_id(String _id) {
//                this._id = _id;
//            }
//
//            public long getCount() {
//                return count;
//            }
//
//            public void setCount(long count) {
//                this.count = count;
//            }
//        }
    }
}
