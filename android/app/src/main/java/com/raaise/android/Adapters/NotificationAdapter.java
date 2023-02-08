package com.raaise.android.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.raaise.android.ApiManager.ApiModels.GetUserNotificationsModel;
import com.raaise.android.Home.Fragments.OtherUserProfileActivity;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.HelperClass;
import com.raaise.android.Utilities.HelperClasses.Prefs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    Context context;
    List<GetUserNotificationsModel.NotificationMessage> list;
    notificationListener listener;

    public NotificationAdapter(Context context, List<GetUserNotificationsModel.NotificationMessage> list, notificationListener mListener) {
        this.context = context;
        this.list = list;
        this.listener = mListener;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.notification_single_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        GetUserNotificationsModel.NotificationMessage model = list.get(position);
        Glide.with(context).load(Prefs.GetBaseUrl(context) + model.getFromProfileImage()).placeholder(R.drawable.placeholder).into(holder.NotificationImage);

        String s = model.getFromUserName() + " " + model.getMessage();
        SpannableString ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1f), 0, model.getFromUserName().length(), 0); // set size
        ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#D3D3D3")), 0, model.getFromUserName().length(), 0);// set color

        ss1.setSpan(new RelativeSizeSpan(0.8f), model.getFromUserName().length() + 1, s.length(), 0); // set size
        ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#797979")), model.getFromUserName().length() + 1, s.length(), 0);
        holder.NotificationName.setText(ss1);

        holder.NotificationDate.setText(HelperClass.findDifference(model.getCreatedAt(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date())));
        if (!model.isRead) {
            holder.Catcher.setVisibility(View.VISIBLE);
        } else {
            holder.Catcher.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(view -> {
            HelperClass.ReadNotification(model.getId(), context);
            model.setRead(true);
            notifyDataSetChanged();
            if (!model.getSlug().equalsIgnoreCase("") && (model.getType().equalsIgnoreCase("add-video")
                    || model.getType().equalsIgnoreCase("comment-video")
                    || model.getType().equalsIgnoreCase("comment-reply")
                    || model.getType().equalsIgnoreCase("payment-donate")
                    || model.getType().equalsIgnoreCase("like-video"))) {
                listener.showVideo(model.getSlug());
            } else if ((!model.getSlug().equalsIgnoreCase("") && model.getType().equalsIgnoreCase("user-follow"))) {
                context.startActivity(new Intent(context, OtherUserProfileActivity.class).putExtra("UserIdForProfile", model.getSlug()).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }

        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void markAllAsRead() {
        for (int i = 0; i < list.size(); i++){
            if (!list.get(i).isRead){
                list.get(i).setRead(true);
                HelperClass.ReadNotification(list.get(i).getId(), context);
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView NotificationImage;
        TextView NotificationName, NotificationDate;
        CardView Catcher;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Catcher = itemView.findViewById(R.id.Catcher);
            NotificationImage = itemView.findViewById(R.id.NotificationImage);
            NotificationName = itemView.findViewById(R.id.NotificationName);
            NotificationDate = itemView.findViewById(R.id.NotificationDate);
        }
    }

    public interface notificationListener{
        void showVideo(String slug);
    }
}
