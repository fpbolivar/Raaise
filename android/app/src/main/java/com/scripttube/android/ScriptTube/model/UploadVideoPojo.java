package com.scripttube.android.ScriptTube.model;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UploadVideoPojo {
    RequestBody videoCaption;
    boolean donation;
    RequestBody donationAmt;
    RequestBody audioID;
    MultipartBody.Part video;
    MultipartBody.Part image;

    public UploadVideoPojo(RequestBody videoCaption, boolean donation, RequestBody donationAmt, RequestBody audioID, MultipartBody.Part video, MultipartBody.Part image) {
        this.videoCaption = videoCaption;
        this.donation = donation;
        this.donationAmt = donationAmt;
        this.audioID = audioID;
        this.video = video;
        this.image = image;
    }

    public RequestBody getVideoCaption() {
        return videoCaption;
    }

    public boolean isDonation() {
        return donation;
    }

    public RequestBody getDonationAmt() {
        return donationAmt;
    }

    public RequestBody getAudioID() {
        return audioID;
    }

    public MultipartBody.Part getVideo() {
        return video;
    }

    public MultipartBody.Part getImage() {
        return image;
    }

    public void setVideoCaption(RequestBody videoCaption) {
        this.videoCaption = videoCaption;
    }

    public void setDonation(boolean donation) {
        this.donation = donation;
    }

    public void setDonationAmt(RequestBody donationAmt) {
        this.donationAmt = donationAmt;
    }

    public void setAudioID(RequestBody audioID) {
        this.audioID = audioID;
    }

    public void setVideo(MultipartBody.Part video) {
        this.video = video;
    }

    public void setImage(MultipartBody.Part image) {
        this.image = image;
    }
}
