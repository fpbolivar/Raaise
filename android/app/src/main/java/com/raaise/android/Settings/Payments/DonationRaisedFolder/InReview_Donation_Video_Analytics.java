package com.raaise.android.Settings.Payments.DonationRaisedFolder;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.raaise.android.R;


public class InReview_Donation_Video_Analytics extends AppCompatActivity {
    ImageView setting_in_review, BackInInReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_review_donation_video_analytics);
        Initialization();
        ClickListeners();
    }

    private void Initialization() {
        setting_in_review = findViewById(R.id.setting_in_review);
        BackInInReview = findViewById(R.id.BackInInReview);
    }

    private void ClickListeners() {
        setting_in_review.setOnClickListener(view -> {
            ShowSearchUserDialog();
        });
        BackInInReview.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void ShowSearchUserDialog() {
        Dialog dialog = new Dialog(InReview_Donation_Video_Analytics.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.date_filter_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCancelable(true);
    }
}