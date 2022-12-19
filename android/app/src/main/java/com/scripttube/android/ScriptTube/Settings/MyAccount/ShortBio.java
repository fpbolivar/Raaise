package com.scripttube.android.ScriptTube.Settings.MyAccount;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.scripttube.android.ScriptTube.ApiManager.ApiManager;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.GetUserProfile;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.UpdateUserProfileModel;
import com.scripttube.android.ScriptTube.ApiManager.DataCallback;
import com.scripttube.android.ScriptTube.ApiManager.RetrofitHelper.App;
import com.scripttube.android.ScriptTube.ApiManager.ServerError;
import com.scripttube.android.ScriptTube.R;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Dialogs;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prefs;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prompt;

public class ShortBio extends AppCompatActivity {
    EditText ShortBioEditText;
    LinearLayout ShortBioSaveButton;
    String ShortBio;
    TextView countLimit;
    ApiManager apiManager = App.getApiManager();
    TextView count_msg;
    ImageView BackArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.anim.zoom_enter;
        setContentView(R.layout.activity_short_bio);
        BackArrow = findViewById(R.id.BackInSignUp);
        BackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Initialization();
        Clicklisteners();
        GetUserProfile();
        ShortBioEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                countLimit.setText(charSequence.length() + "/45");
                if (charSequence.length() == 45){
                    countLimit.setTextColor(ContextCompat.getColor(ShortBio.this, R.color.ButtonRed));
                } else countLimit.setTextColor(ContextCompat.getColor(ShortBio.this, R.color.white));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void Clicklisteners() {
        ShortBioSaveButton.setOnClickListener(view -> Validate());
    }

    private void Validate() {
        ShortBio = ShortBioEditText.getText().toString().trim();
        if (ShortBio.isEmpty()) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Short Bio can't be empty");
        } else {
            UpdateUserData(ShortBio);
        }
    }
    void GetUserProfile() {
//        final ProgressDialog dialog = Dialogs.createProgressDialog(v.getContext());
//        dialog.show();
        Dialogs.createProgressDialog(findViewById(android.R.id.content).getContext());
        apiManager.GetUserProfile(Prefs.GetBearerToken(getApplicationContext()), new DataCallback<GetUserProfile>() {
            @Override
            public void onSuccess(GetUserProfile getUserProfile) {
                Dialogs.HideProgressDialog();
                ShortBioEditText.setText(getUserProfile.getData().getShortBio());

            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
            }
        });
    }

    private void UpdateUserData(String ShortBio) {
        UpdateUserProfileModel update = new UpdateUserProfileModel(ShortBio);
//        final ProgressDialog dialog =  Dialogs.createProgressDialog(ShortBio.this);
//        dialog.show();
        Dialogs.createProgressDialog(ShortBio.this);
        apiManager.UpdateProfile(Prefs.GetBearerToken(findViewById(android.R.id.content).getContext()), update, new DataCallback<UpdateUserProfileModel>() {
            @Override
            public void onSuccess(UpdateUserProfileModel updateUserProfileModel) {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(findViewById(android.R.id.content), "Short Bio Updated Successfully");
                ShortBioEditText.setText("");
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
            }
        });
    }

    private void Initialization() {
        countLimit = findViewById(R.id.count_msg1);
        ShortBioEditText = findViewById(R.id.ShortBioEditText);
        ShortBioSaveButton = findViewById(R.id.ShortBioSaveButton);
    }
}