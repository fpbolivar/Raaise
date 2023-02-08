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
import com.raaise.android.ApiManager.ApiModels.PublicUserFollowingModel;
import com.raaise.android.Home.Fragments.OtherUserProfileActivity;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Prefs;

import java.util.List;

public class PublicUserFollowingAdapter extends RecyclerView.Adapter<PublicUserFollowingAdapter.ViewHolder> {
    List<PublicUserFollowingModel.Data> list;
    Context context;

    public PublicUserFollowingAdapter(List<PublicUserFollowingModel.Data> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public PublicUserFollowingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PublicUserFollowingAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.follower_following_single_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PublicUserFollowingAdapter.ViewHolder holder, int position) {
        PublicUserFollowingModel.Data mo = list.get(position);
        try {

            Glide.with(context)
                    .load(Prefs.GetBaseUrl(context) + mo.getFollowTo().getProfileImage())
                    .circleCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(holder.imageViewInFF);
            holder.NameInFF.setText(mo.getFollowTo().getName());
            holder.FollowrCountInFF.setText(String.format("%s Following", mo.getFollowTo().getFollowersCount()));
            holder.MainFollowListElement.setOnClickListener(view -> {
                context.startActivity(new Intent(context, OtherUserProfileActivity.class).putExtra("UserIdForProfile", mo.getFollowTo().get_id()).putExtra("UserNameForProfile", mo.getFollowTo().getUserName()).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
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
