package com.scripttube.android.ScriptTube.Settings.Payments;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.scripttube.android.ScriptTube.R;

public class WithDrawls extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.anim.zoom_enter;
        setContentView(R.layout.activity_with_drawls);
    }
}