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
import com.raaise.android.ApiManager.ApiModels.UserFollowersModel;
import com.raaise.android.Home.Fragments.OtherUserProfileActivity;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Prefs;

import java.util.List;

public class FollowerFolowingAdapter extends RecyclerView.Adapter<FollowerFolowingAdapter.ViewHolder> {
    Context context;
    List<UserFollowersModel.Data> list;

    public FollowerFolowingAdapter(Context context, List<UserFollowersModel.Data> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FollowerFolowingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FollowerFolowingAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.follower_following_single_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FollowerFolowingAdapter.ViewHolder holder, int position) {
        UserFollowersModel.Data mo = list.get(position);
        try {

            Glide.with(context)
                    .load(Prefs.GetBaseUrl(context) + mo.getFollowedBy().getProfileImage())
                    .circleCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageViewInFF);
            holder.NameInFF.setText(mo.getFollowedBy().getName());
            holder.FollowrCountInFF.setText(mo.getFollowedBy().getFollowersCount() + " followers");

            holder.MainFollowListElement.setOnClickListener(view -> {
                context.startActivity(new Intent(context, OtherUserProfileActivity.class).putExtra("UserIdForProfile", mo.getFollowedBy().get_id()).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            });
        } catch (Exception e) {

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewInFF;
        TextView NameInFF, FollowrCountInFF;
        RelativeLayout MainFollowListElement;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewInFF = itemView.findViewById(R.id.imageViewInFF);
            NameInFF = itemView.findViewById(R.id.NameInFF);
            FollowrCountInFF = itemView.findViewById(R.id.FollowrCountInFF);
            MainFollowListElement = itemView.findViewById(R.id.MainFollowListElement);
        }
    }
}
