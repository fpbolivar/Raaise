package com.raaise.android.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raaise.android.R;

import java.util.ArrayList;

public class PrivacyAdapter extends RecyclerView.Adapter<PrivacyAdapter.ViewHolder> {

    private Context mContext;
    private PrivacyListener listener;
    private ArrayList<String> privacyStrings;

    private int mPosition;

    public PrivacyAdapter(Context context, ArrayList<String> strings, PrivacyListener privacyListener, int pos){
        this.mContext = context;
        this.privacyStrings = strings;
        this.listener = privacyListener;
        this.mPosition = pos;
    }

    @NonNull
    @Override
    public PrivacyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.privacy_control_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrivacyAdapter.ViewHolder holder, int position) {
        holder.privacyTV.setText(privacyStrings.get(position));
        holder.mCheckBox.setOnClickListener(view -> {
            mPosition = position;
            listener.privacySelected(privacyStrings.get(position), position);
            notifyDataSetChanged();
        });

        if (mPosition == position){
            holder.mCheckBox.setChecked(true);
        } else {
            holder.mCheckBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return privacyStrings.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView privacyTV;
        CheckBox mCheckBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            privacyTV = itemView.findViewById(R.id.privacy_tv);
            mCheckBox = itemView.findViewById(R.id.check);
        }
    }

    public interface PrivacyListener{
        void privacySelected(String privacyName, int privacyPosition);
    }
}
