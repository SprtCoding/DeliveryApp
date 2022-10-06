package com.sprtech.quickbite;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sprtech.quickbite.Adapters.OrderAdapter;
import com.sprtech.quickbite.Models.OrderModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminNotificationFragment extends Fragment {
    private LinearLayout no_data_layout;
    private RecyclerView ordered_recycle;
    private FirebaseDatabase mDB;
    private DatabaseReference orderRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    LinearLayoutManager linearLayoutManager;
    List<OrderModel> orderModels;
    OrderAdapter orderAdapter;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_admin_notification, container, false);
        initiate();
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        ordered_recycle.setHasFixedSize(true);
        ordered_recycle.setLayoutManager(linearLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        orderModels = new ArrayList<>();

        mDB = FirebaseDatabase.getInstance();
        orderRef = mDB.getReference("Orders");

        if(mUser != null) {

            orderRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        ordered_recycle.setVisibility(View.VISIBLE);
                        no_data_layout.setVisibility(View.GONE);
                        orderModels.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            OrderModel order = dataSnapshot.getValue(OrderModel.class);
                            orderModels.add(order);
                            if(order.getStatus().equals("Delivered")) {
                                orderRef.child(mAuth.getCurrentUser().getUid()).child(order.getOrderID()).removeValue();
                            }
                        }
                        orderAdapter = new OrderAdapter(getContext(), orderModels);
                        ordered_recycle.scrollToPosition(orderModels.size()-1);
                        new Handler().postDelayed(() -> ordered_recycle.smoothScrollToPosition(orderModels.size()-1),350);
                        ordered_recycle.setAdapter(orderAdapter);
                    }else {
                        ordered_recycle.setVisibility(View.GONE);
                        no_data_layout.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        return v;
    }

    private void initiate() {
        no_data_layout = v.findViewById(R.id.no_data_layout);
        ordered_recycle = v.findViewById(R.id.ordered_recycle);

    }
}