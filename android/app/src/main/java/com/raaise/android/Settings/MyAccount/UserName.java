package com.raaise.android.Settings.MyAccount;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.UpdateUserProfileModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;

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
        try {
            UsernameEditTextInChangeUserName.setText(Prefs.getUserName(com.raaise.android.Settings.MyAccount.UserName.this));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void ChangeUserName(String Username) {
        UpdateUserProfileModel model = new UpdateUserProfileModel();
        model.setUserName(Username);
        apiManager.UpdateProfile(Prefs.GetBearerToken(com.raaise.android.Settings.MyAccount.UserName.this), model, new DataCallback<UpdateUserProfileModel>() {
            @Override
            public void onSuccess(UpdateUserProfileModel updateUserProfileModel) {
                if (updateUserProfileModel.getStatusCode() == 200) {
                    Prompt.SnackBar(findViewById(android.R.id.content), "Username updated Successfully");
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