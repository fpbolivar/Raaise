package com.scripttube.android.ScriptTube.Adapters;

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
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.UserFollowingModel;
import com.scripttube.android.ScriptTube.Home.Fragments.OtherUserProfileActivity;
import com.scripttube.android.ScriptTube.R;

import java.util.List;

public class GetFolowingListAdapter extends RecyclerView.Adapter<GetFolowingListAdapter.ViewHolder> {
    List<UserFollowingModel.Data> list;
    Context context;

    public GetFolowingListAdapter(List<UserFollowingModel.Data> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public GetFolowingListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GetFolowingListAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.follower_following_single_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GetFolowingListAdapter.ViewHolder holder, int position) {
        UserFollowingModel.Data mo = list.get(position);
        try {

            Glide.with(context)
                    .load(mo.getFollowTo().getProfileImage())
                    .circleCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageViewInFF);
            holder.NameInFF.setText(mo.getFollowTo().getName());
            holder.FollowrCountInFF.setText(String.format("%s Following", String.valueOf(mo.getFollowTo().getFollowersCount())));
            holder.MainFollowListElement.setOnClickListener(view -> {
                context.startActivity(new Intent(context, OtherUserProfileActivity.class).putExtra("UserIdForProfile", mo.getFollowTo().get_id()).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
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
