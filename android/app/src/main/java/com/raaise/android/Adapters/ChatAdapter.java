package com.raaise.android.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.raaise.android.Home.Fragments.OtherUserProfileActivity;
import com.raaise.android.Home.Fragments.SingleVideoActivity;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.HelperClass;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.model.ChatModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    Context mContext;
    List<ChatModel.Data> chatModels;
    ChatListener chatListener;

    public ChatAdapter(Context mContext, List<ChatModel.Data> chatModels, ChatListener chatListener) {
        this.mContext = mContext;
        this.chatModels = chatModels;
        this.chatListener = chatListener;
    }


    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        ChatModel.Data chatModel = chatModels.get(position);
        Log.i("chatMsg", "onBindViewHolder: " + chatModel.getMessage());
        holder.bgLayoutLinear.setBackground(!chatModel.getSenderId().getUserName().equalsIgnoreCase(Prefs.getUserName(mContext)) ? ContextCompat.getDrawable(mContext, R.drawable.chat_sender_bg) : ContextCompat.getDrawable(mContext, R.drawable.chat_receiver_bg));
        holder.layout.setGravity(!chatModel.getSenderId().getUserName().equalsIgnoreCase(Prefs.getUserName(mContext)) ? Gravity.START : Gravity.END);
        holder.chatTime.setGravity(!chatModel.getSenderId().getUserName().equalsIgnoreCase(Prefs.getUserName(mContext)) ? Gravity.START : Gravity.END);
        holder.chatTime.setText(HelperClass.findDifference(chatModel.getCreatedAt(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date())));
        holder.chatText.setText(chatModel.message);
        holder.chatSenderIV.setVisibility(!chatModel.getSenderId().getUserName().equalsIgnoreCase(Prefs.getUserName(mContext)) ? View.VISIBLE : View.GONE);
        if (!chatModel.getSenderId().getUserName().equalsIgnoreCase(Prefs.getUserName(mContext))) {
            Glide.with(holder.chatSenderIV)
                    .load(Prefs.GetBaseUrl(mContext) + chatModel.getSenderId().getProfileImage())
                    .circleCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(holder.chatSenderIV);
        }
        holder.chatSenderIV.setOnClickListener(view -> {
            mContext.startActivity(new Intent(view.getContext(), OtherUserProfileActivity.class).putExtra("UserIdForProfile", chatModel.getSenderId().get_id()).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        });
        if (chatModel.getMessageType().equalsIgnoreCase("link")) {
            holder.MediaVisibilityCard.setVisibility(View.VISIBLE);
            holder.chatText.setVisibility(View.GONE);

        } else if (chatModel.getMessageType().equalsIgnoreCase("text")) {
            holder.MediaVisibilityCard.setVisibility(View.GONE);
            holder.chatText.setVisibility(View.VISIBLE);
        }
        if (chatModel.getMessageType().equalsIgnoreCase("link") && chatModel.getVideoId() != null) {
            Glide.with(holder.chatImage)
                    .load(Prefs.GetBaseUrl(mContext) + chatModel.getVideoId().getVideoImage())
                    .override(500, 500)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.chatImage);
        }
        holder.itemView.setOnClickListener(view -> {
            if (chatModel.getMessageType().equalsIgnoreCase("link")) {
                mContext.startActivity(new Intent(mContext, SingleVideoActivity.class).putExtra("SlugForSingleVideo", chatModel.getVideoId().getSlug()));
            }
        });
        holder.DeleteMessageIcon.setOnClickListener(view -> {
            holder.DeleteMessageIcon.setVisibility(View.GONE);
            chatListener.DeleteMessage(chatModel.get_id(), chatModel.getChatSlug());
        });
        if (chatModel.isDeleteVisible()) {
            holder.DeleteMessageIcon.setVisibility(View.VISIBLE);
        } else {
            holder.DeleteMessageIcon.setVisibility(View.GONE);
        }
        holder.itemView.setOnLongClickListener(view -> {

            if (chatModel.getSenderId().getUserName().equalsIgnoreCase(Prefs.getUserName(mContext))) {

                if (chatModel.getSenderId().getUserName().equalsIgnoreCase(Prefs.getUserName(mContext))) {
                    if (holder.DeleteMessageIcon.getVisibility() == View.VISIBLE) {
                        holder.DeleteMessageIcon.setVisibility(View.GONE);
                        chatModel.setDeleteVisible(false);
                        notifyDataSetChanged();
                    } else {
                        holder.DeleteMessageIcon.setVisibility(View.VISIBLE);
                        chatModel.setDeleteVisible(true);
                        notifyDataSetChanged();
                    }
                }
            }

            return false;
        });
    }

    @Override
    public int getItemCount() {
        return chatModels.size();
    }

    public interface ChatListener {
        void DeleteMessage(String MessageId, String ChatSlug);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView chatSenderIV, chatImage;
        CardView DeleteMessageIcon;
        TextView chatText;
        RelativeLayout layout;
        TextView chatTime;
        LinearLayout bgLayoutLinear;
        CardView MediaVisibilityCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            DeleteMessageIcon = itemView.findViewById(R.id.DeleteMessageIcon);
            MediaVisibilityCard = itemView.findViewById(R.id.MediaVisibilityCard);
            chatImage = itemView.findViewById(R.id.chatImage);
            chatSenderIV = itemView.findViewById(R.id.chat_person_iv);
            chatText = itemView.findViewById(R.id.chatTextTV);
            layout = itemView.findViewById(R.id.constraintL);
            chatTime = itemView.findViewById(R.id.chatTimeTV);
            bgLayoutLinear = itemView.findViewById(R.id.bgLayout);
        }
    }

}
