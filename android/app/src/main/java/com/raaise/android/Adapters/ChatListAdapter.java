package com.raaise.android.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.HelperClass;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.model.ChatListModel;
import com.raaise.android.model.ChatUserID;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    public Context mContext;
    public ChatListListener chatListListener;
    public List<ChatListModel.Data> chatListModels;
    private ArrayList<String> chatUserIDS;

    public ChatListAdapter(Context mContext, ChatListListener chatListListener, List<ChatListModel.Data> chatListModels) {
        this.mContext = mContext;
        this.chatListListener = chatListListener;
        this.chatListModels = chatListModels;
        this.chatUserIDS = new ArrayList<>();
    }

    @NonNull
    @Override
    public ChatListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.ViewHolder holder, int position) {
        ChatListModel.Data model = chatListModels.get(position);

        if (!Prefs.GetUserID(mContext).equalsIgnoreCase(model.getSenderId().get_id()) && model.getSenderId() != null) {
            Glide.with(holder.userImage)
                    .load(Prefs.GetBaseUrl(mContext) + model.getSenderId().getProfileImage())
                    .circleCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(holder.userImage);
            holder.userName.setText(model.getSenderId().getName());
            holder.lastChat.setText((model.getLastMessage().length() > 20) ? (model.getLastMessage().substring(0, 19) + "...") : model.getLastMessage());
            if (model.getMessageUnReadCount() != 0) {
                holder.UnreadCount.setText(String.valueOf(model.getMessageUnReadCount()));
            } else {
                holder.UnreadCount.setVisibility(View.GONE);
            }
            HelperClass.calculateTime(Long.parseLong(model.getChatTime()));
            holder.lastChatTime.setText(HelperClass.calculateTime(Long.parseLong(model.getChatTime())));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chatListListener.chatSelected(model.getSlug(), model.getSenderId().get_id(), model.getReceiverId().get_id(), model.getSenderId().getProfileImage(), model.getSenderId().getUserName(), model.getSenderId().getShortBio());
                }
            });
        } else {
            if (model.getReceiverId() != null) {
                Glide.with(holder.userImage)
                        .load(Prefs.GetBaseUrl(mContext) + model.getReceiverId().getProfileImage())
                        .circleCrop()
                        .placeholder(R.drawable.placeholder)
                        .into(holder.userImage);
                holder.userName.setText(model.getReceiverId().getName());
                holder.lastChat.setText((model.getLastMessage().length() > 20) ? (model.getLastMessage().substring(0, 19) + "...") : model.getLastMessage());
                if (model.getMessageUnReadCount() != 0) {
                    holder.UnreadCount.setText(String.valueOf(model.getMessageUnReadCount()));
                } else {
                    holder.UnreadCount.setVisibility(View.GONE);
                }
                holder.lastChatTime.setText(HelperClass.calculateTime(Long.parseLong(model.getChatTime())));
                HelperClass.calculateTime(Long.parseLong(model.getChatTime()));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        chatListListener.chatSelected(model.getSlug(), model.getReceiverId().get_id(), model.getSenderId().get_id(), model.getReceiverId().getProfileImage(), model.getReceiverId().getUserName(), model.getReceiverId().getShortBio());
                    }
                });
            }
        }

        if (chatUserIDS.size() > 0){
            for (String userID : chatUserIDS){

                if (!Prefs.GetUserID(mContext).equalsIgnoreCase(model.getSenderId().get_id()) && model.getSenderId() != null){
                    if (userID.equals(model.senderId._id)){
                        holder.onlineIV.setVisibility(View.VISIBLE);
                        return;
                    } else {
                        holder.onlineIV.setVisibility(View.GONE);
                    }
                } else if (model.getReceiverId() != null){
                    if (userID.equals(model.receiverId._id)){
                        holder.onlineIV.setVisibility(View.VISIBLE);
                        return;
                    } else {
                        holder.onlineIV.setVisibility(View.GONE);
                    }
                }

            }
        }

    }

    @Override
    public int getItemCount() {
        return chatListModels.size();
    }

    public void updateChatList(ArrayList<ChatListModel.Data> chatListModelArrayList) {
        chatListModels = chatListModelArrayList;
        notifyDataSetChanged();
    }

    public interface ChatListListener {
        void chatSelected(String Slug, String ReceiverId, String SenderId, String UserImageLink, String Username, String ShortBio);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage, onlineIV;
        TextView userName;
        TextView lastChat, UnreadCount, lastChatTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lastChatTime = itemView.findViewById(R.id.lastChatTime);
            userName = itemView.findViewById(R.id.headingTV);
            UnreadCount = itemView.findViewById(R.id.UnreadCount);
            lastChat = itemView.findViewById(R.id.lastChatText);
            userImage = itemView.findViewById(R.id.user_image);
            onlineIV = itemView.findViewById(R.id.online_status);
        }
    }

    public void setChatIDS(ArrayList<String> ids){
        this.chatUserIDS.clear();
        this.chatUserIDS.addAll(ids);
        notifyDataSetChanged();
    }
}
