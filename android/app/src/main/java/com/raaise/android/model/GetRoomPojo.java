package com.raaise.android.model;

public class GetRoomPojo {

    public String page;
    public String limit;
    public String query;

    public GetRoomPojo(String page, String limit, String query) {
        this.page = page;
        this.limit = limit;
        this.query = query;
    }
}
