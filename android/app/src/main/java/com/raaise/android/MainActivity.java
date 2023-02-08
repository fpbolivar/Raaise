package com.raaise.android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.raaise.android.Activity.Credentials.Login;
import com.raaise.android.Home.MainHome.Home;
import com.raaise.android.Utilities.HelperClasses.Prefs;


public class MainActivity extends AppCompatActivity {
    Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.anim.zoom_enter;
        setContentView(R.layout.activity_main);

        h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!Prefs.GetBearerToken(MainActivity.this).equalsIgnoreCase("")) {
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    finish();
                } else {
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                }

            }
        }, 1000);

    }
}