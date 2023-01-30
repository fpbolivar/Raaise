package com.raaise.android.ApiManager.ApiModels;

import java.util.List;

public class GlobalSearchModel {
    public String search;
    public String limit;
    public String page;
    public String type;
    public int status;
    public String message;
    public Data data;

    public GlobalSearchModel(String search, String limit, String page, String type) {
        this.search = search;
        this.limit = limit;
        this.page = page;
        this.type = type;
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
        public List<Users> users;
        public List<Posts> posts;
        public List<Audios> audios;

        public List<Users> getUsers() {
            return users;
        }

        public List<Posts> getPosts() {
            return posts;
        }

        public List<Audios> getAudios() {
            return audios;
        }

        public class Users {
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
            public String shortBio;
            public String otp;
            public String city;
            public String state;
            public String address;
            public String postalCode;
            public String customerId;
            public String accountId;
            public String routingNumber;
            public String bankPhone;
            public String stripeAccountId;
            public String walletCreditAmount;
            public String walletDebitAmount;
            public boolean emailNotification;
            public boolean pushNotification;
            public int followersCount;
            public int followingCount;
            public int videoCount;
            public boolean isVerified;
            public String deviceToken;
            public String donation_comment;
            public boolean isDeleted;
            public String createdAt;
            public String updatedAt;

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

            public String getCity() {
                return city;
            }

            public String getState() {
                return state;
            }

            public String getAddress() {
                return address;
            }

            public String getPostalCode() {
                return postalCode;
            }

            public String getCustomerId() {
                return customerId;
            }

            public String getAccountId() {
                return accountId;
            }

            public String getRoutingNumber() {
                return routingNumber;
            }

            public String getBankPhone() {
                return bankPhone;
            }

            public String getStripeAccountId() {
                return stripeAccountId;
            }

            public String getWalletCreditAmount() {
                return walletCreditAmount;
            }

            public String getWalletDebitAmount() {
                return walletDebitAmount;
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

            public String getDonation_comment() {
                return donation_comment;
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
        }

        public class Posts {
            public String _id;
            public UserID userId;
            public String videoCaption;
            public String videoLink;
            public AudioId audioId;
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
            public String createdAt;
            public String updatedAt;
            public int __v;
            public boolean isLiked;
            public boolean isFollow;
            public boolean isViewed;

            public AudioId getAudioId() {
                return audioId;
            }

            public String get_id() {
                return _id;
            }

            public UserID getUserId() {
                return userId;
            }

            public String getVideoCaption() {
                return videoCaption;
            }

            public String getVideoLink() {
                return videoLink;
            }

            public String getCategoryId() {
                return categoryId;
            }

            public boolean isDonation() {
                return isDonation;
            }

            public String getDonationAmount() {
                return donationAmount;
            }

            public String getVideoImage() {
                return videoImage;
            }

            public String getSlug() {
                return slug;
            }

            public int getVideolikeCount() {
                return videolikeCount;
            }

            public int getVideoCommentCount() {
                return videoCommentCount;
            }

            public int getVideoReportCount() {
                return videoReportCount;
            }

            public int getVideoShareCount() {
                return videoShareCount;
            }

            public boolean isReported() {
                return isReported;
            }

            public boolean isBlock() {
                return isBlock;
            }

            public boolean isDeleted() {
                return isDeleted;
            }

            public int getVideoViewCount() {
                return videoViewCount;
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

            public boolean isLiked() {
                return isLiked;
            }

            public boolean isFollow() {
                return isFollow;
            }

            public boolean isViewed() {
                return isViewed;
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

            public class UserID {
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

                public String getShortBio() {
                    return shortBio;
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
            }
        }

        public class Audios {
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
