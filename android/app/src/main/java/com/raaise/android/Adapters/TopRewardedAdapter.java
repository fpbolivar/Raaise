package com.raaise.android.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.raaise.android.R;


public class TopRewardedAdapter extends RecyclerView.Adapter<TopRewardedAdapter.ViewHolder> {
    private Context mContext;
    public TopRewardedAdapter(Context context){
        this.mContext = context;
    }
    @NonNull
    @Override
    public TopRewardedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.top_rewarded_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopRewardedAdapter.ViewHolder holder, int position) {
        if (position == 0){
            holder.cardView.getLayoutParams().height = 90;
            holder.cardView.getLayoutParams().width = 90;
        } if (position == 1){
            holder.cardView.getLayoutParams().height = 80;
            holder.cardView.getLayoutParams().width = 80;
        } if (position == 2){
            holder.cardView.getLayoutParams().height = 70;
            holder.cardView.getLayoutParams().width = 70;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView topRewardedUser;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            topRewardedUser = itemView.findViewById(R.id.top_rewarded_user);
            cardView = itemView.findViewById(R.id.top_rewarded_user_cv);
        }
    }
}
