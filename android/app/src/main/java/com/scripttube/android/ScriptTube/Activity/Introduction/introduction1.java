package com.scripttube.android.ScriptTube.Activity.Introduction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.scripttube.android.ScriptTube.R;

public class introduction1 extends AppCompatActivity {
    ImageView NextInIntroduction1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.anim.zoom_enter;
        setContentView(R.layout.activity_introduction1);
        Initialization();
        clickListeners();
    }

    private void clickListeners() {
        NextInIntroduction1.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Introduction2.class));
            finish();
        });
    }

    private void Initialization() {
        NextInIntroduction1 = findViewById(R.id.NextInIntroduction1);
    }
}