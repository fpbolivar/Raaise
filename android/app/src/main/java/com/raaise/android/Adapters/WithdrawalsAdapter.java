package com.raaise.android.Adapters;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.raaise.android.R;
import com.raaise.android.Utilities.HelperClasses.HelperClass;
import com.raaise.android.model.WithdrawalsPojo;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

public class WithdrawalsAdapter extends RecyclerView.Adapter<WithdrawalsAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<WithdrawalsPojo.WithdrawalData> withdrawalDatas;

    public WithdrawalsAdapter(Context context){
        this.mContext = context;
        this.withdrawalDatas = new ArrayList<>();
    }
    @NonNull
    @Override
    public WithdrawalsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.withdrawal_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WithdrawalsAdapter.ViewHolder holder, int position) {
        WithdrawalsPojo.WithdrawalData withdrawalData = withdrawalDatas.get(position);

        try {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sdf.parse(withdrawalData.updatedAt));

        String date = HelperClass.getMonthForInt(calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.DATE) +
                    ", " + calendar.get(Calendar.YEAR);

        String postTime = calendar.get(Calendar.HOUR) > 12 ? " PM" : " AM";
        String time = calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + postTime;
        holder.withdrawDateTV.setText(date);
        holder.withdrawTimeTV.setText(time);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.withdrawAmountTV.setText("$ " + withdrawalData.amount);
        holder.withdrawStatusTV.setText(withdrawalData.getWithdrawalStatus());
        holder.withdrawStatusTV.setTextColor(withdrawalData.withdrawalStatus.equalsIgnoreCase("Given") ?
                ContextCompat.getColor(mContext, R.color.Green) :
                ContextCompat.getColor(mContext, R.color.Red));

    }

    @Override
    public int getItemCount() {
        return withdrawalDatas.size();
    }

    public void setData(ArrayList<WithdrawalsPojo.WithdrawalData> list) {
        this.withdrawalDatas = list;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView withdrawDateTV;
        TextView withdrawTimeTV;
        TextView withdrawAmountTV;
        TextView withdrawStatusTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            withdrawDateTV = itemView.findViewById(R.id.withdraw_date_tv);
            withdrawTimeTV = itemView.findViewById(R.id.withdraw_time);
            withdrawAmountTV = itemView.findViewById(R.id.withdraw_amount_tv);
            withdrawStatusTV = itemView.findViewById(R.id.withdraw_status);
        }
    }
}
