package com.scripttube.android.ScriptTube.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.GetAllUserVideoModel;
import com.scripttube.android.ScriptTube.ApiManager.ApiModels.PublicUserVideoListModel;
import com.scripttube.android.ScriptTube.R;

import java.util.List;

public class GetPublicUserVideoAdapter extends RecyclerView.Adapter<GetPublicUserVideoAdapter.ViewHolder> {
    Context context;
    List<PublicUserVideoListModel.Data> list;

    public GetPublicUserVideoAdapter(Context context, List<PublicUserVideoListModel.Data> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public GetPublicUserVideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GetPublicUserVideoAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.get_all_user_profile_single_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GetPublicUserVideoAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getVideoImage()).into(holder.videoView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView videoView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.VideoViewInGetAllUser);
        }
    }
}
