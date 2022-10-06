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

import com.sprtech.quickbite.Models.OrderListModel;
import com.sprtech.quickbite.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.myViewHolder> {
    private Context mContext;
    private List<OrderListModel> orderListModels;

    public OrderListAdapter(Context mContext, List<OrderListModel> orderListModels) {
        this.mContext = mContext;
        this.orderListModels = orderListModels;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_details_list,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return orderListModels.size();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        OrderListModel order = orderListModels.get(position);

        holder.food_name.setText(order.getOrderFoodName());
        holder.food_aname.setText(order.getOrderName());
        holder.food_description.setText(order.getOrderDescription());
        holder.food_quantity.setText(order.getOrderQuantity());
        holder.food_price.setText("₱"+order.getOrderTotal());
        Picasso.get()
                .load(order.getOrderPic())
                .fit()
                .centerInside()
                .placeholder(R.drawable.burger)
                .into(holder.food_image);

    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        ImageView food_image;
        TextView food_name, food_aname, food_description, food_quantity, food_price;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            food_image = itemView.findViewById(R.id.food_image);
            food_name = itemView.findViewById(R.id.food_name);
            food_aname = itemView.findViewById(R.id.food_aname);
            food_description = itemView.findViewById(R.id.food_description);
            food_quantity = itemView.findViewById(R.id.food_quantity);
            food_price = itemView.findViewById(R.id.food_price);
        }
    }
}
