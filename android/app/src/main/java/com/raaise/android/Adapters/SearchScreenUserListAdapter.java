package com.raaise.android.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.raaise.android.ApiManager.ApiModels.GlobalSearchModel;
import com.raaise.android.Home.Fragments.OtherUserProfileActivity;
import com.raaise.android.R;

import java.util.List;

public class SearchScreenUserListAdapter extends RecyclerView.Adapter<SearchScreenUserListAdapter.ViewHolder> {

    Context context;
    List<GlobalSearchModel.Data.Users> list;
    boolean userClicked = false;


    public SearchScreenUserListAdapter(Context context, List<GlobalSearchModel.Data.Users> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_search_screen_user_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GlobalSearchModel.Data.Users obj = list.get(position);

        Glide.with(context).load(obj.getProfileImage()).placeholder(R.drawable.placeholder).into(holder.userProfileIV);
        holder.userNameTV.setText(obj.getUserName());
        holder.MainLayoutOfUsersInSearch.setOnClickListener(view -> {
            context.startActivity(new Intent(context, OtherUserProfileActivity.class).putExtra("UserIdForProfile", obj.get_id()).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        });

    }

    @Override
    public int getItemCount() {
        if (userClicked)
            return list.size();
        else
            return list.size() > 4 ? 4 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userProfileIV;
        TextView userNameTV;
        RelativeLayout MainLayoutOfUsersInSearch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            MainLayoutOfUsersInSearch = itemView.findViewById(R.id.MainLayoutOfUsersInSearch);
            userProfileIV = itemView.findViewById(R.id.User_Profile);
            userNameTV = itemView.findViewById(R.id.tv_user_name);

        }
    }
}
