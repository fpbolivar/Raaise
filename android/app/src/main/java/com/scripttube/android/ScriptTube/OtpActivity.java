package com.scripttube.android.ScriptTube;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class OtpActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView backBtn;
    EditText firstOtpET,secondOtpET,thirdOtpET,fourthOtpET;
    LinearLayout submitBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        inItWidgets();
        backBtn.setOnClickListener(this);
        changeOtpEdittextFocus();

    }

    private void changeOtpEdittextFocus() {
        firstOtpET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==1){
                    secondOtpET.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        secondOtpET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==1){
                    thirdOtpET.requestFocus();
                }else if(s.length()==0){
                    firstOtpET.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        thirdOtpET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==1){
                    fourthOtpET.requestFocus();
                }else if(s.length()==0){
                    secondOtpET.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        fourthOtpET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==1){
                    submitBtn.requestFocus();
                }else if(s.length()==0){
                    thirdOtpET.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void inItWidgets() {
        firstOtpET = findViewById(R.id.first_otpNo);
        secondOtpET = findViewById(R.id.second_otpNo);
        thirdOtpET = findViewById(R.id.third_otpNo);
        fourthOtpET = findViewById(R.id.fourth_otpNo);
        backBtn = findViewById(R.id.BackInSignUp);
        submitBtn = findViewById(R.id.submit_Button);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.BackInSignUp:
            onBackPressed();
            break;
        }
    }



}