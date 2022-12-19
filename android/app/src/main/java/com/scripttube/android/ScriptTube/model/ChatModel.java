package com.scripttube.android.ScriptTube.model;

public class ChatModel {
    public String message;
    public boolean fromSender;
    public String time;

    public ChatModel(String message, boolean fromSender, String time) {
        this.message = message;
        this.fromSender = fromSender;
        this.time = time;
    }
}
