package com.raaise.android.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.raaise.android.ApiManager.ApiModels.PublicUserVideoListModel;
import com.raaise.android.Home.Fragments.ShowOtherUserVideoActivity;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Prefs;

import java.util.List;

public class GetPublicUserVideoAdapter extends RecyclerView.Adapter<GetPublicUserVideoAdapter.ViewHolder> {
    Context context;
    List<PublicUserVideoListModel.Data> list;

    public GetPublicUserVideoAdapter(Context context, List<PublicUserVideoListModel.Data> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public GetPublicUserVideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GetPublicUserVideoAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.get_all_user_profile_single_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GetPublicUserVideoAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(Prefs.GetBaseUrl(context) + list.get(position).getVideoImage()).into(holder.videoView);
        holder.videoView.setOnClickListener(view -> {
            context.startActivity(new Intent(context, ShowOtherUserVideoActivity.class).putExtra("ListOfUserData", new Gson().toJson(list)).putExtra("PositionListOfUserData", String.valueOf(position)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        });
        holder.videoViewCount.setText(String.valueOf(list.get(position).getVideoViewCount()));
        if (list.get(position).isDonation()) {
            holder.DonationAmountLayout.setVisibility(View.VISIBLE);
            holder.DonationAmountInVideo.setText(String.format("%s.00", list.get(position).getDonationAmount()));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView videoView;
        TextView videoViewCount;
        LinearLayout DonationAmountLayout;
        TextView DonationAmountInVideo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            DonationAmountLayout = itemView.findViewById(R.id.DonationAmountLayout);
            DonationAmountInVideo = itemView.findViewById(R.id.DonationAmountInVideo);

            videoView = itemView.findViewById(R.id.VideoViewInGetAllUser);
            videoViewCount = itemView.findViewById(R.id.ViewsInGetAllUserVideo);
        }
    }
}
