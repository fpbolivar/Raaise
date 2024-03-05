
package com.raaise.android.Adapters;

import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
        import androidx.cardview.widget.CardView;
        import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.raaise.android.ApiManager.ApiModels.GetPublicUserProfileModel;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Prefs;

import java.util.List;

public class DonationUserAdapter extends RecyclerView.Adapter<DonationUserAdapter.ViewHolder> {

    List<GetPublicUserProfileModel.Data.DonationUsers> donationUsersList;

    public DonationUserAdapter(List<GetPublicUserProfileModel.Data.DonationUsers> donationUsers) {
        this.donationUsersList=donationUsers;
    }

    @NonNull
    @Override
    public DonationUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DonationUserAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.donation_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DonationUserAdapter.ViewHolder holder, int position) {
        GetPublicUserProfileModel.Data.DonationUsers donationUsers=donationUsersList.get(position);
        holder.cardView.setVisibility(View.VISIBLE);
        Glide.with(holder.itemView.getContext()).load(Prefs.GetBaseUrl(holder.itemView.getContext()) +donationUsers.getProfileImage())
                .circleCrop()
                .placeholder(R.drawable.placeholder)
                .into(holder.imageViewInFF);

        holder.username.setText(String.format("@%s",donationUsers.getUsername()));
        holder.amount.setText(String.format("$%s", donationUsers.getCredit() == null ? 00.00 : donationUsers.getCredit()));
    }

    @Override
    public int getItemCount() {
        return donationUsersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageViewInFF;
        TextView username, amount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.CardViewOfImageOnFF);
            imageViewInFF=itemView.findViewById(R.id.imageViewInFF);
            username=itemView.findViewById(R.id.NameInFF);
            amount=itemView.findViewById(R.id.FollowrCountInFF);
        }
    }
}

