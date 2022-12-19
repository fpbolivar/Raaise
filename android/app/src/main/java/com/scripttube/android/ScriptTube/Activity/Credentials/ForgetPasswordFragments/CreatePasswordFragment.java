package com.scripttube.android.ScriptTube.Activity.Credentials.ForgetPasswordFragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.scripttube.android.ScriptTube.Activity.Credentials.ForgetPassword;
import com.scripttube.android.ScriptTube.ApiManager.ApiManager;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.CreateNewPasswordModel;
import com.scripttube.android.ScriptTube.ApiManager.DataCallback;
import com.scripttube.android.ScriptTube.ApiManager.RetrofitHelper.App;
import com.scripttube.android.ScriptTube.ApiManager.ServerError;
import com.scripttube.android.ScriptTube.R;
import com.scripttube.android.ScriptTube.Settings.MyAccount.ChangePassword;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Dialogs;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.HelperClass;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prefs;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prompt;

public class CreatePasswordFragment extends Fragment {
    EditText NewPasswordInCreatePasswordFragment, ConfirmPasswordInCreatePasswordFragment;
    LinearLayout SubmitInCreateNewPassword;
    String NewPass, ConfirmPass;
    ApiManager apiManager = App.getApiManager();
    ImageView BackInCreatePassword;

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_create_password, container, false);
        // Inflate the layout for this fragment
        Initialization();
        clickListeners();
        return v;
    }

    private void clickListeners() {
        SubmitInCreateNewPassword.setOnClickListener(view -> Validate());
    }

    private void Validate() {
        NewPass = NewPasswordInCreatePasswordFragment.getText().toString().trim();
        ConfirmPass = ConfirmPasswordInCreatePasswordFragment.getText().toString().trim();
        if (NewPass.isEmpty()) {
            Prompt.SnackBar(v, "Enter New Password");
        } else if (ConfirmPass.isEmpty()) {
            Prompt.SnackBar(v, "Enter Confirm Password");
        }

//        else if (NewPass.length()<6) {
//            Prompt.SnackBar(v, "Minimum Length Of New password Should Be 6");
//        }
//        else if (ConfirmPass.length()<6) {
//            Prompt.SnackBar(v, "Minimum Length Of Confirm password Should Be 6");
//        }
        else if (!HelperClass.passwordCharValidation(NewPass)) {
            Prompt.SnackBar(v, "Password Must Contain 1 Uppercase,1 Lowercase,1 Special character with length 8");
        }
        else if (!NewPass.equals(ConfirmPass)) {
            Prompt.SnackBar(v, "New password & Confirm Password Must Match");
        }
        else {
            ResetUserPassword(NewPass);
        }
    }

    private void ResetUserPassword(String NewPass) {
//        final ProgressDialog dialog =  Dialogs.createProgressDialog(v.getContext());
//        dialog.show();
        Dialogs.createProgressDialog(v.getContext());
        CreateNewPasswordModel model = new CreateNewPasswordModel(NewPass, Prefs.GetFORGETPASSWORDTOKEN(getActivity().getApplicationContext()));
        apiManager.CreateNewPassword(model, new DataCallback<CreateNewPasswordModel>() {
            @Override
            public void onSuccess(CreateNewPasswordModel createNewPasswordModel) {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(v,"Password Changed Successfully");
                ((ForgetPassword) requireActivity()).FinishChangePasswordActivity();

            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(v,serverError.getErrorMsg());

            }
        });

    }

    private void Initialization() {
        BackInCreatePassword = v.findViewById(R.id.BackInCreatePassword);
        NewPasswordInCreatePasswordFragment = v.findViewById(R.id.NewPasswordInCreatePasswordFragment);
        ConfirmPasswordInCreatePasswordFragment = v.findViewById(R.id.ConfirmPasswordInCreatePasswordFragment);
        SubmitInCreateNewPassword = v.findViewById(R.id.SubmitInCreateNewPassword);
    }
}