package com.raaise.android.ApiManager.ApiModels;

public class DeleteVideoModel {
    public String slug;
    public int status;
    public String message;

    public DeleteVideoModel(String slug) {
        this.slug = slug;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
