package com.sprtech.quickbite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sprtech.quickbite.Adapters.JollyAdapter;
import com.sprtech.quickbite.Models.FoodModel;

import java.util.ArrayList;
import java.util.List;

public class kape_point_list extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase myDB;
    private DatabaseReference foodRef;
    private ImageView backBtn;
    private RecyclerView knl_recycle;
    List<FoodModel> foodModelList;
    JollyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kape_point_list);
        var();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        myDB = FirebaseDatabase.getInstance();
        foodRef = myDB.getReference("Foods");

        foodModelList = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(kape_point_list.this, 2);
        knl_recycle.setLayoutManager(layoutManager);
        knl_recycle.setHasFixedSize(true);

        foodRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    foodModelList.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        FoodModel food = dataSnapshot.getValue(FoodModel.class);
                        if(food.getBranch().equals("Kafe Point")){
                            foodModelList.add(food);
                        }
                    }
                    myAdapter = new JollyAdapter(kape_point_list.this, foodModelList);
                    knl_recycle.setAdapter(myAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(kape_point_list.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(kape_point_list.this, customer_dashboard.class);
            startActivity(intent);
            finish();
        });
    }

    private void var() {
        backBtn = findViewById(R.id.backBtn);

        knl_recycle = findViewById(R.id.knl_recycle);
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}