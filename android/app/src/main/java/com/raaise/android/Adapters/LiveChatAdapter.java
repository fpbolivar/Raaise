package com.raaise.android.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.model.LiveChatData;
import com.raaise.android.model.LiveRoomChat;

import java.util.ArrayList;
import java.util.Collections;

public class LiveChatAdapter extends RecyclerView.Adapter<LiveChatAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> liveChats;

    public LiveChatAdapter(Context context){
        this.mContext = context;
        this.liveChats = new ArrayList<>();
    }

    @NonNull
    @Override
    public LiveChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_chat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LiveChatAdapter.ViewHolder holder, int position) {
        LiveChatData chatData = new Gson().fromJson(liveChats.get(position), LiveChatData.class);

        holder.liveChatTV.setText(chatData.message);
        holder.senderNameTV.setText(chatData.userName + " :");
        Glide.with(holder.senderIV)
                .load(Prefs.GetBaseUrl(mContext) + chatData.userProfileImage)
                .placeholder(R.drawable.placeholder)
                .into(holder.senderIV);


    }

    @Override
    public int getItemCount() {
        return liveChats.size();
    }

    public void addChat(String message) {
        if (liveChats.isEmpty()){
            liveChats.add(message);
            notifyDataSetChanged();
        } else {
            liveChats.add(liveChats.size(), message);
            notifyDataSetChanged();
        }

    }

    public void setList(ArrayList<LiveRoomChat.ChatData> chatData) {
        ArrayList<String> strings = new ArrayList<>();
        for (LiveRoomChat.ChatData data : chatData){
            LiveChatData liveChatData = new LiveChatData(data.senderId.profileImage, data.senderId.userName, data.message);
            strings.add(new Gson().toJson(liveChatData));
        }
        Collections.reverse(strings);
        this.liveChats.addAll(strings);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView senderIV;
        TextView senderNameTV;
        TextView liveChatTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            liveChatTV = itemView.findViewById(R.id.live_chat_tv);
            senderIV = itemView.findViewById(R.id.sender_img);
            senderNameTV = itemView.findViewById(R.id.sender_name);
        }
    }
}
