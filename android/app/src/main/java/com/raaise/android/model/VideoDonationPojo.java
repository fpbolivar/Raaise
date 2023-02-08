package com.raaise.android.model;

import java.util.ArrayList;

public class VideoDonationPojo {

    private int status;
    private String message;
    private String claimedAmount;
    private String raisedDonationAmount;
    private String completedDonationAmount;
    private ArrayList<VideoData> data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getClaimedAmount() {
        return claimedAmount;
    }

    public String getRaisedDonationAmount() {
        return raisedDonationAmount;
    }

    public String getCompletedDonationAmount() {
        return completedDonationAmount;
    }

    public ArrayList<VideoData> getData() {
        return data;
    }

    public VideoDonationPojo(int status, String message, String claimedAmount, String raisedDonationAmount, String completedDonationAmount, ArrayList<VideoData> data) {
        this.status = status;
        this.message = message;
        this.claimedAmount = claimedAmount;
        this.raisedDonationAmount = raisedDonationAmount;
        this.completedDonationAmount = completedDonationAmount;
        this.data = data;
    }

    public class VideoData{
        private String id;
        private long pendingAmount;
        private String raisedAmount;
        private String name;
        private String date;

        public String getId() {
            return id;
        }

        public long getPendingAmount() {
            return pendingAmount;
        }

        public String getRaisedAmount() {
            return raisedAmount;
        }

        public String getName() {
            return name;
        }

        public String getDate() {
            return date;
        }

        public VideoData(String id, long pendingAmount, String raisedAmount, String name, String date) {
            this.id = id;
            this.pendingAmount = pendingAmount;
            this.raisedAmount = raisedAmount;
            this.name = name;
            this.date = date;
        }
    }
}


