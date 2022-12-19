package com.scripttube.android.ScriptTube.model;

public class LoginPojo {
    public String email;
    public String password;
    public String deviceToken;

    public LoginPojo(String email, String password, String deviceToken) {
        this.email = email;
        this.password = password;
        this.deviceToken = deviceToken;
    }
}
