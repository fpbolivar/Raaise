package com.raaise.android.Settings.Logins;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.raaise.android.Activity.Credentials.Login;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.DeactivateAccountModel;
import com.raaise.android.ApiManager.ApiModels.DeleteAccountModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;

public class DeactivateAccount extends AppCompatActivity {
    Intent i;
    RelativeLayout DeactivateAccountLayout, DeleteAccountLayout;
    ImageView backBtn;
    ImageView BackArrow, ImageInDeactivateAccount, ImageInDeleteAccount;
    LinearLayout DeactivateAccountButton, DeleteAccountButton;
    ApiManager apiManager = App.getApiManager();
    TextView NameInDeactivate, NameInDeactivateDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.anim.zoom_enter;
        setContentView(R.layout.activity_deactivate_account);
        BackArrow = findViewById(R.id.BackInSignUp);
        BackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Initialization();
        ClickListeners();
        ShowHide();
        GetUserProfile();
        backBtn.setOnClickListener(v -> onBackPressed());
    }

    private void ShowHide() {
        if (i.getStringExtra("DeactivateDelete").equalsIgnoreCase("DeactivateAccountInSettings")) {
            DeactivateAccountLayout.setVisibility(View.VISIBLE);
            DeleteAccountLayout.setVisibility(View.GONE);

        } else if (i.getStringExtra("DeactivateDelete").equalsIgnoreCase("DeleteAccountInSettings")) {
            DeactivateAccountLayout.setVisibility(View.GONE);
            DeleteAccountLayout.setVisibility(View.VISIBLE);

        }
    }

    void GetUserProfile() {
        NameInDeactivate.setText(Prefs.getUserName(getApplicationContext()));
        Glide.with(DeactivateAccount.this)
                .load(Prefs.GetBaseUrl(getApplicationContext()) + Prefs.getUserImage(getApplicationContext()))
                .circleCrop()
                .placeholder(R.drawable.placeholder)
                .into(ImageInDeactivateAccount);
        NameInDeactivateDelete.setText(Prefs.getUserName(getApplicationContext()));
        Glide.with(DeactivateAccount.this)
                .load(Prefs.GetBaseUrl(getApplicationContext()) + Prefs.getUserImage(getApplicationContext()))
                .circleCrop()
                .placeholder(R.drawable.placeholder)
                .into(ImageInDeleteAccount);
    }

    private void ClickListeners() {
        DeactivateAccountButton.setOnClickListener(view -> showDeactivateAccountDialog());
        DeleteAccountButton.setOnClickListener(view -> showDeleteAccountDialog());
    }

    public void showDeleteAccountDialog() {
        final Dialog dialog = new Dialog(DeactivateAccount.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.delete_account_dialog);
        TextView No = dialog.findViewById(R.id.NoInDeleteAccount);
        TextView LogoutYes = dialog.findViewById(R.id.YesInDeleteAccount);
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
                DeleteAccountApi();

            }
        });

        dialog.show();

    }

    public void showDeactivateAccountDialog() {
        final Dialog dialog = new Dialog(DeactivateAccount.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.setContentView(R.layout.deactivate_account_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView No = dialog.findViewById(R.id.NoInDeactivateAccount);
        TextView LogoutYes = dialog.findViewById(R.id.YesInDeactivateAccount);
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
                DeactivateAccountApi();

            }
        });

        dialog.show();

    }

    private void Initialization() {
        i = getIntent();
        backBtn = findViewById(R.id.BackInDelete);
        ImageInDeactivateAccount = findViewById(R.id.ImageInDeactivateAccount);
        ImageInDeleteAccount = findViewById(R.id.ImageInDeleteAccount);
        NameInDeactivate = findViewById(R.id.NameInDeactivate);
        NameInDeactivateDelete = findViewById(R.id.NameInDeactivateDelete);
        DeactivateAccountLayout = findViewById(R.id.DeactivateAccountLayout);
        DeactivateAccountButton = findViewById(R.id.DeactivateAccountButton);
        DeleteAccountButton = findViewById(R.id.DeleteAccountButton);
        DeleteAccountLayout = findViewById(R.id.DeleteAccountLayout);

    }


    private void DeleteAccountApi() {
        Dialogs.createProgressDialog(DeactivateAccount.this);
        apiManager.DeleteAccount(Prefs.GetBearerToken(DeactivateAccount.this), new DataCallback<DeleteAccountModel>() {
            @Override
            public void onSuccess(DeleteAccountModel deleteAccountModel) {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(findViewById(android.R.id.content), deleteAccountModel.getMessage());
                Toast.makeText(DeactivateAccount.this, deleteAccountModel.getMessage(), Toast.LENGTH_SHORT).show();
                Prefs.ClearBearerToken(DeactivateAccount.this);
                Prefs.ClearBaseUrl(DeactivateAccount.this);
                Prefs.ClearForgetPasswordEmail(DeactivateAccount.this);
                Prefs.ClearFORGETPASSWORDTOKEN(DeactivateAccount.this);
                Prefs.ClearForgetPasswordVerifyOtp(DeactivateAccount.this);
                Prefs.clearUserDetails(getApplicationContext());
                startActivity(new Intent(DeactivateAccount.this, Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
            }
        });
    }

    private void DeactivateAccountApi() {
        Dialogs.createProgressDialog(DeactivateAccount.this);
        apiManager.DeactivateAccount(Prefs.GetBearerToken(DeactivateAccount.this), new DataCallback<DeactivateAccountModel>() {
            @Override
            public void onSuccess(DeactivateAccountModel deactivateAccountModel) {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(findViewById(android.R.id.content), deactivateAccountModel.getMessage());
                Toast.makeText(DeactivateAccount.this, deactivateAccountModel.getMessage(), Toast.LENGTH_SHORT).show();
                Prefs.ClearBearerToken(DeactivateAccount.this);
                Prefs.ClearBaseUrl(DeactivateAccount.this);
                Prefs.ClearForgetPasswordEmail(DeactivateAccount.this);
                Prefs.ClearFORGETPASSWORDTOKEN(DeactivateAccount.this);
                Prefs.ClearForgetPasswordVerifyOtp(DeactivateAccount.this);
                Prefs.clearUserDetails(getApplicationContext());
                startActivity(new Intent(DeactivateAccount.this, Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());

            }
        });
    }
}