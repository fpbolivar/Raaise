package com.scripttube.android.ScriptTube.Settings.MyAccount;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.scripttube.android.ScriptTube.Activity.Credentials.Login;
import com.scripttube.android.ScriptTube.Activity.Credentials.SignUp;
import com.scripttube.android.ScriptTube.ApiManager.ApiManager;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.ChangePasswordModel;
import com.scripttube.android.ScriptTube.ApiManager.DataCallback;
import com.scripttube.android.ScriptTube.ApiManager.RetrofitHelper.App;
import com.scripttube.android.ScriptTube.ApiManager.ServerError;
import com.scripttube.android.ScriptTube.R;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Dialogs;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.HelperClass;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prefs;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prompt;

public class ChangePassword extends AppCompatActivity implements View.OnClickListener {

    ImageView backArrow;
    EditText OldPasswordEditTextInChangePassword, NewPasswordEditTextInChangePassword, ConfirmPasswordEditTextInChangePassword;
    LinearLayout UpdateButtonInChangePassword;
    ApiManager apiManager = App.getApiManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.anim.zoom_enter;
        setContentView(R.layout.activity_change_password);
        inItWidgets();

        backArrow.setOnClickListener(this);
        Clicklisteners();
    }

    private void inItWidgets(){
        backArrow = findViewById(R.id.back_btn);
        OldPasswordEditTextInChangePassword = findViewById(R.id.OldPasswordEditTextInChangePassword);
        NewPasswordEditTextInChangePassword = findViewById(R.id.NewPasswordEditTextInChangePassword);
        ConfirmPasswordEditTextInChangePassword = findViewById(R.id.ConfirmPasswordEditTextInChangePassword);
        UpdateButtonInChangePassword = findViewById(R.id.UpdateButtonInChangePassword);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_btn:
                onBackPressed();
                break;
        }
       
    }

    private void Clicklisteners() {
        UpdateButtonInChangePassword.setOnClickListener(view -> Validate());
    }

    private void Validate() {
        String oldPass, newPass, confirmPass;
        oldPass = OldPasswordEditTextInChangePassword.getText().toString().trim();
        newPass = NewPasswordEditTextInChangePassword.getText().toString().trim();
        confirmPass = ConfirmPasswordEditTextInChangePassword.getText().toString().trim();

        if (oldPass.isEmpty()) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Please Enter Old Password");
        } else if (newPass.isEmpty()) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Please Enter New Password");
        } else if (confirmPass.isEmpty()) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Please Enter Confirm Password");
        }
        else if (!HelperClass.passwordCharValidation(newPass)) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Password Must Contain 1 Uppercase,1 Lowercase,1 Special character with length 8");
        }
        else if(!confirmPass.matches(newPass)) {
            Prompt.SnackBar(findViewById(android.R.id.content), "New password and confirm password must match");
        }
        else {
            DoChangePassword(oldPass, newPass, confirmPass);
        }


    }

    private void DoChangePassword(String OldPass, String NewPass, String ConfirmPass) {
        Dialogs.createProgressDialog(ChangePassword.this);
        ChangePasswordModel changePassword = new ChangePasswordModel(OldPass, NewPass, ConfirmPass);
        apiManager.ChangePassword(Prefs.GetBearerToken(getApplicationContext()),
                changePassword, new DataCallback<ChangePasswordModel>() {
                    @Override
                    public void onSuccess(ChangePasswordModel changePasswordModel) {
                        Dialogs.HideProgressDialog();
                            OldPasswordEditTextInChangePassword.setText("");
                            NewPasswordEditTextInChangePassword.setText("");
                            ConfirmPasswordEditTextInChangePassword.setText("");
                            Prompt.SnackBar(findViewById(android.R.id.content), "Password Changed Successfully");

                    }

                    @Override
                    public void onError(ServerError serverError) {

                        Dialogs.HideProgressDialog();
                        Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
                    }
                });
    }
}