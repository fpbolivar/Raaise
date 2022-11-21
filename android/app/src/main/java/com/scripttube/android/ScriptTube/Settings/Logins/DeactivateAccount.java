package com.scripttube.android.ScriptTube.Settings.Logins;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.scripttube.android.ScriptTube.R;

public class DeactivateAccount extends AppCompatActivity {
    Intent i;
    RelativeLayout DeactivateAccountLayout, DeleteAccountLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.anim.zoom_enter;
        setContentView(R.layout.activity_deactivate_account);
        Initialization();
        ClickListeners();
        ShowHide();
    }

    private void ShowHide() {
        if (i.getStringExtra("DeactivateDelete").equalsIgnoreCase("DeactivateAccountInSettings")) {
            DeactivateAccountLayout.setVisibility(View.VISIBLE);
            DeleteAccountLayout.setVisibility(View.GONE);

        } else if (i.getStringExtra("DeactivateDelete").equalsIgnoreCase("DeleteAccountInSettings")) {
            DeactivateAccountLayout.setVisibility(View.GONE);
            DeleteAccountLayout.setVisibility(View.VISIBLE);

        }
    }

    private void ClickListeners() {
    }

    private void Initialization() {
        i = getIntent();
        DeactivateAccountLayout = findViewById(R.id.DeactivateAccountLayout);
        DeleteAccountLayout = findViewById(R.id.DeleteAccountLayout);
    }
}