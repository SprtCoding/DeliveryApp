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

public class thirTeaAnn_list extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase myDB;
    private DatabaseReference foodRef;
    private ImageView backBtn;
    private RecyclerView jollibee_recycle;
    List<FoodModel> foodModelList;
    JollyAdapter jollyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thir_tea_ann_list);
        var();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        myDB = FirebaseDatabase.getInstance();
        foodRef = myDB.getReference("Foods");

        foodModelList = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(thirTeaAnn_list.this, 2);
        jollibee_recycle.setLayoutManager(layoutManager);
        jollibee_recycle.setHasFixedSize(true);

        foodRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    foodModelList.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        FoodModel food = dataSnapshot.getValue(FoodModel.class);
                        if(food.getBranch().equals("ThirTea Ann")){
                            foodModelList.add(food);
                        }
                    }
                    jollyAdapter = new JollyAdapter(thirTeaAnn_list.this, foodModelList);
                    jollibee_recycle.setAdapter(jollyAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(thirTeaAnn_list.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(thirTeaAnn_list.this, customer_dashboard.class);
            startActivity(intent);
            finish();
        });
    }

    private void var() {
        backBtn = findViewById(R.id.backBtn);

        jollibee_recycle = findViewById(R.id.jollibee_recycle);
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}