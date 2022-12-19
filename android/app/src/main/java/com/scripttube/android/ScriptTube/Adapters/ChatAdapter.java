package com.scripttube.android.ScriptTube.Adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.scripttube.android.ScriptTube.R;
import com.scripttube.android.ScriptTube.model.ChatModel;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private Context mContext;
    ArrayList<ChatModel> chatModels;

    public ChatAdapter(Context mContext) {
        this.mContext = mContext;
        chatModels = new ArrayList<>();
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        ChatModel chatModel = chatModels.get(position);

        holder.bgLayoutLinear.setBackground(chatModel.fromSender ? ContextCompat.getDrawable(mContext, R.drawable.chat_sender_bg) : ContextCompat.getDrawable(mContext, R.drawable.chat_receiver_bg));

        holder.layout.setGravity(chatModel.fromSender ? Gravity.LEFT : Gravity.END);
        holder.chatText.setText(chatModel.message);
        holder.chatTime.setText(chatModel.time);
        holder.chatTime.setGravity(chatModel.fromSender ? Gravity.LEFT : Gravity.END);
        holder.chatSenderIV.setVisibility(chatModel.fromSender ? View.VISIBLE : View.GONE);
        if (chatModel.fromSender){
            Glide.with(holder.chatSenderIV)
                    .load(ContextCompat.getDrawable(mContext, R.drawable.delete_it))
                    .circleCrop()
                    .into(holder.chatSenderIV);
        }
    }

    @Override
    public int getItemCount() {
        return chatModels.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView chatSenderIV;
        TextView chatText;
        LinearLayout layout;
        TextView chatTime;
        LinearLayout bgLayoutLinear;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chatSenderIV = itemView.findViewById(R.id.chat_person_iv);
            chatText = itemView.findViewById(R.id.chatTextTV);
            layout = itemView.findViewById(R.id.constraintL);
            chatTime = itemView.findViewById(R.id.chatTimeTV);
            bgLayoutLinear = itemView.findViewById(R.id.bgLayout);
        }
    }

    public void updateChats(ArrayList<ChatModel> chatModelArrayList) {
        chatModels = chatModelArrayList;
        notifyDataSetChanged();
    }
}
