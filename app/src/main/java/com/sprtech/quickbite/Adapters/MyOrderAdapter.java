package com.sprtech.quickbite.Adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sprtech.quickbite.FCM.FCMNotificationSender;
import com.sprtech.quickbite.Models.OrderListModel;
import com.sprtech.quickbite.R;
import com.sprtech.quickbite.cart;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.myViewHolder> {
    private Context mContext;
    List<OrderListModel> orderListModels;
    String userToken, fName;

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

        String QuickBiteUID = "gfA1z5sjGBeWknBSCSQgQrld7BW2";
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        FirebaseDatabase mDB = FirebaseDatabase.getInstance();
        DatabaseReference cusOrderRef = mDB.getReference("CustomerOrderList");
        DatabaseReference OrdersDetailsRef = mDB.getReference("OrdersDetails");
        DatabaseReference feeRef = mDB.getReference("TotalDeliveryFee");
        ProgressDialog loadingBar = new ProgressDialog(mContext);
        loadingBar.setTitle("Cancel Order");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(true);

        fName = order.getOrderFoodName();

        holder.cancelBtn.setOnClickListener(view -> {
            loadingBar.show();
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                cusOrderRef.child(mUser.getUid()).child(order.getOrderID()).removeValue();
                feeRef.child(QuickBiteUID).child(order.getOrderID()).removeValue();
                OrdersDetailsRef.child(QuickBiteUID).child(order.getID()).child(mUser.getUid()).child(order.getOrderFoodName()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(mContext, order.getOrderFoodName()+" cancel successfully!", Toast.LENGTH_SHORT).show();
                            sendNotification(QuickBiteUID, fName);
                            loadingBar.dismiss();
                        }
                    }
                });
            }, 3000);
        });
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

    private void sendNotification(String QuickBiteUID, String fName) {
        FirebaseDatabase.getInstance().getReference("UserToken").child(QuickBiteUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    userToken = snapshot.child("token").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            FCMNotificationSender.sendNotification(
                    mContext,
                    userToken,
                    "QuickBite",
                    fName + " canceled"
            );
        }, 3000);
    }
}
