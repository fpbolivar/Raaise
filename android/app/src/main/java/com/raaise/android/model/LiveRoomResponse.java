package com.raaise.android.model;

import java.util.ArrayList;

public class LiveRoomResponse {

    public int status;
    public String message;
    public ArrayList<LiveRoomData> data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<LiveRoomData> getData() {
        return data;
    }
}
