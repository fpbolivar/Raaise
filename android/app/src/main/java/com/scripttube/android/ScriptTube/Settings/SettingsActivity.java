package com.scripttube.android.ScriptTube.Settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.scripttube.android.ScriptTube.Activity.Credentials.Login;
import com.scripttube.android.ScriptTube.R;
import com.scripttube.android.ScriptTube.Settings.About.SingleAbout;
import com.scripttube.android.ScriptTube.Settings.Logins.DeactivateAccount;
import com.scripttube.android.ScriptTube.Settings.MyAccount.ChangePassword;
import com.scripttube.android.ScriptTube.Settings.MyAccount.PersonalInformation;
import com.scripttube.android.ScriptTube.Settings.MyAccount.ShortBio;
import com.scripttube.android.ScriptTube.Settings.MyAccount.UserName;
import com.scripttube.android.ScriptTube.Settings.Payments.BankDetails;
import com.scripttube.android.ScriptTube.Settings.Payments.DonationRaised;
import com.scripttube.android.ScriptTube.Settings.Payments.PaymentMethods;
import com.scripttube.android.ScriptTube.Settings.Payments.WithDrawls;

public class SettingsActivity extends AppCompatActivity {
    RelativeLayout PersonalInformationInSettings,
            UserNameInSettings, ShortBioInSettings, ChangePasswordInSettings, PaymentMethodsInSettings, DonationRaisedInSettings,
            WithDrawlsInSettings, BankDetailsInSettings, TermsOfServiceInSettings, PrivacyPolicyInSettings, CopyrightPolicyInSettings,
            DeactivateAccountInSettings, DeleteAccountInSettings, LogoutInSettings;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.anim.zoom_enter;
        setContentView(R.layout.activity_settings);
        Initialization();
        ClickListeners();
    }

    private void Initialization() {
        context = getApplicationContext();
        PersonalInformationInSettings = findViewById(R.id.PersonalInformationInSettings);
        UserNameInSettings = findViewById(R.id.UserNameInSettings);
        ShortBioInSettings = findViewById(R.id.ShortBioInSettings);
        ChangePasswordInSettings = findViewById(R.id.ChangePasswordInSettings);
        PaymentMethodsInSettings = findViewById(R.id.PaymentMethodsInSettings);
        DonationRaisedInSettings = findViewById(R.id.DonationRaisedInSettings);
        WithDrawlsInSettings = findViewById(R.id.WithDrawlsInSettings);
        BankDetailsInSettings = findViewById(R.id.BankDetailsInSettings);
        TermsOfServiceInSettings = findViewById(R.id.TermsOfServiceInSettings);
        PrivacyPolicyInSettings = findViewById(R.id.PrivacyPolicyInSettings);
        CopyrightPolicyInSettings = findViewById(R.id.CopyrightPolicyInSettings);
        DeactivateAccountInSettings = findViewById(R.id.DeactivateAccountInSettings);
        DeleteAccountInSettings = findViewById(R.id.DeleteAccountInSettings);
        LogoutInSettings = findViewById(R.id.LogoutInSettings);
    }

    private void ClickListeners() {
        PersonalInformationInSettings.setOnClickListener(view -> startActivity(new Intent(context, PersonalInformation.class)));
        UserNameInSettings.setOnClickListener(view -> startActivity(new Intent(context, UserName.class)));
        ShortBioInSettings.setOnClickListener(view -> startActivity(new Intent(context, ShortBio.class)));
        ChangePasswordInSettings.setOnClickListener(view -> startActivity(new Intent(context, ChangePassword.class)));
        PaymentMethodsInSettings.setOnClickListener(view -> startActivity(new Intent(context, PaymentMethods.class)));
        DonationRaisedInSettings.setOnClickListener(view -> startActivity(new Intent(context, DonationRaised.class)));
        WithDrawlsInSettings.setOnClickListener(view -> startActivity(new Intent(context, WithDrawls.class)));
        BankDetailsInSettings.setOnClickListener(view -> startActivity(new Intent(context, BankDetails.class)));
        TermsOfServiceInSettings.setOnClickListener(view -> startActivity(new Intent(context, SingleAbout.class).putExtra("AboutFrom", "TermsOfServiceInSettings")));
        PrivacyPolicyInSettings.setOnClickListener(view -> startActivity(new Intent(context, SingleAbout.class).putExtra("AboutFrom", "PrivacyPolicyInSettings")));
        CopyrightPolicyInSettings.setOnClickListener(view -> startActivity(new Intent(context, SingleAbout.class).putExtra("AboutFrom", "CopyrightPolicyInSettings")));
        DeactivateAccountInSettings.setOnClickListener(view -> startActivity(new Intent(context, DeactivateAccount.class).putExtra("DeactivateDelete", "DeactivateAccountInSettings")));
        DeleteAccountInSettings.setOnClickListener(view -> startActivity(new Intent(context, DeactivateAccount.class).putExtra("DeactivateDelete", "DeleteAccountInSettings")));
        LogoutInSettings.setOnClickListener(view -> {
            startActivity(new Intent(context, Login.class));
            finish();
        });
    }
}