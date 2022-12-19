package com.scripttube.android.ScriptTube.Activity.Credentials.ForgetPasswordFragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.scripttube.android.ScriptTube.Activity.Credentials.ForgetPassword;
import com.scripttube.android.ScriptTube.Activity.Credentials.Login;
import com.scripttube.android.ScriptTube.ApiManager.ApiManager;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.ForgetPasswordModel;
import com.scripttube.android.ScriptTube.ApiManager.DataCallback;
import com.scripttube.android.ScriptTube.ApiManager.RetrofitHelper.App;
import com.scripttube.android.ScriptTube.ApiManager.ServerError;
import com.scripttube.android.ScriptTube.R;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Dialogs;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prefs;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prompt;

public class ForgetPasswordFragment extends Fragment {
    EditText EmailEditTextInForgetPassword;
    LinearLayout NextInForgetPassword;
    String Email;
    ApiManager apiManager = App.getApiManager();
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_forget_password, container, false);
        Initialization(v);
        clickListeners();
        // Inflate the layout for this fragment
        return v;
    }

    private void clickListeners() {
        NextInForgetPassword.setOnClickListener(view -> Validate());
    }

    private void Validate() {
        Email = EmailEditTextInForgetPassword.getText().toString().trim();
        if (Email.isEmpty()) {
            Prompt.SnackBar(v, "Enter Email Address");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            Prompt.SnackBar(v, "Enter Valid Email Address");
        } else {
//            ForgetPassword ff = new ForgetPassword();
//            ((ForgetPassword) requireActivity()).changeFragment(new VerifyOtpFragment());
            ForgetPasswordApi(Email);
        }
    }

    private void ForgetPasswordApi(String Email) {
        ForgetPasswordModel fPmodel = new ForgetPasswordModel(Email);
//        final ProgressDialog dialog =  Dialogs.createProgressDialog(v.getContext());
//        dialog.show();
        Dialogs.createProgressDialog(v.getContext());
        apiManager.ForgetPassword(fPmodel, new DataCallback<ForgetPasswordModel>() {
            @Override
            public void onSuccess(ForgetPasswordModel forgetPasswordModel) {
               Dialogs.HideProgressDialog();
                Prompt.SnackBar(v, forgetPasswordModel.getMessage());
                Prompt.SnackBar(v, forgetPasswordModel.getOtp()+"  Enter This OTP");
                Prefs.SetForgetPasswordEmail(getActivity().getApplicationContext(),forgetPasswordModel.getEmail());
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
        EmailEditTextInForgetPassword = v.findViewById(R.id.EmailEditTextInForgetPasswordFragment);
        NextInForgetPassword = v.findViewById(R.id.NextInForgetPassword);
    }
}