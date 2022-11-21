package com.scripttube.android.ScriptTube.Settings.MyAccount;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.scripttube.android.ScriptTube.R;

public class PersonalInformation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.anim.zoom_enter;
        setContentView(R.layout.activity_personal_information);
    }
}