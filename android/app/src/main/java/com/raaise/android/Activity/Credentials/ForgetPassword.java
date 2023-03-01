package com.raaise.android.Activity.credentials;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.raaise.android.Activity.credentials.ForgetPasswordFragments.ForgetPasswordFragment;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.FragmentChangerForgetPassword;

public class ForgetPassword extends AppCompatActivity implements FragmentChangerForgetPassword {

    ImageView BackArrow;
    public static String otpMessage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.anim.zoom_enter;
        setContentView(R.layout.activity_forget_password);

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