package com.raaise.android.model;

public class RoomResponse {

    public int status;
    public String message;
    public RoomData insertedData;
    public RoomToken token;

    public RoomToken getToken() {
        return token;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public RoomData getInsertedData() {
        return insertedData;
    }
}
