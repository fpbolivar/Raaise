package com.raaise.android.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raaise.android.ApiManager.ApiModels.UserVideoDonationHistoryModel;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.HelperClass;

import java.util.List;

public class VideoDonationHistoryAdapter extends RecyclerView.Adapter<VideoDonationHistoryAdapter.ViewHolder> {
    Context context;
    List<UserVideoDonationHistoryModel.Data> list;

    public VideoDonationHistoryAdapter(Context context, List<UserVideoDonationHistoryModel.Data> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public VideoDonationHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoDonationHistoryAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.video_donation_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoDonationHistoryAdapter.ViewHolder holder, int position) {
        UserVideoDonationHistoryModel.Data obj = list.get(position);
        holder.PendingTvVideoHistory.setText(obj.getUserAmount());
        holder.RaisedTvVideoHistory.setText(obj.getAmount());
//        holder.DateTvVideoHistory.setText(HelperClass.findDifference(obj.getCreatedAt(),
//                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date())));
//        holder.DateTvVideoHistory.setText(obj.getCreatedAt());
        holder.DateTvVideoHistory.setText(HelperClass.getMonthAndDay(obj.getCreatedAt()));
        holder.UsernameTvVideoHistory.setText(obj.getDonatedBy().getUserName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView PendingTvVideoHistory, RaisedTvVideoHistory, DateTvVideoHistory, UsernameTvVideoHistory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            PendingTvVideoHistory = itemView.findViewById(R.id.PendingTvVideoHistory);
            RaisedTvVideoHistory = itemView.findViewById(R.id.RaisedTvVideoHistory);
            DateTvVideoHistory = itemView.findViewById(R.id.DateTvVideoHistory);
            UsernameTvVideoHistory = itemView.findViewById(R.id.UsernameTvVideoHistory);
        }
    }
}
