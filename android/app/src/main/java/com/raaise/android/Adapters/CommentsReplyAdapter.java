package com.raaise.android.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.raaise.android.ApiManager.ApiModels.ListOfVideoCommentsModel;
import com.raaise.android.Home.Fragments.OtherUserProfileActivity;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.HelperClass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentsReplyAdapter extends RecyclerView.Adapter<CommentsReplyAdapter.ViewHolder> {
    Context context;
    List<ListOfVideoCommentsModel.Data.ReplyId> list;

    public CommentsReplyAdapter(Context context, List<ListOfVideoCommentsModel.Data.ReplyId> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CommentsReplyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentsReplyAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.comment_reply_single_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsReplyAdapter.ViewHolder holder, int position) {
        ListOfVideoCommentsModel.Data.ReplyId model = list.get(position);
        holder.CommentReply.setText(model.getReply());


        holder.NameCommentReply.setOnClickListener(view -> DoOpenUserProfile(model));
        holder.commentatorReplyImage.setOnClickListener(view -> DoOpenUserProfile(model));
        holder.Time.setText(HelperClass.findDifference(model.getCreatedAt(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date())));
        if (model.getReplyBy() != null) {
            holder.NameCommentReply.setText(model.getReplyBy().getUserName());
            Glide.with(holder.commentatorReplyImage)
                    .load(model.getReplyBy().getProfileImage())
                    .placeholder(R.drawable.placeholder)
                    .circleCrop()
                    .into(holder.commentatorReplyImage);
        }

    }

    private void DoOpenUserProfile(ListOfVideoCommentsModel.Data.ReplyId obj) {
        context.startActivity(new Intent(context, OtherUserProfileActivity.class).putExtra("UserIdForProfile", obj.getReplyBy().get_id()).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView CommentReply, NameCommentReply, Time;
        ImageView commentatorReplyImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commentatorReplyImage = itemView.findViewById(R.id.commentatorReplyImage);
            NameCommentReply = itemView.findViewById(R.id.NameCommentReply);
            CommentReply = itemView.findViewById(R.id.CommentReply);
            Time = itemView.findViewById(R.id.time);
        }
    }
}
