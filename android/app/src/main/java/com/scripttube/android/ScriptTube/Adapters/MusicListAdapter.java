package com.scripttube.android.ScriptTube.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.scripttube.android.ScriptTube.R;
import com.scripttube.android.ScriptTube.model.MusicData;

import java.util.ArrayList;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<MusicData> musicData;
    private int checkedPosition = -1;
    private MusicListener musicListener;

    public MusicListAdapter(Context mContext, MusicListener listener) {
        this.mContext = mContext;
        this.musicListener = listener;
        musicData = new ArrayList<>();
    }

    @NonNull
    @Override
    public MusicListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicListAdapter.ViewHolder holder, int position) {
        MusicData music = musicData.get(position);

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
            if (music.audio != null || !music.audio.isEmpty())
            musicListener.playMusic(music.audio);
        });

        holder.itemView.setOnClickListener(v -> musicListener.audioSelected(music));

            if (checkedPosition == position){
                holder.musicPlaying.setVisibility(View.VISIBLE);
                holder.playPauseBtn.setVisibility(View.GONE);
            } else {
                holder.musicPlaying.setVisibility(View.GONE);
                holder.playPauseBtn.setVisibility(View.VISIBLE);
            }
    }

    @Override
    public int getItemCount() {
        return musicData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
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

    public void updateMusicList(ArrayList<MusicData> musicDataArrayList){
        musicData = musicDataArrayList;
        notifyDataSetChanged();
    }

    public interface MusicListener{
        void playMusic(String songUrl);
        void audioSelected(MusicData data);
    }
}
