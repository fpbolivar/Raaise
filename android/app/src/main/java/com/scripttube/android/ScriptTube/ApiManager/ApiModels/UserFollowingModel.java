package com.scripttube.android.ScriptTube.ApiManager.ApiModels;

import java.util.List;

public class UserFollowingModel {

    public String search;
    public String limit;
    public String page;

    public UserFollowingModel(String search, String limit, String page) {
        this.search = search;
        this.limit = limit;
        this.page = page;
    }

    public UserFollowingModel() {

    }

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

    public class  Data{
        public String _id;
        public FollowTo followTo;
        public String createdAt;
        public String updatedAt;
        public int __v;

        public String get_id() {
            return _id;
        }

        public FollowTo getFollowTo() {
            return followTo;
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

        public class FollowTo{
            public String _id;
            public String userName;
            public String name;
            public String profileImage;
            public int followersCount;
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

            public String getProfileImage() {
                return profileImage;
            }

            public int getFollowersCount() {
                return followersCount;
            }

            public boolean isDeleted() {
                return isDeleted;
            }
        }
    }
}
