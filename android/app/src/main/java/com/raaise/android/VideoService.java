package com.raaise.android;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.raaise.android.ApiManager.RetrofitHelper.ApiUtilities;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.Home.Fragments.PlusFragment;
import com.raaise.android.Home.MainHome.Home;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.model.VideoPojo;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoService extends Service {

    public VideoService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("serviceClass", "onCreate: Started");
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("serviceClass", "onDestroy: Service destroyed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            new Thread(() -> {
                VideoPojo pojo = PlusFragment.pojo;
                ApiUtilities.getApiInterface().uploadVideo(pojo.getToken(), pojo.getVideoCaption(), pojo.isDonation(), pojo.getDonationAmt(), pojo.getFinalAudioId(),
                        pojo.getFinalCategoryId(), pojo.getVdoBody(), pojo.getImgBody()).enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            Log.i("serviceClass", "onResponse: Success");
                            try {

                                File audioFile = new File(Environment.getExternalStorageDirectory().getPath() + "/Download/" + pojo.getHome().musicTitle);

                                if (audioFile.exists()) {
                                    audioFile.delete();
                                }


                            } catch (Exception e) {
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.i("serviceClass", "onResponse: Failure");
                    }
                });

            }).start();
        } catch (Exception e){
        }
        return super.onStartCommand(intent, flags, startId);
    }
}