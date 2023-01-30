package com.raaise.android.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.model.ChatListModel;

import java.util.List;

public class ShareUserAdapet extends RecyclerView.Adapter<ShareUserAdapet.ViewHolder> {
    public Context mContext;
    public ShareVideoListener chatListListener;
    public List<ChatListModel.Data> chatListModels;

    @NonNull
    @Override
    public ShareUserAdapet.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShareUserAdapet.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.follower_following_single_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShareUserAdapet.ViewHolder holder, int position) {
        ChatListModel.Data model = chatListModels.get(position);
        if (!Prefs.GetUserID(mContext).equalsIgnoreCase(model.getSenderId().get_id())) {
            Glide.with(holder.userProfileIV)
                    .load(model.getSenderId().getProfileImage())
                    .circleCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(holder.userProfileIV);
            holder.userNameTV.setText(model.getSenderId().getName());
            holder.Count.setText(model.getSenderId().getFollowersCount() + " following");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chatListListener.chatSelected(model.getSenderId().get_id(), model.getReceiverId().get_id(), model.getSenderId().getProfileImage(), model.getSenderId().getUserName(), model.getSenderId().getShortBio());
                }
            });
        } else {
            Glide.with(holder.userProfileIV)
                    .load(model.getReceiverId().getProfileImage())
                    .circleCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(holder.userProfileIV);
            holder.userNameTV.setText(model.getReceiverId().getName());
            holder.Count.setText(model.getReceiverId().getFollowersCount() + " following");
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chatListListener.chatSelected(model.getReceiverId().get_id(), model.getSenderId().get_id(), model.getReceiverId().getProfileImage(), model.getReceiverId().getUserName(), model.getReceiverId().getShortBio());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return chatListModels.size();
    }

    public interface ShareVideoListener {
        void chatSelected(String ReceiverId, String SenderId, String UserImageLink, String Username, String ShortBio);
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
