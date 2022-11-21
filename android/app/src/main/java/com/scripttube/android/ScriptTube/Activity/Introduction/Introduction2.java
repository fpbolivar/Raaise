package com.scripttube.android.ScriptTube.Activity.Introduction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.scripttube.android.ScriptTube.Activity.Credentials.Login;
import com.scripttube.android.ScriptTube.R;

public class Introduction2 extends AppCompatActivity {
    ImageView NextInIntroduction2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.anim.zoom_enter;
        setContentView(R.layout.activity_introduction2);
        Initialization();
        clickListeners();
    }

    private void clickListeners() {
        NextInIntroduction2.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        });
    }

    private void Initialization() {
        NextInIntroduction2 = findViewById(R.id.NextInIntroduction2);
    }
}