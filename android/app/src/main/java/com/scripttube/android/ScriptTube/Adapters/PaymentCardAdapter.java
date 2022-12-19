package com.scripttube.android.ScriptTube.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.scripttube.android.ScriptTube.R;

import java.util.List;

public class PaymentCardAdapter extends RecyclerView.Adapter<PaymentCardAdapter.ViewHolder> {
    List<String> list;
    Context context;

    public PaymentCardAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public PaymentCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PaymentCardAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.card_single_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentCardAdapter.ViewHolder holder, int position) {
        holder.CardNumber.setText(String.format("**** **** **** %s", list.get(position)));
        holder.PaymentCardParentView.setOnClickListener(view -> {
            holder.SelectedCard.setVisibility(View.VISIBLE);

            for (int i = 0; i < list.size(); i++) {
                if (i != position) {
                    holder.SelectedCard.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView CardNumber;
        ImageView SelectedCard;
        CardView PaymentCardParentView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            PaymentCardParentView = itemView.findViewById(R.id.PaymentCardParentView);
            CardNumber = itemView.findViewById(R.id.CardNumber);
            SelectedCard = itemView.findViewById(R.id.SelectedCard);
        }
    }
}
