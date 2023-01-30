package com.raaise.android.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.raaise.android.R;

import java.util.ArrayList;

public class AmountAdapter extends RecyclerView.Adapter<AmountAdapter.ViewHolder> {

    private final ArrayList<String> amtList;
    private final Context mContext;
    private final AmountListener listener;
    public int checkedPosition = -1;

    public AmountAdapter(Context context, ArrayList<String> amtList, AmountListener listener) {
        this.mContext = context;
        this.amtList = amtList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AmountAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.amount_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AmountAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String amount = amtList.get(position);

        holder.amtTV.setText("$" + amount);
        holder.amtTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.amountSelected(amount);
                checkedPosition = position;
                notifyDataSetChanged();
            }
        });

        if (checkedPosition == position) {
            holder.amtTV.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            holder.consLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.donation_selected_bg));
        } else {
            holder.amtTV.setTextColor(ContextCompat.getColor(mContext, R.color.DarkGrey));
            holder.consLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.curved_dark_grey_background));
        }
    }

    @Override
    public int getItemCount() {
        return amtList.size();
    }

    public interface AmountListener {
        void amountSelected(String amount);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout consLayout;
        TextView amtTV;
        ImageView Tick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            consLayout = itemView.findViewById(R.id.constraint_layout);
            amtTV = itemView.findViewById(R.id.amt_text_view);
        }
    }
}
