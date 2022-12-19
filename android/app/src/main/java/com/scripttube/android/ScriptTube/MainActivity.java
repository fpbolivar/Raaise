package com.scripttube.android.ScriptTube;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.scripttube.android.ScriptTube.Activity.Introduction.Introduction2;
import com.scripttube.android.ScriptTube.Activity.Introduction.introduction1;
import com.scripttube.android.ScriptTube.Home.MainHome.Home;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prefs;

public class MainActivity extends AppCompatActivity {
    Handler h ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.anim.zoom_enter;
        setContentView(R.layout.activity_main);
     
        h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!Prefs.GetBearerToken(MainActivity.this).equalsIgnoreCase(""))
                {
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    finish();
                }
                else
                {
                    startActivity(new Intent(getApplicationContext(), introduction1.class));
                    finish();
                }

            }
        },1000);

    }
}