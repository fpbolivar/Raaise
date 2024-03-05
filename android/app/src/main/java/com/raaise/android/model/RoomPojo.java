package com.raaise.android.model;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RoomPojo {
    public RequestBody title;
    public RequestBody description;
    public MultipartBody.Part logo;
    public RequestBody memberIds;
    public RequestBody roomType;
    public RequestBody scheduleType;
    public RequestBody scheduleDateTime;

    public RequestBody getScheduleType() {
        return scheduleType;
    }

    public RequestBody getScheduleDateTime() {
        return scheduleDateTime;
    }

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

    public RoomPojo(RequestBody title, RequestBody description, MultipartBody.Part logo, RequestBody memberIds, RequestBody roomType,
                    RequestBody scheduleType, RequestBody scheduleDateTime) {
        this.title = title;
        this.description = description;
        this.logo = logo;
        this.memberIds = memberIds;
        this.roomType = roomType;
        this.scheduleType = scheduleType;
        this.scheduleDateTime = scheduleDateTime;
    }
}
