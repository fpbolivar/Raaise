package com.raaise.android.Settings.MyAccount;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.UpdateUserProfileModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;

public class ShortBio extends AppCompatActivity {
    public static String countVal, bio;
    public static int count_length;
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
        bio = ShortBioEditText.getText().toString().trim();
        count_length = bio.length();


        if (!bio.isEmpty() && count_length != 0) {
            countLimit.setText(count_length + "/45");
            if (count_length == 45){
                countLimit.setTextColor(ContextCompat.getColor(ShortBio.this, R.color.ButtonRed));
            } else {
                countLimit.setTextColor(ContextCompat.getColor(ShortBio.this, R.color.white));
            }
        } else {
            countLimit.setText("0" + "/45");
            countLimit.setTextColor(ContextCompat.getColor(ShortBio.this, R.color.white));
        }

        ShortBioEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                countLimit.setText(charSequence.toString().length() + "/45");
                countVal = countLimit.toString();
                if (charSequence.length() == 45) {
                    countLimit.setTextColor(ContextCompat.getColor(ShortBio.this, R.color.ButtonRed));
                } else
                    countLimit.setTextColor(ContextCompat.getColor(ShortBio.this, R.color.white));
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
        ShortBioEditText.setText(Prefs.GetUserShortBio(com.raaise.android.Settings.MyAccount.ShortBio.this));
    }

    private void UpdateUserData(String ShortBio) {
        UpdateUserProfileModel update = new UpdateUserProfileModel(ShortBio);
        Dialogs.createProgressDialog(ShortBio.this);
        apiManager.UpdateProfile(Prefs.GetBearerToken(findViewById(android.R.id.content).getContext()), update, new DataCallback<UpdateUserProfileModel>() {
            @Override
            public void onSuccess(UpdateUserProfileModel updateUserProfileModel) {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(findViewById(android.R.id.content), "Short Bio Updated Successfully");
                ShortBioEditText.setText(updateUserProfileModel.getData().getShortBio());
                Prefs.SetUserShortBio(com.raaise.android.Settings.MyAccount.ShortBio.this, updateUserProfileModel.getData().getShortBio());
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