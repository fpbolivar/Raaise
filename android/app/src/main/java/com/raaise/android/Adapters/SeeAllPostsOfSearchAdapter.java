package com.raaise.android.Adapters;

import android.annotation.SuppressLint;
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
import com.raaise.android.ApiManager.ApiModels.GlobalSearchModel;
import com.raaise.android.Home.Fragments.ViewSeeAllVideosFromSearchAcitivty;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Prefs;

import java.util.List;

public class SeeAllPostsOfSearchAdapter extends RecyclerView.Adapter<SeeAllPostsOfSearchAdapter.ViewHolder> {
    Context context;
    List<GlobalSearchModel.Data.Posts> list;

    public SeeAllPostsOfSearchAdapter(Context context, List<GlobalSearchModel.Data.Posts> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SeeAllPostsOfSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SeeAllPostsOfSearchAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.get_all_user_profile_single_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SeeAllPostsOfSearchAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(Prefs.GetBaseUrl(context) + list.get(position).getVideoImage()).into(holder.videoView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ViewSeeAllVideosFromSearchAcitivty.class).putExtra("ListOfSeeAllVideosPlaying", new Gson().toJson(list)).putExtra("PositionListOfSeeAllVideosPlaying", String.valueOf(position)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        holder.ViewsInGetAllUserVideo.setText(String.valueOf(0));
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
        TextView ViewsInGetAllUserVideo;
        LinearLayout DonationAmountLayout;
        TextView DonationAmountInVideo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            DonationAmountLayout = itemView.findViewById(R.id.DonationAmountLayout);
            DonationAmountInVideo = itemView.findViewById(R.id.DonationAmountInVideo);

            ViewsInGetAllUserVideo = itemView.findViewById(R.id.ViewsInGetAllUserVideo);
            videoView = itemView.findViewById(R.id.VideoViewInGetAllUser);
        }
    }
}
