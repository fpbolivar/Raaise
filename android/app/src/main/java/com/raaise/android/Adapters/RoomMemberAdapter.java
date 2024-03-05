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
import com.raaise.android.model.MemberIds;

import java.util.ArrayList;

public class RoomMemberAdapter extends RecyclerView.Adapter<RoomMemberAdapter.ViewHolder> {

    private Context mContext;
    private RoomUserListener listener;
    private ArrayList<MemberIds> memberIds;

    public RoomMemberAdapter(Context context, RoomUserListener roomUserListener){
        this.mContext = context;
        this.memberIds = new ArrayList<>();
        this.listener = roomUserListener;
    }

    @NonNull
    @Override
    public RoomMemberAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomMemberAdapter.ViewHolder holder, int position) {
        MemberIds data = memberIds.get(position);

        Glide.with(holder.userImg)
                .load(Prefs.GetBaseUrl(mContext) + data.profileImage)
                .placeholder(R.drawable.placeholder)
                .into(holder.userImg);

        holder.userNameTV.setText(data.userName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.UserSelected(data._id, data.userName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return memberIds.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView userImg;
        TextView userNameTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImg = itemView.findViewById(R.id.user_img);
            userNameTV = itemView.findViewById(R.id.user_name_tv);
        }
    }

    public void setList(ArrayList<MemberIds> ids){
        this.memberIds.clear();
        this.memberIds.addAll(ids);
        notifyDataSetChanged();
    }

    public interface RoomUserListener{
        void UserSelected(String id, String userName);
    }

}
