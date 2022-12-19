package com.scripttube.android.ScriptTube.Activity.Credentials;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.scripttube.android.ScriptTube.Activity.Credentials.ForgetPasswordFragments.ForgetPasswordFragment;
import com.scripttube.android.ScriptTube.R;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.FragmentChangerForgetPassword;

public class ForgetPassword extends AppCompatActivity implements FragmentChangerForgetPassword {

    ImageView BackArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.anim.zoom_enter;
        setContentView(R.layout.activity_forget_password);
//        BackArrow = findViewById(R.id.BackInSignUp);
//        BackArrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
        SelectForgetPassword();
    }

    private void SelectForgetPassword() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.ForgetPasswordContainer, new ForgetPasswordFragment())
                .commit();
    }

    @Override
    public void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.ForgetPasswordContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void FinishChangePasswordActivity() {
        finish();
    }

}