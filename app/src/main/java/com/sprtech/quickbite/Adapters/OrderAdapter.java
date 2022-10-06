package com.sprtech.quickbite.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.sprtech.quickbite.Models.OrderModel;
import com.sprtech.quickbite.R;
import com.sprtech.quickbite.grocery_order_details;
import com.sprtech.quickbite.order_details;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.myViewHolder> {
    private Context mContext;
    private List<OrderModel> orderModels;

    public OrderAdapter(Context mContext, List<OrderModel> orderModels) {
        this.mContext = mContext;
        this.orderModels = orderModels;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout_notification,parent,false);
        return new myViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        OrderModel order = orderModels.get(position);
        holder.name.setText(order.getCusName());
        holder.date.setText(order.getOrderDate());
        holder.time.setText(order.getOrderTime());
        holder.riderName.setText("Processing By: " + order.getDriver());
        Picasso.get().load(order.getCusPic()).placeholder(R.drawable.userman).fit().centerInside().into(holder.cusPic);

        if(order.getOrderStatus().equals("Grocery")) {
            holder.txt.setText("Order Groceries!");
            holder.bt_view.setOnClickListener(view -> {
                String cusUID = order.getCusID();
                String cusLocation = order.getCusAddress();
                String cusPhone = order.getCusPhone();
                String cusName = order.getCusName();
                String cusPic = order.getCusPic();
                String orderID = order.getOrderID();
                String orders = order.getOrder();
                String ID = order.getCusOrderID();

                Intent i = new Intent(mContext, grocery_order_details.class);
                i.putExtra("cusID", cusUID);
                i.putExtra("orderID", orderID);
                i.putExtra("cusLocation", cusLocation);
                i.putExtra("cusPhone", cusPhone);
                i.putExtra("cusName", cusName);
                i.putExtra("cusPic", cusPic);
                i.putExtra("order", orders);
                i.putExtra("ID", ID);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
            });
        }else if(order.getOrderStatus().equals("FastFood")) {
            holder.bt_view.setOnClickListener(view -> {
                String cusUID = order.getCusID();
                String cusTotalOrder = order.getTotalOrderFee();
                String cusLocation = order.getCusAddress();
                String cusPhone = order.getCusPhone();
                String cusName = order.getCusName();
                String cusPic = order.getCusPic();
                String dFee = order.getDeliveryFee();
                String ID = order.getCusOrderID();
                String orderID = order.getOrderID();

                Intent i = new Intent(mContext, order_details.class);
                i.putExtra("cusID", cusUID);
                i.putExtra("orderID", orderID);
                i.putExtra("cartOrderID", order.getCartOrderID());
                i.putExtra("ID", ID);
                i.putExtra("DeliveryFee", dFee);
                i.putExtra("cusTotalOrder", cusTotalOrder);
                i.putExtra("cusLocation", cusLocation);
                i.putExtra("cusPhone", cusPhone);
                i.putExtra("cusName", cusName);
                i.putExtra("cusPic", cusPic);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);

            });
        }
    }

    @Override
    public int getItemCount() {
        return orderModels.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        MaterialButton bt_view;
        TextView name, date, time, riderName, txt;
        CircleImageView cusPic;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.txt);
            name = itemView.findViewById(R.id.name);
            riderName = itemView.findViewById(R.id.riderName);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            bt_view = itemView.findViewById(R.id.bt_view);
            cusPic = itemView.findViewById(R.id.cusPic);
        }
    }
}
