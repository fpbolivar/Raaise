package com.raaise.android.Settings.Payments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.Payment_AddUpdateBankDetailsModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;

import java.util.Calendar;

public class BankDetails extends AppCompatActivity {

    LinearLayout SaveBankDetailsButton;
    ImageView BackBtn;
    TextView BankNameDOBTV;
    EditText BankNameET,
            BankLastFourSSN,
            BankAccountIdET,
            BankNameLET,
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

    String ACCLastName = "LastName";
    String DOB = "dob";
    String LastFour = "lastfour";
    String AccID = "accId";


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

        BankNameDOBTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        BankDetails.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our edit text.
                                BankNameDOBTV.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                Log.i("onDateSet", "onDateSet: " + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth );

                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();

            }
        });

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
        } else if (Get(BankNameLET)){
            Prompt.SnackBar(findViewById(android.R.id.content), "Enter Account Holder Last Name");
        } else if (BankNameDOBTV.getText().toString().equals("") || BankNameDOBTV.getText().toString().isEmpty()) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Enter Date of Birth");
        } else if (Get(BankAccountIdET)){
            Prompt.SnackBar(findViewById(android.R.id.content), "Enter Bank Account ID");
        }else if (Get(AccountNumberET)) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Enter Account Number");
        } else if (Get(ConfirmAccountNumberET)) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Enter Confirm Account Number");
        } else if (!AccountNumberET.getText().toString().equals(ConfirmAccountNumberET.getText().toString())){
            Prompt.SnackBar(findViewById(android.R.id.content), "Account number should match");
        }else if (Get(RoutingNumberET)) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Enter Routing Number");
        } else if (Get(CityET)) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Enter City");
        } else if (Get(StateET)) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Enter State");
        } else if (Get(AddressET)) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Enter Address");
        } else if (Get(PostalCodeET)) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Enter Postal Code");
        } else if (Get(BankLastFourSSN)){
            Prompt.SnackBar(findViewById(android.R.id.content), "Enter Last 4-Digits of SSN");
        }else if (Get(PhoneNumberNumberET) || PhoneNumberNumberET.getText().toString().trim().length() < 10) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Enter Phone Number");
        } else if (!AccountNumberET.getText().toString().trim().equalsIgnoreCase(ConfirmAccountNumberET.getText().toString().trim())) {
            Prompt.SnackBar(findViewById(android.R.id.content), "Confirm Account Must Match With Account Number");
        } else {

            HitAddUpdateBankDetailsApi(GetString(BankNameET),
                    GetString(BankNameLET),
                    BankNameDOBTV.getText().toString(),
                    GetString(AccountNumberET),
                    GetString(RoutingNumberET),
                    GetString(CityET),
                    GetString(StateET),
                    GetString(AddressET),
                    GetString(PostalCodeET),
                    GetString(PhoneNumberNumberET),
                    GetString(BankLastFourSSN));

        }
    }

    private void HitAddUpdateBankDetailsApi(String BankName,
                                            String accHolderLN,
                                            String dob,
                                            String AccountNumber,
                                            String RoutingNumber,
                                            String City,
                                            String State,
                                            String Address,
                                            String PostalCode,
                                            String PhoneNumberNumber,
                                            String lastFourSSN) {

        Payment_AddUpdateBankDetailsModel model =
                new Payment_AddUpdateBankDetailsModel(BankName, accHolderLN, dob, AccountNumber, Long.parseLong(RoutingNumber), Long.parseLong(PhoneNumberNumber), City, State, Long.parseLong(PostalCode), Address, lastFourSSN);
        Log.i("bankDetails", "HitAddUpdateBankDetailsApi: " + new Gson().toJson(model));
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
                Prefs.SetBankDetails(BankDetails.this, GetString(PhoneNumberNumberET), ACCLastName);
                Prefs.SetBankDetails(BankDetails.this, GetString(PhoneNumberNumberET), DOB);
                Prefs.SetBankDetails(BankDetails.this, GetString(PhoneNumberNumberET), LastFour);
                Prefs.SetBankDetails(BankDetails.this, GetString(PhoneNumberNumberET), AccID);

                BankNameET.setText("");
                BankNameLET.setText("");
                BankNameDOBTV.setText("");
                BankLastFourSSN.setText("");
                BankAccountIdET.setText("");
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
        BankNameDOBTV = findViewById(R.id.BankNameDOBET);
        BankLastFourSSN = findViewById(R.id.BankLastFourSSN);
        BankAccountIdET = findViewById(R.id.BankAccountIdET);
        BankNameLET = findViewById(R.id.BankNameLET);
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

        BankNameLET.setText(Prefs.GetBankDetails(BankDetails.this, ACCLastName));
        BankNameDOBTV.setText(Prefs.GetBankDetails(BankDetails.this, DOB));
        BankLastFourSSN.setText(Prefs.GetBankDetails(BankDetails.this, LastFour));
        BankAccountIdET.setText(Prefs.GetBankDetails(BankDetails.this, AccID));
    }
}