package com.raaise.android.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raaise.android.ApiManager.ApiModels.GlobalSearchModel;
import com.raaise.android.R;

import java.util.List;

public class SearchScreenAudioListAdapter extends RecyclerView.Adapter<SearchScreenAudioListAdapter.ViewHolder> {
    Context context;
    List<GlobalSearchModel.Data.Audios> list;
    AudioListener audioListener;

    public SearchScreenAudioListAdapter(Context context, List<GlobalSearchModel.Data.Audios> list, AudioListener audioListener) {
        this.context = context;
        this.list = list;
        this.audioListener = audioListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_search_screen_music_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.audioTV.setText(list.get(position).getSongName());
        holder.itemView.setOnClickListener(view -> {
            audioListener.AudioClicked(list.get(position).getAudio(), list.get(position).get_id(), list.get(position).getSongName());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface AudioListener {
        void AudioClicked(String AudioLink, String AudioID, String AudioName);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView audioTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            audioTV = itemView.findViewById(R.id.tv_audio_name);
        }
    }
}
