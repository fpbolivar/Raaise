package com.raaise.android.ApiManager.ApiModels;

import java.util.List;

public class GetGlobalVideoModel {
    public String type;
    public String limit;
    public String page;
    public int status;
    public String message;
    public List<Data> data;
    public GetGlobalVideoModel(String limit, String page) {
        this.limit = limit;
        this.page = page;
    }
    public GetGlobalVideoModel(String type, String limit, String page) {
        this.type = type;
        this.limit = limit;
        this.page = page;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
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
        public UserId userId;
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
        public String createdAt;
        public String updatedAt;
        public int __v;
        public boolean isLiked;
        public boolean isFollow;

        public int getVideoReportCount() {
            return videoReportCount;
        }

        public void setVideoReportCount(int videoReportCount) {
            this.videoReportCount = videoReportCount;
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

        public int getVideoShareCount() {
            return videoShareCount;
        }

        public void setVideoShareCount(int videoShareCount) {
            this.videoShareCount = videoShareCount;
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

        public class UserId {
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
            public String createdAt;
            public String updatedAt;
            public String __v;
            public boolean isVerified;

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

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
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

            public String getOtp() {
                return otp;
            }

            public void setOtp(String otp) {
                this.otp = otp;
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

            public boolean isVerified() {
                return isVerified;
            }

            public void setVerified(boolean verified) {
                isVerified = verified;
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

    }

}
