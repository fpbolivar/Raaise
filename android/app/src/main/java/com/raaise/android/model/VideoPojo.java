package com.raaise.android.model;

import com.raaise.android.Home.MainHome.Home;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class VideoPojo {
    Home home;
    String token;
    RequestBody videoCaption;
    boolean isDonation;
    RequestBody donationAmt;
    RequestBody finalAudioId;
    RequestBody finalCategoryId;
    MultipartBody.Part vdoBody;
    MultipartBody.Part imgBody;

    public VideoPojo(Home home, String getBearerToken, RequestBody videoCaption, boolean isDonation, RequestBody donationAmt, RequestBody finalAudioID,
                     RequestBody finalCategoryID, MultipartBody.Part vdoBody, MultipartBody.Part imageBody) {

        this.home = home;
        this.token = getBearerToken;
        this.videoCaption = videoCaption;
        this.isDonation = isDonation;
        this.donationAmt = donationAmt;
        this.finalAudioId = finalAudioID;
        this.finalCategoryId = finalCategoryID;
        this.vdoBody = vdoBody;
        this.imgBody = imageBody;
    }

    public Home getHome() {
        return home;
    }

    public String getToken() {
        return token;
    }

    public RequestBody getVideoCaption() {
        return videoCaption;
    }

    public boolean isDonation() {
        return isDonation;
    }

    public RequestBody getDonationAmt() {
        return donationAmt;
    }

    public RequestBody getFinalAudioId() {
        return finalAudioId;
    }

    public RequestBody getFinalCategoryId() {
        return finalCategoryId;
    }

    public MultipartBody.Part getVdoBody() {
        return vdoBody;
    }

    public MultipartBody.Part getImgBody() {
        return imgBody;
    }
}
