package com.scripttube.android.ScriptTube.Settings.About;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.scripttube.android.ScriptTube.ApiManager.ApiManager;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.GetPolicyModel;
import com.scripttube.android.ScriptTube.ApiManager.DataCallback;
import com.scripttube.android.ScriptTube.ApiManager.RetrofitHelper.App;
import com.scripttube.android.ScriptTube.ApiManager.ServerError;
import com.scripttube.android.ScriptTube.R;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Dialogs;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prefs;

public class SingleAbout extends AppCompatActivity {
    TextView HeadingInSingleAbout, DescriptionInSingleAbout;
    Intent i;
    ImageView BackInSingleAbout;
    ApiManager apiManager = App.getApiManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.anim.zoom_enter;
        setContentView(R.layout.activity_single_about);
        Initialization();
        ClickListeners();
        setText();
    }

    private void setText() {
        if (i.getStringExtra("AboutFrom").equalsIgnoreCase("TermsOfServiceInSettings")) {
            HeadingInSingleAbout.setText(R.string.terms_of_service_heading);
            GetPolicy("service");
        } else if (i.getStringExtra("AboutFrom").equalsIgnoreCase("PrivacyPolicyInSettings")) {
            HeadingInSingleAbout.setText(R.string.privacy_policy_heading);
            GetPolicy("privacy");
        } else if (i.getStringExtra("AboutFrom").equalsIgnoreCase("CopyrightPolicyInSettings")) {
            HeadingInSingleAbout.setText(R.string.copyright_policy_heading);
            GetPolicy("copyright");
        } else {
            GetPolicy("");
        }

    }

    private void ClickListeners() {
        BackInSingleAbout.setOnClickListener(view -> finish());
    }

    private void Initialization() {
        i = getIntent();
        BackInSingleAbout = findViewById(R.id.BackInSingleAbout);
        HeadingInSingleAbout = findViewById(R.id.HeadingInSingleAbout);
        DescriptionInSingleAbout = findViewById(R.id.DescriptionInSingleAbout);

    }

    public void GetPolicy(String text) {
        Dialogs.createProgressDialog(SingleAbout.this);
        GetPolicyModel model = new GetPolicyModel(text);
        apiManager.GetPolicy(Prefs.GetBearerToken(SingleAbout.this), model, new DataCallback<GetPolicyModel>() {
            @Override
            public void onSuccess(GetPolicyModel getPolicyModel) {
                Dialogs.HideProgressDialog();

                DescriptionInSingleAbout.setText(Html.fromHtml(getPolicyModel.getData().getDescription()));
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();

            }
        });
    }
}