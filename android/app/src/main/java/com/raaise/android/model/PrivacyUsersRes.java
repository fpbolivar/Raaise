package com.raaise.android.model;

import java.util.ArrayList;

public class PrivacyUsersRes {
    public int status;
    public String message;
    public PrivacyCurrentData currentData;
    public ArrayList<PrivacyData> data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public PrivacyCurrentData getCurrentData() {
        return currentData;
    }

    public ArrayList<PrivacyData> getData() {
        return data;
    }

    public class PrivacyCurrentData{
        public String _id;
        public String invitePrivacyControl;

        public String get_id() {
            return _id;
        }

        public String getInvitePrivacyControl() {
            return invitePrivacyControl;
        }
    }

    public class PrivacyData{
        public String _id;
        public String userName;
        public String name;
        public String email;
        public String profileImage;

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

        public String getProfileImage() {
            return profileImage;
        }
    }
}
