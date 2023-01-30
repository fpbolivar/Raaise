package com.raaise.android.Settings.Payments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.raaise.android.Adapters.PaymentCardAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.DeleteCardModel;
import com.raaise.android.ApiManager.ApiModels.MakePaymentByCardIdModel;
import com.raaise.android.ApiManager.ApiModels.Payment_AddCardModel;
import com.raaise.android.ApiManager.ApiModels.Payment_GetCardsModel;
import com.raaise.android.ApiManager.ApiModels.SetDefaultCardModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;

import java.util.ArrayList;
import java.util.List;

public class PaymentMethods extends AppCompatActivity implements PaymentCardAdapter.CardsListeners {

    LinearLayout Layout_Login_Btn;
    ImageView iv_back_btn;
    PaymentCardAdapter adapter;
    List<Payment_GetCardsModel.Cards> list = new ArrayList<>();
    RecyclerView PaymentCardsRecyclerView;
    TextView AddNewCardInPaymentMethods, NoCardText;
    String amount, donateTo, VideoId;
    ApiManager apiManager = App.getApiManager();
    int count = 0;
    Dialog dialog;
    boolean AddCard = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getAttributes().windowAnimations = R.anim.zoom_enter;
        setContentView(R.layout.activity_payment_methods);
        Initialization();

        HitGetCardAPi();
        iv_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Layout_Login_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HitPaymentAPi();
            }


        });
        AddNewCardInPaymentMethods.setOnClickListener(view -> {
            if (AddCard) {
                AddCard = false;
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AddCard = true;
                    }
                }, 2000);
                ShowAddCardBottomDialog();
            }

        });
    }

    private void HitPaymentAPi() {
        MakePaymentByCardIdModel model = new MakePaymentByCardIdModel(Integer.parseInt(amount), donateTo, VideoId);
        Dialogs.createProgressDialog(PaymentMethods.this);
        apiManager.MakePaymentByCardId(Prefs.GetBearerToken(PaymentMethods.this), model, new DataCallback<MakePaymentByCardIdModel>() {
            @Override
            public void onSuccess(MakePaymentByCardIdModel makePaymentByCardIdModel) {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(findViewById(android.R.id.content), makePaymentByCardIdModel.getMessage());
                ShowConfirmPaymentDialog();
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
            }
        });
    }

    private void ShowConfirmPaymentDialog() {
        final Dialog dialog = new Dialog(PaymentMethods.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow()
                .setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.layout_paymentconfirm_dialog);
        LinearLayout dialog_btn_DonationConfirm = dialog.findViewById(R.id.dialog_btn_DonationConfirm);
        dialog_btn_DonationConfirm.setOnClickListener(view -> {
            dialog.dismiss();
            PaymentMethods.this.finish();
        });

        dialog.show();
    }

    private void ShowAddCardBottomDialog() {

        dialog = new Dialog(PaymentMethods.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_card_bottom_sheet_dialog);
        EditText CardNameEditTextDialog, CardNumberEditTextDialog, CardExpiryEditTextDialog, CardCvvEditTextDialog;
        LinearLayout CardSaveButton;
        CardNameEditTextDialog = dialog.findViewById(R.id.CardNameEditTextDialog);
        CardNumberEditTextDialog = dialog.findViewById(R.id.CardNumberEditTextDialog);
        CardExpiryEditTextDialog = dialog.findViewById(R.id.CardExpiryEditTextDialog);
        CardCvvEditTextDialog = dialog.findViewById(R.id.CardCvvEditTextDialog);
        CardSaveButton = dialog.findViewById(R.id.CardSaveButton);
        CardNumberEditTextDialog.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                Log.d("DEBUG", "afterTextChanged : " + editable);
//                String ccNum = editable.toString().replace(" ", "");
//

                if (count <= CardNumberEditTextDialog.getText().toString().length()
                        && (CardNumberEditTextDialog.getText().toString().length() == 4
                        || CardNumberEditTextDialog.getText().toString().length() == 9
                        || CardNumberEditTextDialog.getText().toString().length() == 14)) {
                    CardNumberEditTextDialog.setText(CardNumberEditTextDialog.getText().toString() + " ");
                    int pos = CardNumberEditTextDialog.getText().length();
                    CardNumberEditTextDialog.setSelection(pos);
                } else if (count >= CardNumberEditTextDialog.getText().toString().length()
                        && (CardNumberEditTextDialog.getText().toString().length() == 4
                        || CardNumberEditTextDialog.getText().toString().length() == 9
                        || CardNumberEditTextDialog.getText().toString().length() == 14)) {
                    CardNumberEditTextDialog.setText(CardNumberEditTextDialog.getText().toString().substring(0, CardNumberEditTextDialog.getText().toString().length() - 1));
                    int pos = CardNumberEditTextDialog.getText().length();
                    CardNumberEditTextDialog.setSelection(pos);
                }

                count = CardNumberEditTextDialog.getText().toString().length();

            }
        });
        CardSaveButton.setOnClickListener(view -> {
            Validate(
                    CardNameEditTextDialog.getText().toString().trim(),
                    CardNumberEditTextDialog.getText().toString().trim(),
                    CardExpiryEditTextDialog.getText().toString().trim(),
                    CardCvvEditTextDialog.getText().toString().trim(),
                    dialog);
        });
        if (!dialog.isShowing()) {
            dialog.show();
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void Validate(String NameOnCard, String CardNumber, String MonthYear, String Cvv, Dialog dialog) {
        if (NameOnCard.equalsIgnoreCase("")) {
            Prompt.SnackBar(dialog.findViewById(android.R.id.content), "Enter Valid Name");
        } else if (CardNumber.equalsIgnoreCase("") || CardNumber.length() < 16) {
            Prompt.SnackBar(dialog.findViewById(android.R.id.content), "Enter Valid Card Number");
        } else if (MonthYear.equalsIgnoreCase("") || MonthYear.length() < 5) {
            Prompt.SnackBar(dialog.findViewById(android.R.id.content), "Enter Valid Month & Year");
        } else if (Cvv.equalsIgnoreCase("") || Cvv.length() < 3) {
            Prompt.SnackBar(dialog.findViewById(android.R.id.content), "Enter Valid CVV");
        } else {
            HitAddCardApi(NameOnCard, CardNumber.replace(" ", ""), Integer.parseInt(MonthYear.substring(0, 2)), Integer.parseInt(MonthYear.substring(3, 5)), Cvv, dialog);
        }

    }

    public void HitGetCardAPi() {
        Dialogs.createProgressDialog(PaymentMethods.this);
        apiManager.GetCards(Prefs.GetBearerToken(PaymentMethods.this), new DataCallback<Payment_GetCardsModel>() {
            @Override
            public void onSuccess(Payment_GetCardsModel payment_getCardsModel) {
                Dialogs.HideProgressDialog();
                list.clear();
                list.addAll(payment_getCardsModel.getCards());
                adapter.notifyDataSetChanged();
                Layout_Login_Btn.setVisibility((list.size() == 0) ? View.GONE : View.VISIBLE);
                PaymentCardsRecyclerView.setVisibility((list.size() == 0) ? View.GONE : View.VISIBLE);
                NoCardText.setVisibility((list.size() == 0) ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
            }
        });
    }

    private void HitAddCardApi(String nameOnCard, String cardNumber, int Month, int Year, String cvv, Dialog dialog) {

        Payment_AddCardModel model = new Payment_AddCardModel(nameOnCard, cardNumber, Month, Year, cvv);
        Dialogs.createProgressDialog(PaymentMethods.this);
        apiManager.AddCard(Prefs.GetBearerToken(PaymentMethods.this), model, new DataCallback<Payment_AddCardModel>() {
            @Override
            public void onSuccess(Payment_AddCardModel payment_addCardModel) {
                Dialogs.HideProgressDialog();
                dialog.dismiss();
                Prompt.SnackBar(findViewById(android.R.id.content), payment_addCardModel.getMessage());
                HitGetCardAPi();
            }

            @Override
            public void onError(ServerError serverError) {
//                dialog.dismiss();
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(dialog.findViewById(android.R.id.content), serverError.getErrorMsg());
            }
        });

    }

    private void Initialization() {
        AddNewCardInPaymentMethods = findViewById(R.id.AddNewCardInPaymentMethods);
        PaymentCardsRecyclerView = findViewById(R.id.PaymentCardsRecyclerView);
        iv_back_btn = findViewById(R.id.BackInSignUp);
        Layout_Login_Btn = findViewById(R.id.PayButton);
        NoCardText = findViewById(R.id.NoCardText);

        PaymentCardsRecyclerView.setHasFixedSize(true);
        PaymentCardsRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        adapter = new PaymentCardAdapter(list, getApplicationContext(), PaymentMethods.this);
        PaymentCardsRecyclerView.setAdapter(adapter);
        Intent i = getIntent();
        amount = i.getStringExtra("AmountToDonate");
        donateTo = i.getStringExtra("DonateTo");
        VideoId = i.getStringExtra("VideoId");
        if (VideoId == null || VideoId.equalsIgnoreCase("")) {
            Layout_Login_Btn.setVisibility(View.GONE);
        }
    }

    @Override
    public void DataChanged(String CardId) {
        SetDefaultCardModel model = new SetDefaultCardModel(CardId);
        Dialogs.createProgressDialog(PaymentMethods.this);
        apiManager.SetDefaultCard(Prefs.GetBearerToken(PaymentMethods.this), model, new DataCallback<SetDefaultCardModel>() {
            @Override
            public void onSuccess(SetDefaultCardModel setDefaultCardModel) {
                Dialogs.HideProgressDialog();
                HitGetCardAPi();
            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.HideProgressDialog();
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
            }
        });
    }

    @Override
    public void DeleteCard(String CardId) {
        DeleteCardModel model = new DeleteCardModel(CardId);
        apiManager.DeleteCard(Prefs.GetBearerToken(PaymentMethods.this), model, new DataCallback<DeleteCardModel>() {
            @Override
            public void onSuccess(DeleteCardModel deleteCardModel) {
                Prompt.SnackBar(findViewById(android.R.id.content), deleteCardModel.getMessage());
                HitGetCardAPi();
            }

            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
            }
        });
    }
}