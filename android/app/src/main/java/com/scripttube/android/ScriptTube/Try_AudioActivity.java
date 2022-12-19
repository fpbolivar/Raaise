package com.scripttube.android.ScriptTube;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.scripttube.android.ScriptTube.Home.Fragments.Try_AudioFragment;

public class Try_AudioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try_audio);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.try_audio_container,new Try_AudioFragment())
                .commit();
    }
}