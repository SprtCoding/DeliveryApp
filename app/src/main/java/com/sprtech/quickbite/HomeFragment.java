package com.sprtech.quickbite;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sprtech.quickbite.FCM.FCMNotificationSender;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {
    String MyAddress, cusName, cusPic, cusPhone, QuickBiteUID = "gfA1z5sjGBeWknBSCSQgQrld7BW2", userToken;
    private ProgressDialog loadingBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        FirebaseDatabase myDb = FirebaseDatabase.getInstance();
        DatabaseReference userRef = myDb.getReference("Users");
        DatabaseReference orderRef = myDb.getReference("Orders");
        TextView customer_name = v.findViewById(R.id.customer_name);
        TextInputEditText groceryET = v.findViewById(R.id.groceryET);
        MaterialButton buyBtn = v.findViewById(R.id.buyBtn);
        CircleImageView customer_profile = v.findViewById(R.id.customer_profile);

        loadingBar = new ProgressDialog(getContext());
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(true);

        String currentDate = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(new Date());

        if(mUser != null) {
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
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            buyBtn.setOnClickListener(view -> {
                loadingBar.show();
                String listOfGroceries = groceryET.getText().toString();
                if(TextUtils.isEmpty(listOfGroceries)) {
                    Toast.makeText(getContext(), "Input field is empty!", Toast.LENGTH_SHORT).show();
                }else {
                    String orderID = orderRef.push().getKey();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("cusID", mUser.getUid());
                    map.put("orderID", orderID);
                    map.put("cusName", cusName);
                    map.put("orderDate", String.valueOf(currentDate));
                    map.put("orderTime", String.valueOf(currentTime));
                    map.put("cusAddress", MyAddress);
                    map.put("orderStatus", "Grocery");
                    map.put("cusPic", cusPic);
                    map.put("Status", "");
                    map.put("cusPhone", cusPhone);
                    map.put("order", listOfGroceries);
                    assert orderID != null;
                    orderRef.child(QuickBiteUID).child(orderID).updateChildren(map)
                            .addOnCompleteListener(task -> {
                                if(task.isSuccessful()) {
                                    Handler handler = new Handler();
                                    handler.postDelayed(() -> {
                                        sendNotification(QuickBiteUID, cusName);
                                        loadingBar.dismiss();
                                        groceryET.setText("");
                                        Toast.makeText(getContext(), "Order Successfully!", Toast.LENGTH_SHORT).show();
                                    }, 3000);

                                }
                            });
                }
            });
        }

        //branch button
        CardView ThirTeaAnn_bt, knlBtn, mInasalBtn, SizzlingBtn, ElSorbeteroBtn, kapeBtn, rotondaBtn, thirstBtn, niceSpiceBtn;
        ThirTeaAnn_bt = v.findViewById(R.id.ThirTeaAnn_bt);
        knlBtn = v.findViewById(R.id.knlBtn);
        mInasalBtn = v.findViewById(R.id.mInasalBtn);
        SizzlingBtn = v.findViewById(R.id.SizzlingBtn);
        ElSorbeteroBtn = v.findViewById(R.id.ElSorbeteroBtn);
        kapeBtn = v.findViewById(R.id.kapeBtn);
        rotondaBtn = v.findViewById(R.id.rotondaBtn);
        thirstBtn = v.findViewById(R.id.thirstBtn);
        niceSpiceBtn = v.findViewById(R.id.niceSpiceBtn);

        ThirTeaAnn_bt.setOnClickListener(v1 -> {
            Intent intent = new Intent(getContext(), thirTeaAnn_list.class);
            startActivity(intent);
        });

        knlBtn.setOnClickListener(v1 -> {
            Intent intent = new Intent(getContext(), KNL_list.class);
            startActivity(intent);
        });

        mInasalBtn.setOnClickListener(v1 -> {
            Intent intent = new Intent(getContext(), inasal_list.class);
            startActivity(intent);
        });

        SizzlingBtn.setOnClickListener(v1 -> {
            Intent intent = new Intent(getContext(), sizzling_list.class);
            startActivity(intent);
        });

        ElSorbeteroBtn.setOnClickListener(v1 -> {
            Intent intent = new Intent(getContext(), ElSorbetero_list.class);
            startActivity(intent);
        });

        kapeBtn.setOnClickListener(v1 -> {
            Intent intent = new Intent(getContext(), kape_point_list.class);
            startActivity(intent);
        });

        rotondaBtn.setOnClickListener(v1 -> {
            Intent intent = new Intent(getContext(), rotonda_list.class);
            startActivity(intent);
        });

        thirstBtn.setOnClickListener(v1 -> {
            Intent intent = new Intent(getContext(), crcle_list.class);
            startActivity(intent);
        });

        niceSpiceBtn.setOnClickListener(v1 -> {
            Intent intent = new Intent(getContext(), nice_spice_list.class);
            startActivity(intent);
        });

        userRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    if(snapshot.hasChild("Fullname")) {
                        String name = snapshot.child("Fullname").getValue(String.class);
                        customer_name.setText(name);
                    }
                    if(snapshot.hasChild("userPic")) {
                        String pic = snapshot.child("userPic").getValue(String.class);
                        Picasso
                                .get()
                                .load(pic)
                                .fit()
                                .centerCrop()
                                .placeholder(R.drawable.userman)
                                .into(customer_profile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return v;
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
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            FCMNotificationSender.sendNotification(
                    getContext(),
                    userToken,
                    "QuickBite",
                    cusName + " Order Foods"
            );
        }, 3000);
    }
}