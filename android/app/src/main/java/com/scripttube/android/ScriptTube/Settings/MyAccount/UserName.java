package com.scripttube.android.ScriptTube.Settings.MyAccount;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.scripttube.android.ScriptTube.ApiManager.ApiManager;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.GetUserProfile;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.UpdateUserProfileModel;
import com.scripttube.android.ScriptTube.ApiManager.DataCallback;
import com.scripttube.android.ScriptTube.ApiManager.RetrofitHelper.App;
import com.scripttube.android.ScriptTube.ApiManager.ServerError;
import com.scripttube.android.ScriptTube.R;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prefs;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prompt;

public class UserName extends AppCompatActivity {

    ImageView BackArrow;
    EditText UsernameEditTextInChangeUserName;
    LinearLayout UpdateUserNameButton;
    String UserName;
    ApiManager apiManager = App.getApiManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.anim.zoom_enter;
        setContentView(R.layout.activity_user_name);
        initialization();
        ClickListeners();
        GetUserProfile();

    }

    private void ClickListeners() {
        BackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        UpdateUserNameButton.setOnClickListener(view -> Validate());
    }

    private void Validate() {
        UserName = UsernameEditTextInChangeUserName.getText().toString().trim();
        if (UserName.isEmpty()) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Please Enter Username");
        } else {
            ChangeUserName(UserName);
        }
    }

    void GetUserProfile() {
        apiManager.GetUserProfile(Prefs.GetBearerToken(com.scripttube.android.ScriptTube.Settings.MyAccount.UserName.this), new DataCallback<GetUserProfile>() {
            @Override
            public void onSuccess(GetUserProfile getUserProfile) {
                try {
                    UsernameEditTextInChangeUserName.setText(getUserProfile.getData().getUserName());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
            }
        });
    }

    private void ChangeUserName(String Username) {
        UpdateUserProfileModel model = new UpdateUserProfileModel();
        model.setUserName(Username);
        apiManager.UpdateProfile(Prefs.GetBearerToken(com.scripttube.android.ScriptTube.Settings.MyAccount.UserName.this), model, new DataCallback<UpdateUserProfileModel>() {
            @Override
            public void onSuccess(UpdateUserProfileModel updateUserProfileModel) {
                if (updateUserProfileModel.getStatus() == 200) {
                    Prompt.SnackBar(findViewById(android.R.id.content), "Username updated successfully");
                }

            }

            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
            }
        });
    }

    private void initialization() {
        BackArrow = findViewById(R.id.BackInSignUp);
        UsernameEditTextInChangeUserName = findViewById(R.id.UsernameEditTextInChangeUserName);
        UpdateUserNameButton = findViewById(R.id.UpdateUserNameButton);
    }
}