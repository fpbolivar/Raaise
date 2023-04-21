package com.raaise.android.model;

import java.util.ArrayList;

public class LiveRoomChat {

    public int status;
    public String message;
    public ArrayList<ChatData> data;


    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<ChatData> getData() {
        return data;
    }

    public class ChatData {

        public ChatModel.Data.SenderId senderId;
        public String message;

        public ChatModel.Data.SenderId getSenderID() {
            return senderId;
        }

        public String getMessage() {
            return message;
        }
    }
}
