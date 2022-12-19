package com.scripttube.android.ScriptTube.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.scripttube.android.ScriptTube.R;

public class MyContentAdapter extends RecyclerView.Adapter<MyContentAdapter.ViewHolder> {

    private Context mContext;

    public MyContentAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyContentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyContentAdapter.ViewHolder holder, int position) {
        Glide.with(holder.contentImageV)
                .load(R.drawable.delete_it)
                .into(holder.contentImageV);
    }

    @Override
    public int getItemCount() {
        return 9;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView contentImageV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contentImageV = itemView.findViewById(R.id.content_image);
        }
    }
}
