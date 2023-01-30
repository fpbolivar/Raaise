package com.raaise.android.ApiManager.ApiModels;

public class UpdateCaptionModel {
    public String slug;
    public String videoCaption;
    public int status;
    public String message;
    public Data data;
    public UpdateCaptionModel(String slug, String videoCaption) {
        this.slug = slug;
        this.videoCaption = videoCaption;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    public class Data {
        public String videoCaption;

        public String getVideoCaption() {
            return videoCaption;
        }
    }
}
