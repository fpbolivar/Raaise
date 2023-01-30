package com.raaise.android.Settings.Payments;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.Payment_AddUpdateBankDetailsModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;

public class BankDetails extends AppCompatActivity {

    LinearLayout SaveBankDetailsButton;
    ImageView BackBtn;
    EditText BankNameET,
            AccountNumberET,
            ConfirmAccountNumberET,
            RoutingNumberET,
            CityET,
            StateET,
            AddressET,
            PostalCodeET,
            PhoneNumberNumberET;
    ApiManager apiManager = App.getApiManager();
    String AccHolderName = "AccHolderName";
    String AccAccNumber = "AccNumber";
    String AccRoutingNumber = "RoutingNumber";
    String AccCity = "City";
    String AccState = "State";
    String AccAddress = "Address";
    String AccPostalCode = "PostalCode";
    String AccPhoneNumber = "PhoneNumber";


    public static boolean Get(EditText editText) {
        return editText.getText().toString().trim().equalsIgnoreCase("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.anim.zoom_enter;
        setContentView(R.layout.activity_bank_details);
        Initialization();
        ClickListeners();


    }

    private void ClickListeners() {
        SaveBankDetailsButton.setOnClickListener(view -> {
            ValidateBankDetails();
        });
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void ValidateBankDetails() {
        if (Get(BankNameET)) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Enter Account Holder Name");
        } else if (Get(AccountNumberET)) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Enter Account Number");
        } else if (Get(ConfirmAccountNumberET)) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Enter Confirm Account Number");
        } else if (Get(RoutingNumberET)) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Enter Routing Number");
        } else if (Get(CityET)) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Enter City");
        } else if (Get(StateET)) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Enter State");
        } else if (Get(AddressET)) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Enter Address");
        } else if (Get(PostalCodeET)) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Enter Postal Code");
        } else if (Get(PhoneNumberNumberET) || PhoneNumberNumberET.getText().toString().trim().length() < 10) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Enter Phone Number");
        } else if (!AccountNumberET.getText().toString().trim().equalsIgnoreCase(ConfirmAccountNumberET.getText().toString().trim())) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Confirm Account Must Match With Account Number");
        } else {

            HitAddUpdateBankDetailsApi(GetString(BankNameET),
                    GetString(AccountNumberET),
                    GetString(RoutingNumberET),
                    GetString(CityET),
                    GetString(StateET),
                    GetString(AddressET),
                    GetString(PostalCodeET),
                    GetString(PhoneNumberNumberET));

        }
    }

    private void HitAddUpdateBankDetailsApi(String BankName,
                                            String AccountNumber,
                                            String RoutingNumber,
                                            String City,
                                            String State,
                                            String Address,
                                            String PostalCode,
                                            String PhoneNumberNumber) {

        Payment_AddUpdateBankDetailsModel model =
                new Payment_AddUpdateBankDetailsModel(BankName, AccountNumber, Long.parseLong(RoutingNumber), Long.parseLong(PhoneNumberNumber), City, State, Long.parseLong(PostalCode), Address);
        Dialogs.createProgressDialog(BankDetails.this);
        apiManager.AddUpdateBankDetails(Prefs.GetBearerToken(BankDetails.this), model, new DataCallback<Payment_AddUpdateBankDetailsModel>() {
            @Override
            public void onSuccess(Payment_AddUpdateBankDetailsModel payment_addUpdateBankDetailsModel) {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(findViewById(android.R.id.content), payment_addUpdateBankDetailsModel.getMessage());

                Prefs.SetBankDetails(BankDetails.this, GetString(BankNameET), AccHolderName);
                Prefs.SetBankDetails(BankDetails.this, GetString(AccountNumberET), AccAccNumber);
                Prefs.SetBankDetails(BankDetails.this, GetString(RoutingNumberET), AccRoutingNumber);
                Prefs.SetBankDetails(BankDetails.this, GetString(CityET), AccCity);
                Prefs.SetBankDetails(BankDetails.this, GetString(StateET), AccState);
                Prefs.SetBankDetails(BankDetails.this, GetString(AddressET), AccAddress);
                Prefs.SetBankDetails(BankDetails.this, GetString(PostalCodeET), AccPostalCode);
                Prefs.SetBankDetails(BankDetails.this, GetString(PhoneNumberNumberET), AccPhoneNumber);
                BankNameET.setText("");
                AccountNumberET.setText("");
                ConfirmAccountNumberET.setText("");
                RoutingNumberET.setText("");
                CityET.setText("");
                StateET.setText("");
                AddressET.setText("");
                PostalCodeET.setText("");
                PhoneNumberNumberET.setText("");
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());

            }
        });
    }

    private String GetString(EditText ET) {
        return ET.getText().toString().trim();
    }

    private void Initialization() {
        SaveBankDetailsButton = findViewById(R.id.SaveBankDetailsButton);
        BackBtn = findViewById(R.id.BackInSignUp);
        BankNameET = findViewById(R.id.BankNameET);
        AccountNumberET = findViewById(R.id.AccountNumberET);
        ConfirmAccountNumberET = findViewById(R.id.ConfirmAccountNumberET);
        RoutingNumberET = findViewById(R.id.RoutingNumberET);
        CityET = findViewById(R.id.CityET);
        StateET = findViewById(R.id.StateET);
        AddressET = findViewById(R.id.AddressET);
        PostalCodeET = findViewById(R.id.PostalCodeET);
        PhoneNumberNumberET = findViewById(R.id.PhoneNumberNumberET);

        BankNameET.setText(Prefs.GetBankDetails(BankDetails.this, AccHolderName));
        AccountNumberET.setText(Prefs.GetBankDetails(BankDetails.this, AccAccNumber));
        ConfirmAccountNumberET.setText(Prefs.GetBankDetails(BankDetails.this, AccAccNumber));
        RoutingNumberET.setText(Prefs.GetBankDetails(BankDetails.this, AccRoutingNumber));
        CityET.setText(Prefs.GetBankDetails(BankDetails.this, AccCity));
        StateET.setText(Prefs.GetBankDetails(BankDetails.this, AccState));
        AddressET.setText(Prefs.GetBankDetails(BankDetails.this, AccAddress));
        PostalCodeET.setText(Prefs.GetBankDetails(BankDetails.this, AccPostalCode));
        PhoneNumberNumberET.setText(Prefs.GetBankDetails(BankDetails.this, AccPhoneNumber));
    }
}