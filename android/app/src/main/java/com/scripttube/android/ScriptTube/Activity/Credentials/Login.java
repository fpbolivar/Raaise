package com.scripttube.android.ScriptTube.Activity.Credentials;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.scripttube.android.ScriptTube.Home.MainHome.Home;
import com.scripttube.android.ScriptTube.R;
import com.scripttube.android.ScriptTube.Settings.SettingsActivity;

public class Login extends AppCompatActivity {
    TextView DoSignUpButton,ForgetPassword;
    LinearLayout LoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.anim.zoom_enter;
        setContentView(R.layout.activity_login);
        Initialization();
        clickListeners();
    }

    private void clickListeners() {
        DoSignUpButton.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), SignUp.class));
            finish();
        });
        ForgetPassword.setOnClickListener(view ->
        {
            startActivity(new Intent(getApplicationContext(), ForgetPassword.class));
        });
        LoginButton.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();
        });
    }

    private void Initialization() {
        DoSignUpButton = findViewById(R.id.DoSignUpButton);
        ForgetPassword = findViewById(R.id.ForgetPassword);
        LoginButton = findViewById(R.id.LoginButton);
    }
}