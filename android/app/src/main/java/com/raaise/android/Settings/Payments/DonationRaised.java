package com.raaise.android.Settings.Payments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.raaise.android.R;

public class DonationRaised extends AppCompatActivity {

    ImageView iv_Back_Btn;
    TextView tv_Withdraw;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.anim.zoom_enter;
        setContentView(R.layout.activity_donation_raised);
        iv_Back_Btn = findViewById(R.id.BackInSignUp);
        iv_Back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        tv_Withdraw = findViewById(R.id.tv_withdraw);
        tv_Withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize Dialog Confirm Donation
                Dialog dialog = new Dialog(DonationRaised.this);

                //Set content view
                dialog.setContentView(R.layout.layout_withdraw_selectamount_dialog);
                //Set outside touch
                dialog.setCanceledOnTouchOutside(false);
                //Set height and width
                dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT);
                //Set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                //Set animation
                dialog.getWindow().getAttributes().windowAnimations =
                        android.R.style.Animation_Dialog;
                dialog.show();
                LinearLayout Submit_btn = dialog.findViewById(R.id.dialog_btn_Submit);
                Submit_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        startActivity(new Intent(DonationRaised.this, WithDrawls.class));
                        finish();
                    }
                });
            }
        });
    }
}