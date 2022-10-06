package com.sprtech.quickbite;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
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
import com.sprtech.quickbite.Adapters.FoodAdapter;
import com.sprtech.quickbite.Models.FoodModel;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeFragment extends Fragment {
    private MaterialButton ThirTeaBtn, InasalBtn, allBtn, kusinaBtn, RotondaBtn, SizzlingBtn,
            KafeBtn, SorbeteroBtn, nice_spiceBtn, ThirstBtn;
    private LinearLayout no_data_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_home, container, false);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        FirebaseDatabase myDb = FirebaseDatabase.getInstance();
        DatabaseReference userRef = myDb.getReference("Users");
        DatabaseReference foodRef = myDb.getReference("Foods");
        TextView customer_name = v.findViewById(R.id.customer_name);
        RecyclerView item_recycle = v.findViewById(R.id.item_recycle);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        item_recycle.setLayoutManager(layoutManager);
        item_recycle.setHasFixedSize(true);
        List<FoodModel> foodModelList;
        no_data_layout = v.findViewById(R.id.no_data_layout);
        ThirTeaBtn = v.findViewById(R.id.ThirTeaBtn);
        InasalBtn = v.findViewById(R.id.InasalBtn);
        allBtn = v.findViewById(R.id.allBtn);
        kusinaBtn = v.findViewById(R.id.kusinaBtn);
        RotondaBtn = v.findViewById(R.id.RotondaBtn);
        SizzlingBtn = v.findViewById(R.id.SizzlingBtn);
        KafeBtn = v.findViewById(R.id.KafeBtn);
        SorbeteroBtn = v.findViewById(R.id.SorbeteroBtn);
        nice_spiceBtn = v.findViewById(R.id.nice_spiceBtn);
        ThirstBtn = v.findViewById(R.id.ThirstBtn);

        foodModelList = new ArrayList<>();

        allBtn.setOnClickListener(view -> {
            foodRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        item_recycle.setVisibility(View.VISIBLE);
                        no_data_layout.setVisibility(View.GONE);
                        foodModelList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            FoodModel food = dataSnapshot.getValue(FoodModel.class);
                            foodModelList.add(food);
                        }
                        FoodAdapter mAdapter = new FoodAdapter(getContext(), foodModelList);
                        item_recycle.setAdapter(mAdapter);
                    }else {
                        item_recycle.setVisibility(View.GONE);
                        no_data_layout.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            allBtn.setBackgroundColor(Color.rgb(136, 207, 251));
            allBtn.setTextColor(Color.rgb(255, 255, 255));
            InasalBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            InasalBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            InasalBtn.setTextColor(Color.rgb(136, 207, 251));
            ThirTeaBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            ThirTeaBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            ThirTeaBtn.setTextColor(Color.rgb(136, 207, 251));
            kusinaBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            kusinaBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            kusinaBtn.setTextColor(Color.rgb(136, 207, 251));
            RotondaBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            RotondaBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            RotondaBtn.setTextColor(Color.rgb(136, 207, 251));
            SizzlingBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            SizzlingBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            SizzlingBtn.setTextColor(Color.rgb(136, 207, 251));
            KafeBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            KafeBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            KafeBtn.setTextColor(Color.rgb(136, 207, 251));
            SorbeteroBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            SorbeteroBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            SorbeteroBtn.setTextColor(Color.rgb(136, 207, 251));
            nice_spiceBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            nice_spiceBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            nice_spiceBtn.setTextColor(Color.rgb(136, 207, 251));
            ThirstBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            ThirstBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            ThirstBtn.setTextColor(Color.rgb(136, 207, 251));
        });

        ThirTeaBtn.setOnClickListener(view -> {
            foodRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        item_recycle.setVisibility(View.VISIBLE);
                        no_data_layout.setVisibility(View.GONE);
                        foodModelList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            FoodModel food = dataSnapshot.getValue(FoodModel.class);
                            String thirtea = ThirTeaBtn.getText().toString();
                            if(food.getBranch().equals(thirtea)) {
                                foodModelList.add(food);
                            }
                        }
                        FoodAdapter mAdapter = new FoodAdapter(getContext(), foodModelList);
                        item_recycle.setAdapter(mAdapter);
                    }else {
                        item_recycle.setVisibility(View.GONE);
                        no_data_layout.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            ThirTeaBtn.setBackgroundColor(Color.rgb(136, 207, 251));
            ThirTeaBtn.setTextColor(Color.rgb(255, 255, 255));
            InasalBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            InasalBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            InasalBtn.setTextColor(Color.rgb(136, 207, 251));
            allBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            allBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            allBtn.setTextColor(Color.rgb(136, 207, 251));
            kusinaBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            kusinaBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            kusinaBtn.setTextColor(Color.rgb(136, 207, 251));
            RotondaBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            RotondaBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            RotondaBtn.setTextColor(Color.rgb(136, 207, 251));
            SizzlingBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            SizzlingBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            SizzlingBtn.setTextColor(Color.rgb(136, 207, 251));
            KafeBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            KafeBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            KafeBtn.setTextColor(Color.rgb(136, 207, 251));
            SorbeteroBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            SorbeteroBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            SorbeteroBtn.setTextColor(Color.rgb(136, 207, 251));
            nice_spiceBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            nice_spiceBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            nice_spiceBtn.setTextColor(Color.rgb(136, 207, 251));
            ThirstBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            ThirstBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            ThirstBtn.setTextColor(Color.rgb(136, 207, 251));
        });

        InasalBtn.setOnClickListener(view -> {
            foodRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        item_recycle.setVisibility(View.VISIBLE);
                        no_data_layout.setVisibility(View.GONE);
                        foodModelList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            FoodModel food = dataSnapshot.getValue(FoodModel.class);
                            String txt = InasalBtn.getText().toString();
                            if(food.getBranch().equals(txt)) {
                                foodModelList.add(food);
                            }
                        }
                        FoodAdapter mAdapter = new FoodAdapter(getContext(), foodModelList);
                        item_recycle.setAdapter(mAdapter);
                    }else {
                        item_recycle.setVisibility(View.GONE);
                        no_data_layout.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            InasalBtn.setBackgroundColor(Color.rgb(136, 207, 251));
            InasalBtn.setTextColor(Color.rgb(255, 255, 255));
            ThirTeaBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            ThirTeaBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            ThirTeaBtn.setTextColor(Color.rgb(136, 207, 251));
            allBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            allBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            allBtn.setTextColor(Color.rgb(136, 207, 251));
            kusinaBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            kusinaBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            kusinaBtn.setTextColor(Color.rgb(136, 207, 251));
            RotondaBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            RotondaBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            RotondaBtn.setTextColor(Color.rgb(136, 207, 251));
            SizzlingBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            SizzlingBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            SizzlingBtn.setTextColor(Color.rgb(136, 207, 251));
            KafeBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            KafeBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            KafeBtn.setTextColor(Color.rgb(136, 207, 251));
            SorbeteroBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            SorbeteroBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            SorbeteroBtn.setTextColor(Color.rgb(136, 207, 251));
            nice_spiceBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            nice_spiceBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            nice_spiceBtn.setTextColor(Color.rgb(136, 207, 251));
            ThirstBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            ThirstBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            ThirstBtn.setTextColor(Color.rgb(136, 207, 251));
        });

        kusinaBtn.setOnClickListener(view -> {
            foodRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        item_recycle.setVisibility(View.VISIBLE);
                        no_data_layout.setVisibility(View.GONE);
                        foodModelList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            FoodModel food = dataSnapshot.getValue(FoodModel.class);
                            String txt = kusinaBtn.getText().toString();
                            if(food.getBranch().equals(txt)) {
                                foodModelList.add(food);
                            }
                        }
                        FoodAdapter mAdapter = new FoodAdapter(getContext(), foodModelList);
                        item_recycle.setAdapter(mAdapter);
                    }else {
                        item_recycle.setVisibility(View.GONE);
                        no_data_layout.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            kusinaBtn.setBackgroundColor(Color.rgb(136, 207, 251));
            kusinaBtn.setTextColor(Color.rgb(255, 255, 255));
            ThirTeaBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            ThirTeaBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            ThirTeaBtn.setTextColor(Color.rgb(136, 207, 251));
            allBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            allBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            allBtn.setTextColor(Color.rgb(136, 207, 251));
            InasalBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            InasalBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            InasalBtn.setTextColor(Color.rgb(136, 207, 251));
            RotondaBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            RotondaBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            RotondaBtn.setTextColor(Color.rgb(136, 207, 251));
            SizzlingBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            SizzlingBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            SizzlingBtn.setTextColor(Color.rgb(136, 207, 251));
            KafeBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            KafeBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            KafeBtn.setTextColor(Color.rgb(136, 207, 251));
            SorbeteroBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            SorbeteroBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            SorbeteroBtn.setTextColor(Color.rgb(136, 207, 251));
            nice_spiceBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            nice_spiceBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            nice_spiceBtn.setTextColor(Color.rgb(136, 207, 251));
            ThirstBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            ThirstBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            ThirstBtn.setTextColor(Color.rgb(136, 207, 251));
        });

        RotondaBtn.setOnClickListener(view -> {
            foodRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        item_recycle.setVisibility(View.VISIBLE);
                        no_data_layout.setVisibility(View.GONE);
                        foodModelList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            FoodModel food = dataSnapshot.getValue(FoodModel.class);
                            String txt = RotondaBtn.getText().toString();
                            if(food.getBranch().equals(txt)) {
                                foodModelList.add(food);
                            }
                        }
                        FoodAdapter mAdapter = new FoodAdapter(getContext(), foodModelList);
                        item_recycle.setAdapter(mAdapter);
                    }else {
                        item_recycle.setVisibility(View.GONE);
                        no_data_layout.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            RotondaBtn.setBackgroundColor(Color.rgb(136, 207, 251));
            RotondaBtn.setTextColor(Color.rgb(255, 255, 255));
            ThirTeaBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            ThirTeaBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            ThirTeaBtn.setTextColor(Color.rgb(136, 207, 251));
            allBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            allBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            allBtn.setTextColor(Color.rgb(136, 207, 251));
            InasalBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            InasalBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            InasalBtn.setTextColor(Color.rgb(136, 207, 251));
            kusinaBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            kusinaBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            kusinaBtn.setTextColor(Color.rgb(136, 207, 251));
            SizzlingBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            SizzlingBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            SizzlingBtn.setTextColor(Color.rgb(136, 207, 251));
            KafeBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            KafeBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            KafeBtn.setTextColor(Color.rgb(136, 207, 251));
            SorbeteroBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            SorbeteroBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            SorbeteroBtn.setTextColor(Color.rgb(136, 207, 251));
            nice_spiceBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            nice_spiceBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            nice_spiceBtn.setTextColor(Color.rgb(136, 207, 251));
            ThirstBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            ThirstBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            ThirstBtn.setTextColor(Color.rgb(136, 207, 251));
        });

        SizzlingBtn.setOnClickListener(view -> {
            foodRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        item_recycle.setVisibility(View.VISIBLE);
                        no_data_layout.setVisibility(View.GONE);
                        foodModelList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            FoodModel food = dataSnapshot.getValue(FoodModel.class);
                            String txt = SizzlingBtn.getText().toString();
                            if(food.getBranch().equals(txt)) {
                                foodModelList.add(food);
                            }
                        }
                        FoodAdapter mAdapter = new FoodAdapter(getContext(), foodModelList);
                        item_recycle.setAdapter(mAdapter);
                    }else {
                        item_recycle.setVisibility(View.GONE);
                        no_data_layout.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            SizzlingBtn.setBackgroundColor(Color.rgb(136, 207, 251));
            SizzlingBtn.setTextColor(Color.rgb(255, 255, 255));
            ThirTeaBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            ThirTeaBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            ThirTeaBtn.setTextColor(Color.rgb(136, 207, 251));
            allBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            allBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            allBtn.setTextColor(Color.rgb(136, 207, 251));
            InasalBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            InasalBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            InasalBtn.setTextColor(Color.rgb(136, 207, 251));
            kusinaBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            kusinaBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            kusinaBtn.setTextColor(Color.rgb(136, 207, 251));
            RotondaBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            RotondaBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            RotondaBtn.setTextColor(Color.rgb(136, 207, 251));
            KafeBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            KafeBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            KafeBtn.setTextColor(Color.rgb(136, 207, 251));
            SorbeteroBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            SorbeteroBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            SorbeteroBtn.setTextColor(Color.rgb(136, 207, 251));
            nice_spiceBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            nice_spiceBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            nice_spiceBtn.setTextColor(Color.rgb(136, 207, 251));
            ThirstBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            ThirstBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            ThirstBtn.setTextColor(Color.rgb(136, 207, 251));
        });

        KafeBtn.setOnClickListener(view -> {
            foodRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        item_recycle.setVisibility(View.VISIBLE);
                        no_data_layout.setVisibility(View.GONE);
                        foodModelList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            FoodModel food = dataSnapshot.getValue(FoodModel.class);
                            String txt = KafeBtn.getText().toString();
                            if(food.getBranch().equals(txt)) {
                                foodModelList.add(food);
                            }
                        }
                        FoodAdapter mAdapter = new FoodAdapter(getContext(), foodModelList);
                        item_recycle.setAdapter(mAdapter);
                    }else {
                        item_recycle.setVisibility(View.GONE);
                        no_data_layout.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            KafeBtn.setBackgroundColor(Color.rgb(136, 207, 251));
            KafeBtn.setTextColor(Color.rgb(255, 255, 255));
            ThirTeaBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            ThirTeaBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            ThirTeaBtn.setTextColor(Color.rgb(136, 207, 251));
            allBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            allBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            allBtn.setTextColor(Color.rgb(136, 207, 251));
            InasalBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            InasalBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            InasalBtn.setTextColor(Color.rgb(136, 207, 251));
            kusinaBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            kusinaBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            kusinaBtn.setTextColor(Color.rgb(136, 207, 251));
            RotondaBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            RotondaBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            RotondaBtn.setTextColor(Color.rgb(136, 207, 251));
            SizzlingBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            SizzlingBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            SizzlingBtn.setTextColor(Color.rgb(136, 207, 251));
            SorbeteroBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            SorbeteroBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            SorbeteroBtn.setTextColor(Color.rgb(136, 207, 251));
            nice_spiceBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            nice_spiceBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            nice_spiceBtn.setTextColor(Color.rgb(136, 207, 251));
            ThirstBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            ThirstBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            ThirstBtn.setTextColor(Color.rgb(136, 207, 251));
        });

        SorbeteroBtn.setOnClickListener(view -> {
            foodRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        item_recycle.setVisibility(View.VISIBLE);
                        no_data_layout.setVisibility(View.GONE);
                        foodModelList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            FoodModel food = dataSnapshot.getValue(FoodModel.class);
                            String txt = SorbeteroBtn.getText().toString();
                            if(food.getBranch().equals(txt)) {
                                foodModelList.add(food);
                            }
                        }
                        FoodAdapter mAdapter = new FoodAdapter(getContext(), foodModelList);
                        item_recycle.setAdapter(mAdapter);
                    }else {
                        item_recycle.setVisibility(View.GONE);
                        no_data_layout.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            SorbeteroBtn.setBackgroundColor(Color.rgb(136, 207, 251));
            SorbeteroBtn.setTextColor(Color.rgb(255, 255, 255));
            ThirTeaBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            ThirTeaBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            ThirTeaBtn.setTextColor(Color.rgb(136, 207, 251));
            allBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            allBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            allBtn.setTextColor(Color.rgb(136, 207, 251));
            InasalBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            InasalBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            InasalBtn.setTextColor(Color.rgb(136, 207, 251));
            kusinaBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            kusinaBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            kusinaBtn.setTextColor(Color.rgb(136, 207, 251));
            RotondaBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            RotondaBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            RotondaBtn.setTextColor(Color.rgb(136, 207, 251));
            SizzlingBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            SizzlingBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            SizzlingBtn.setTextColor(Color.rgb(136, 207, 251));
            KafeBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            KafeBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            KafeBtn.setTextColor(Color.rgb(136, 207, 251));
            nice_spiceBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            nice_spiceBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            nice_spiceBtn.setTextColor(Color.rgb(136, 207, 251));
            ThirstBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            ThirstBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            ThirstBtn.setTextColor(Color.rgb(136, 207, 251));
        });

        nice_spiceBtn.setOnClickListener(view -> {
            foodRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        item_recycle.setVisibility(View.VISIBLE);
                        no_data_layout.setVisibility(View.GONE);
                        foodModelList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            FoodModel food = dataSnapshot.getValue(FoodModel.class);
                            String txt = nice_spiceBtn.getText().toString();
                            if(food.getBranch().equals(txt)) {
                                foodModelList.add(food);
                            }
                        }
                        FoodAdapter mAdapter = new FoodAdapter(getContext(), foodModelList);
                        item_recycle.setAdapter(mAdapter);
                    }else {
                        item_recycle.setVisibility(View.GONE);
                        no_data_layout.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            nice_spiceBtn.setBackgroundColor(Color.rgb(136, 207, 251));
            nice_spiceBtn.setTextColor(Color.rgb(255, 255, 255));
            ThirTeaBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            ThirTeaBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            ThirTeaBtn.setTextColor(Color.rgb(136, 207, 251));
            allBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            allBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            allBtn.setTextColor(Color.rgb(136, 207, 251));
            InasalBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            InasalBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            InasalBtn.setTextColor(Color.rgb(136, 207, 251));
            kusinaBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            kusinaBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            kusinaBtn.setTextColor(Color.rgb(136, 207, 251));
            RotondaBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            RotondaBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            RotondaBtn.setTextColor(Color.rgb(136, 207, 251));
            SizzlingBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            SizzlingBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            SizzlingBtn.setTextColor(Color.rgb(136, 207, 251));
            KafeBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            KafeBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            KafeBtn.setTextColor(Color.rgb(136, 207, 251));
            SorbeteroBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            SorbeteroBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            SorbeteroBtn.setTextColor(Color.rgb(136, 207, 251));
            ThirstBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            ThirstBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            ThirstBtn.setTextColor(Color.rgb(136, 207, 251));
        });

        ThirstBtn.setOnClickListener(view -> {
            foodRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        item_recycle.setVisibility(View.VISIBLE);
                        no_data_layout.setVisibility(View.GONE);
                        foodModelList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            FoodModel food = dataSnapshot.getValue(FoodModel.class);
                            String txt = ThirstBtn.getText().toString();
                            if(food.getBranch().equals(txt)) {
                                foodModelList.add(food);
                            }
                        }
                        FoodAdapter mAdapter = new FoodAdapter(getContext(), foodModelList);
                        item_recycle.setAdapter(mAdapter);
                    }else {
                        item_recycle.setVisibility(View.GONE);
                        no_data_layout.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            ThirstBtn.setBackgroundColor(Color.rgb(136, 207, 251));
            ThirstBtn.setTextColor(Color.rgb(255, 255, 255));
            ThirTeaBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            ThirTeaBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            ThirTeaBtn.setTextColor(Color.rgb(136, 207, 251));
            allBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            allBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            allBtn.setTextColor(Color.rgb(136, 207, 251));
            InasalBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            InasalBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            InasalBtn.setTextColor(Color.rgb(136, 207, 251));
            kusinaBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            kusinaBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            kusinaBtn.setTextColor(Color.rgb(136, 207, 251));
            RotondaBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            RotondaBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            RotondaBtn.setTextColor(Color.rgb(136, 207, 251));
            SizzlingBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            SizzlingBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            SizzlingBtn.setTextColor(Color.rgb(136, 207, 251));
            KafeBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            KafeBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            KafeBtn.setTextColor(Color.rgb(136, 207, 251));
            SorbeteroBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            SorbeteroBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            SorbeteroBtn.setTextColor(Color.rgb(136, 207, 251));
            nice_spiceBtn.setBackgroundColor(Color.rgb(255, 255, 255));
            nice_spiceBtn.setStrokeColor(ColorStateList.valueOf(Color.rgb(136, 207, 251)));
            nice_spiceBtn.setTextColor(Color.rgb(136, 207, 251));
        });

        foodRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    item_recycle.setVisibility(View.VISIBLE);
                    no_data_layout.setVisibility(View.GONE);
                    foodModelList.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        FoodModel food = dataSnapshot.getValue(FoodModel.class);
                        foodModelList.add(food);
                    }
                    FoodAdapter mAdapter = new FoodAdapter(getContext(), foodModelList);
                    item_recycle.setAdapter(mAdapter);
                }else {
                    item_recycle.setVisibility(View.GONE);
                    no_data_layout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        assert mUser != null;
        userRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    if(snapshot.hasChild("Fullname")) {
                        String name = snapshot.child("Fullname").getValue(String.class);
                        customer_name.setText(name);
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
}