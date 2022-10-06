package com.sprtech.quickbite.Adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sprtech.quickbite.FCM.FCMNotificationSender;
import com.sprtech.quickbite.Models.CustomerNotificationModel;
import com.sprtech.quickbite.R;
import com.sprtech.quickbite.order_details;

import java.util.HashMap;
import java.util.List;

public class CustomerNotificationAdapter extends RecyclerView.Adapter<CustomerNotificationAdapter.myViewHolerCustomer> {
    private Context mContext;
    List<CustomerNotificationModel> customerNotificationModels;

    public CustomerNotificationAdapter(Context mContext, List<CustomerNotificationModel> customerNotificationModels) {
        this.mContext = mContext;
        this.customerNotificationModels = customerNotificationModels;
    }

    @NonNull
    @Override
    public myViewHolerCustomer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cus_notif_list,parent,false);
        return new myViewHolerCustomer(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull myViewHolerCustomer holder, int position) {
        CustomerNotificationModel notificationModel = customerNotificationModels.get(position);
        holder.senderName.setText(notificationModel.getSenderName());
        holder.senderMessage.setText(notificationModel.getSenderMessage());
        holder.orderStatus.setText("The status of your order is "+notificationModel.getStatus());
        holder.et.setText("Quickbite Delivery: The estimated time for your order to arrive is "+notificationModel.getEstimatedTime());
        holder.driver.setText("Rider: "+notificationModel.getDriver() + "Rider Contact No. " + notificationModel.getDriverPhoneNumber());
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String QuickBiteUID = "gfA1z5sjGBeWknBSCSQgQrld7BW2";
        ProgressDialog loadingBar;
        loadingBar = new ProgressDialog(mContext);
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(true);

        if(notificationModel.getStatus().equals("On Going")) {
            String textToHighlight = notificationModel.getStatus();
            String replace = "<span style='color:yellow'>" + textToHighlight + "</span>";
            String orig = holder.orderStatus.getText().toString();
            String modifiedText = orig.replaceAll(textToHighlight, replace);
            holder.orderStatus.setText(Html.fromHtml(modifiedText));
        }else if(notificationModel.getStatus().equals("Out for Delivery")) {
            String textToHighlight = notificationModel.getStatus();
            String replace = "<span style='color:red'>" + textToHighlight + "</span>";
            String orig = holder.orderStatus.getText().toString();
            String modifiedText = orig.replaceAll(textToHighlight, replace);
            holder.orderStatus.setText(Html.fromHtml(modifiedText));
        }else if(notificationModel.getStatus().equals("Delivered")) {
            String textToHighlight = notificationModel.getStatus();
            String replace = "<span style='color:green'>" + textToHighlight + "</span>";
            String orig = holder.orderStatus.getText().toString();
            String modifiedText = orig.replaceAll(textToHighlight, replace);
            holder.orderStatus.setText(Html.fromHtml(modifiedText));
            holder.orderReceivedBtn.setVisibility(View.GONE);
        }

        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders");
        DatabaseReference nOrderRef = FirebaseDatabase.getInstance().getReference("NotificationOrder");
        holder.orderReceivedBtn.setOnClickListener(view -> {
            loadingBar.show();
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("Status", "Delivered");
            nOrderRef.child(mAuth.getCurrentUser().getUid()).child(QuickBiteUID).child(notificationModel.getOrderID()).updateChildren(hashMap);
            orderRef.child(QuickBiteUID).child(notificationModel.getOrderID()).updateChildren(hashMap)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            loadingBar.dismiss();
                            Toast.makeText(mContext, "Order Status set to Delivered successfully.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });


//        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("NotificationOrder");
//        orderRef.child(mAuth.getCurrentUser().getUid()).child(QuickBiteUID).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()) {
//                    if(!notificationModel.getOrderStatus().isEmpty()) {
//                        if (notificationModel.getOrderStatus().equals("Delivered")) {
//                            holder.viewBtn.setText("Delete");
//                            holder.viewBtn.setIconResource(R.drawable.baseline_delete_outline_24);
//                            holder.viewBtn.setIconTintResource(R.color.red);
//                            holder.viewBtn.setOnClickListener(view -> {
//                                loadingBar.show();
//                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                                builder.setTitle("Delete")
//                                        .setMessage("Are you sure you want to delete this notification?")
//                                        .setCancelable(true)
//                                        .setPositiveButton("Yes", (dialogInterface, i) -> {
//                                            android.os.Handler handler = new Handler();
//                                            handler.postDelayed(() -> {
//                                                orderRef.child(mAuth.getCurrentUser().getUid()).child(QuickBiteUID).child(notificationModel.getOrderID()).removeValue().addOnCompleteListener(task -> {
//                                                    Toast.makeText(mContext, "Order deleted successfully!", Toast.LENGTH_SHORT).show();
//                                                    loadingBar.dismiss();
//                                                });
//                                            }, 3000);
//                                        })
//                                        .setNegativeButton("No", (dialogInterface, i) -> {
//                                            dialogInterface.cancel();
//                                            loadingBar.dismiss();
//                                        })
//                                        .show();
//                            });
//                        }else {
//
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

//        holder.viewBtn.setOnClickListener(view -> {
//            loadingBar.show();
//            Handler handler = new Handler();
//            handler.postDelayed(() -> {
//                loadingBar.dismiss();
//                Toast.makeText(mContext, "View Details", Toast.LENGTH_SHORT).show();
//            }, 3000);
//
//        });

    }

    @Override
    public int getItemCount() {
        return customerNotificationModels.size();
    }

    public class myViewHolerCustomer extends RecyclerView.ViewHolder {
        private TextView senderName, senderMessage, orderStatus, et, driver;
        private MaterialButton orderReceivedBtn;

        public myViewHolerCustomer(@NonNull View itemView) {
            super(itemView);
            senderName = itemView.findViewById(R.id.senderName);
            senderMessage = itemView.findViewById(R.id.senderMessage);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            et = itemView.findViewById(R.id.et);
            driver = itemView.findViewById(R.id.driver);
            orderReceivedBtn = itemView.findViewById(R.id.orderReceivedBtn);
        }
    }
}
