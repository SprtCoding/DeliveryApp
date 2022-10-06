package com.sprtech.quickbite.Adapters;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sprtech.quickbite.Models.CartModel;
import com.sprtech.quickbite.R;
import com.sprtech.quickbite.cart;
import com.sprtech.quickbite.food_add_to_cart;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ImageViewHolder> {
    private Context mContext;
    private List<CartModel> cartModels;
    String orderID, ID;

    public CartAdapter(Context mContext, List<CartModel> cartModels) {
        this.mContext = mContext;
        this.cartModels = cartModels;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item,parent,false);
        return new ImageViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        CartModel cart = cartModels.get(position);
        holder.fAName.setText(cart.getfAName());
        holder.quantity_tb.setText(cart.getQuantity());
        holder.fPrice.setText("₱"+cart.getfPrice());
        holder.branchName.setText(cart.getfBrach());
        Picasso
                .get()
                .load(cart.getfPic())
                .fit()
                .centerInside()
                .placeholder(R.drawable.userman)
                .into(holder.fPic);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        FirebaseDatabase mDB = FirebaseDatabase.getInstance();
        DatabaseReference cartRef = mDB.getReference("Cart");
        DatabaseReference orderDetailsRef = mDB.getReference("OrdersDetails");
        DatabaseReference orderPendingRef = mDB.getReference("OrdersPending");
        DatabaseReference customerOrderListRef = mDB.getReference("CustomerOrderList");
        DatabaseReference customerOrderStatus = mDB.getReference("CustomerOrderStatus");

        //check if have others and drinks ads on
        if(cart.getfBrach().equals("ThirTea Ann")) {
            holder.cupSize.setVisibility(View.VISIBLE);
        }else if(cart.getfBrach().equals("Mang Inasal")) {
            holder.cupSize.setVisibility(View.GONE);
        }else if(cart.getfBrach().equals("Kusina ni Lea")) {
            holder.cupSize.setVisibility(View.GONE);
        }else if(cart.getfBrach().equals("Rotonda Cafe")) {
            holder.cupSize.setVisibility(View.VISIBLE);
        }else if(cart.getfBrach().equals("Sizzling 99")) {
            holder.cupSize.setVisibility(View.GONE);
        }else if(cart.getfBrach().equals("Kafe Point")) {
            holder.cupSize.setVisibility(View.VISIBLE);
        }else if(cart.getfBrach().equals("El Sorbetero")) {
            holder.cupSize.setVisibility(View.GONE);
        }else if(cart.getfBrach().equals("Nice & Spice")) {
            holder.cupSize.setVisibility(View.GONE);
        }else if(cart.getfBrach().equals("Thirst Zone")) {
            holder.cupSize.setVisibility(View.GONE);
        }

        int q = Integer.parseInt(cart.getQuantity());
        double p = Double.parseDouble(cart.getfPrice());
        double totals = p * q;

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("total", totals);
        cartRef.child(mUser.getUid()).child(cart.getCartOrderID()).updateChildren(hashMap);

        holder.totalPrice.setText("₱"+String.valueOf(totals));

        holder.increaseBtn.setOnClickListener(view -> {
            String currentValue = holder.quantity_tb.getText().toString();
            int value = Integer.parseInt(currentValue);
            value++;
            holder.quantity_tb.setText(String.valueOf(value));

            HashMap<String, Object> hashMap1 = new HashMap<>();
            hashMap1.put("quantity", String.valueOf(value));
            cartRef.child(mUser.getUid()).child(cart.getCartOrderID()).updateChildren(hashMap1);
        });

        holder.decreaseBtn.setOnClickListener(view -> {
            String currentValue = holder.quantity_tb.getText().toString();
            int value = Integer.parseInt(currentValue);
            value--;
            if(String.valueOf(value).equals("0")) {
                //cartRef.child(mUser.getUid()).child(cart.getfAName()).removeValue();
                holder.quantity_tb.setText("0");
                Toast.makeText(mContext, "Value is already zero!", Toast.LENGTH_SHORT).show();
                holder.decreaseBtn.setEnabled(false);
            }else {
                holder.quantity_tb.setText(String.valueOf(value));
                HashMap<String, Object> hashMap1 = new HashMap<>();
                hashMap1.put("quantity", String.valueOf(value));
                cartRef.child(mUser.getUid()).child(cart.getCartOrderID()).updateChildren(hashMap1);
            }
        });

        String QuickBiteUID = "gfA1z5sjGBeWknBSCSQgQrld7BW2";

        String fName = cart.getfAName();
        String quantity = cart.getQuantity();

        HashMap<String, Object> map = new HashMap<>();
        map.put("orderName", cart.getfName());
        customerOrderStatus.child(mUser.getUid()).child(cart.getfBrach()).child(cart.getfName()).setValue(map);

        String storeName = cart.getfBrach();
        String orderDetailsID = cart.getCartOrderID();
        String currentDate = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(new Date());
        ID = orderDetailsRef.push().getKey();
        holder.delete.setVisibility(View.VISIBLE);
        HashMap<String, Object> hashMap1 = new HashMap<>();
        customerOrderStatus.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int num = (int) snapshot.getChildrenCount();
                    if (num == 1) {
                        if (snapshot.child(cart.getfBrach()).hasChild(cart.getfName())) {
                            int num1 = (int) snapshot.child(cart.getfBrach()).getChildrenCount();
                            if (num1 == 1) {
                                hashMap1.put("status", "Other Branch");
                            } else if (num1 > 1) {
                                hashMap1.put("status", "Same Branch");
                            }
                        }
                        orderID = "Order";
                        hashMap1.put("status", "Same Branch");
                        hashMap1.put("ID", orderID + ID);
                        hashMap1.put("storeName", storeName);
                        hashMap1.put("orderID", orderDetailsID);
                        hashMap1.put("cusID", mUser.getUid());
                        hashMap1.put("orderFoodName", cart.getfName());
                        hashMap1.put("orderDescription", cart.getDescription());
                        hashMap1.put("orderName", fName);
                        hashMap1.put("orderDate", String.valueOf(currentDate));
                        hashMap1.put("orderTime", String.valueOf(currentTime));
                        hashMap1.put("orderPic", cart.getfPic());
                        hashMap1.put("orderQuantity", quantity);
                        hashMap1.put("orderTotal", String.valueOf(totals));
                        //orderDetailsRef.child(QuickBiteUID).child("Order" + ID).child(mUser.getUid()).child(cart.getfName()).removeValue();
                    } else if (num > 1) {
                        if (snapshot.child(cart.getfBrach()).hasChild(cart.getfName())) {
                            int num1 = (int) snapshot.child(cart.getfBrach()).getChildrenCount();
                            if (num1 == 1) {
                                hashMap1.put("status", "Other Branch");
                            } else if (num1 > 1) {
                                hashMap1.put("status", "Same Branch");
                            }
                        }
                        orderID = "Order";
                        hashMap1.put("status", "Other Branch");
                        hashMap1.put("ID", orderID + ID);
                        hashMap1.put("storeName", storeName);
                        hashMap1.put("orderID", orderDetailsID);
                        hashMap1.put("cusID", mUser.getUid());
                        hashMap1.put("orderFoodName", cart.getfName());
                        hashMap1.put("orderDescription", cart.getDescription());
                        hashMap1.put("orderName", fName);
                        hashMap1.put("orderDate", String.valueOf(currentDate));
                        hashMap1.put("orderTime", String.valueOf(currentTime));
                        hashMap1.put("orderPic", cart.getfPic());
                        hashMap1.put("orderQuantity", quantity);
                        hashMap1.put("orderTotal", String.valueOf(totals));
                        //orderDetailsRef.child(QuickBiteUID).child("Order" + ID).child(mUser.getUid()).child(cart.getfName()).removeValue();
                    }

                    orderDetailsRef.child(QuickBiteUID).child(orderID + ID).child(mUser.getUid()).child(cart.getfName()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()) {
                                orderDetailsRef.child(QuickBiteUID).child(orderID + ID).child(mUser.getUid()).child(cart.getfName()).setValue(hashMap1);
                                cartRef.child(mUser.getUid()).child(orderDetailsID).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()) {
                                            orderPendingRef.child(mUser.getUid()).child(orderDetailsID).setValue(hashMap1);
                                            customerOrderListRef.child(mUser.getUid()).child(orderDetailsID).setValue(hashMap1);
                                        }else {
                                            orderPendingRef.child(mUser.getUid()).child(orderDetailsID).removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

//                    //check items
//                    holder.check.setOnClickListener(view -> {
//                        if (!holder.check.isChecked()) {
//                            holder.delete.setVisibility(View.GONE);
//                            String orderDetailsID = cart.getCartOrderID();
//                            holder.check.setChecked(false);
//                            cartRef.child(mUser.getUid()).child(orderDetailsID).addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                    if(snapshot.exists()) {
//                                        orderPendingRef.child(mUser.getUid()).child(orderDetailsID).removeValue();
//                                        customerOrderListRef.child(mUser.getUid()).child(orderDetailsID).removeValue();
//                                        customerOrderStatus.child(mUser.getUid()).child(cart.getfBrach()).child(cart.getfName()).removeValue();
//                                        orderDetailsRef.child(QuickBiteUID).child(orderID + ID).child(mUser.getUid()).child(cart.getfName()).removeValue();
//                                    }else {
//                                        orderPendingRef.child(mUser.getUid()).child(orderDetailsID).removeValue();
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//                                    Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }else {
//                            holder.check.setChecked(true);
//                            holder.delete.setVisibility(View.VISIBLE);
//                            HashMap<String, Object> map = new HashMap<>();
//                            map.put("orderName", cart.getfName());
//                            customerOrderStatus.child(mUser.getUid()).child(cart.getfBrach()).child(cart.getfName()).setValue(map);
//                            orderDetailsRef.child(QuickBiteUID).child(orderID + ID).addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                    if (!snapshot.exists()) {
//                                        cartRef.child(mUser.getUid()).child(orderDetailsID).addValueEventListener(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                if(snapshot.exists()) {
//                                                    orderDetailsRef.child(QuickBiteUID).child(orderID + ID).child(mUser.getUid()).child(cart.getfName()).setValue(hashMap1);
//                                                    orderPendingRef.child(mUser.getUid()).child(orderDetailsID).setValue(hashMap1);
//                                                    customerOrderListRef.child(mUser.getUid()).child(orderDetailsID).setValue(hashMap1);
//                                                }else {
//                                                    orderPendingRef.child(mUser.getUid()).child(orderDetailsID).removeValue();
//                                                }
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError error) {
//                                                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//                                    Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
//                                }
//                            });
//                        }
//                    });

                    ProgressDialog loadingBar;
                    loadingBar = new ProgressDialog(mContext);
                    loadingBar.setTitle("Deleting Order!");
                    loadingBar.setMessage("Please wait...");
                    loadingBar.setCanceledOnTouchOutside(true);

                    //delete
                    holder.delete.setOnClickListener(view -> {
                        loadingBar.show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("Delete")
                                .setMessage("Are you sure you want to delete " + cart.getfName() + "?")
                                .setCancelable(true)
                                .setPositiveButton("Yes", (dialogInterface, i) -> {
                                    android.os.Handler handler = new Handler();
                                    handler.postDelayed(() -> {
                                        orderPendingRef.child(mUser.getUid()).child(orderDetailsID).removeValue();
                                        customerOrderListRef.child(mUser.getUid()).child(orderDetailsID).removeValue();
                                        customerOrderStatus.child(mUser.getUid()).child(cart.getfBrach()).child(cart.getfName()).removeValue();
                                        orderDetailsRef.child(QuickBiteUID).child(orderID + ID).child(mUser.getUid()).child(cart.getfName()).removeValue();
                                        cartRef.child(mUser.getUid()).child(cart.getCartOrderID()).removeValue().addOnCompleteListener(task -> {
                                            Toast.makeText(mContext, cart.getfName() + " Deleted successfully!", Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                        });
                                    }, 3000);
                                })
                                .setNegativeButton("No", (dialogInterface, i) -> {
                                    dialogInterface.cancel();
                                    loadingBar.dismiss();
                                })
                                .show();
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //size of cup drinks

        holder.bt_regular_size.setOnClickListener(view -> {
            holder.bt_unselect_size.setVisibility(View.VISIBLE);
            holder.bt_regular_size.setCardBackgroundColor(Color.rgb(51, 153, 51));
            holder.bt_medium_size.setCardBackgroundColor(Color.rgb(136, 207, 251));
            holder.bt_lg_size.setCardBackgroundColor(Color.rgb(136, 207, 251));
        });

        holder.bt_medium_size.setOnClickListener(view -> {
            holder.bt_unselect_size.setVisibility(View.VISIBLE);
            holder.bt_medium_size.setCardBackgroundColor(Color.rgb(51, 153, 51));
            holder.bt_regular_size.setCardBackgroundColor(Color.rgb(136, 207, 251));
            holder.bt_lg_size.setCardBackgroundColor(Color.rgb(136, 207, 251));
        });

        holder.bt_lg_size.setOnClickListener(view -> {
            holder.bt_unselect_size.setVisibility(View.VISIBLE);
            holder.bt_lg_size.setCardBackgroundColor(Color.rgb(51, 153, 51));
            holder.bt_regular_size.setCardBackgroundColor(Color.rgb(136, 207, 251));
            holder.bt_medium_size.setCardBackgroundColor(Color.rgb(136, 207, 251));
        });

        //unselect cup size
        holder.bt_unselect_size.setOnClickListener(view -> {
            holder.bt_unselect_size.setVisibility(View.GONE);

            holder.bt_regular_size.setCardBackgroundColor(Color.rgb(136, 207, 251));
            holder.bt_medium_size.setCardBackgroundColor(Color.rgb(136, 207, 251));
            holder.bt_lg_size.setCardBackgroundColor(Color.rgb(136, 207, 251));
        });

    }

    @Override
    public int getItemCount() {
        return cartModels.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView fPic;
        TextView fAName, fPrice, quantity_tb, totalPrice, bt_unselect_size, branchName, regular_tv_size,
                medium_tv_size, lg_tv_size;
        CardView decreaseBtn, increaseBtn, bt_regular_size, bt_medium_size, bt_lg_size;
        CheckBox check;
        LinearLayout cupSize;
        MaterialButton delete;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            delete = itemView.findViewById(R.id.delete);
            fPic = itemView.findViewById(R.id.fPic);
            fAName = itemView.findViewById(R.id.fAName);
            fPrice = itemView.findViewById(R.id.fPrice);
            quantity_tb = itemView.findViewById(R.id.quantity_tb);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            decreaseBtn = itemView.findViewById(R.id.decreaseBtn);
            increaseBtn = itemView.findViewById(R.id.increaseBtn);
//            check = itemView.findViewById(R.id.check);
            cupSize = itemView.findViewById(R.id.cupSize);
            bt_regular_size = itemView.findViewById(R.id.bt_regular_size);
            bt_medium_size = itemView.findViewById(R.id.bt_medium_size);
            bt_lg_size = itemView.findViewById(R.id.bt_lg_size);
            bt_unselect_size = itemView.findViewById(R.id.bt_unselect_size);
            branchName = itemView.findViewById(R.id.branchName);
            regular_tv_size = itemView.findViewById(R.id.regular_tv_size);
            medium_tv_size = itemView.findViewById(R.id.medium_tv_size);
            lg_tv_size = itemView.findViewById(R.id.lg_tv_size);
        }
    }

}
