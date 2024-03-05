package com.raaise.android.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.raaise.android.ApiManager.ApiModels.GetCategoryModel;
import com.raaise.android.ApiManager.ApiModels.GetPublicUserProfileModel;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Prefs;

import java.util.ArrayList;
import java.util.List;

public class TagsCategoryAdapterInProfile extends RecyclerView.Adapter<TagsCategoryAdapterInProfile.ViewHolder> {
    List<GetPublicUserProfileModel.Data.InterestedCategories> categories = new ArrayList<>();
    Context mContext;

    public TagsCategoryAdapterInProfile(Context mContext,List<GetPublicUserProfileModel.Data.InterestedCategories> categories) {
        this.mContext = mContext;
        this.categories = categories;
    }

    @NonNull
    @Override
    public TagsCategoryAdapterInProfile.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TagsCategoryAdapterInProfile.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tags_categories_tems, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TagsCategoryAdapterInProfile.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        GetPublicUserProfileModel.Data.InterestedCategories getCategories=categories.get(position);

        Glide.with(holder.itemView.getContext())
                .load(Prefs.GetBaseUrl(holder.itemView.getContext())+getCategories.image)
                .into(holder.imageView);

        Log.e("Model","getCategories"+new Gson().toJson(getCategories));

        holder.textView.setText(getCategories.name);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        RelativeLayout categoryRelative;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.tagsIcon);
            textView=itemView.findViewById(R.id.tagsName);
            categoryRelative=itemView.findViewById(R.id.categoryRelative);
        }
    }
}
