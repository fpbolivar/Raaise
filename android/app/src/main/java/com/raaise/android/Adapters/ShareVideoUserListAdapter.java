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

public class ShareVideoUserListAdapter extends RecyclerView.Adapter<ShareVideoUserListAdapter.ViewHolder> {
    public Context mContext;
    public ChatListListener chatListListener;
    public List<ChatListModel.Data> chatListModels;

    public ShareVideoUserListAdapter(Context mContext, ChatListListener chatListListener, List<ChatListModel.Data> chatListModels) {
        this.mContext = mContext;
        this.chatListListener = chatListListener;
        this.chatListModels = chatListModels;
    }

    @NonNull
    @Override
    public ShareVideoUserListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_following_single_item, parent, false);
        return new ShareVideoUserListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShareVideoUserListAdapter.ViewHolder holder, int position) {
        ChatListModel.Data model = chatListModels.get(position);
        if (!Prefs.GetUserID(mContext).equalsIgnoreCase(model.getSenderId().get_id()) && model.getSenderId() != null) {
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
                    chatListListener.chatSelected(model.getSlug(), model.getSenderId().get_id(), model.getReceiverId().get_id(), model.getSenderId().getProfileImage(), model.getSenderId().getUserName(), model.getSenderId().getShortBio());
                }
            });
        } else {
            if (model.getReceiverId() != null) {
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
                        chatListListener.chatSelected(model.getSlug(), model.getReceiverId().get_id(), model.getSenderId().get_id(), model.getReceiverId().getProfileImage(), model.getReceiverId().getUserName(), model.getReceiverId().getShortBio());
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return chatListModels.size();
    }

    public interface ChatListListener {
        void chatSelected(String Slug, String ReceiverId, String SenderId, String UserImageLink, String Username, String ShortBio);
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
