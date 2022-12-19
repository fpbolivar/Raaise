package com.scripttube.android.ScriptTube.model;

public class ChatListModel {
    public String chatPersonName;
    public String lastChatMessage;

    public ChatListModel(String chatPersonName, String lastChatMessage) {
        this.chatPersonName = chatPersonName;
        this.lastChatMessage = lastChatMessage;
    }
}
