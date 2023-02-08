package com.raaise.android;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.raaise.android.Home.Fragments.Try_AudioFragment;

public class Try_AudioActivity extends AppCompatActivity {

    // For new implementation
    public String videoPath = "";
    public String musicData = "";
    public String musicTitle = "";
    public String videoUri = "";
    public boolean shouldMergeAudio = false;

    public void FinishHere() {
        Try_AudioActivity.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try_audio);

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.try_audio_container, new Try_AudioFragment(getIntent().getStringExtra("AudioId")))
                .commit();
    }
}