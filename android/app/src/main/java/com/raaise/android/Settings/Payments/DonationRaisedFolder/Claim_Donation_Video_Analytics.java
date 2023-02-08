package com.raaise.android.Settings.Payments.DonationRaisedFolder;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.raaise.android.R;

public class Claim_Donation_Video_Analytics extends AppCompatActivity {
    ImageView BackInClaim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_donation_video_analytics);
        Initialization();
        ClickListeners();
    }
    private void Initialization() {
        BackInClaim = findViewById(R.id.BackInClaim);
    }

    private void ClickListeners() {
        BackInClaim.setOnClickListener(view -> {
            onBackPressed();
        });
    }
}