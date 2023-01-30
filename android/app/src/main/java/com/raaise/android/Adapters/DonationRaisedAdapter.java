package com.raaise.android.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.raaise.android.ApiManager.ApiModels.UserDonationHistoryModel;
import com.raaise.android.R;
import com.raaise.android.Settings.Payments.DonationRaisedFolder.Claim_Donation_Video_Analytics;
import com.raaise.android.Settings.Payments.DonationRaisedFolder.InReview_Donation_Video_Analytics;
import com.raaise.android.Settings.Payments.DonationRaisedFolder.View_Donation_Video_Analytics;

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
        if (obj.getStatus().equalsIgnoreCase("view")) {
            holder.DonationRaisedButtonText.setText("View");
            holder.DonationRaisedButtonLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.view_donation_bg));
        } else if (obj.getStatus().equalsIgnoreCase("claim")) {
            holder.DonationRaisedButtonText.setText("Claim");
            holder.DonationRaisedButtonLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.claim_donation_bg));
        } else if (obj.getStatus().equalsIgnoreCase("inreview")) {
            holder.DonationRaisedButtonText.setText("In Review");
            holder.DonationRaisedButtonLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.inreview_donation_bg));
        }
        holder.DonationRaisedButtonLayout.setOnClickListener(view -> {
            if (obj.getStatus().equalsIgnoreCase("inreview")) {
                context.startActivity(new Intent(context, InReview_Donation_Video_Analytics.class).putExtra("HistoryVideoId", obj.get_id()));
            } else if (obj.getStatus().equalsIgnoreCase("claim")) {
                context.startActivity(new Intent(context, Claim_Donation_Video_Analytics.class).putExtra("HistoryVideoId", obj.get_id()));
            } else if (obj.getStatus().equalsIgnoreCase("view")) {
                context.startActivity(new Intent(context, View_Donation_Video_Analytics.class).putExtra("HistoryVideoId", obj.get_id()));
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
