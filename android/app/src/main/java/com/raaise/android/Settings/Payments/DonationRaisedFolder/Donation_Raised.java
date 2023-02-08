package com.raaise.android.Settings.Payments.DonationRaisedFolder;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.raaise.android.Adapters.DonationRaisedAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.UserDonationHistoryModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;

import java.util.ArrayList;
import java.util.List;

public class Donation_Raised extends AppCompatActivity {
    TextView TotalDonatedAmountText, TotalRaisedAmountText, TotalWithdrawalAmountText;
    ApiManager apiManager = App.getApiManager();
    DonationRaisedAdapter adapter;
    List<UserDonationHistoryModel.Data.UserVideo> list;
    RecyclerView DonationHistoryRecyclerView;
    ImageView BackInDonationRaised;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_raised2);
        Initialization();
        ClickListeners();
        HitApi();
    }

    private void HitApi() {
        apiManager.DonationHistory(Prefs.GetBearerToken(Donation_Raised.this), new DataCallback<UserDonationHistoryModel>() {
            @Override
            public void onSuccess(UserDonationHistoryModel userDonationHistoryModel) {

                TotalDonatedAmountText.setText("$" + userDonationHistoryModel.getData().getDonatedAmount());
                TotalRaisedAmountText.setText("$" + userDonationHistoryModel.getData().getTotalRaised());
                TotalWithdrawalAmountText.setText("$" + userDonationHistoryModel.getData().getTotalWithdraw());
                list.addAll(userDonationHistoryModel.getData().getUserVideo());
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
            }
        });
    }

    private void ClickListeners() {
        BackInDonationRaised.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void Initialization() {
        BackInDonationRaised = findViewById(R.id.BackInDonationRaised);
        DonationHistoryRecyclerView = findViewById(R.id.DonationHistoryRecyclerView);
        DonationHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(Donation_Raised.this));
        DonationHistoryRecyclerView.setHasFixedSize(true);
        TotalDonatedAmountText = findViewById(R.id.TotalDonatedAmountText);
        TotalRaisedAmountText = findViewById(R.id.TotalRaisedAmountText);
        TotalWithdrawalAmountText = findViewById(R.id.TotalWithdrawalAmountText);
        list = new ArrayList<>();
        adapter = new DonationRaisedAdapter(Donation_Raised.this, list);
        DonationHistoryRecyclerView.setAdapter(adapter);
    }
}