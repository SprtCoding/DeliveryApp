package com.sprtech.quickbite;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sprtech.quickbite.FCM.FCMNotificationSender;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class MeFragment extends Fragment {
    String cus_name, cus_email, cus_phone, cus_address, cus_photo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_me, container, false);

        MaterialButton logout = v.findViewById(R.id.logout);
        MaterialButton orders = v.findViewById(R.id.orders);
        MaterialButton editBt = v.findViewById(R.id.editBt);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        FirebaseDatabase myDb = FirebaseDatabase.getInstance();
        DatabaseReference userRef = myDb.getReference("Users");
        TextView customer_name = v.findViewById(R.id.customer_name);
        TextView customer_email = v.findViewById(R.id.email);
        TextView customer_phone = v.findViewById(R.id.phone);
        TextView customer_address = v.findViewById(R.id.address);
        CircleImageView customer_profile = v.findViewById(R.id.customer_profile);
        ProgressDialog loadingBar, loadingBar1;

        loadingBar1 = new ProgressDialog(getContext());
        loadingBar1.setTitle("Loading");
        loadingBar1.setMessage("Please wait...");
        loadingBar1.setCanceledOnTouchOutside(true);

        loadingBar = new ProgressDialog(getContext());
        loadingBar.setTitle("Signing Out!");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(true);

        orders.setOnClickListener(view -> {
            loadingBar1.show();
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                Intent i = new Intent(getContext(), my_order.class);
                startActivity(i);
                loadingBar1.dismiss();
            }, 3000);
        });

        editBt.setOnClickListener(view -> {
            loadingBar1.show();
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                Intent i = new Intent(getContext(), edit_profile.class);
                i.putExtra("cus_pic", cus_photo);
                i.putExtra("cus_name", cus_name);
                i.putExtra("cus_email", cus_email);
                i.putExtra("cus_phone", cus_phone);
                i.putExtra("cus_address", cus_address);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                loadingBar1.dismiss();
            }, 3000);
        });

        assert mUser != null;
        userRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    if(snapshot.hasChild("Fullname")) {
                        cus_name = snapshot.child("Fullname").getValue(String.class);
                        cus_email = snapshot.child("Email").getValue(String.class);
                        cus_phone = snapshot.child("Phone").getValue(String.class);
                        cus_address = snapshot.child("Address").getValue(String.class);
                        customer_name.setText(cus_name);
                        customer_email.setText(cus_email);
                        customer_phone.setText(cus_phone);
                        customer_address.setText(cus_address);
                    }
                    if(snapshot.hasChild("userPic")) {
                        cus_photo = snapshot.child("userPic").getValue(String.class);
                        Picasso
                                .get()
                                .load(cus_photo)
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

        logout.setOnClickListener(v1 -> {
            loadingBar.show();
            mAuth.signOut();
            Intent intent = new Intent(getContext(), login_activity.class);
            startActivity(intent);
        });

        return v;
    }
}