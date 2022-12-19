package com.scripttube.android.ScriptTube.Activity.Credentials;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.scripttube.android.ScriptTube.ApiManager.ApiManager;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.FacebookLoginModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.GoogleLoginModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.LoginModel;
import com.scripttube.android.ScriptTube.ApiManager.DataCallback;
import com.scripttube.android.ScriptTube.ApiManager.RetrofitHelper.App;
import com.scripttube.android.ScriptTube.ApiManager.ServerError;
import com.scripttube.android.ScriptTube.Home.MainHome.Home;
import com.scripttube.android.ScriptTube.R;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Dialogs;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.HelperClass;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prefs;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prompt;
import com.scripttube.android.ScriptTube.model.LoginPojo;

import java.util.Arrays;

public class Login extends AppCompatActivity {
    TextView DoSignUpButton, ForgetPassword;
    LinearLayout LoginButton, FacebookButton, GoogleButton;
    ApiManager apiManager = App.getApiManager();
    EditText EmailEditText, PasswordEditText;
    CallbackManager callbackManager;
    ApiManager apiManagerApi = App.getApiManager();
    GoogleSignInOptions Gso;
    GoogleSignInClient Gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.anim.zoom_enter;
        setContentView(R.layout.activity_login);
        Initialization();
        Gsc.signOut();

        clickListeners();

        LoginManager.getInstance().logOut();
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


    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            HitGoogleApi(idToken);
            Log.e(" Icon  ", "ServerauthCode: " + idToken);
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

    //    private void LoginWithGoogle(String token) {
//        GoogleLoginModel model = new GoogleLoginModel(token, "android");
//        Dialogs.createProgressDialog(Login.this);
//        apiManager.GoogleLogin(model, new DataCallback<GoogleLoginModel>() {
//            @Override
//            public void onSuccess(GoogleLoginModel googleLoginModel) {
//                Dialogs.HideProgressDialog();
//                try {
//                    Prefs.SetBearerToken(getApplicationContext(), googleLoginModel.getToken());
//                    startActivity(new Intent(getApplicationContext(), Home.class));
//                    finish();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onError(ServerError serverError) {
//                Dialogs.HideProgressDialog();
//                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
//            }
//        });
//    }
    void HitGoogleApi(String token) {
        GoogleLoginModel model = new GoogleLoginModel(token, "android");
        Dialogs.createProgressDialog(Login.this);
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

    private void clickListeners() {
        DoSignUpButton.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), SignUp.class));
//            finish();
        });
        ForgetPassword.setOnClickListener(view ->
        {
            startActivity(new Intent(getApplicationContext(), ForgetPassword.class));
        });
        LoginButton.setOnClickListener(view -> {
            PerformLogin(EmailEditText.getText().toString().trim(), PasswordEditText.getText().toString().trim());
        });
        FacebookButton.setOnClickListener(view -> {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
        });
        GoogleButton.setOnClickListener(view -> DoSignInWithGoogle());
    }

    private void DoSignInWithGoogle() {
        startActivityForResult(Gsc.getSignInIntent(), 1000);
    }


    private void Initialization() {
        callbackManager = CallbackManager.Factory.create();
        DoSignUpButton = findViewById(R.id.DoSignUpButton);
        ForgetPassword = findViewById(R.id.ForgetPassword);
        LoginButton = findViewById(R.id.LoginButton);
        EmailEditText = findViewById(R.id.EmailEditText);
        PasswordEditText = findViewById(R.id.PasswordEditText);
        FacebookButton = findViewById(R.id.FacebookButton);
        GoogleButton = findViewById(R.id.GoogleButton);
//        Gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        Gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestServerAuthCode(getString(R.string.server_client_id))
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();
        Gsc = GoogleSignIn.getClient(this, Gso);
        GoogleSignInAccount ss = GoogleSignIn.getLastSignedInAccount(this);
    }

    private void PerformLogin(String Email, String Password) {
        if (Email.isEmpty()) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Enter Email or Username");
        } else if (Password.isEmpty()) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Enter Password");
        } else if (!HelperClass.passwordCharValidation(Password)) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Password Must Contain 1 Uppercase,1 Lowercase,1 Special character with length 8");
        } else {
            Dialogs.createProgressDialog(Login.this);
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Log.e("Checking Token ", "PerformLogin: "+task.getResult() );
                    LoginPojo loginPojo = new LoginPojo(Email, Password, task.getResult());
                    apiManager.Login(loginPojo, new DataCallback<LoginModel>() {
                        @Override
                        public void onSuccess(LoginModel loginModel) {
                            Dialogs.HideProgressDialog();
                            try {
                                Prefs.SetBearerToken(getApplicationContext(), loginModel.getToken());
                                startActivity(new Intent(getApplicationContext(), Home.class));
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ServerError serverError) {
                            Dialogs.HideProgressDialog();
                            Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
                        }
                    });
                } else Dialogs.HideProgressDialog();
            });

        }
    }


    private void LoginWithFacebook(String token) {
        FacebookLoginModel fbModel = new FacebookLoginModel(token, "android");
        Dialogs.createProgressDialog(Login.this);
        apiManagerApi.FacebookLogin(fbModel, new DataCallback<FacebookLoginModel>() {
            @Override
            public void onSuccess(FacebookLoginModel facebookLoginModel) {
                Dialogs.HideProgressDialog();
                if (!facebookLoginModel.getToken().isEmpty()) {
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    Prefs.SetBearerToken(Login.this, facebookLoginModel.getToken());
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

}