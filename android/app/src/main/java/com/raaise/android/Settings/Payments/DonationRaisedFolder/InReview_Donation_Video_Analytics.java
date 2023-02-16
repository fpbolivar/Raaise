package com.raaise.android.Settings.Payments.DonationRaisedFolder;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.raaise.android.Adapters.VideoHistoryAdapter;
import com.raaise.android.ApiManager.ApiManager;
import com.raaise.android.ApiManager.DataCallback;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.ApiManager.ServerError;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Dialogs;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.Utilities.HelperClasses.Prompt;
import com.raaise.android.model.ClaimedAmountPojo;
import com.raaise.android.model.VideoDonationModal;
import com.raaise.android.model.VideoDonationPojo;

import java.util.Calendar;


public class InReview_Donation_Video_Analytics extends AppCompatActivity {
    ImageView setting_in_review, BackInInReview;
    ApiManager apiManager = App.getApiManager();

    TextView totalPostsTV;
    TextView totalRaisedTV;
    TextView claimedAmtTV;
    TextView toBeClaimedTV;
    TextView claimNowBtn;
    RecyclerView videoHistoryRV;
    VideoHistoryAdapter adapter;
    String videoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_review_donation_video_analytics);
        Initialization();
        ClickListeners();
        Intent intent = getIntent();
        videoID = intent.getStringExtra("HistoryVideoId");

        VideoDonationModal modal = new VideoDonationModal(videoID);
        getData(modal);
        
        claimNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                claimNow(videoID);
            }
        });

    }

    private void claimNow(String videoID) {
        try {
            Dialogs.createProgressDialog(InReview_Donation_Video_Analytics.this);
            View parentLayout = findViewById(android.R.id.content);
            VideoDonationModal modal = new VideoDonationModal(videoID);
            apiManager.claimVideoAmount(Prefs.GetBearerToken(InReview_Donation_Video_Analytics.this), modal, new DataCallback<ClaimedAmountPojo>() {
                @Override
                public void onSuccess(ClaimedAmountPojo claimedAmountPojo) {
                    Dialogs.HideProgressDialog();
                    claimNowBtn.setVisibility(View.GONE);
                    App.claim = false;
                    Prompt.SnackBar(parentLayout, "Request sent successfully");
                }

                @Override
                public void onError(ServerError serverError) {
                    Dialogs.HideProgressDialog();
                    Toast.makeText(InReview_Donation_Video_Analytics.this, serverError.getErrorMsg(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e){
            Dialogs.HideProgressDialog();
            Toast.makeText(InReview_Donation_Video_Analytics.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void getData(VideoDonationModal modal) {
        try {
            Dialogs.showProgressDialog(InReview_Donation_Video_Analytics.this);
        } catch (Exception e){

        }

        apiManager.getVideoDonationHistory(Prefs.GetBearerToken(this), modal, new DataCallback<VideoDonationPojo>() {
            @Override
            public void onSuccess(VideoDonationPojo videoDonationPojo) {
                Dialogs.dismissProgressDialog();
                Log.i("apiResponse", "onSuccess: " + videoDonationPojo.getData().size());
                if (videoDonationPojo.getData() != null){
                    totalPostsTV.setText("" + videoDonationPojo.getData().size());
                }
                totalRaisedTV.setText("$" + videoDonationPojo.getRaisedDonationAmount());
                claimedAmtTV.setText("$" + videoDonationPojo.getCompletedDonationAmount());
                toBeClaimedTV.setText("$" + videoDonationPojo.getClaimedAmount());
                if (App.claim){
                    toBeClaimedTV.setTextColor(ContextCompat.getColor(InReview_Donation_Video_Analytics.this, R.color.Green));
                    claimNowBtn.setVisibility(View.VISIBLE);
                } 

                if (videoDonationPojo.getData() != null)
                    adapter.setList(videoDonationPojo.getData());

            }

            @Override
            public void onError(ServerError serverError) {
                Dialogs.dismissProgressDialog();
                Log.i("apiResponse", "onError: " + serverError.error);
            }
        });
    }

    private void Initialization() {
        setting_in_review = findViewById(R.id.setting_in_review);
        BackInInReview = findViewById(R.id.BackInInReview);
        totalPostsTV = findViewById(R.id.total_posts_tv);
        totalRaisedTV = findViewById(R.id.total_raised_tv);
        claimedAmtTV = findViewById(R.id.claimed_amt_tv);
        toBeClaimedTV = findViewById(R.id.to_be_claimed);
        videoHistoryRV = findViewById(R.id.video_history_rv);
        claimNowBtn = findViewById(R.id.claim_now_btn);
        adapter = new VideoHistoryAdapter(InReview_Donation_Video_Analytics.this);

        videoHistoryRV.setLayoutManager(new LinearLayoutManager(InReview_Donation_Video_Analytics.this));
        videoHistoryRV.setAdapter(adapter);

    }

    private void ClickListeners() {
        setting_in_review.setOnClickListener(view -> {
            ShowSearchUserDialog();
        });
        BackInInReview.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void ShowSearchUserDialog() {
        Dialog dialog = new Dialog(InReview_Donation_Video_Analytics.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.date_filter_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final Calendar myCalendar= Calendar.getInstance();
        TextView fromDateTV = dialog.findViewById(R.id.from_date_tv);
        TextView toDateTV = dialog.findViewById(R.id.to_date_tv);
        LinearLayout applyBtn = dialog.findViewById(R.id.CardSaveButton);

        fromDateTV.setOnClickListener(new View.OnClickListener() {
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
                        InReview_Donation_Video_Analytics.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our edit text.
                                fromDateTV.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
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

        toDateTV.setOnClickListener(new View.OnClickListener() {
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
                        InReview_Donation_Video_Analytics.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our edit text.
                                toDateTV.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
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

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fromDateTV.getText().toString().equals("") || toDateTV.getText().toString().equals("")){
                    Toast.makeText(InReview_Donation_Video_Analytics.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    VideoDonationModal modal = new VideoDonationModal(videoID, fromDateTV.getText().toString(), toDateTV.getText().toString());
                    getData(modal);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.setCancelable(true);
    }
}