package com.raaise.android.Adapters;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.HelperClass;
import com.raaise.android.model.VideoDonationPojo;

import java.util.ArrayList;
import java.util.Calendar;

public class VideoHistoryAdapter extends RecyclerView.Adapter<VideoHistoryAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<VideoDonationPojo.VideoData> videoData;

    public VideoHistoryAdapter(Context mContext) {
        this.mContext = mContext;
        this.videoData = new ArrayList<>();
    }

    @NonNull
    @Override
    public VideoHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.video_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHistoryAdapter.ViewHolder holder, int position) {
        VideoDonationPojo.VideoData data = videoData.get(position);

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
//        ZonedDateTime zonedDateTime = ZonedDateTime.parse(data.getDate().replace("T", " "), formatter);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(data.getDate()));


            String date = HelperClass.getMonthForInt(calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.DATE) +
                    ", " + calendar.get(Calendar.YEAR) + ", ";
            String postTime = calendar.get(Calendar.HOUR) > 12 ? "PM" : "AM";
            String time = calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + postTime;

            holder.videoDateTV.setText(date + time);

        } catch (Exception e){
            Log.i("dateTime", "onBindViewHolder: " + e.getMessage());
        }
//        String date = String.valueOf(zonedDateTime);

        holder.raisedAmtTV.setText("$" + data.getRaisedAmount());
        holder.pendingAmtTV.setText("$" + data.getPendingAmount());
        holder.userNameTV.setText(data.getName());
    }

    @Override
    public int getItemCount() {
        Log.i("getItemCount", "getItemCount: " + videoData.size());
        return videoData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView videoDateTV;
        TextView raisedAmtTV;
        TextView pendingAmtTV;
        TextView userNameTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            videoDateTV = itemView.findViewById(R.id.video_date_tv);
            raisedAmtTV = itemView.findViewById(R.id.raised_amt_tv);
            pendingAmtTV = itemView.findViewById(R.id.pending_amt_tv);
            userNameTV = itemView.findViewById(R.id.user_name_tv);
        }
    }

    public void setList(ArrayList<VideoDonationPojo.VideoData> list){
        this.videoData = list;
        notifyDataSetChanged();
    }

}
