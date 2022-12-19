package com.scripttube.android.ScriptTube.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.GetGlobalVideoModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.ListOfVideoCommentsModel;
import com.scripttube.android.ScriptTube.Home.Fragments.OtherUserProfileActivity;
import com.scripttube.android.ScriptTube.R;
import com.scripttube.android.ScriptTube.Utilities.HelperClasses.HelperClass;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    boolean visible = false;
    CommentReplyListener listener;
    private RecyclerView.RecycledViewPool
            viewPool
            = new RecyclerView
            .RecycledViewPool();
    private Context mContext;
    private List<ListOfVideoCommentsModel.Data> comments;

    public CommentsAdapter(Context mContext, List<ListOfVideoCommentsModel.Data> comments, CommentReplyListener listener) {
        this.mContext = mContext;
        this.comments = comments;
        this.listener = listener;
    }
    //    public CommentsAdapter(Context mContext, List<ListOfVideoCommentsModel.Data> comments) {
//        this.mContext = mContext;
//        this.comments = comments;
//    }

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
//        System.out.printf("Buisiness   ");
        Log.i("Buisiness   ", "onBindViewHolder: " + comments.get(position).getCreatedAt() + "   " + comments.get(position).getComment());
        Log.i("Buisiness   ", "onBindViewHolder: " + comments.get(position).getCreatedAt() + "   " + comments.get(position).getComment());
        holder.ViewRepliesInComments.setText(String.format("View replies (%d)", comments.get(position).getReplyId().size()));

        holder.commentatorName.setText(Model.getCommentBy().getUserName());
//        SimpleDateFormat sdf
//                = new SimpleDateFormat(
//                "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        holder.commentatorText.setText(comments.get(position).getComment()
                + " " +
                HelperClass.findDifference(comments.get(position).getCreatedAt(),
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date())));


        ;


//        holder.commentatorText.setText(comments.get(position).getComment()+" "+);
        if (comments.get(position).getReplyId().size() != 0) {
            holder.ReplyInCommentsRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 1));
//            LinearLayoutManager Manager = new LinearLayoutManager(holder.ReplyInCommentsRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
//            Manager.setInitialPrefetchItemCount(comments.get(position).getReplyId().size());
//            holder.ReplyInCommentsRecyclerView.setHasFixedSize(true);
//            CommentsReplyAdapter adapter = new CommentsReplyAdapter(mContext, comments.get(position).getReplyId());
            CommentsReplyAdapter adapter = new CommentsReplyAdapter(holder.ReplyInCommentsRecyclerView.getContext(), comments.get(position).getReplyId());
//            holder.ReplyInCommentsRecyclerView.setLayoutManager(Manager);
            holder.ReplyInCommentsRecyclerView.setAdapter(adapter);
            holder.ReplyInCommentsRecyclerView.setRecycledViewPool(viewPool);
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
        holder.ReplyComment.setOnClickListener(view -> listener.ShowReplySheet(comments.get(position).getComment(), comments.get(position).get_id()));
        holder.userIV.setOnClickListener(view -> DoOpenUserProfile(Model));
        holder.commentatorName.setOnClickListener(view -> DoOpenUserProfile(Model));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public interface CommentReplyListener {
        void ShowReplySheet(String Name, String CommentId);
    }
    private void DoOpenUserProfile(ListOfVideoCommentsModel.Data obj) {
        mContext.startActivity(new Intent(mContext, OtherUserProfileActivity.class).putExtra("UserIdForProfile", obj.getCommentBy().get_id()).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userIV;
        TextView commentatorName, ReplyComment;
        TextView commentatorText, ViewRepliesInComments;
        RecyclerView ReplyInCommentsRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ReplyComment = itemView.findViewById(R.id.ReplyComment);
            ReplyInCommentsRecyclerView = itemView.findViewById(R.id.ReplyInCommentsRecyclerView);
            userIV = itemView.findViewById(R.id.commentatorImage);
            commentatorName = itemView.findViewById(R.id.commentatorName);
            commentatorText = itemView.findViewById(R.id.commentatorText);
            ViewRepliesInComments = itemView.findViewById(R.id.ViewRepliesInComments);
        }
    }

//    public void updateComments(ArrayList<Comments> commentsList) {
//        comments = commentsList;
//        notifyDataSetChanged();
//    }
}
