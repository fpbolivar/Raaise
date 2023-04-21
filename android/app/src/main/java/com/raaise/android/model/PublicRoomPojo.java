package com.raaise.android.model;

public class PublicRoomPojo {

    public String page;
    public String limit;
    public String query;

    public PublicRoomPojo(String page, String limit) {
        this.page = page;
        this.limit = limit;
        this.query = "room";
    }
}
