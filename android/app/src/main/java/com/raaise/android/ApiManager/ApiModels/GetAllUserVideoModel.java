package com.raaise.android.ApiManager.ApiModels;

import java.util.ArrayList;
import java.util.List;

public class GetAllUserVideoModel {

    public int status;
    public String message;
    public List<Data> data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<Data> getData() {
        return data;
    }

    public class Data {
        public int videolikeCount;
        public int videoCommentCount;
        public int videoShareCount;
        public int videoViewCount;
        public String _id;
        public UserId userId;
        public String videoCaption;
        public String videoLink;
        public boolean isDonation;
        public AudioId audioId;
        public String donationAmount;
        public String videoImage;
        public String slug;
        public String createdAt;
        public String totalDanotionAmount;
        public String updatedAt;
        public int __v;
        public boolean isLiked;
        public boolean isFollow;

        public String getTotalDanotionAmount() {
            return totalDanotionAmount;
        }

        public void setTotalDanotionAmount(String totalDanotionAmount) {
            this.totalDanotionAmount = totalDanotionAmount;
        }

        public AudioId getAudioId() {
            return audioId;
        }

        public void setAudioId(AudioId audioId) {
            this.audioId = audioId;
        }

        public boolean isLiked() {
            return isLiked;
        }

        public void setLiked(boolean liked) {
            isLiked = liked;
        }

        public boolean isFollow() {
            return isFollow;
        }

        public void setFollow(boolean follow) {
            isFollow = follow;
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

        public int getVideoShareCount() {
            return videoShareCount;
        }

        public void setVideoShareCount(int videoShareCount) {
            this.videoShareCount = videoShareCount;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public UserId getUserId() {
            return userId;
        }

        public void setUserId(UserId userId) {
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

        public int getVideoViewCount() {
            return videoViewCount;
        }

        public void setVideoViewCount(int videoViewCount) {
            this.videoViewCount = videoViewCount;
        }

        public class AudioId {
            public String _id;
            public String slug;
            public String songName;
            public String artistName;
            public String Thumbnail;
            public String audio;
            public String audioTime;
            public GenreId genreId;
            public String createdAt;
            public String updatedAt;
            public int __v;

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

            public String getSongName() {
                return songName;
            }

            public void setSongName(String songName) {
                this.songName = songName;
            }

            public String getArtistName() {
                return artistName;
            }

            public void setArtistName(String artistName) {
                this.artistName = artistName;
            }

            public String getThumbnail() {
                return Thumbnail;
            }

            public void setThumbnail(String thumbnail) {
                Thumbnail = thumbnail;
            }

            public String getAudio() {
                return audio;
            }

            public void setAudio(String audio) {
                this.audio = audio;
            }

            public String getAudioTime() {
                return audioTime;
            }

            public void setAudioTime(String audioTime) {
                this.audioTime = audioTime;
            }

            public GenreId getGenreId() {
                return genreId;
            }

            public void setGenreId(GenreId genreId) {
                this.genreId = genreId;
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

            public class GenreId {
                public String _id;
                public String name;

                public String get_id() {
                    return _id;
                }

                public String getName() {
                    return name;
                }
            }
        }

        public class UserId {
            public boolean emailNotification;
            public boolean pushNotification;
            public String _id;
            public String userName;
            public String name;
            public String email;
            public String password;
            public String phoneNumber;
            public String deviceType;
            public String loginType;
            public String profileImage;
            public boolean isBlock;
            public boolean isActive;
            public String userGoogleId;
            public String userFaceBookId;
            public String shortBio;
            public String otp;
            public String createdAt;
            public String updatedAt;
            public int __v;
            public boolean isVerified;

            public boolean isEmailNotification() {
                return emailNotification;
            }

            public boolean isPushNotification() {
                return pushNotification;
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

            public String getPassword() {
                return password;
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

            public String getCreatedAt() {
                return createdAt;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }

            public int get__v() {
                return __v;
            }

        }
    }
}
