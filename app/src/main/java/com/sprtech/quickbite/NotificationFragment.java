package com.sprtech.quickbite;

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
import com.sprtech.quickbite.Adapters.CustomerNotificationAdapter;
import com.sprtech.quickbite.Models.CustomerNotificationModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {
    private LinearLayout no_data_layout;
    private RecyclerView ordered_recycle;
    private FirebaseDatabase mDB;
    private DatabaseReference orderRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    LinearLayoutManager linearLayoutManager;
    List<CustomerNotificationModel> customerNotificationModels;
    CustomerNotificationAdapter customerNotificationAdapter;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_notification, container, false);
        initiate();

        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        ordered_recycle.setHasFixedSize(true);
        ordered_recycle.setLayoutManager(linearLayoutManager);

        customerNotificationModels = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mDB = FirebaseDatabase.getInstance();
        orderRef = mDB.getReference("NotificationOrder");
        String QuickBiteUID = "gfA1z5sjGBeWknBSCSQgQrld7BW2";

        if(mUser != null) {
            orderRef.child(mUser.getUid()).child(QuickBiteUID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        customerNotificationModels.clear();
                        ordered_recycle.setVisibility(View.VISIBLE);
                        no_data_layout.setVisibility(View.GONE);
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            CustomerNotificationModel cusNotif = dataSnapshot.getValue(CustomerNotificationModel.class);
                            customerNotificationModels.add(cusNotif);
                        }
                        customerNotificationAdapter = new CustomerNotificationAdapter(getContext(), customerNotificationModels);
                        ordered_recycle.scrollToPosition(customerNotificationModels.size()-1);
                        new Handler().postDelayed(() -> ordered_recycle.smoothScrollToPosition(customerNotificationModels.size()-1),350);
                        ordered_recycle.setAdapter(customerNotificationAdapter);
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
        no_data_layout = v.findViewById(R.id.no_data_layout_cus);
        ordered_recycle = v.findViewById(R.id.Notification_recycle);
    }
}