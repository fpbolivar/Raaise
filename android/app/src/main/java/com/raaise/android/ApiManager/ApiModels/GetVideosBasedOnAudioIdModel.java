package com.raaise.android.ApiManager.ApiModels;

import java.util.List;

public class GetVideosBasedOnAudioIdModel {
    public String audioId;
    public int status;
    public String message;
    public Audio audio;
    public List<Videos> videos;
    public GetVideosBasedOnAudioIdModel(String audioId) {
        this.audioId = audioId;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Audio getAudio() {
        return audio;
    }

    public List<Videos> getVideos() {
        return videos;
    }

    public class Audio {
        public String _id;
        public String slug;
        public String songName;
        public String artistName;
        public String Thumbnail;
        public String audio;
        public String audioTime;
        public String genreId;
        public String createdAt;
        public String updatedAt;
        public int __v;

        public String get_id() {
            return _id;
        }

        public String getSlug() {
            return slug;
        }

        public String getSongName() {
            return songName;
        }

        public String getArtistName() {
            return artistName;
        }

        public String getThumbnail() {
            return Thumbnail;
        }

        public String getAudio() {
            return audio;
        }

        public String getAudioTime() {
            return audioTime;
        }

        public String getGenreId() {
            return genreId;
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

    public class Videos {
        public String _id;
        public UserID userId;
        public String videoCaption;
        public String videoLink;
        public AudioId audioId;
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
        public String createdAt;
        public String updatedAt;
        public int __v;
        public boolean isLiked;
        public boolean isFollow;
        public boolean isViewed;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public UserID getUserId() {
            return userId;
        }

        public void setUserId(UserID userId) {
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

        public AudioId getAudioId() {
            return audioId;
        }

        public void setAudioId(AudioId audioId) {
            this.audioId = audioId;
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

        public boolean isViewed() {
            return isViewed;
        }

        public void setViewed(boolean viewed) {
            isViewed = viewed;
        }

        public class UserID {
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
            public boolean emailNotification;
            public boolean pushNotification;
            public int followersCount;
            public int followingCount;
            public int videoCount;
            public boolean isVerified;
            public String deviceToken;
            public boolean isDeleted;
            public String createdAt;
            public String updatedAt;
            public int __v;

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

            public int getVideoCount() {
                return videoCount;
            }

            public boolean isVerified() {
                return isVerified;
            }

            public String getDeviceToken() {
                return deviceToken;
            }

            public boolean isDeleted() {
                return isDeleted;
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

            public String getSlug() {
                return slug;
            }

            public String getSongName() {
                return songName;
            }

            public String getArtistName() {
                return artistName;
            }

            public String getThumbnail() {
                return Thumbnail;
            }

            public String getAudio() {
                return audio;
            }

            public String getAudioTime() {
                return audioTime;
            }

            public GenreId getGenreId() {
                return genreId;
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

    }
}
