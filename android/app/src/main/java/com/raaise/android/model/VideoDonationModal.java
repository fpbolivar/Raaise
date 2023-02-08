package com.raaise.android.model;

public class VideoDonationModal {

    private String videoId;
    private String startDate;
    private String endDate;

    public VideoDonationModal(String videoId) {
        this.videoId = videoId;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public VideoDonationModal(String videoId, String startDate, String endDate) {
        this.videoId = videoId;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
