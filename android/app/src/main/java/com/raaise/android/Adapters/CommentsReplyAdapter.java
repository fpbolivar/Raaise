package com.raaise.android.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
import com.raaise.android.Utilities.HelperClasses.Prefs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CommentsReplyAdapter extends RecyclerView.Adapter<CommentsReplyAdapter.ViewHolder> {
    Context context;
    List<ListOfVideoCommentsModel.Data.ReplyId> list;
    private VideoReplyListener mListener;

    public CommentsReplyAdapter(Context context, List<ListOfVideoCommentsModel.Data.ReplyId> list, VideoReplyListener listener) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
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
        if (model.replyBy._id.equals(Prefs.GetUserID(context))){
            holder.moreOptions.setVisibility(View.VISIBLE);
        } else {
            holder.moreOptions.setVisibility(View.INVISIBLE);
        }
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
        holder.moreOptions.setOnClickListener(view -> mListener.VdoRplMoreOptions(model.get_id(), model.getReply()));

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
        ImageView moreOptions;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            moreOptions = itemView.findViewById(R.id.more_options);
            commentatorReplyImage = itemView.findViewById(R.id.commentatorReplyImage);
            NameCommentReply = itemView.findViewById(R.id.NameCommentReply);
            CommentReply = itemView.findViewById(R.id.CommentReply);
            Time = itemView.findViewById(R.id.time);
        }
    }

    public interface VideoReplyListener{
        void VdoRplMoreOptions(String id, String reply);
    }
}
