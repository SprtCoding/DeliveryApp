package com.sprtech.quickbite;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminMeFragment extends Fragment {
    View v;
    private ProgressDialog loadingBar1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_admin_me, container, false);
        MaterialButton logout = v.findViewById(R.id.logout);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        FirebaseDatabase myDb = FirebaseDatabase.getInstance();
        DatabaseReference userRef = myDb.getReference("Users");
        TextView customer_name = v.findViewById(R.id.customer_name);
        CircleImageView customer_profile = v.findViewById(R.id.customer_profile);
        ProgressDialog loadingBar;

        loadingBar1 = new ProgressDialog(getContext());
        loadingBar1.setTitle("Loading");
        loadingBar1.setMessage("Please wait...");
        loadingBar1.setCanceledOnTouchOutside(true);

        loadingBar = new ProgressDialog(getContext());
        loadingBar.setTitle("Signing Out!");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(true);

        if(mUser != null) {
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

            logout.setOnClickListener(v1 -> {
                loadingBar.show();
                mAuth.signOut();
                Intent intent = new Intent(getContext(), login_activity.class);
                startActivity(intent);
            });

        }

        return v;
    }
}