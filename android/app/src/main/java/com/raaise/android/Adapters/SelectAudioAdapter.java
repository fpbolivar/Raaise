package com.raaise.android.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.raaise.android.R;
import com.raaise.android.model.SelectAudio;

import java.util.ArrayList;

public class SelectAudioAdapter extends RecyclerView.Adapter<SelectAudioAdapter.ViewHolder> {

    private final Context mContext;
    ArrayList<SelectAudio> selectAudios;

    public SelectAudioAdapter(Context mContext) {
        this.mContext = mContext;
        selectAudios = new ArrayList<>();
    }

    @NonNull
    @Override
    public SelectAudioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_audio_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectAudioAdapter.ViewHolder holder, int position) {
        SelectAudio selectAudio = selectAudios.get(position);

        Glide.with(holder.userImageV)
                .load(R.drawable.placeholder)
                .circleCrop()
                .into(holder.userImageV);
        holder.headingTV.setText(selectAudio.heading);
        holder.followersTV.setText(selectAudio.followers);
    }

    @Override
    public int getItemCount() {
        return selectAudios.size();
    }

    public void updateAudios(ArrayList<SelectAudio> selectAudiosData) {
        selectAudios = selectAudiosData;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userImageV;
        TextView headingTV;
        TextView followersTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImageV = itemView.findViewById(R.id.user_image);
            headingTV = itemView.findViewById(R.id.headingTV);
            followersTV = itemView.findViewById(R.id.followersCountTV);
        }
    }
}
