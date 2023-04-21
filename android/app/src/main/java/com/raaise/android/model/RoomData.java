package com.raaise.android.model;

import java.util.ArrayList;

public class RoomData {

    public String slug;
    public String title;
    public String description;
    public String logo;
    public String hostId;
    public ArrayList<String> memberIds;
    public String roomId;

    public String getRoomId() {
        return roomId;
    }

    public String getSlug() {
        return slug;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLogo() {
        return logo;
    }

    public String getHostId() {
        return hostId;
    }

    public ArrayList<String> getMemberIds() {
        return memberIds;
    }
}
