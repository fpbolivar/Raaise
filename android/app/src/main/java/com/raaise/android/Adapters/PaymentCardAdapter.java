package com.raaise.android.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.raaise.android.ApiManager.ApiModels.Payment_GetCardsModel;
import com.raaise.android.R;

import java.util.List;

public class PaymentCardAdapter extends RecyclerView.Adapter<PaymentCardAdapter.ViewHolder> {
    List<Payment_GetCardsModel.Cards> list;
    Context context;
    CardsListeners cardsListeners;

    public PaymentCardAdapter(List<Payment_GetCardsModel.Cards> list, Context context, CardsListeners cardsListeners) {
        this.list = list;
        this.context = context;
        this.cardsListeners = cardsListeners;
    }

    @NonNull
    @Override
    public PaymentCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PaymentCardAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.card_single_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentCardAdapter.ViewHolder holder, int position) {
        Payment_GetCardsModel.Cards model = list.get(position);
        holder.CardNumber.setText(String.format("**** **** **** %s", model.getLast4()));
        if (model.isDefaultCard()) {
            holder.SelectedCard.setVisibility(View.VISIBLE);
        } else {
            holder.SelectedCard.setVisibility(View.GONE);
        }
        if (model.getBrand().equalsIgnoreCase("Visa")) {
            holder.CardType.setImageDrawable(context.getResources().getDrawable(R.drawable.visa_card));
        } else if (model.getBrand().equalsIgnoreCase("MasterCard")) {
            holder.CardType.setImageDrawable(context.getResources().getDrawable(R.drawable.mastercard));
        } else if (model.getBrand().equalsIgnoreCase("Discover")) {
            holder.CardType.setImageDrawable(context.getResources().getDrawable(R.drawable.discover));
        } else if (model.getBrand().equalsIgnoreCase("American Express")) {
            holder.CardType.setImageDrawable(context.getResources().getDrawable(R.drawable.american));
        } else if (model.getBrand().equalsIgnoreCase("visa")) {
            holder.CardType.setImageDrawable(context.getResources().getDrawable(R.drawable.visa_card));
        }

        holder.PaymentCardParentView.setOnClickListener(view -> {
            cardsListeners.DataChanged(model.getId());
        });
        holder.DeleteCard.setOnClickListener(view -> {
            cardsListeners.DeleteCard(model.getId());
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface CardsListeners {
        void DataChanged(String CardId);

        void DeleteCard(String CardId);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView CardNumber;
        ImageView SelectedCard, DeleteCard, CardType;
        CardView PaymentCardParentView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            CardType = itemView.findViewById(R.id.CardType);
            DeleteCard = itemView.findViewById(R.id.DeleteCard);
            PaymentCardParentView = itemView.findViewById(R.id.PaymentCardParentView);
            CardNumber = itemView.findViewById(R.id.CardNumber);
            SelectedCard = itemView.findViewById(R.id.SelectedCard);
        }
    }
}
