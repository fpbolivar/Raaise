package com.raaise.android.Activity.credentials;

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
import com.google.gson.Gson;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.FacebookLoginModel;
import com.raaise.android.ApiManager.ApiModels.GetPolicyModel;
import com.raaise.android.ApiManager.ApiModels.GoogleLoginModel;
import com.raaise.android.ApiManager.ApiModels.LoginModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.Home.MainHome.Home;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.HelperClass;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;
import com.raaise.android.model.LoginPojo;

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
                Log.i("fbToken", "onSuccess: fb " + new Gson().toJson(loginResult));
                LoginWithFacebook(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(@NonNull FacebookException e) {
                Log.i("fbToken", "onSuccess: fb " + e.getMessage());
                Prompt.SnackBar(findViewById(android.R.id.content), e.getMessage());
            }
        });
    }


    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            HitGoogleApi(idToken);

        } catch (ApiException e) {

        }
    }


    void HitGoogleApi(String token) {
        Dialogs.createProgressDialog(Login.this);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Dialogs.HideProgressDialog();
                GoogleLoginModel model = new GoogleLoginModel(token, "android", task.getResult());
                Dialogs.createProgressDialog(Login.this);
                apiManager.GoogleLogin(model, new DataCallback<GoogleLoginModel>() {
                    @Override
                    public void onSuccess(GoogleLoginModel googleLoginModel) {
                        Dialogs.HideProgressDialog();
                        try {

                            apiManager.GetPolicy(googleLoginModel.token, new GetPolicyModel("s3bucket"), new DataCallback<GetPolicyModel>() {
                                @Override
                                public void onSuccess(GetPolicyModel getPolicyModel) {
                                    Prefs.SetBaseUrl(Login.this, getPolicyModel.getData().getDescription());
                                    Prefs.SetBearerToken(getApplicationContext(), googleLoginModel.getToken());
                                    startActivity(new Intent(getApplicationContext(), Home.class));
                                    finish();
                                }

                                @Override
                                public void onError(ServerError serverError) {

                                }
                            });


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
            } else {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(findViewById(android.R.id.content), "unable to fetch device token");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000) {
            Log.i("googleSignIn", "onActivityResult: " + new Gson().toJson(data));
            if (data != null) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                Log.i("googleSignIn", "onActivityResult: " + task.isSuccessful());
                if (task.isSuccessful()){
                    Log.i("googleSignIn", "onActivityResult: " + task.isSuccessful());
                }
                handleSignInResult(task);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    String ServerauthCode = account.getServerAuthCode();

                } catch (ApiException e) {
                    Log.i("googleSignIn", "onActivityResult: " + e.getMessage());
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
        Gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
//                .requestServerAuthCode(getString(R.string.server_client_id))
                .requestIdToken(getString(R.string.default_web_client_id))
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
        } else if (Password.length() < 8) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Minumum password length should be 8");
        } else {
            Dialogs.createProgressDialog(Login.this);
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    LoginPojo loginPojo = new LoginPojo(Email, Password, task.getResult());
                    Log.i("loginPojo", "PerformLogin: " + new Gson().toJson(loginPojo));
                    apiManager.Login(loginPojo, new DataCallback<LoginModel>() {
                        @Override
                        public void onSuccess(LoginModel loginModel) {
                            Dialogs.HideProgressDialog();
//                            try {
//                                Log.i("loginPojo", "PerformLogin: success inside login  " + new Gson().toJson(loginModel));
                                apiManager.GetPolicy(loginModel.token, new GetPolicyModel("s3bucket"), new DataCallback<GetPolicyModel>() {
                                    @Override
                                    public void onSuccess(GetPolicyModel getPolicyModel) {
//                                        Log.i("loginPojo", "PerformLogin: success s3Bucket " + getPolicyModel.getData().getDescription());
                                        Prefs.SetBaseUrl(Login.this, getPolicyModel.getData().getDescription());
                                        Prefs.SetBearerToken(getApplicationContext(), loginModel.getToken());
                                        startActivity(new Intent(getApplicationContext(), Home.class));
                                        Log.i("Token", loginModel.getToken());
                                        finish();
                                    }

                                    @Override
                                    public void onError(ServerError serverError) {

                                    }
                                });

//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
                        }

                        @Override
                        public void onError(ServerError serverError) {
                            Dialogs.HideProgressDialog();
                            Log.i("loginPojo", "PerformLogin: success inside login  " + serverError.getErrorMsg());
                            Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
                        }
                    });
                } else Dialogs.HideProgressDialog();
            });

        }
    }


    private void LoginWithFacebook(String token) {
        Log.i("fbToken", "LoginWithFacebook: " + token);
        Dialogs.createProgressDialog(Login.this);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Dialogs.HideProgressDialog();
                FacebookLoginModel fbModel = new FacebookLoginModel(token, "android", task.getResult());
                Dialogs.createProgressDialog(Login.this);
                apiManagerApi.FacebookLogin(fbModel, new DataCallback<FacebookLoginModel>() {
                    @Override
                    public void onSuccess(FacebookLoginModel facebookLoginModel) {
                        Dialogs.HideProgressDialog();
                        apiManager.GetPolicy(facebookLoginModel.token, new GetPolicyModel("s3bucket"), new DataCallback<GetPolicyModel>() {
                            @Override
                            public void onSuccess(GetPolicyModel getPolicyModel) {
                                Prefs.SetBaseUrl(Login.this, getPolicyModel.getData().getDescription());
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

                            }
                        });

                    }

                    @Override
                    public void onError(ServerError serverError) {
                        Dialogs.HideProgressDialog();
                        Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
                    }
                });
            } else {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(findViewById(android.R.id.content), "unable to fetch device token");
            }
        });


    }

}