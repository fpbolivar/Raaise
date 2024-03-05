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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.raaise.android.ApiManager.ApiModels.GetCategoryModel;
import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.Prefs;

import java.util.ArrayList;
import java.util.List;

public class TagsCategoryAdapter extends RecyclerView.Adapter<TagsCategoryAdapter.ViewHolder> {
    List<GetCategoryModel.Data> categories = new ArrayList<>();
    Context mContext;
    TagsCategoryListener tagsCategoryListener;
    List<String> ids=new ArrayList<>();
    public boolean isMultiSelect=false;
    public int selectedPosition=-1;

    public TagsCategoryAdapter(Context mContext, TagsCategoryListener tagsCategoryListener, List<GetCategoryModel.Data> categories,boolean isMultiSelect) {
        this.mContext = mContext;
        this.tagsCategoryListener = tagsCategoryListener;
        this.categories = categories;
        this.isMultiSelect=isMultiSelect;
    }

    @NonNull
    @Override
    public TagsCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TagsCategoryAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tags_categories_tems, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TagsCategoryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        GetCategoryModel.Data getCategories=categories.get(position);

        Glide.with(holder.itemView.getContext())
                .load(Prefs.GetBaseUrl(holder.itemView.getContext())+getCategories.image)
                .into(holder.imageView);

        Log.e("Model","getCategories"+new Gson().toJson(getCategories));

        holder.textView.setText(getCategories.name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMultiSelect){
                    addIds(getCategories.get_id());
                }else {
                    selectedPosition=position;
                }
                tagsCategoryListener.multiSelectCategory(getCategories.get_id(),ids);
                notifyDataSetChanged();
            }
        });

        if(isMultiSelect){
            if(ids.contains(getCategories.get_id())){
                holder.textView.setTextColor(holder.itemView.getContext().getColor(R.color.black));
                holder.categoryRelative.setBackground(holder.itemView.getContext().getDrawable(R.drawable.category_bg_white));
            }else {
                holder.textView.setTextColor(holder.itemView.getContext().getColor(R.color.white));
                holder.categoryRelative.setBackground(holder.itemView.getContext().getDrawable(R.drawable.category_bg));
            }
        }else{
            if(selectedPosition==position){
                holder.textView.setTextColor(holder.itemView.getContext().getColor(R.color.black));
                holder.categoryRelative.setBackground(holder.itemView.getContext().getDrawable(R.drawable.category_bg_white));
            }else {
                holder.textView.setTextColor(holder.itemView.getContext().getColor(R.color.white));
                holder.categoryRelative.setBackground(holder.itemView.getContext().getDrawable(R.drawable.category_bg));
            }
        }
    }

    public void addIds(String id){
        if(ids.contains(id)){
            ids.remove(id);
        }else{
            ids.add(id);
        }
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

    public interface TagsCategoryListener{
        void multiSelectCategory(String id,List<String> ids);
    }
}
