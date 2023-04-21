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
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.model.LiveRoomData;

import java.util.ArrayList;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder> {

    Context mContext;
    RoomListener mListener;
    ArrayList<LiveRoomData> liveRoomData;

    public RoomAdapter(Context context, RoomListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.liveRoomData = new ArrayList<>();
    }

    @NonNull
    @Override
    public RoomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_room_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomAdapter.ViewHolder holder, int position) {
        LiveRoomData data = liveRoomData.get(position);
        Glide.with(holder.roomLogo)
                .load(Prefs.GetBaseUrl(mContext) + data.logo)
                .placeholder(R.drawable.placeholder)
                .into(holder.roomLogo);

        holder.roomNameTV.setText(data.title);

        holder.itemView.setOnClickListener(view -> mListener.roomSelected(data));
    }

    @Override
    public int getItemCount() {
        return liveRoomData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView roomLogo;
        TextView roomNameTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            roomLogo = itemView.findViewById(R.id.room_img_iv);
            roomNameTV = itemView.findViewById(R.id.room_name_tv);
        }
    }

    public void setList(ArrayList<LiveRoomData> data){
        this.liveRoomData.clear();
        this.liveRoomData.addAll(data);
        notifyDataSetChanged();
    }

    public interface RoomListener{
        void roomSelected(LiveRoomData data);
    }
}
