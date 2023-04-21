package com.raaise.android.model;

public class SendChatBody {

    public String senderId;
    public String slug;
    public String message;

    public SendChatBody(String senderId, String slug, String message) {
        this.senderId = senderId;
        this.slug = slug;
        this.message = message;
    }
}
