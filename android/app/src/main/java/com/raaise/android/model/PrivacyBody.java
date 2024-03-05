package com.raaise.android.model;

public class PrivacyBody {
    public String page;
    public String limit;
    public String query;

    public PrivacyBody(String page, String limit, String query) {
        this.page = page;
        this.limit = limit;
        this.query = query;
    }
}
