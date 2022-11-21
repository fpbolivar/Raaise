package com.scripttube.android.ScriptTube.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.scripttube.android.ScriptTube.R;

import java.util.List;

public class HomeFollowingAdapter extends RecyclerView.Adapter<HomeFollowingAdapter.ViewHolder> {
    Context context;
    List<String> list ;

    public HomeFollowingAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HomeFollowingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeFollowingAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.home_videos_single_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeFollowingAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
