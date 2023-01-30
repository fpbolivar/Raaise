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
import com.raaise.android.ApiManager.ApiModels.GlobalSearchModel;
import com.raaise.android.Home.Fragments.ViewSeeAllVideosFromSearchAcitivty;
import com.raaise.android.R;

import java.util.List;

public class SearchScreenDummyVideoListAdapter extends RecyclerView.Adapter<SearchScreenDummyVideoListAdapter.ViewHolder> {
    Context context;
    List<GlobalSearchModel.Data.Posts> list;

    public SearchScreenDummyVideoListAdapter(Context context, List<GlobalSearchModel.Data.Posts> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.get_all_user_profile_single_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GlobalSearchModel.Data.Posts obj = list.get(position);
        Glide.with(context).load(obj.getVideoImage()).placeholder(R.drawable.placeholder).into(holder.postViewIV);
        holder.viewCountTV.setText(String.valueOf(obj.getVideoViewCount()));
        holder.postViewIV.setOnClickListener(view -> {
            context.startActivity(new Intent(context, ViewSeeAllVideosFromSearchAcitivty.class).putExtra("ListOfSeeAllVideosPlaying", new Gson().toJson(list)).putExtra("PositionListOfSeeAllVideosPlaying", String.valueOf(position)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        });
        if (obj.isDonation()) {
            holder.DonationAmountLayout.setVisibility(View.VISIBLE);
            holder.DonationAmountInVideo.setText(String.format("%s.00", obj.getDonationAmount()));
        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView viewCountTV;
        ImageView postViewIV;
        LinearLayout DonationAmountLayout;
        TextView DonationAmountInVideo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            DonationAmountLayout = itemView.findViewById(R.id.DonationAmountLayout);
            DonationAmountInVideo = itemView.findViewById(R.id.DonationAmountInVideo);

            viewCountTV = itemView.findViewById(R.id.ViewsInGetAllUserVideo);
            postViewIV = itemView.findViewById(R.id.VideoViewInGetAllUser);
        }
    }
}
