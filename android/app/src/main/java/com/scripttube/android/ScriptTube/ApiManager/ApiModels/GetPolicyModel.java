package com.scripttube.android.ScriptTube.ApiManager.ApiModels;

public class GetPolicyModel {
    public String type;
    public int status;
    public String message;
    public Data data;
    public Error errors;

    public Error getErrors() {
        return errors;
    }

    public GetPolicyModel(String type) {
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

    public class Error {
        public String message;
        public String param;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getParam() {
            return param;
        }

        public void setParam(String param) {
            this.param = param;
        }
    }

    public class Data {
        public String _id;
        public String type;
        public String title;
        public String description;
        public String createdAt;
        public String updatedAt;
        public int __v;

        public String get_id() {
            return _id;
        }

        public String getType() {
            return type;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
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
