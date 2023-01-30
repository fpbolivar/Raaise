package com.raaise.android.Settings.Payments.DonationRaisedFolder;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.raaise.android.Adapters.VideoDonationHistoryAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.ApiModels.UserVideoDonationHistoryModel;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;

import java.util.ArrayList;
import java.util.List;

public class View_Donation_Video_Analytics extends AppCompatActivity {
    ImageView BackInView;
    VideoDonationHistoryAdapter adapter;
    RecyclerView VideoDonationHistoryRv;
    List<UserVideoDonationHistoryModel.Data> list;
    ApiManager apiManager = App.getApiManager();
    TextView AmountToBeClaimedInVideoAnalytics, TotalWithdrawalInVideoAnalytics, TotalRaisedInVideoAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_donation_video_analytics);
        Initialization();
        ClickListeners();
        HitApi();
    }

    private void HitApi() {
        UserVideoDonationHistoryModel model = new UserVideoDonationHistoryModel(getIntent().getStringExtra("HistoryVideoId"));
        apiManager.VideoDonationHistory(Prefs.GetBearerToken(View_Donation_Video_Analytics.this), model, new DataCallback<UserVideoDonationHistoryModel>() {
            @Override
            public void onSuccess(UserVideoDonationHistoryModel userVideoDonationHistoryModel) {

                TotalRaisedInVideoAnalytics.setText("$" + userVideoDonationHistoryModel.getRaisedDonationAmount());
                TotalWithdrawalInVideoAnalytics.setText("$" + userVideoDonationHistoryModel.getCompletedDonationAmount());
                AmountToBeClaimedInVideoAnalytics.setText("$" + userVideoDonationHistoryModel.getTotalDanotionAmount());
                list.addAll(userVideoDonationHistoryModel.getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(ServerError serverError) {
                Prompt.SnackBar(findViewById(android.R.id.content), serverError.getErrorMsg());
            }
        });
    }

    private void Initialization() {
        AmountToBeClaimedInVideoAnalytics = findViewById(R.id.AmountToBeClaimedInVideoAnalytics);
        TotalWithdrawalInVideoAnalytics = findViewById(R.id.TotalWithdrawalInVideoAnalytics);
        TotalRaisedInVideoAnalytics = findViewById(R.id.TotalRaisedInVideoAnalytics);
        list = new ArrayList<>();
        VideoDonationHistoryRv = findViewById(R.id.VideoDonationHistoryRv);
        VideoDonationHistoryRv.setHasFixedSize(true);
        VideoDonationHistoryRv.setLayoutManager(new LinearLayoutManager(View_Donation_Video_Analytics.this));
        BackInView = findViewById(R.id.BackInView);
        adapter = new VideoDonationHistoryAdapter(View_Donation_Video_Analytics.this, list);
        VideoDonationHistoryRv.setAdapter(adapter);
    }

    private void ClickListeners() {
        BackInView.setOnClickListener(view -> {
            onBackPressed();
        });
    }
}