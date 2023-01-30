package com.raaise.android.Activity.Credentials.ForgetPasswordFragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.raaise.android.Activity.Credentials.ForgetPassword;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.VerifyOtpModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;

public class VerifyOtpFragment extends Fragment {
    EditText OtpEditTextInVerifyOtp;
    LinearLayout SubmitInVerifyOtp;
    View v;
    String Otp;
    ApiManager apiManager = App.getApiManager();
    TextView CodeSentToText;
    ImageView BackInEnterOtp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_verify_otp, container, false);
        Initialization();
        clickListeners();
        return v;
    }

    private void clickListeners() {
        SubmitInVerifyOtp.setOnClickListener(view -> Validate());
        BackInEnterOtp.setOnClickListener(view -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void Validate() {
        Otp = OtpEditTextInVerifyOtp.getText().toString().trim();
        if (Otp.isEmpty()) {
            Prompt.SnackBar(v, "Enter OTP");
        } else if (Otp.length() < 4) {
            Prompt.SnackBar(v, "Enter Valid OTP");
        } else {
            VerifyOtpApi(Otp);
        }

    }

    private void VerifyOtpApi(String Otp) {

        Dialogs.createProgressDialog(v.getContext());
        VerifyOtpModel otpModel = new VerifyOtpModel(Prefs.GetForgetPasswordEmail(getActivity().getApplicationContext()), Otp);
        apiManager.VerifyOtp(otpModel, new DataCallback<VerifyOtpModel>() {
            @Override
            public void onSuccess(VerifyOtpModel verifyOtpModel) {
                Dialogs.HideProgressDialog();
                Prefs.SetFORGETPASSWORDTOKEN(getActivity().getApplicationContext(), verifyOtpModel.getToken());
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
        BackInEnterOtp = v.findViewById(R.id.BackInEnterOtp);
        OtpEditTextInVerifyOtp = v.findViewById(R.id.OtpEditTextInVerifyOtp);
        SubmitInVerifyOtp = v.findViewById(R.id.SubmitInVerifyOtp);
        CodeSentToText = v.findViewById(R.id.CodeSentToText);
        String sourceString = "OTP has been sent to " + Prefs.GetForgetPasswordEmail(v.getContext());
        CodeSentToText.setText(Html.fromHtml(sourceString));
    }
}