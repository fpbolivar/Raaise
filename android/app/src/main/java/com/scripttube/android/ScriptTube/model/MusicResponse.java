package com.scripttube.android.ScriptTube.model;

import java.util.ArrayList;

public class MusicResponse {
    public int status;
    public String message;
    public ArrayList<MusicData> data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<MusicData> getData() {
        return data;
    }
}
