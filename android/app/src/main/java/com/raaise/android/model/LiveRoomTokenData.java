package com.raaise.android.model;

import java.util.ArrayList;

public class LiveRoomTokenData {

    public String _id;
    public String slug;
    public String title;
    public String description;
    public String logo;
    public HostID hostId;
    public ArrayList<MemberIds> memberIds;
    public String roomId;
    public String token;

    public String get_id() {
        return _id;
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

    public HostID getHostId() {
        return hostId;
    }

    public ArrayList<MemberIds> getMemberIds() {
        return memberIds;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getToken() {
        return token;
    }
}
