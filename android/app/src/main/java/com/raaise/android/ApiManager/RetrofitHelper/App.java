package com.raaise.android.ApiManager.RetrofitHelper;

import android.app.Application;
import android.content.Context;

import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiManagerImplementation;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class App extends Application {
    public static ApiManagerImplementation apiManager;
    public static boolean fromTryAudio = false;
    public static String musicTitle = "";
    public static String songName = "";
    public static boolean claim = false;
    private static Context context;
    private final Socket mSocket;

    {
        try {
            mSocket = IO.socket("http://34.230.148.158:5000");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static ApiManager getApiManager() {
        if (apiManager == null) {
            apiManager = new ApiManagerImplementation();
        }
        return apiManager;
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        App.context = getApplicationContext();
    }

    public Socket getSocket() {
        return mSocket;
    }

//    private HttpProxyCacheServer proxy;
//
//    public static HttpProxyCacheServer getProxy(Context context) {
//        App app = (App) context.getApplicationContext();
//        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
//    }
//
//    private HttpProxyCacheServer newProxy() {
//        return new HttpProxyCacheServer(this);
//    }
}
