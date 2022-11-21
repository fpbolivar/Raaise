package com.scripttube.android.ScriptTube.Settings.About;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.scripttube.android.ScriptTube.R;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.HelperClass;

public class SingleAbout extends AppCompatActivity {
    TextView HeadingInSingleAbout, DescriptionInSingleAbout;
    Intent i;
    ImageView BackInSingleAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.anim.zoom_enter;
        setContentView(R.layout.activity_single_about);
        Initialization();
        ClickListeners();
        setText();
    }

    private void setText() {
        if (i.getStringExtra("AboutFrom").equalsIgnoreCase("TermsOfServiceInSettings")) {
            HeadingInSingleAbout.setText(HelperClass.getTermsOfServiceHeading());
            DescriptionInSingleAbout.setText(HelperClass.getTermsOfServiceDescription());
        } else if (i.getStringExtra("AboutFrom").equalsIgnoreCase("PrivacyPolicyInSettings")) {
            HeadingInSingleAbout.setText(HelperClass.getPrivacyPolicyHeading());
            DescriptionInSingleAbout.setText(HelperClass.getPrivacyPolicyDescription());
        } else if (i.getStringExtra("AboutFrom").equalsIgnoreCase("CopyrightPolicyInSettings")) {
            HeadingInSingleAbout.setText(HelperClass.getCopyrightPolicyHeading());
            DescriptionInSingleAbout.setText(HelperClass.getCopyrightPolicyDescription());
        } else {
            HeadingInSingleAbout.setText("");
            DescriptionInSingleAbout.setText("");
        }

    }

    private void ClickListeners() {
        BackInSingleAbout.setOnClickListener(view -> finish());
    }

    private void Initialization() {
        i = getIntent();
        BackInSingleAbout = findViewById(R.id.BackInSingleAbout);
        HeadingInSingleAbout = findViewById(R.id.HeadingInSingleAbout);
        DescriptionInSingleAbout = findViewById(R.id.DescriptionInSingleAbout);

    }
}