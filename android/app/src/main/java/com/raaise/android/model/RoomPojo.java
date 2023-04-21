package com.raaise.android.model;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RoomPojo {
    public RequestBody title;
    public RequestBody description;
    public MultipartBody.Part logo;
    public RequestBody memberIds;
    public RequestBody roomType;

    public RequestBody getTitle() {
        return title;
    }

    public RequestBody getDescription() {
        return description;
    }

    public MultipartBody.Part getLogo() {
        return logo;
    }

    public RequestBody getMemberIds() {
        return memberIds;
    }

    public RequestBody getRoomType() {
        return roomType;
    }

    public RoomPojo(RequestBody title, RequestBody description, MultipartBody.Part logo, RequestBody memberIds, RequestBody roomType) {
        this.title = title;
        this.description = description;
        this.logo = logo;
        this.memberIds = memberIds;
        this.roomType = roomType;
    }
}
