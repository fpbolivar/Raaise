package com.scripttube.android.ScriptTube.Settings;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;

import com.scripttube.android.ScriptTube.Activity.Credentials.Login;
import com.scripttube.android.ScriptTube.ApiManager.ApiManager;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.LogoutModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.Notification_On_OffModel;
import com.scripttube.android.ScriptTube.ApiManager.DataCallback;
import com.scripttube.android.ScriptTube.ApiManager.RetrofitHelper.App;
import com.scripttube.android.ScriptTube.ApiManager.ServerError;
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
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Dialogs;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prefs;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prompt;

public class SettingsActivity extends AppCompatActivity {
    RelativeLayout PersonalInformationInSettings,
            UserNameInSettings, ShortBioInSettings, ChangePasswordInSettings, PaymentMethodsInSettings, DonationRaisedInSettings,
            WithDrawlsInSettings, BankDetailsInSettings, TermsOfServiceInSettings, PrivacyPolicyInSettings, CopyrightPolicyInSettings,
            DeactivateAccountInSettings, DeleteAccountInSettings, LogoutInSettings;
    Context context;
    ImageView BackArrow;
    SwitchCompat EmailNotificationSwitch, PushNotificationSwitch;
    ApiManager apiManager = App.getApiManager();
    int check = 1;
    boolean EmailBool = false, PushBool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.anim.zoom_enter;
        setContentView(R.layout.activity_settings);
        BackArrow = findViewById(R.id.BackInSignUp);
        BackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
        EmailNotificationSwitch = findViewById(R.id.EmailNotificationSwitch);
        PushNotificationSwitch = findViewById(R.id.PushNotificationSwitch);


        if (Prefs.GetEmailNotification(SettingsActivity.this).equals("1")) {
            EmailNotificationSwitch.setChecked(true);
            EmailBool = true;

        }
        if (Prefs.GetPushNotification(SettingsActivity.this).equals("1")) {
            PushBool = true;
            PushNotificationSwitch.setChecked(true);
        }


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
            showLogoutAccountDialog();
        });
        EmailNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                check = 2;
                if (b) {
                    EmailBool = true;
                    UpdateNotification();
                } else {
                    EmailBool = false;
                    UpdateNotification();
                }

            }
        });
        PushNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                check = 3;
                if (b) {
                    PushBool = true;
                    UpdateNotification();
                } else {
                    PushBool = false;
                    UpdateNotification();
                }
            }
        });
    }

    public void showLogoutAccountDialog() {
        final Dialog dialog = new Dialog(SettingsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.logout_dialog);
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        CardView No = dialog.findViewById(R.id.NoInLogout);
        CardView LogoutYes = dialog.findViewById(R.id.YesInLogout);
        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        LogoutYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                DoLogout();

            }
        });

        dialog.show();

    }

    private void DoLogout() {
        Dialogs.createProgressDialog(SettingsActivity.this);
        apiManager.LogOut(Prefs.GetBearerToken(SettingsActivity.this), new DataCallback<LogoutModel>() {
            @Override
            public void onSuccess(LogoutModel logoutModel) {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(findViewById(android.R.id.content), logoutModel.getMessage());
                Prefs.ClearBearerToken(SettingsActivity.this);
                Prefs.ClearForgetPasswordEmail(SettingsActivity.this);
                Prefs.ClearFORGETPASSWORDTOKEN(SettingsActivity.this);
                Prefs.ClearForgetPasswordVerifyOtp(SettingsActivity.this);
                Intent intent = new Intent(SettingsActivity.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
            }
        });

    }

    void UpdateNotification() {
        Notification_On_OffModel NotificationModel = new Notification_On_OffModel();
        if (check == 1) {

        } else if (check == 2) {


            NotificationModel = new Notification_On_OffModel(EmailBool, PushBool);
        } else if (check == 3) {


            NotificationModel = new Notification_On_OffModel(EmailBool, PushBool);
        }

        apiManager.NotificationOnOff(Prefs.GetBearerToken(SettingsActivity.this), NotificationModel, new DataCallback<Notification_On_OffModel>() {
            @Override
            public void onSuccess(Notification_On_OffModel notification_on_offModel) {

                if (notification_on_offModel.isEmailNotification()) {
                    Prefs.SetEmailNotification(SettingsActivity.this, "1");
                } else {
                    Prefs.SetEmailNotification(SettingsActivity.this, "2");
                }
                if (notification_on_offModel.isPushNotification()) {
                    Prefs.SetPushNotification(SettingsActivity.this, "1");
                } else {
                    Prefs.SetPushNotification(SettingsActivity.this, "2");
                }

            }

            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
            }
        });
    }
}