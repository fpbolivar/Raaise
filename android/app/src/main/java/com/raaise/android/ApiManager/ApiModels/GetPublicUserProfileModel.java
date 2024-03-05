package com.raaise.android.ApiManager.ApiModels;

import java.util.ArrayList;

public class GetPublicUserProfileModel {
    public String userIdentity;
    public int status;
    public String message;
    public Data data;
    public GetPublicUserProfileModel(String userIdentity) {
        this.userIdentity = userIdentity;
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
        public String deviceToken;
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
        public String coverImage;
        public String userGoogleId;
        public String userFaceBookId;
        public String donatedAmount;

        public String shortBio;
        public String otp;
        public boolean emailNotification;
        public boolean pushNotification;
        public int followersCount;
        public int followingCount;
        public boolean isVerified;
        public String createdAt;
        public String updatedAt;
        public int __v;
        ArrayList<String> blockedIds;
        ArrayList<InterestedCategories> interestCategoryData;
        ArrayList<DonationUsers> donationUsers;

        public ArrayList<DonationUsers> getDonationUsers() {
            return donationUsers;
        }

        public void setDonationUsers(ArrayList<DonationUsers> donationUsers) {
            this.donationUsers = donationUsers;
        }

        public String blocked_by;

        public String getBlocked_by() {
            return blocked_by;
        }

        public ArrayList<String> getBlockedIds() {
            return blockedIds;
        }

        public int videoCount;
        public boolean follow;

        public String getDonatedAmount() {
            return donatedAmount;
        }

        public boolean isFollow() {
            return follow;
        }

        public String getDeviceToken() {
            return deviceToken;
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

        public boolean isVerified() {
            return isVerified;
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

        public int getVideoCount() {
            return videoCount;
        }

        public ArrayList<InterestedCategories> getInterestCategoryData() {
            return interestCategoryData;
        }

        public void setInterestCategoryData(ArrayList<InterestedCategories> interestCategoryData) {
            this.interestCategoryData = interestCategoryData;
        }

        public class DonationUsers{
            public String id;
            public String name;
            public String userName;

            public String profileImage;
            public String credit;

            public String getUsername() {
                return userName;
            }

            public void setUsername(String userName) {
                this.userName = userName;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getProfileImage() {
                return profileImage;
            }

            public void setProfileImage(String profileImage) {
                this.profileImage = profileImage;
            }

            public String getCredit() {
                return credit;
            }

            public void setCredit(String credit) {
                this.credit = credit;
            }
        }

        public class InterestedCategories {
            public String _id;
            public String name;
            public boolean isDeleted;
            public String createdAt;
            public String updatedAt;
            public String image;
            public int __v;

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String get_id() {
                return _id;
            }

            public String getName() {
                return name;
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
    }
}
