package com.sprtech.quickbite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sprtech.quickbite.Adapters.MyOrderAdapter;
import com.sprtech.quickbite.Models.OrderListModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class my_order extends AppCompatActivity {
    private RecyclerView my_order_recycle;
    private LinearLayout no_data;
    private FirebaseDatabase mDB;
    private DatabaseReference cusOrderRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ImageView backBtn;
    LinearLayoutManager linearLayoutManager;
    List<OrderListModel> orderListModels;
    MyOrderAdapter myOrderAdapter;
    String QuickBiteUID = "gfA1z5sjGBeWknBSCSQgQrld7BW2", orderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        initiate();

        linearLayoutManager = new LinearLayoutManager(my_order.this);
        my_order_recycle.setHasFixedSize(true);
        my_order_recycle.setLayoutManager(linearLayoutManager);

        orderListModels = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mDB = FirebaseDatabase.getInstance();
        cusOrderRef = mDB.getReference("CustomerOrderList");

        if(mUser != null) {
            cusOrderRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        orderListModels.clear();
                        my_order_recycle.setVisibility(View.VISIBLE);
                        no_data.setVisibility(View.GONE);
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            OrderListModel order = dataSnapshot.getValue(OrderListModel.class);
                            orderListModels.add(order);
                            if(snapshot.child(order.getOrderID()).hasChild("OrderStatus")) {
                                if(order.getOrderStatus().equals("Delivered")) {
                                    cusOrderRef.child(mUser.getUid()).removeValue();
                                }
                            }
                        }
                        myOrderAdapter = new MyOrderAdapter(my_order.this, orderListModels);
                        my_order_recycle.setAdapter(myOrderAdapter);
                    }else {
                        my_order_recycle.setVisibility(View.GONE);
                        no_data.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(my_order.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        backBtn.setOnClickListener(view -> finish());

    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    private void initiate() {
        my_order_recycle = findViewById(R.id.my_order_recycle);
        no_data = findViewById(R.id.no_data);
        backBtn = findViewById(R.id.backBtn);
    }
}