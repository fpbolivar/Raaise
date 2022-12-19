package com.scripttube.android.ScriptTube.ApiManager.RetrofitHelper;

import android.app.Application;
import android.content.Context;

import com.scripttube.android.ScriptTube.ApiManager.ApiManager;
import com.scripttube.android.ScriptTube.ApiManager.ApiManagerImplementation;

public class App extends Application {
    private static Context context;
    public static ApiManagerImplementation apiManager;

    @Override
    public void onCreate() {
        super.onCreate();
        App.context = getApplicationContext();
    }

    public static ApiManager getApiManager(){
        if (apiManager == null){
            apiManager = new ApiManagerImplementation();
        }
        return apiManager;
    }
    public static Context getContext(){
        return context;
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
