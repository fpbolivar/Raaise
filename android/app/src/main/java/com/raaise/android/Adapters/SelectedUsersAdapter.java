package com.raaise.android.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Prefs;
import com.raaise.android.model.SelectUsersModel;

import java.util.ArrayList;

public class SelectedUsersAdapter extends RecyclerView.Adapter<SelectedUsersAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<SelectUsersModel> usersModels;
    private UserUnselectedListener mListener;

    public SelectedUsersAdapter(Context context, UserUnselectedListener listener){
        this.mContext = context;
        this.mListener = listener;
        this.usersModels = new ArrayList<>();
    }
    @NonNull
    @Override
    public SelectedUsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedUsersAdapter.ViewHolder holder, int position) {
        SelectUsersModel model = usersModels.get(position);
        Glide.with(holder.userImg)
                .load(Prefs.GetBaseUrl(mContext) + model.getProfileImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.userImg);

        holder.removeUserBtn.setOnClickListener(view -> {
            removeItem(model);
            mListener.UserUnselected(model);
        });
    }

    private void removeItem(SelectUsersModel model) {
        usersModels.remove(model);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return usersModels.size();
    }

    public void addItem(SelectUsersModel selectedUsersModels) {
        this.usersModels.add(selectedUsersModels);
        notifyDataSetChanged();
    }

    public ArrayList<String> getList() {
        ArrayList<String> ids = new ArrayList<>();
        for (SelectUsersModel model : usersModels){
            ids.add(model.getId());
        }

        return ids;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView userImg;
        ImageView removeUserBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImg = itemView.findViewById(R.id.users_iv);
            removeUserBtn = itemView.findViewById(R.id.remove_user_iv);
        }
    }

    public interface UserUnselectedListener{
        void UserUnselected(SelectUsersModel model);
    }
}
