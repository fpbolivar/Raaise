package com.raaise.android.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.raaise.android.R;

import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private Context mContext;
    List<String> list;
    private GalleryListener mListener;

    public GalleryAdapter(Context context, ArrayList<String> strings, GalleryListener listener){
        this.mContext = context;
        this.list = strings;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryAdapter.ViewHolder holder, int position) {
        String imgStr = list.get(position);
        Glide.with(holder.imageV)
                .load(imgStr)
                .into(holder.imageV);

        holder.itemView.setOnClickListener(v -> mListener.ImageSelected(imgStr));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageV = itemView.findViewById(R.id.gallery_img_iv);
        }
    }

    public interface GalleryListener{
        void ImageSelected(String imageUri);
    }
}
