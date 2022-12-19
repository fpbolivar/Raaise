package com.scripttube.android.ScriptTube.Activity.Credentials;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.scripttube.android.ScriptTube.ApiManager.ApiManager;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.FacebookLoginModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.GoogleLoginModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.SignUpModel;
import com.scripttube.android.ScriptTube.ApiManager.DataCallback;
import com.scripttube.android.ScriptTube.ApiManager.RetrofitHelper.App;
import com.scripttube.android.ScriptTube.ApiManager.ServerError;
import com.scripttube.android.ScriptTube.Home.MainHome.Home;
import com.scripttube.android.ScriptTube.R;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Dialogs;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.HelperClass;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prefs;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prompt;

import java.util.Arrays;

public class SignUp extends AppCompatActivity {
    ApiManager apiManager = App.getApiManager();
    EditText NameEditTextInSignUp,
            UserNameEditTextInSignUp,
            EmailEditTextInSignUp,
            PhoneNumberEditTextInSignUp,
            PasswordEditTextInSignUp, ConfirmPasswordEditTextInSignUp;
    LinearLayout SignUpButton, GoogleButtonInSignUp, FacebookButtonInSignUp;
    TextView SignInInSignupButton;
    String Name, UserName, Email, PhoneNumber, Password, ConfirmPassword;
    ImageView BackArrow;
    GoogleSignInOptions Gso;
    GoogleSignInClient Gsc;
    CallbackManager callbackManager;
    ApiManager apiManagerApi = App.getApiManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.anim.zoom_enter;
        setContentView(R.layout.activity_sign_up);
        BackArrow = findViewById(R.id.BackInSignUp);
        BackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Initialization();
        clickListener();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                LoginWithFacebook(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(@NonNull FacebookException e) {
                Prompt.SnackBar(findViewById(android.R.id.content), e.getMessage());
            }
        });
    }

    private void LoginWithFacebook(String token) {
        FacebookLoginModel fbModel = new FacebookLoginModel(token, "android");
        Dialogs.createProgressDialog(SignUp.this);
        apiManagerApi.FacebookLogin(fbModel, new DataCallback<FacebookLoginModel>() {
            @Override
            public void onSuccess(FacebookLoginModel facebookLoginModel) {
                Dialogs.HideProgressDialog();
                if (!facebookLoginModel.getToken().isEmpty()) {
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    Prefs.SetBearerToken(SignUp.this, facebookLoginModel.getToken());
                    finish();
                } else {
                    Prompt.SnackBar(findViewById(android.R.id.content), facebookLoginModel.getMessage());
                }
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
            }
        });
    }

    private void clickListener() {
        SignUpButton.setOnClickListener(view -> ExtractTextFromEditText());
        SignInInSignupButton.setOnClickListener(view -> {
//            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        });
        FacebookButtonInSignUp.setOnClickListener(view -> LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile")));
        GoogleButtonInSignUp.setOnClickListener(view -> DoSignInWithGoogle());
    }

    private void DoSignInWithGoogle() {
        startActivityForResult(Gsc.getSignInIntent(), 1000);
    }

    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            HitGoogleApi(idToken);
            Log.e(" Icon  ", "idToken: " + idToken);
            Log.e(" Icon  ", "getId(): " + account.getId());
            Log.e(" Icon  ", "getIdToken(: " + account.getIdToken());
            Log.e(" Icon  ", "getAccount(): " + account.getAccount());
            Log.e(" Icon  ", "zac(): " + account.zac());
            Log.e(" Icon  ", "zad(): " + account.zad());
            Log.e(" Icon  ", "getServerAuthCode(): " + account.getServerAuthCode());
            Log.e(" Icon  ", "getId(: " + account.getId());
        } catch (ApiException e) {
            Log.e(" Icon  ", "ServerauthCode: " + e.getMessage());
            Log.e(" Icon  ", "ServerauthCode: " + e.getCause());
        }
    }

    void HitGoogleApi(String token) {
        GoogleLoginModel model = new GoogleLoginModel(token, "android");
        Dialogs.createProgressDialog(SignUp.this);
        apiManager.GoogleLogin(model, new DataCallback<GoogleLoginModel>() {
            @Override
            public void onSuccess(GoogleLoginModel googleLoginModel) {
                Dialogs.HideProgressDialog();
                try {
                    Prefs.SetBearerToken(getApplicationContext(), googleLoginModel.getToken());
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(ServerError serverError) {
                Log.e(" Icon  Man ", "Google Error: " + serverError.getErrorMsg());
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000) {
            if (data != null) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    String ServerauthCode = account.getServerAuthCode();
                    Log.e("   ", "ServerauthCode: " + ServerauthCode);
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void ExtractTextFromEditText() {
        Name = NameEditTextInSignUp.getText().toString().trim();
        UserName = UserNameEditTextInSignUp.getText().toString().trim();
        Email = EmailEditTextInSignUp.getText().toString().trim();
        PhoneNumber = PhoneNumberEditTextInSignUp.getText().toString().trim();
        Password = PasswordEditTextInSignUp.getText().toString().trim();
        ConfirmPassword = ConfirmPasswordEditTextInSignUp.getText().toString().trim();
        PerformSignUp(Email, Name, UserName, Password, PhoneNumber, ConfirmPassword);
    }

    private void Initialization() {
        callbackManager = CallbackManager.Factory.create();
        Gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestServerAuthCode(getString(R.string.server_client_id))
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        Gsc = GoogleSignIn.getClient(this, Gso);
        FacebookButtonInSignUp = findViewById(R.id.FacebookButtonInSignUp);
        GoogleButtonInSignUp = findViewById(R.id.GoogleButtonInSignUp);
        SignInInSignupButton = findViewById(R.id.SignInInSignupButton);
        NameEditTextInSignUp = findViewById(R.id.NameEditTextInSignUp);
        UserNameEditTextInSignUp = findViewById(R.id.UserNameEditTextInSignUp);
        EmailEditTextInSignUp = findViewById(R.id.EmailEditTextInSignUp);
        PhoneNumberEditTextInSignUp = findViewById(R.id.PhoneNumberEditTextInSignUp);
        PasswordEditTextInSignUp = findViewById(R.id.PasswordEditTextInSignUp);
        SignUpButton = findViewById(R.id.SignUpButton);
        ConfirmPasswordEditTextInSignUp = findViewById(R.id.ConfirmPasswordEditTextInSignUp);
    }

    private void PerformSignUp(String email, String name, String username, String pswrd, String phoneNum,String ConfirmPassword) {
        if (name.isEmpty()){
            showMessage("Please enter Name");
            return;
        } else if (username.isEmpty() ) {
            showMessage("Please enter Username");
            return;
        } else if (email.isEmpty()){
            showMessage("Please enter Email Address");
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showMessage("Please enter a valid Email Address");
            return;
        } else if (phoneNum.isEmpty()){
            showMessage("Please enter Phone number");
            return;
        } else if (pswrd.isEmpty()){
            showMessage("Please enter Password");
            return;
        } else if (ConfirmPassword.isEmpty()){
            showMessage("Please enter Confirm password filed");
            return;
        } else if (phoneNum.length() < 8) {
            showMessage("Phone number must be 10 digits");
            return;
        }
//        Minimum 8 Digit Phone Number Is Required
//        else if (pswrd.length() < 6) {
//            Prompt.SnackBar(findViewById(android.R.id.content), "Password length must be or greater than 6");
//            return;
//        }
        else if (!HelperClass.passwordCharValidation(pswrd)) {
            showMessage("Password Must Contain 1 Uppercase,1 Lowercase,1 Special character with length 8");
        }
        else if(!ConfirmPassword.matches(pswrd)){
            showMessage("New password and confirm password must match");
        } else {
//            final ProgressDialog dialog =  Dialogs.createProgressDialog(SignUp.this);
//            dialog.show();
            Dialogs.createProgressDialog(SignUp.this);
            SignUpModel signUpModel = new SignUpModel(name, username, email, pswrd, phoneNum, "android");
            apiManager.SignUp(signUpModel, new DataCallback<SignUpModel>() {
                @Override
                public void onSuccess(SignUpModel signUpModel) {
                    Dialogs.HideProgressDialog();
                    Prompt.SnackBar(findViewById(android.R.id.content), "Login With New Id");
                    NameEditTextInSignUp.setText("");
                    UserNameEditTextInSignUp.setText("");
                    EmailEditTextInSignUp.setText("");
                    PhoneNumberEditTextInSignUp.setText("");
                    PasswordEditTextInSignUp.setText("");
                    startActivity(new Intent(getApplicationContext(), Login.class));
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

    private void showMessage(String message) {
        Prompt.SnackBar(findViewById(android.R.id.content), message);
    }
}
