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

public class PublicRoomAdapter extends RecyclerView.Adapter<PublicRoomAdapter.ViewHolder> {

    private Context mContext;
    private PublicRoomListener mListener;
    private ArrayList<LiveRoomData> liveRoomData;

    public PublicRoomAdapter(Context context, PublicRoomListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.liveRoomData = new ArrayList<>();
    }

    @NonNull
    @Override
    public PublicRoomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.public_room_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PublicRoomAdapter.ViewHolder holder, int position) {
        LiveRoomData data = liveRoomData.get(position);

        Glide.with(holder.roomLogoIV)
                .load(Prefs.GetBaseUrl(mContext) + data.logo)
                .placeholder(R.drawable.placeholder)
                .into(holder.roomLogoIV);

        holder.roomNameTV.setText(data.title);
        holder.roomDescTV.setText(data.description);

        if (data.isOnline.equals("0")){
            holder.online.setVisibility(View.GONE);
        } else {
            holder.online.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(view -> mListener.RoomSelected(data));
    }

    @Override
    public int getItemCount() {
        return liveRoomData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView roomLogoIV;
        TextView roomNameTV;
        TextView roomDescTV;
        ImageView online;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            online = itemView.findViewById(R.id.online_status);
            roomLogoIV = itemView.findViewById(R.id.public_room_logo);
            roomNameTV = itemView.findViewById(R.id.public_room_tv);
            roomDescTV = itemView.findViewById(R.id.public_room_desc_tv);
        }
    }

    public void setList(ArrayList<LiveRoomData> data){
        this.liveRoomData.clear();
        this.liveRoomData.addAll(data);
        notifyDataSetChanged();
    }

    public interface PublicRoomListener{
        void RoomSelected(LiveRoomData data);
    }
}
