package com.raaise.android.model;

import java.util.List;

public class ChatModel {
    //   public String chatSlug;
//
//    public ChatModel(String chatSlug) {
//        this.chatSlug = chatSlug;
//    }
//    public String receiverId;
//    public String senderId;
    public String chatSlug;
    public String page;
    public String limit;
    public int status;
    public String message;

//    public ChatModel(String receiverId, String senderId, String limit) {
//        this.receiverId = receiverId;
//        this.senderId = senderId;
//        this.limit = limit;
//    }
//
//    public ChatModel(String receiverId, String senderId) {
//        this.receiverId = receiverId;
//        this.senderId = senderId;
//    }
    public List<Data> data;
    public ChatModel(String chatSlug, String page, String limit) {
        this.chatSlug = chatSlug;
        this.page = page;
        this.limit = limit;
    }
    public ChatModel() {
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
        public String chatId;
        public String chatSlug;
        public String messageType;
        public String message;
        public SenderId senderId;
        public ReceiverId receiverId;
        public String chatHistoryTime;
        public boolean isRead;
        public VideoId videoId;
        public String createdAt;
        public String updatedAt;
        public boolean isDeleteVisible;
        public int __v;

        public boolean isDeleteVisible() {
            return isDeleteVisible;
        }

        public void setDeleteVisible(boolean deleteVisible) {
            isDeleteVisible = deleteVisible;
        }

        public VideoId getVideoId() {
            return videoId;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getChatId() {
            return chatId;
        }

        public void setChatId(String chatId) {
            this.chatId = chatId;
        }

        public String getChatSlug() {
            return chatSlug;
        }

        public void setChatSlug(String chatSlug) {
            this.chatSlug = chatSlug;
        }

        public String getMessageType() {
            return messageType;
        }

        public void setMessageType(String messageType) {
            this.messageType = messageType;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
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

        public String getChatHistoryTime() {
            return chatHistoryTime;
        }

        public void setChatHistoryTime(String chatHistoryTime) {
            this.chatHistoryTime = chatHistoryTime;
        }

        public boolean isRead() {
            return isRead;
        }

        public void setRead(boolean read) {
            isRead = read;
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

        public class VideoId {
            public String _id;
            public String userId;
            public String videoCaption;
            public String videoLink;
            public String audioId;
            public String categoryId;
            public boolean isDonation;
            public String donationAmount;
            public String videoImage;
            public String slug;
            public int videolikeCount;
            public int videoCommentCount;
            public int videoReportCount;
            public int videoShareCount;
            public boolean isReported;
            public boolean isBlock;
            public boolean isDeleted;
            public int videoViewCount;
            public int totalDanotionAmount;
            public String createdAt;
            public String updatedAt;
            public int __v;

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

            public String getVideoCaption() {
                return videoCaption;
            }

            public void setVideoCaption(String videoCaption) {
                this.videoCaption = videoCaption;
            }

            public String getVideoLink() {
                return videoLink;
            }

            public void setVideoLink(String videoLink) {
                this.videoLink = videoLink;
            }

            public String getAudioId() {
                return audioId;
            }

            public void setAudioId(String audioId) {
                this.audioId = audioId;
            }

            public String getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(String categoryId) {
                this.categoryId = categoryId;
            }

            public boolean isDonation() {
                return isDonation;
            }

            public void setDonation(boolean donation) {
                isDonation = donation;
            }

            public String getDonationAmount() {
                return donationAmount;
            }

            public void setDonationAmount(String donationAmount) {
                this.donationAmount = donationAmount;
            }

            public String getVideoImage() {
                return videoImage;
            }

            public void setVideoImage(String videoImage) {
                this.videoImage = videoImage;
            }

            public String getSlug() {
                return slug;
            }

            public void setSlug(String slug) {
                this.slug = slug;
            }

            public int getVideolikeCount() {
                return videolikeCount;
            }

            public void setVideolikeCount(int videolikeCount) {
                this.videolikeCount = videolikeCount;
            }

            public int getVideoCommentCount() {
                return videoCommentCount;
            }

            public void setVideoCommentCount(int videoCommentCount) {
                this.videoCommentCount = videoCommentCount;
            }

            public int getVideoReportCount() {
                return videoReportCount;
            }

            public void setVideoReportCount(int videoReportCount) {
                this.videoReportCount = videoReportCount;
            }

            public int getVideoShareCount() {
                return videoShareCount;
            }

            public void setVideoShareCount(int videoShareCount) {
                this.videoShareCount = videoShareCount;
            }

            public boolean isReported() {
                return isReported;
            }

            public void setReported(boolean reported) {
                isReported = reported;
            }

            public boolean isBlock() {
                return isBlock;
            }

            public void setBlock(boolean block) {
                isBlock = block;
            }

            public boolean isDeleted() {
                return isDeleted;
            }

            public void setDeleted(boolean deleted) {
                isDeleted = deleted;
            }

            public int getVideoViewCount() {
                return videoViewCount;
            }

            public void setVideoViewCount(int videoViewCount) {
                this.videoViewCount = videoViewCount;
            }

            public int getTotalDanotionAmount() {
                return totalDanotionAmount;
            }

            public void setTotalDanotionAmount(int totalDanotionAmount) {
                this.totalDanotionAmount = totalDanotionAmount;
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
        }

        public class SenderId {
            public String _id;
            public String userName;
            public String profileImage;

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

            public String getProfileImage() {
                return profileImage;
            }

            public void setProfileImage(String profileImage) {
                this.profileImage = profileImage;
            }
        }

        public class ReceiverId {
            public String _id;
            public String userName;
            public String profileImage;

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

            public String getProfileImage() {
                return profileImage;
            }

            public void setProfileImage(String profileImage) {
                this.profileImage = profileImage;
            }
        }
    }
}
