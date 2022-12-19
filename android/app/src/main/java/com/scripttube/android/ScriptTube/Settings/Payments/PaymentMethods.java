package com.scripttube.android.ScriptTube.Settings.Payments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import com.scripttube.android.ScriptTube.Adapters.PaymentCardAdapter;
import com.scripttube.android.ScriptTube.R;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.Prompt;

import java.util.ArrayList;
import java.util.List;

public class PaymentMethods extends AppCompatActivity {

    LinearLayout Layout_Login_Btn;
    ImageView iv_back_btn;
    PaymentCardAdapter adapter;
    List<String> list = new ArrayList<>();
    RecyclerView PaymentCardsRecyclerView;
    TextView AddNewCardInPaymentMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getAttributes().windowAnimations = R.anim.zoom_enter;
        setContentView(R.layout.activity_payment_methods);


        Initialization();


        iv_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Layout_Login_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PaymentMethods.this, DonationRaised.class));
            }
        });
        AddNewCardInPaymentMethods.setOnClickListener(view -> {
            ShowAddCardBottomDialog();
        });
    }

    private void ShowAddCardBottomDialog() {
        Dialog dialog = new Dialog(PaymentMethods.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_card_bottom_sheet_dialog);
        EditText CardNumberEditTextDialog, CardExpiryEditTextDialog, CardCvvEditTextDialog;
        LinearLayout CardSaveButton;
        CardNumberEditTextDialog = dialog.findViewById(R.id.CardNumberEditTextDialog);
        CardExpiryEditTextDialog = dialog.findViewById(R.id.CardExpiryEditTextDialog);
        CardCvvEditTextDialog = dialog.findViewById(R.id.CardCvvEditTextDialog);
        CardSaveButton = dialog.findViewById(R.id.CardSaveButton);
        CardSaveButton.setOnClickListener(view -> {
            if(CardNumberEditTextDialog.getText().toString().length() <16)
            {
                Prompt.SnackBar(dialog.findViewById(android.R.id.content),"Enter Valid Card Number");
            }
            else {
                Validate(CardNumberEditTextDialog.getText().toString().substring(12, 15));
                dialog.hide();
            }
        });


        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void Validate(String Card) {
        list.add(Card);
        adapter.notifyDataSetChanged();
    }

    private void Initialization() {
        AddNewCardInPaymentMethods = findViewById(R.id.AddNewCardInPaymentMethods);
        PaymentCardsRecyclerView = findViewById(R.id.PaymentCardsRecyclerView);
        iv_back_btn = findViewById(R.id.BackInSignUp);
        Layout_Login_Btn = findViewById(R.id.PayButton);

        PaymentCardsRecyclerView.setHasFixedSize(true);
        PaymentCardsRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        adapter = new PaymentCardAdapter(list, getApplicationContext());
        PaymentCardsRecyclerView.setAdapter(adapter);
    }
}