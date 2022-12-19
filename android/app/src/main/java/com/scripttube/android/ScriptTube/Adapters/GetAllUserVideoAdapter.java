package com.scripttube.android.ScriptTube.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.GetAllUserVideoModel;
import com.scripttube.android.ScriptTube.R;

import java.util.List;

public class GetAllUserVideoAdapter extends RecyclerView.Adapter<GetAllUserVideoAdapter.ViewHolder> {
    Context context;
    List<GetAllUserVideoModel.Data> list;
    UserVideoListener listener;

    public GetAllUserVideoAdapter(Context context, List<GetAllUserVideoModel.Data> list, UserVideoListener userVideoListener) {
        this.context = context;
        this.listener = userVideoListener;
        this.list = list;
    }

    @NonNull
    @Override
    public GetAllUserVideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.get_all_user_profile_single_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull GetAllUserVideoAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        holder.videoView.setImageURI(Uri.parse(list.get(position).getVideoImage()));
//        
        Glide.with(context).load(list.get(position).getVideoImage()).into(holder.videoView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.showVideo(position);
            }
        });
        holder.ViewsInGetAllUserVideo.setText(String.valueOf(0));
//        holder.videoView.setVideoURI(Uri.parse(list.get(position).getVideoLink()));
//        holder.videoView.start();
//        holder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                holder.videoView.start();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView videoView;
        TextView ViewsInGetAllUserVideo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ViewsInGetAllUserVideo = itemView.findViewById(R.id.ViewsInGetAllUserVideo);
            videoView = itemView.findViewById(R.id.VideoViewInGetAllUser);
        }
    }

    public interface UserVideoListener{
        void showVideo(int position);
    }
}
