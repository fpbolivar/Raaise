package com.raaise.android.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.raaise.android.ApiManager.ApiModels.GlobalSearchModel;
import com.raaise.android.R;

import java.util.List;

public class SeeAllAudioOfSearchAdapter extends RecyclerView.Adapter<SeeAllAudioOfSearchAdapter.ViewHolder> {
    Context context;
    List<GlobalSearchModel.Data.Audios> list;
    AudioListener audioListener;
    private int checkedPosition = -1;

    public SeeAllAudioOfSearchAdapter(Context context, List<GlobalSearchModel.Data.Audios> list, AudioListener audioListener) {
        this.context = context;
        this.list = list;
        this.audioListener = audioListener;
    }

    @NonNull
    @Override
    public SeeAllAudioOfSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SeeAllAudioOfSearchAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.music_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SeeAllAudioOfSearchAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        GlobalSearchModel.Data.Audios music = list.get(position);

        Glide.with(holder.singerIV)
                .load(music.Thumbnail)
                .circleCrop()
                .centerCrop()
                .into(holder.singerIV);
        holder.singerName.setText(music.artistName);
        holder.songName.setText(music.songName);
        holder.genreName.setText(music.genreId.name);
        if (!music.audioTime.equals(""))
            holder.trackTime.setText(music.audioTime);

        holder.playPauseBtn.setOnClickListener(view -> {
            checkedPosition = position;
            notifyDataSetChanged();


        });

        holder.itemView.setOnClickListener(v -> audioListener.AudioClicked(music.getAudio(), music.get_id(), music.getSongName()));

        if (checkedPosition == position) {
            holder.musicPlaying.setVisibility(View.VISIBLE);
            holder.playPauseBtn.setVisibility(View.GONE);
        } else {
            holder.musicPlaying.setVisibility(View.GONE);
            holder.playPauseBtn.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface AudioListener {
        void AudioClicked(String AudioLink, String AudioID, String AudioName);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LottieAnimationView musicPlaying;
        ImageView singerIV;
        TextView songName;
        TextView singerName;
        TextView genreName;
        TextView trackTime;
        ImageView playPauseBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            musicPlaying = itemView.findViewById(R.id.lottie_music);
            singerIV = itemView.findViewById(R.id.singer_image);
            songName = itemView.findViewById(R.id.song_name);
            singerName = itemView.findViewById(R.id.singer_name);
            genreName = itemView.findViewById(R.id.genreType);
            trackTime = itemView.findViewById(R.id.musicTime);
            playPauseBtn = itemView.findViewById(R.id.play_pause_button);
        }
    }
}
