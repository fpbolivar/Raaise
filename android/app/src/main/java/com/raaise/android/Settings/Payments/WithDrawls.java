package com.raaise.android.Settings.Payments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.raaise.android.Adapters.WithdrawalsAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.model.WithdrawalsPojo;

public class WithDrawls extends AppCompatActivity {

    TextView BankDetail;
    ImageView BackArrow;
    private RecyclerView withdrawalsRV;
    private WithdrawalsAdapter adapter;

    ApiManager apiManager = App.getApiManager();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.anim.zoom_enter;
        setContentView(R.layout.activity_with_drawls);
        inItWidgets();

        BackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        BankDetail.setOnClickListener(v -> {
            startActivity(new Intent(WithDrawls.this, BankDetails.class));
            finish();
        });

        apiManager.getUSerWithdrawals(Prefs.GetBearerToken(this), new DataCallback<WithdrawalsPojo>() {
            @Override
            public void onSuccess(WithdrawalsPojo withdrawalsPojo) {
                Log.i("getUSerWithdrawals", "onSuccess: " + withdrawalsPojo.data.size());
                adapter.setData(withdrawalsPojo.getData());
            }

            @Override
            public void onError(ServerError serverError) {
                Log.i("getUSerWithdrawals", "onError: " + serverError.error);
            }
        });
    }

    private void inItWidgets() {
        adapter = new WithdrawalsAdapter(WithDrawls.this);
        withdrawalsRV = findViewById(R.id.withdrawals_rv);
        BackArrow = findViewById(R.id.BackInSignUp);
        BankDetail = findViewById(R.id.tv_bankDetails);

        withdrawalsRV.setAdapter(adapter);

    }
}