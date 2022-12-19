package com.scripttube.android.ScriptTube.Adapters;

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
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.ListOfVideoCommentsModel;
import com.scripttube.android.ScriptTube.Home.Fragments.OtherUserProfileActivity;
import com.scripttube.android.ScriptTube.R;

import java.util.List;

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
        if (model.getReplyBy() != null) {
            holder.NameCommentReply.setText(model.getReplyBy().getUserName());
            Glide.with(holder.commentatorReplyImage)
                    .load(model.getReplyBy().getProfileImage())
                    .placeholder(R.drawable.placeholder)
                    .circleCrop()
                    .into(holder.commentatorReplyImage);
        }


        holder.NameCommentReply.setOnClickListener(view -> DoOpenUserProfile(model));
        holder.commentatorReplyImage.setOnClickListener(view -> DoOpenUserProfile(model));

    }

    private void DoOpenUserProfile(ListOfVideoCommentsModel.Data.ReplyId obj) {
        context.startActivity(new Intent(context, OtherUserProfileActivity.class).putExtra("UserIdForProfile", obj.getReplyBy().get_id()).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView CommentReply, NameCommentReply;
        ImageView commentatorReplyImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commentatorReplyImage = itemView.findViewById(R.id.commentatorReplyImage);
            NameCommentReply = itemView.findViewById(R.id.NameCommentReply);
            CommentReply = itemView.findViewById(R.id.CommentReply);
        }
    }
}
