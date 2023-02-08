package com.raaise.android.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.raaise.android.ApiManager.ApiModels.GetAllUserVideoModel;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Prefs;

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
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.get_all_user_profile_single_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GetAllUserVideoAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        GetAllUserVideoModel.Data obj = list.get(position);
        Glide.with(context).load(Prefs.GetBaseUrl(context) + list.get(position).getVideoImage()).into(holder.videoView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.showVideo(position);
            }
        });
        holder.ViewsInGetAllUserVideo.setText(String.valueOf(list.get(position).getVideoViewCount()));
        if (obj.isDonation()) {
            holder.DonationAmountLayout.setVisibility(View.VISIBLE);
            holder.DonationAmountInVideo.setText(obj.getTotalDanotionAmount());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface UserVideoListener {
        void showVideo(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView videoView;
        TextView ViewsInGetAllUserVideo;
        LinearLayout DonationAmountLayout;
        TextView DonationAmountInVideo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            DonationAmountLayout = itemView.findViewById(R.id.DonationAmountLayout);
            DonationAmountInVideo = itemView.findViewById(R.id.DonationAmountInVideo);

            ViewsInGetAllUserVideo = itemView.findViewById(R.id.ViewsInGetAllUserVideo);
            videoView = itemView.findViewById(R.id.VideoViewInGetAllUser);
        }
    }
}
