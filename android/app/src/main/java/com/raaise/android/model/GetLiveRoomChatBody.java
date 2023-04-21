package com.raaise.android.model;

public class GetLiveRoomChatBody {

    public String userId;
    public String slug;
    public String page;
    public String limit;

    public GetLiveRoomChatBody(String userId, String slug, String page, String limit) {
        this.userId = userId;
        this.slug = slug;
        this.page = page;
        this.limit = limit;
    }
}
