package com.scripttube.android.ScriptTube.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.scripttube.android.ScriptTube.R;
import com.scripttube.android.ScriptTube.model.ChatListModel;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    private Context mContext;
    private ChatListListener chatListListener;
    private ArrayList<ChatListModel> chatListModels;

    public ChatListAdapter(Context mContext, ChatListListener listener) {
        this.mContext = mContext;
        this.chatListListener = listener;
        chatListModels = new ArrayList<>();
    }

    @NonNull
    @Override
    public ChatListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.ViewHolder holder, int position) {
        ChatListModel model = chatListModels.get(position);

        Glide.with(holder.userImage)
                        .load(R.drawable.delete_it)
                                .circleCrop()
                                        .into(holder.userImage);
        holder.userName.setText(model.chatPersonName);
        holder.lastChat.setText(model.lastChatMessage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatListListener.chatSelected();
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatListModels.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView userImage;
        TextView userName;
        TextView lastChat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.headingTV);
            lastChat = itemView.findViewById(R.id.lastChatText);
            userImage = itemView.findViewById(R.id.user_image);
        }
    }

    public void updateChatList(ArrayList<ChatListModel> chatListModelArrayList){
        chatListModels = chatListModelArrayList;
        notifyDataSetChanged();
    }

    public interface ChatListListener{
        void chatSelected();
    }
}
