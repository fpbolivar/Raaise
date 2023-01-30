package com.raaise.android.ApiManager.ApiModels;

public class GetSingleVideoModel {
    public String slug;
    public int status;
    public String message;
    public Data data;
    public Error errors;
    public GetSingleVideoModel(String slug) {
        this.slug = slug;
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

    public Error getErrors() {
        return errors;
    }

    public class Data {
        public GetVideo getVideo;
        public boolean isLike;
        public boolean isFollow;

        public GetVideo getGetVideo() {
            return getVideo;
        }

        public void setGetVideo(GetVideo getVideo) {
            this.getVideo = getVideo;
        }

        public boolean isLike() {
            return isLike;
        }

        public void setLike(boolean like) {
            isLike = like;
        }

        public boolean isFollow() {
            return isFollow;
        }

        public void setFollow(boolean follow) {
            isFollow = follow;
        }

        public class GetVideo {
            public String _id;
            public UserId userId;
            public String videoCaption;
            public String videoLink;
            public AudioId audioId;
            public CategoryId categoryId;
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

            public AudioId getAudioId() {
                return audioId;
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

            public CategoryId getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(CategoryId categoryId) {
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
                public int followersCount;
                public int followingCount;
                public int videoCount;
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

                public int getFollowersCount() {
                    return followersCount;
                }

                public void setFollowersCount(int followersCount) {
                    this.followersCount = followersCount;
                }

                public int getFollowingCount() {
                    return followingCount;
                }

                public void setFollowingCount(int followingCount) {
                    this.followingCount = followingCount;
                }

                public int getVideoCount() {
                    return videoCount;
                }

                public void setVideoCount(int videoCount) {
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

            public class CategoryId {
                public String _id;
                public String name;
                public String image;

                public String get_id() {
                    return _id;
                }

                public void set_id(String _id) {
                    this._id = _id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getImage() {
                    return image;
                }

                public void setImage(String image) {
                    this.image = image;
                }
            }

        }

    }

    public class Error {
        public String message;
        public String param;

        public String getMessage() {
            return message;
        }

        public String getParam() {
            return param;
        }
    }
}
