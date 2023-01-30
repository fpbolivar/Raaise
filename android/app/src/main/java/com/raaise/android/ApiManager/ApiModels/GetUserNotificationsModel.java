package com.raaise.android.ApiManager.ApiModels;


import java.util.List;

public class GetUserNotificationsModel {
    public String page;
    public String limit;
    public int status;
    public String message;
    public List<NotificationMessage> notificationMessage;
    public int current;
    public int next;
    public int totalData;
    public int totalPages;
    public Error errors;
    public GetUserNotificationsModel(String page, String limit) {
        this.page = page;
        this.limit = limit;
    }

    public int getCurrent() {
        return current;
    }

    public int getNext() {
        return next;
    }

    public int getTotalData() {
        return totalData;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public Error getErrors() {
        return errors;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<NotificationMessage> getNotificationMessage() {
        return notificationMessage;
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

    public class NotificationMessage {
        public String id;
        public String title;
        public String type;
        public String fromUserName;
        public String fromProfileImage;
        public String message;
        public String createdAt;
        public String slug;
        public String targetId;
        public String targetName;
        public boolean isRead;
        public boolean isAdmin;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getFromUserName() {
            return fromUserName;
        }

        public void setFromUserName(String fromUserName) {
            this.fromUserName = fromUserName;
        }

        public String getFromProfileImage() {
            return fromProfileImage;
        }

        public void setFromProfileImage(String fromProfileImage) {
            this.fromProfileImage = fromProfileImage;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public String getTargetId() {
            return targetId;
        }

        public void setTargetId(String targetId) {
            this.targetId = targetId;
        }

        public String getTargetName() {
            return targetName;
        }

        public void setTargetName(String targetName) {
            this.targetName = targetName;
        }

        public boolean isRead() {
            return isRead;
        }

        public void setRead(boolean read) {
            isRead = read;
        }

        public boolean isAdmin() {
            return isAdmin;
        }

        public void setAdmin(boolean admin) {
            isAdmin = admin;
        }
    }
}
