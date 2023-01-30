package com.raaise.android.Adapters;

import android.annotation.SuppressLint;
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
import com.google.gson.Gson;
import com.raaise.android.ApiManager.ApiModels.GetVideosBasedOnAudioIdModel;
import com.raaise.android.Home.Fragments.TryAudioAllVideosActivity;
import com.raaise.android.R;

import java.util.List;

public class GetVideosBasedOnAudio extends RecyclerView.Adapter<GetVideosBasedOnAudio.ViewHolder> {
    Context context;
    List<GetVideosBasedOnAudioIdModel.Videos> list;

    public GetVideosBasedOnAudio(Context context, List<GetVideosBasedOnAudioIdModel.Videos> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public GetVideosBasedOnAudio.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GetVideosBasedOnAudio.ViewHolder(LayoutInflater.from(context).inflate(R.layout.try_audio_videos_with_white_background, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GetVideosBasedOnAudio.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(list.get(position).getVideoImage()).into(holder.videoView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, TryAudioAllVideosActivity.class).putExtra("ListOfAudioVideos", new Gson().toJson(list)).putExtra("PositionListOfAudioVideos", String.valueOf(position)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        holder.ViewsInGetAllUserVideo.setText(String.valueOf(list.get(position).getVideoViewCount()));

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
}
