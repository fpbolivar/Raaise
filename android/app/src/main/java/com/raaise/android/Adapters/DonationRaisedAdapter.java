package com.raaise.android.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.raaise.android.ApiManager.ApiModels.UserDonationHistoryModel;
import com.raaise.android.ApiManager.RetrofitHelper.App;
import com.raaise.android.R;
import com.raaise.android.Settings.Payments.DonationRaisedFolder.InReview_Donation_Video_Analytics;

import java.util.List;

public class DonationRaisedAdapter extends RecyclerView.Adapter<DonationRaisedAdapter.ViewHolder> {
    Context context;
    List<UserDonationHistoryModel.Data.UserVideo> list;

    public DonationRaisedAdapter(Context context, List<UserDonationHistoryModel.Data.UserVideo> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DonationRaisedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DonationRaisedAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.donation_history_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DonationRaisedAdapter.ViewHolder holder, int position) {
        UserDonationHistoryModel.Data.UserVideo obj = list.get(position);
        holder.VideoNameText.setText((obj.getVideoCaption().length() > 20) ? (obj.getVideoCaption().substring(0, 19) + "...") : obj.getVideoCaption());
        holder.VideoDateTimeText.setText(obj.getCreatedAt());
        if (obj.getStatus().equalsIgnoreCase("View")) {
            holder.DonationRaisedButtonText.setText("View");
            holder.DonationRaisedButtonLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.view_donation_bg));
        } else if (obj.getStatus().equalsIgnoreCase("Claim")) {
            holder.DonationRaisedButtonText.setText("Claim");
            holder.DonationRaisedButtonLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.claim_donation_bg));
        } else if (obj.getStatus().equalsIgnoreCase("In Review")) {
            holder.DonationRaisedButtonText.setText("In Review");
            holder.DonationRaisedButtonLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.inreview_donation_bg));
        }
        Log.i("inReviewIDS", "onBindViewHolder: " + obj.getStatus());
        holder.DonationRaisedButtonLayout.setOnClickListener(view -> {
            Log.i("claimReward", "onBindViewHolder: " + obj.getStatus());
            if (obj.getStatus().equalsIgnoreCase("In Review")) {
                context.startActivity(new Intent(context, InReview_Donation_Video_Analytics.class).putExtra("HistoryVideoId", obj.get_id()));
            } else if (obj.getStatus().equalsIgnoreCase("Claim")) {
                Log.i("claimReward", "onBindViewHolder: Gone claim");
                App.claim = true;
                context.startActivity(new Intent(context, InReview_Donation_Video_Analytics.class).putExtra("HistoryVideoId", obj.get_id()));
            } else if (obj.getStatus().equalsIgnoreCase("view")) {
                Log.i("objectID", "onBindViewHolder: " + obj.get_id());
                context.startActivity(new Intent(context, InReview_Donation_Video_Analytics.class).putExtra("HistoryVideoId", obj.get_id()));
            }


        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView VideoNameText, VideoDateTimeText, DonationRaisedButtonText;
        LinearLayout DonationRaisedButtonLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            VideoNameText = itemView.findViewById(R.id.VideoNameText);
            VideoDateTimeText = itemView.findViewById(R.id.VideoDateTimeText);
            DonationRaisedButtonText = itemView.findViewById(R.id.DonationRaisedButtonText);
            DonationRaisedButtonLayout = itemView.findViewById(R.id.DonationRaisedButtonLayout);
        }
    }
}
