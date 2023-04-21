package com.raaise.android.Adapters;

import android.content.Context;
import android.util.Log;
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
import com.raaise.android.model.SelectUsersModel;

import java.util.ArrayList;

public class SelectUsersAdapter extends RecyclerView.Adapter<SelectUsersAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<SelectUsersModel> usersList;
    private SelectUserListener mListener;

    public SelectUsersAdapter(Context context, SelectUserListener listener){
        this.mContext = context;
        this.usersList = new ArrayList<>();
        this.mListener = listener;
    }

    @NonNull
    @Override
    public SelectUsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectUsersAdapter.ViewHolder holder, int position) {
        SelectUsersModel model = usersList.get(position);
        holder.userNameTV.setText(model.getUserName());
        Glide.with(holder.userImg)
                .load(Prefs.GetBaseUrl(mContext) + model.getProfileImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.userImg);
        holder.itemView.setOnClickListener(view -> mListener.UserSelected(model));
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public void setList(ArrayList<SelectUsersModel> selectUsersList) {
        this.usersList.clear();
        this.usersList.addAll(selectUsersList);
        notifyDataSetChanged();
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

    public interface SelectUserListener{
        void UserSelected(SelectUsersModel model);
    }
}
