package com.raaise.android.model;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UpdateRoomPojo {

    public RequestBody title;
    public RequestBody description;
    public MultipartBody.Part logo;
    public RequestBody slug;

    public UpdateRoomPojo(RequestBody title, RequestBody description, MultipartBody.Part logo, RequestBody slug) {
        this.title = title;
        this.description = description;
        this.logo = logo;
        this.slug = slug;
    }
}
