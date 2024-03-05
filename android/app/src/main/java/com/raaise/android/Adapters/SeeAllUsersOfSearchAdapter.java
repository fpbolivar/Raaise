package com.raaise.android.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.raaise.android.ApiManager.ApiModels.GlobalSearchModel;
import com.raaise.android.Home.Fragments.OtherUserProfileActivity;
import com.raaise.android.R;

import java.util.List;

public class SeeAllUsersOfSearchAdapter extends RecyclerView.Adapter<SeeAllUsersOfSearchAdapter.ViewHolder> {
    Context context;
    List<GlobalSearchModel.Data.Users> list;

    public SeeAllUsersOfSearchAdapter(Context context, List<GlobalSearchModel.Data.Users> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SeeAllUsersOfSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SeeAllUsersOfSearchAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.follower_following_single_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SeeAllUsersOfSearchAdapter.ViewHolder holder, int position) {
        GlobalSearchModel.Data.Users obj = list.get(position);
        holder.Count.setText(obj.getFollowersCount() + " followers");
        holder.userNameTV.setText(obj.getName());
        Glide.with(context).load(obj.getProfileImage()).placeholder(R.drawable.placeholder).into(holder.userProfileIV);
        holder.MainFollowListElement.setOnClickListener(view -> {
            context.startActivity(new Intent(context, OtherUserProfileActivity.class).putExtra("UserIdForProfile", obj.get_id()).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userProfileIV;
        TextView userNameTV, Count;
        RelativeLayout MainFollowListElement;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            MainFollowListElement = itemView.findViewById(R.id.MainFollowListElement);
            userProfileIV = itemView.findViewById(R.id.imageViewInFF);
            userNameTV = itemView.findViewById(R.id.NameInFF);
            Count = itemView.findViewById(R.id.FollowrCountInFF);
        }
    }
}
