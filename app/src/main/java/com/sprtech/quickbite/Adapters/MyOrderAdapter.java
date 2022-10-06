package com.sprtech.quickbite.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.sprtech.quickbite.Models.OrderListModel;
import com.sprtech.quickbite.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.myViewHolder> {
    private Context mContext;
    List<OrderListModel> orderListModels;

    public MyOrderAdapter(Context mContext, List<OrderListModel> orderListModels) {
        this.mContext = mContext;
        this.orderListModels = orderListModels;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_item,parent,false);
        return new myViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        OrderListModel order = orderListModels.get(position);
        Picasso.get().load(order.getOrderPic()).fit().centerInside().placeholder(R.drawable.burger).into(holder.food_pic);
        holder.foodName.setText(order.getOrderFoodName());
        holder.foodDescription.setText(order.getOrderDescription());
        holder.foodQuantity.setText(order.getOrderQuantity());
        holder.foodPrice.setText("₱"+order.getOrderTotal());
    }

    @Override
    public int getItemCount() {
        return orderListModels.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        ImageView food_pic;
        TextView foodName, foodDescription, foodQuantity, foodPrice;
        MaterialButton cancelBtn;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            food_pic = itemView.findViewById(R.id.food_pic);
            foodName = itemView.findViewById(R.id.foodName);
            foodDescription = itemView.findViewById(R.id.foodDescription);
            foodQuantity = itemView.findViewById(R.id.foodQuantity);
            foodPrice = itemView.findViewById(R.id.foodPrice);
            cancelBtn = itemView.findViewById(R.id.cancelBtn);
        }
    }
}
