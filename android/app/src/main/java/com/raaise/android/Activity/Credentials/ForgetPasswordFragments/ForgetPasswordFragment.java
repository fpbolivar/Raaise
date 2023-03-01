package com.raaise.android.Activity.credentials.ForgetPasswordFragments;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.raaise.android.Activity.credentials.ForgetPassword;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.ForgetPasswordModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgetPasswordFragment extends Fragment {
    EditText EmailEditTextInForgetPassword;
    LinearLayout NextInForgetPassword;
    String Email;
    ApiManager apiManager = App.getApiManager();
    View v;
    ImageView BackInForgetPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_forget_password, container, false);
        Initialization(v);
        clickListeners();
        Log.i("forgotEmail", "onCreateView: " + Prefs.GetForgetPasswordEmail(v.getContext()));
        return v;
    }

    private void clickListeners() {
        NextInForgetPassword.setOnClickListener(view -> Validate());
        BackInForgetPassword.setOnClickListener(view -> ((ForgetPassword) requireActivity()).FinishChangePasswordActivity());
    }

    private void Validate() {
        Email = EmailEditTextInForgetPassword.getText().toString().trim();
        if (Email.isEmpty()) {
            Prompt.SnackBar(v, "Enter Email Address");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            Prompt.SnackBar(v, "Enter Valid Email Address");
        } else {
            ForgetPasswordApi(Email);
        }
    }

    private void ForgetPasswordApi(String Email) {
        ForgetPasswordModel fPmodel = new ForgetPasswordModel(Email);
        Dialogs.createProgressDialog(v.getContext());
        apiManager.ForgetPassword(fPmodel, new DataCallback<ForgetPasswordModel>() {
            @Override
            public void onSuccess(ForgetPasswordModel forgetPasswordModel) {
                Dialogs.HideProgressDialog();
                String userEmail = "";
                Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(forgetPasswordModel.getMessage());
                while (m.find()) {
                    userEmail = m.group();
                }
                Log.i("forgotEmail", "onSuccess: " + new Gson().toJson(forgetPasswordModel));
//                Prompt.SnackBar(v, forgetPasswordModel.getMessage());
                Prefs.SetForgetPasswordEmail(getActivity().getApplicationContext(), userEmail);

                Log.i("forgotEmail", "onSuccess: After while " + userEmail);
                ForgetPassword.otpMessage = forgetPasswordModel.getMessage();
                ((ForgetPassword) requireActivity()).changeFragment(new VerifyOtpFragment());
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(v, serverError.getErrorMsg());

            }
        });
    }

    private void Initialization(View v) {
        BackInForgetPassword = v.findViewById(R.id.BackInForgetPassword);
        EmailEditTextInForgetPassword = v.findViewById(R.id.EmailEditTextInForgetPasswordFragment);
        NextInForgetPassword = v.findViewById(R.id.NextInForgetPassword);
    }
}