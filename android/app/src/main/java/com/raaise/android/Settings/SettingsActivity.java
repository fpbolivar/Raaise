package com.raaise.android.Settings;

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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.raaise.android.Activity.credentials.Login;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.LogoutModel;
import com.raaise.android.ApiManager.ApiModels.Notification_On_OffModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.R;
import com.raaise.android.Settings.About.SingleAbout;
import com.raaise.android.Settings.Logins.DeactivateAccount;
import com.raaise.android.Settings.MyAccount.ChangePassword;
import com.raaise.android.Settings.MyAccount.PersonalInformation;
import com.raaise.android.Settings.MyAccount.ShortBio;
import com.raaise.android.Settings.MyAccount.UserName;
import com.raaise.android.Settings.Payments.BankDetails;
import com.raaise.android.Settings.Payments.DonationRaisedFolder.Donation_Raised;
import com.raaise.android.Settings.Payments.PaymentMethods;
import com.raaise.android.Settings.Payments.WithDrawls;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;

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
    String AccHolderName = "AccHolderName";
    String AccAccNumber = "AccNumber";
    String AccRoutingNumber = "RoutingNumber";
    String AccCity = "City";
    String AccState = "State";
    String AccAddress = "Address";
    String AccPostalCode = "PostalCode";
    String AccPhoneNumber = "PhoneNumber";

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
        DonationRaisedInSettings.setOnClickListener(view -> startActivity(new Intent(context, Donation_Raised.class)));
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
        TextView No = dialog.findViewById(R.id.NoInLogout);
        TextView LogoutYes = dialog.findViewById(R.id.YesInLogout);
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
                Prefs.ClearBaseUrl(SettingsActivity.this);
                Prefs.ClearForgetPasswordEmail(SettingsActivity.this);
                Prefs.ClearFORGETPASSWORDTOKEN(SettingsActivity.this);
                Prefs.ClearUserID(SettingsActivity.this);
                Prefs.ClearUserShortBio(SettingsActivity.this);
                Prefs.ClearPushNotification(SettingsActivity.this);
                Prefs.ClearEmailNotification(SettingsActivity.this);
                Prefs.ClearPhoneNumberOfTheUser(SettingsActivity.this);
                Prefs.ClearUserEmail(SettingsActivity.this);
                Prefs.ClearForgetPasswordVerifyOtp(SettingsActivity.this);
                Prefs.ClearEmailNotification(SettingsActivity.this);
                Prefs.ClearBankDetails(SettingsActivity.this, AccHolderName);
                Prefs.ClearBankDetails(SettingsActivity.this, AccAccNumber);
                Prefs.ClearBankDetails(SettingsActivity.this, AccRoutingNumber);
                Prefs.ClearBankDetails(SettingsActivity.this, AccCity);
                Prefs.ClearBankDetails(SettingsActivity.this, AccState);
                Prefs.ClearBankDetails(SettingsActivity.this, AccAddress);
                Prefs.ClearBankDetails(SettingsActivity.this, AccPostalCode);
                Prefs.ClearBankDetails(SettingsActivity.this, AccPhoneNumber);

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