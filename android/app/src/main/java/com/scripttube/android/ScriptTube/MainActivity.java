package com.scripttube.android.ScriptTube;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.scripttube.android.ScriptTube.Activity.Introduction.Introduction2;
import com.scripttube.android.ScriptTube.Activity.Introduction.introduction1;

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
                startActivity(new Intent(getApplicationContext(), introduction1.class));
                finish();
            }
        },1000);
    }
}