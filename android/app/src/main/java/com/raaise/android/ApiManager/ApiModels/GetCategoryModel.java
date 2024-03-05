package com.raaise.android.ApiManager.ApiModels;

import java.util.List;

public class GetCategoryModel {
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
