package com.sprtech.quickbite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.sprtech.quickbite.Adapters.CartAdapter;
import com.sprtech.quickbite.FCM.FCMNotificationSender;
import com.sprtech.quickbite.Models.CartModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class cart extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDB;
    private DatabaseReference cartRef, orderRef, userRef;
    private RecyclerView cart_recycle;
    private TextView total, delivery_fee, total_fee, address;
    private ImageView backBtn;
    private MaterialButton btCheckOut;
    private LinearLayout no_data_layout, have_cart_layout;
    LinearLayoutManager linearLayoutManager;
    List<CartModel> cartModels;
    CartAdapter cartAdapter;
    List<String> orderNameList;
    private ProgressDialog loadingBar;
    double totalFees;
    String names, totalPrice, MyAddress, cusName, cusPic, cusPhone, userToken, ID, cartOrderID;
    double totalDeliveryFee = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        var();
        mDB = FirebaseDatabase.getInstance();
        cartRef = mDB.getReference("Cart");
        orderRef = mDB.getReference("Orders");
        userRef = mDB.getReference("Users");
        DatabaseReference orderPendingRef = mDB.getReference("OrdersPending");
        DatabaseReference deliveryFeeRef = mDB.getReference("TotalDeliveryFee");

        totalPrice = total_fee.getText().toString();

        String QuickBiteUID = "gfA1z5sjGBeWknBSCSQgQrld7BW2";

        loadingBar = new ProgressDialog(this);
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(true);

        cartModels = new ArrayList<>();
        orderNameList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        linearLayoutManager = new LinearLayoutManager(this);
        cart_recycle.setHasFixedSize(true);
        cart_recycle.setLayoutManager(linearLayoutManager);

        if(mUser != null) {
            cartRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        no_data_layout.setVisibility(View.GONE);
                        have_cart_layout.setVisibility(View.VISIBLE);
                        cartModels.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            CartModel cart = dataSnapshot.getValue(CartModel.class);
                            cartOrderID = cart.getCartOrderID();
                            names = cart.getfAName();
                            cartModels.add(cart);
                        }
                        cartAdapter = new CartAdapter(cart.this, cartModels);
                        cart_recycle.setAdapter(cartAdapter);

                    }else {
                        no_data_layout.setVisibility(View.VISIBLE);
                        have_cart_layout.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(cart.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            orderPendingRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        btCheckOut.setEnabled(true);
                        double sum = 0;
                        double singleOrderFee = 37;
                        double multipleOrderFeeLessThan = 50;
                        double multipleOrderFeeGreaterThan = 60;
                        double multipleOrderFeeGreaterThan1k = 100;
                        double additionalStoreFee = 25;

                        for(DataSnapshot ds : snapshot.getChildren()) {
                            Map<String, Object> map = (Map<String, Object>) ds.getValue();
                            assert map != null;
                            Object totalOrderCount = snapshot.getChildrenCount();
                            Object orderStatus = map.get("status");
                            Object totalPrice = map.get("orderTotal");
                            Object orderQuantity = map.get("orderQuantity");
                            ID = (String) map.get("ID");
                            int orderCount = Integer.parseInt(String.valueOf(totalOrderCount));
                            int quantity = Integer.parseInt(String.valueOf(orderQuantity));
                            double totalPriceValue = Double.parseDouble(String.valueOf(totalPrice));
                            String stat = String.valueOf(orderStatus);
                            sum += totalPriceValue;
                            if(orderCount == 1) {
                                if(quantity == 1) {
                                    totalDeliveryFee = singleOrderFee;
                                    totalFees = sum + totalDeliveryFee;
                                    total.setText("₱"+String.valueOf(sum));
                                    delivery_fee.setText("₱"+String.valueOf(singleOrderFee));
                                    total_fee.setText("₱"+String.valueOf(totalFees));
                                }else {
                                    if(sum <= 200) {
                                        totalDeliveryFee = multipleOrderFeeLessThan;
                                        totalFees = sum + totalDeliveryFee;
                                        total.setText("₱"+String.valueOf(sum));
                                        delivery_fee.setText("₱"+String.valueOf(multipleOrderFeeLessThan));
                                        total_fee.setText("₱"+String.valueOf(totalFees));
                                    }else if(sum > 200 && sum <= 999) {
                                        totalDeliveryFee = multipleOrderFeeGreaterThan;
                                        totalFees = sum + totalDeliveryFee;
                                        total.setText("₱"+String.valueOf(sum));
                                        delivery_fee.setText("₱"+String.valueOf(multipleOrderFeeGreaterThan));
                                        total_fee.setText("₱"+String.valueOf(totalFees));
                                    }
                                    else if(sum >= 1000) {
                                        totalDeliveryFee = multipleOrderFeeGreaterThan1k;
                                        totalFees = sum + totalDeliveryFee;
                                        total.setText("₱"+String.valueOf(sum));
                                        delivery_fee.setText("₱"+String.valueOf(multipleOrderFeeGreaterThan1k));
                                        total_fee.setText("₱"+String.valueOf(totalFees));
                                    }
                                }
                            }else if(orderCount > 1){
                                if(stat.equals("Same Branch")) {
                                    if(sum <= 200) {
                                        totalDeliveryFee = multipleOrderFeeLessThan;
                                        totalFees = sum + totalDeliveryFee;
                                        total.setText("₱"+String.valueOf(sum));
                                        delivery_fee.setText("₱"+String.valueOf(multipleOrderFeeLessThan));
                                        total_fee.setText("₱"+String.valueOf(totalFees));
                                    }else if(sum >= 200 && sum <= 999) {
                                        totalDeliveryFee = multipleOrderFeeGreaterThan;
                                        totalFees = sum + totalDeliveryFee;
                                        total.setText("₱"+String.valueOf(sum));
                                        delivery_fee.setText("₱"+String.valueOf(multipleOrderFeeGreaterThan));
                                        total_fee.setText("₱"+String.valueOf(totalFees));
                                    }else if(sum >= 1000) {
                                        totalDeliveryFee = multipleOrderFeeGreaterThan1k;
                                        totalFees = sum + totalDeliveryFee;
                                        total.setText("₱"+String.valueOf(sum));
                                        delivery_fee.setText("₱"+String.valueOf(multipleOrderFeeGreaterThan1k));
                                        total_fee.setText("₱"+String.valueOf(totalFees));
                                    }
                                }else if(stat.equals("Other Branch")) {
                                    if(sum <= 200) {
                                        totalDeliveryFee = multipleOrderFeeLessThan + additionalStoreFee;
                                        totalFees = sum + totalDeliveryFee;
                                        total.setText("₱"+String.valueOf(sum));
                                        delivery_fee.setText("₱"+String.valueOf(multipleOrderFeeLessThan) + " + " + String.valueOf(additionalStoreFee));
                                        total_fee.setText("₱"+String.valueOf(totalFees));
                                    }else if(sum >= 200 && sum <= 999) {
                                        totalDeliveryFee = multipleOrderFeeGreaterThan + additionalStoreFee;
                                        totalFees = sum + totalDeliveryFee;
                                        total.setText("₱"+String.valueOf(sum));
                                        delivery_fee.setText("₱"+String.valueOf(multipleOrderFeeGreaterThan) + " + " + String.valueOf(additionalStoreFee));
                                        total_fee.setText("₱"+String.valueOf(totalFees));
                                    }else if(sum >= 1000) {
                                        totalDeliveryFee = multipleOrderFeeGreaterThan1k + additionalStoreFee;
                                        totalFees = sum + totalDeliveryFee;
                                        total.setText("₱"+String.valueOf(sum));
                                        delivery_fee.setText("₱"+String.valueOf(multipleOrderFeeGreaterThan1k) + " + " + String.valueOf(additionalStoreFee));
                                        total_fee.setText("₱"+String.valueOf(totalFees));
                                    }
                                }
                            }
                        }

                    }else {
                        btCheckOut.setEnabled(false);
                        total.setText("₱0.0");
                        delivery_fee.setText("₱0.0");
                        total_fee.setText("₱0.0");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(cart.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            //get delivery address
            userRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        cusName = snapshot.child("Fullname").getValue(String.class);
                        cusPic = snapshot.child("userPic").getValue(String.class);
                        cusPhone = snapshot.child("Phone").getValue(String.class);
                        if(snapshot.hasChild("Address")) {
                            MyAddress = snapshot.child("Address").getValue(String.class);
                            address.setText(MyAddress);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(cart.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        backBtn.setOnClickListener(view -> {
            Intent i = new Intent(this, customer_dashboard.class);
            startActivity(i);
            finish();
        });

        btCheckOut.setOnClickListener(view -> {
            loadingBar.show();

            orderPendingRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    btCheckOut.setEnabled(snapshot.exists());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(cart.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            String currentDate = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(new Date());

            String deliveryID = deliveryFeeRef.push().getKey();
            String orderID = orderRef.push().getKey();
            HashMap<String, Object> map = new HashMap<>();
            map.put("customerName", cusName);
            map.put("orderDate", String.valueOf(currentDate));
            map.put("orderTime", String.valueOf(currentTime));
            map.put("deliveryFee", String.valueOf(totalDeliveryFee));
            map.put("deliveryFeeID", String.valueOf(cartOrderID));
            assert deliveryID != null;
            assert orderID != null;
            deliveryFeeRef.child(QuickBiteUID).child(cartOrderID).updateChildren(map);

            HashMap<String, Object> hashMap1 = new HashMap<>();
            hashMap1.put("cusID", mUser.getUid());
            hashMap1.put("orderID", orderID);
            hashMap1.put("cartOrderID", cartOrderID);
            hashMap1.put("CusOrderID", ID);
            hashMap1.put("cusName", cusName);
            hashMap1.put("orderDate", String.valueOf(currentDate));
            hashMap1.put("orderTime", String.valueOf(currentTime));
            hashMap1.put("cusAddress", MyAddress);
            hashMap1.put("deliveryFee", String.valueOf(totalDeliveryFee));
            hashMap1.put("cusPic", cusPic);
            hashMap1.put("orderStatus", "FastFood");
            hashMap1.put("Status", "");
            hashMap1.put("cusPhone", cusPhone);
            hashMap1.put("totalOrderFee", String.valueOf(totalFees));

            orderRef.child(QuickBiteUID).child(orderID).updateChildren(hashMap1)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            DatabaseReference customerOrderStatus = mDB.getReference("CustomerOrderStatus");
                            btCheckOut.setEnabled(false);
                            customerOrderStatus.child(mUser.getUid()).removeValue();
                            cartRef.child(mUser.getUid()).removeValue();
                            orderPendingRef.child(mUser.getUid()).removeValue();
                            Toast.makeText(cart.this, "CheckOut successfully!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(cart.this, customer_dashboard.class);
                            startActivity(i);
                            finish();
                        }
                    });

            sendNotification(QuickBiteUID, cusName);

        });
    }

    private void sendNotification(String QuickBiteUID, String cusName) {
        FirebaseDatabase.getInstance().getReference("UserToken").child(QuickBiteUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    userToken = snapshot.child("token").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(cart.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            FCMNotificationSender.sendNotification(
                    getApplicationContext(),
                    userToken,
                    "QuickBite",
                    cusName + " Order Foods"
            );
        }, 3000);
    }

    private void var() {
        cart_recycle = findViewById(R.id.cart_recycle);
        total = findViewById(R.id.total);
        backBtn = findViewById(R.id.backBtn);
        delivery_fee = findViewById(R.id.delivery_fee);
        total_fee = findViewById(R.id.total_fee);
        btCheckOut = findViewById(R.id.btCheckOut);
        address = findViewById(R.id.address);
        have_cart_layout = findViewById(R.id.have_cart_layout);
        no_data_layout = findViewById(R.id.no_data_layout);
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}