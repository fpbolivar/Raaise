package com.raaise.android.Activity.credentials;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.raaise.android.ApiManager.ApiModels.GoogleLoginModel;
import com.raaise.android.ApiManager.ApiModels.SignUpModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.Home.MainHome.Home;
import com.raaise.android.R;
import com.raaise.android.Settings.About.SingleAbout;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.HelperClass;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    TextView termsTextViewBtn;
    CheckBox termsPolicyCheckBox;
    ApiManager apiManagerApi = App.getApiManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.anim.zoom_enter;
        setContentView(R.layout.activity_sign_up);
        BackArrow = findViewById(R.id.BackInSignUp);
        termsTextViewBtn = findViewById(R.id.termsTextView);
        termsPolicyCheckBox = findViewById(R.id.termsCheckBox);

        SpannableString string = new SpannableString("By signing in your agreeing to our Terms of Service and Privacy Policy");

        string.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                startActivity(new Intent(SignUp.this, SingleAbout.class).putExtra("AboutFrom", "TermsOfServiceInSettings"));
            }
        }, 35, 52, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        string.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                startActivity(new Intent(SignUp.this, SingleAbout.class).putExtra("AboutFrom", "PrivacyPolicyInSettings"));
            }
        }, 56, 70, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        termsTextViewBtn.setText(string);
        termsTextViewBtn.setMovementMethod(LinkMovementMethod.getInstance());

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
                Log.i("fbToken", "onSuccess: fb " + new Gson().toJson(loginResult));
                LoginWithFacebook(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(@NonNull FacebookException e) {
                try {
                    PackageInfo info =     getPackageManager().getPackageInfo("com.raaise.android",     PackageManager.GET_SIGNATURES);
                    for (Signature signature : info.signatures) {
                        MessageDigest md = MessageDigest.getInstance("SHA");
                        md.update(signature.toByteArray());
                        String sign=Base64.encodeToString(md.digest(), Base64.DEFAULT);
                        Log.i("fbToken", "hash " + sign);
                        Toast.makeText(getApplicationContext(),sign,     Toast.LENGTH_LONG).show();
                    }
                } catch (PackageManager.NameNotFoundException ex) {
                } catch (NoSuchAlgorithmException ec) {
                }
                Log.i("fbToken", "onSuccess: fb " + e.getMessage());
                Prompt.SnackBar(findViewById(android.R.id.content), e.getMessage());
            }
        });
    }

    private void LoginWithFacebook(String token) {


        Dialogs.createProgressDialog(SignUp.this);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Dialogs.HideProgressDialog();
                FacebookLoginModel fbModel = new FacebookLoginModel(token, "android", task.getResult());
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

            } else {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(findViewById(android.R.id.content), "unable to fetch device token");
            }
        });


    }

    private void clickListener() {
        SignUpButton.setOnClickListener(view -> ExtractTextFromEditText());
        SignInInSignupButton.setOnClickListener(view -> {
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


        } catch (ApiException e) {


        }
    }

    void HitGoogleApi(String token) {
        Dialogs.createProgressDialog(SignUp.this);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Dialogs.HideProgressDialog();
                GoogleLoginModel model = new GoogleLoginModel(token, "android", task.getResult());
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
            if (data != null) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    String ServerauthCode = account.getServerAuthCode();

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
//                .requestServerAuthCode(getString(R.string.server_client_id))
                .requestIdToken(getString(R.string.default_web_client_id))
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

    private void PerformSignUp(String email, String name, String username, String pswrd, String phoneNum, String ConfirmPassword) {
        if (name.isEmpty()) {
            showMessage("Please enter Name");
            return;
        } else if (username.isEmpty()) {
            showMessage("Please enter Username");
            return;
        } else if (email.isEmpty()) {
            showMessage("Please enter Email Address");
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showMessage("Please enter a valid Email Address");
            return;
        } else if (!phoneNum.isEmpty()) {
            if (phoneNum.length() < 10) {
                showMessage("Phone number must be 10 digits");
                return;
            }

        } else if (pswrd.isEmpty()) {
            showMessage("Please enter Password");
            return;
        } else if (ConfirmPassword.isEmpty()) {
            showMessage("Please enter Confirm password filed");
            return;
        } else if (pswrd.length() < 8) {
            Log.i("password", "PerformSignUp: " + pswrd);
            showMessage("Minumum password length should be 8");
        } else if (!ConfirmPassword.matches(pswrd)) {
            showMessage("New password and confirm password must match");
        } else if (!termsPolicyCheckBox.isChecked()){
            showMessage("Please agree to our Terms and Privacy Policies");
        } else {

            Dialogs.createProgressDialog(SignUp.this);
            if (phoneNum.isEmpty()){
                phoneNum = "";
            }
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
