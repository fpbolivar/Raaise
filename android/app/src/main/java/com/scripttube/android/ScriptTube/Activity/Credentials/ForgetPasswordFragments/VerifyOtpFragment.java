package com.scripttube.android.ScriptTube.Activity.Credentials.ForgetPasswordFragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scripttube.android.ScriptTube.Activity.Credentials.ForgetPassword;
import com.scripttube.android.ScriptTube.ApiManager.ApiManager;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.VerifyOtpModel;
import com.scripttube.android.ScriptTube.ApiManager.DataCallback;
import com.scripttube.android.ScriptTube.ApiManager.RetrofitHelper.App;
import com.scripttube.android.ScriptTube.ApiManager.ServerError;
import com.scripttube.android.ScriptTube.R;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Dialogs;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prefs;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prompt;

public class VerifyOtpFragment extends Fragment {
    EditText OtpEditTextInVerifyOtp;
    LinearLayout SubmitInVerifyOtp;
    View v;
    String Otp;
    ApiManager apiManager = App.getApiManager();
    TextView CodeSentToText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v =inflater.inflate(R.layout.fragment_verify_otp, container, false);
        Initialization();
        clickListeners();
        return v;
    }

    private void clickListeners() {
        SubmitInVerifyOtp.setOnClickListener(view -> Validate());
    }

    private void Validate() {
        Otp = OtpEditTextInVerifyOtp.getText().toString().trim();
        if(Otp.isEmpty())
        {
            Prompt.SnackBar(v,"Enter OTP");
        }
        else if(Otp.length()<4)
        {
            Prompt.SnackBar(v,"Enter Valid OTP");
        }
        else
        {
            VerifyOtpApi(Otp);
        }

    }

    private void VerifyOtpApi(String Otp) {
//        final ProgressDialog dialog =  Dialogs.createProgressDialog(v.getContext());
//        dialog.show();
        Dialogs.createProgressDialog(v.getContext());
        VerifyOtpModel otpModel = new VerifyOtpModel(Prefs.GetForgetPasswordEmail(getActivity().getApplicationContext()),Otp);
        apiManager.VerifyOtp(otpModel, new DataCallback<VerifyOtpModel>() {
            @Override
            public void onSuccess(VerifyOtpModel verifyOtpModel) {
               Dialogs.HideProgressDialog();
                Prefs.SetFORGETPASSWORDTOKEN(getActivity().getApplicationContext(),verifyOtpModel.getToken());
//                Prefs.SetForgetPasswordVerifyOtp(getActivity().getApplicationContext(),verifyOtpModel.getOtp());
                ((ForgetPassword) requireActivity()).changeFragment(new CreatePasswordFragment());
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(v, serverError.getErrorMsg());
            }
        });
    }

    private void Initialization() {
        OtpEditTextInVerifyOtp  = v.findViewById(R.id.OtpEditTextInVerifyOtp);
        SubmitInVerifyOtp  = v.findViewById(R.id.SubmitInVerifyOtp);
        CodeSentToText  = v.findViewById(R.id.CodeSentToText);
        CodeSentToText.setText("The code has been sent on "+Prefs.GetForgetPasswordEmail(v.getContext()));
    }
}