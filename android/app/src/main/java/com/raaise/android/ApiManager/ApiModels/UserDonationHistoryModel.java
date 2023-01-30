package com.raaise.android.ApiManager.ApiModels;

import java.util.List;

public class UserDonationHistoryModel {
    public int status;
    public String message;
    public Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        public String walletCreditAmount;
        public String donatedAmount;
        public String walletDebitAmount;
        public List<UserVideo> userVideo;

        public String getWalletCreditAmount() {
            return walletCreditAmount;
        }

        public void setWalletCreditAmount(String walletCreditAmount) {
            this.walletCreditAmount = walletCreditAmount;
        }

        public String getDonatedAmount() {
            return donatedAmount;
        }

        public void setDonatedAmount(String donatedAmount) {
            this.donatedAmount = donatedAmount;
        }

        public String getWalletDebitAmount() {
            return walletDebitAmount;
        }

        public void setWalletDebitAmount(String walletDebitAmount) {
            this.walletDebitAmount = walletDebitAmount;
        }

        public List<UserVideo> getUserVideo() {
            return userVideo;
        }

        public void setUserVideo(List<UserVideo> userVideo) {
            this.userVideo = userVideo;
        }

        public class UserVideo{
            public String _id;
            public String userId;
            public String videoCaption;
            public String videoLink;
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
            public String totalDanotionAmount;
            public String createdAt;
            public String updatedAt;
            public int __v;
            public String status;

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

            public String getTotalDanotionAmount() {
                return totalDanotionAmount;
            }

            public void setTotalDanotionAmount(String totalDanotionAmount) {
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

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }

}
