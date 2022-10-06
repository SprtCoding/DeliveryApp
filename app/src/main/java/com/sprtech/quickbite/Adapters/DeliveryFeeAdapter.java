package com.sprtech.quickbite.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sprtech.quickbite.Models.DeliveryFeeModel;
import com.sprtech.quickbite.R;

import java.util.List;

public class DeliveryFeeAdapter extends RecyclerView.Adapter<DeliveryFeeAdapter.myViewHolder> {
    private Context mContext;
    List<DeliveryFeeModel> deliveryFeeModels;

    public DeliveryFeeAdapter(Context mContext, List<DeliveryFeeModel> deliveryFeeModels) {
        this.mContext = mContext;
        this.deliveryFeeModels = deliveryFeeModels;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_fee_list,parent,false);
        return new myViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        DeliveryFeeModel dFee = deliveryFeeModels.get(position);
        holder.date.setText(dFee.getOrderDate());
        holder.time.setText(dFee.getOrderTime());
        holder.name.setText(dFee.getCustomerName());
        holder.driver.setText(dFee.getDriver());
        holder.fee.setText("₱"+dFee.getDeliveryFee());
    }

    @Override
    public int getItemCount() {
        return deliveryFeeModels.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView date, time, name, fee, driver;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            driver = itemView.findViewById(R.id.driver);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            name = itemView.findViewById(R.id.name);
            fee = itemView.findViewById(R.id.fee);
        }
    }
}
