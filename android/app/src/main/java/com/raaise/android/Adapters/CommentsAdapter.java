package com.raaise.android.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
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

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    public static String time;
    private final RecyclerView.RecycledViewPool
            viewPool
            = new RecyclerView
            .RecycledViewPool();
    private final Context mContext;
    private final List<ListOfVideoCommentsModel.Data> comments;
    boolean visible = false;
    CommentReplyListener listener;

    public CommentsAdapter(Context mContext, List<ListOfVideoCommentsModel.Data> comments, CommentReplyListener listener) {
        this.mContext = mContext;
        this.comments = comments;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder holder, int position) {
        ListOfVideoCommentsModel.Data Model = comments.get(position);
        Glide.with(holder.userIV)
                .load(Model.getCommentBy().getProfileImage())
                .placeholder(R.drawable.placeholder)
                .circleCrop()
                .into(holder.userIV);
        holder.ViewRepliesInComments.setText(String.format("View replies (%d)", comments.get(position).getReplyId().size()));
        holder.commentatorName.setText(Model.getCommentBy().getUserName());
        holder.commentatorText.setText(comments.get(position).getComment());

        holder.Time.setText(HelperClass.findDifference(comments.get(position).getCreatedAt(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date())));
        time = String.valueOf(holder.Time.getText());

        if (comments.get(position).getReplyId().size() != 0) {
            holder.ViewRepliesInComments.setVisibility(View.VISIBLE);
            holder.ReplyInCommentsRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 1));
            CommentsReplyAdapter adapter = new CommentsReplyAdapter(holder.ReplyInCommentsRecyclerView.getContext(), comments.get(position).getReplyId());
            holder.ReplyInCommentsRecyclerView.setAdapter(adapter);
            holder.ReplyInCommentsRecyclerView.setRecycledViewPool(viewPool);
        } else {
            holder.ViewRepliesInComments.setVisibility(View.GONE);
        }
        if (comments.get(position).getReplyId().size() != 0) {
            holder.ViewRepliesInComments.setOnClickListener(view -> {
                if (!visible) {
                    visible = true;
                    holder.ViewRepliesInComments.setText(String.format("Hide replies (%d)", comments.get(position).getReplyId().size()));
                    holder.ReplyInCommentsRecyclerView.setVisibility(View.VISIBLE);
                } else {
                    visible = false;
                    holder.ViewRepliesInComments.setText(String.format("View replies (%d)", comments.get(position).getReplyId().size()));
                    holder.ReplyInCommentsRecyclerView.setVisibility(View.GONE);
                }
            });
        }
        holder.ReplyComment.setOnClickListener(view -> listener.ShowReplySheet(Model.getCommentBy().getUserName(), comments.get(position).get_id()));
        holder.userIV.setOnClickListener(view -> DoOpenUserProfile(Model));
        holder.commentatorName.setOnClickListener(view -> DoOpenUserProfile(Model));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    private void DoOpenUserProfile(ListOfVideoCommentsModel.Data obj) {
        mContext.startActivity(new Intent(mContext, OtherUserProfileActivity.class).putExtra("UserIdForProfile", obj.getCommentBy().get_id()).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public interface CommentReplyListener {
        void ShowReplySheet(String Name, String CommentId);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userIV;
        TextView commentatorName, Time, ReplyComment;
        TextView commentatorText, ViewRepliesInComments;
        RecyclerView ReplyInCommentsRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Time = itemView.findViewById(R.id.Time);
            ReplyComment = itemView.findViewById(R.id.ReplyComment);
            ReplyInCommentsRecyclerView = itemView.findViewById(R.id.ReplyInCommentsRecyclerView);
            userIV = itemView.findViewById(R.id.commentatorImage);
            commentatorName = itemView.findViewById(R.id.commentatorName);
            commentatorText = itemView.findViewById(R.id.commentatorText);
            ViewRepliesInComments = itemView.findViewById(R.id.ViewRepliesInComments);
        }
    }

}
