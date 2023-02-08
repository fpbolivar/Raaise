package com.raaise.android.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.raaise.android.ApiManager.ApiModels.GetGlobalVideoModel;
import com.raaise.android.Home.Fragments.OtherUserProfileActivity;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Prefs;

import java.util.ArrayList;


public class TopRewardedAdapter extends RecyclerView.Adapter<TopRewardedAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<GetGlobalVideoModel.DonationUsers> donationUsers;
    public TopRewardedAdapter(Context context){
        this.mContext = context;
        this.donationUsers = new ArrayList<>();
    }
    @NonNull
    @Override
    public TopRewardedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.top_rewarded_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopRewardedAdapter.ViewHolder holder, int position) {

        GetGlobalVideoModel.DonationUsers users = donationUsers.get(position);

        if (!users.getProfileImage().equals("")){
            Glide.with(mContext)
                    .load(Prefs.GetBaseUrl(mContext) + users.getProfileImage())
                    .circleCrop()
                    .into(holder.topRewardedUser);
        } else {
            Glide.with(mContext)
                    .load(mContext.getDrawable(R.drawable.blank_profile_img))
                    .circleCrop()
                    .into(holder.topRewardedUser);
        }
        holder.topRewardedUserName.setText(users.getName());

            if (position == 1){
//                holder.cardView.getLayoutParams().height = 100;
//                holder.cardView.getLayoutParams().width = 100;

                holder.topRewardedUserName.setTextSize(9);
            } if (position == 2){
//                holder.cardView.getLayoutParams().height = holder.cardView.getMeasuredHeight() - 6;
//                holder.cardView.getLayoutParams().width = holder.cardView.getMeasuredWidth() - 6;

                holder.topRewardedUserName.setTextSize(8);
        }

        holder.cardView.setOnClickListener(view -> mContext.startActivity(new Intent(mContext, OtherUserProfileActivity.class).putExtra("UserIdForProfile", users.id).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)));

    }

    @Override
    public int getItemCount() {
        return donationUsers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView topRewardedUser;
        TextView topRewardedUserName;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            topRewardedUser = itemView.findViewById(R.id.top_rewarded_user);
            topRewardedUserName = itemView.findViewById(R.id.top_rewarded_user_tv);
            cardView = itemView.findViewById(R.id.top_rewarded_user_cv);
        }
    }

    public void updateSupporterList(ArrayList<GetGlobalVideoModel.DonationUsers> users){
        this.donationUsers = users;
        notifyDataSetChanged();
    }
}
