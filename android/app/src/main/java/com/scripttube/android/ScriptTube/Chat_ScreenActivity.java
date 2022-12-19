package com.scripttube.android.ScriptTube;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Chat_ScreenActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView backArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        inItWidgets();
        backArrow.setOnClickListener(this);

    }

    private void inItWidgets() {
        backArrow = findViewById(R.id.BackInSignUp);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.BackInSignUp:
                onBackPressed();
                break;
        }
    }
}