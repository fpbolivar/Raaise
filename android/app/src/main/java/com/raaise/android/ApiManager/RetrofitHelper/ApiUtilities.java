package com.raaise.android.ApiManager.RetrofitHelper;

import com.raaise.android.Utilities.HelperClasses.StringHelper;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtilities {
    private static Retrofit retrofit = null;

    public static ApiInterface getApiInterface() {
         final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(2, TimeUnit.MINUTES)
                 .writeTimeout(2, TimeUnit.MINUTES)
                .build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(StringHelper.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit.create(ApiInterface.class);
    }
}
