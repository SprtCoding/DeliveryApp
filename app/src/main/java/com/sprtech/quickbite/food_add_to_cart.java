package com.sprtech.quickbite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

public class food_add_to_cart extends AppCompatActivity {
    private TextView aName, fDescription, quantity_tb, fPrice;
    private ImageView food_pic, backBtn;
    private CardView decreaseBtn, increaseBtn;
    private MaterialButton btAdd;
    private FirebaseAuth mAuth;
    private FirebaseDatabase myDB;
    private DatabaseReference cartRef;
    private ProgressDialog loadingBar;
    String AName, description, food_picture, fBranch, price, fName;
    Intent i;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_add_to_cart);
        var();
        mAuth = FirebaseAuth.getInstance();
        myDB = FirebaseDatabase.getInstance();
        cartRef = myDB.getReference("Cart");

        loadingBar = new ProgressDialog(this);
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(true);

        i = getIntent();
        fName = i.getStringExtra("food_name");
        AName = i.getStringExtra("food_acro_name");
        description = i.getStringExtra("food_description");
        food_picture = i.getStringExtra("food_picture");
        price = i.getStringExtra("food_price");
        fBranch = i.getStringExtra("food_branch");

        aName.setText(fName);
        fPrice.setText("₱"+price);
        quantity_tb.setText("1");
        fDescription.setText(description);
        Picasso.get().load(food_picture).placeholder(R.drawable.userman).fit().centerInside().into(food_pic);

        //add to cart
        btAdd.setOnClickListener(view -> {
            loadingBar.show();
            if(quantity_tb.getText().toString().equals("0")) {
                Toast.makeText(this, "Your order is 0!", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }else {
                String cartOrderID = cartRef.push().getKey();
                String q = quantity_tb.getText().toString();
                double p = Double.parseDouble(price);
                double quantity = Double.parseDouble(q);
                double total = quantity * p;

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("cartOrderID", cartOrderID);
                hashMap.put("fBrach", fBranch);
                hashMap.put("fName", fName);
                hashMap.put("fAName", AName);
                hashMap.put("description", description);
                hashMap.put("quantity", q);
                hashMap.put("fPic", food_picture);
                hashMap.put("fPrice", price);
                hashMap.put("total", total);
                hashMap.put("customerID", mAuth.getCurrentUser().getUid());
                assert cartOrderID != null;
                cartRef.child(mAuth.getCurrentUser().getUid())
                        .child(cartOrderID)
                        .setValue(hashMap)
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()) {
                                loadingBar.dismiss();
                                Toast.makeText(food_add_to_cart.this, AName+" Added to cart successfully!", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(food_add_to_cart.this, customer_dashboard.class);
                                startActivity(i);
                                finish();
                            }
                        });
            }
        });

        //incre and decre button click
        increaseBtn.setOnClickListener(view -> {
            String currentValue = quantity_tb.getText().toString();
            int value = Integer.parseInt(currentValue);
            value++;
            quantity_tb.setText(String.valueOf(value));
            if(value > 0) {
                decreaseBtn.setEnabled(true);
            }
        });

        decreaseBtn.setOnClickListener(view -> {
            String currentValue = quantity_tb.getText().toString();
            int value = Integer.parseInt(currentValue);
            value--;
            if(value == 0) {
                quantity_tb.setText("0");
                decreaseBtn.setEnabled(false);
                Toast.makeText(this, "Value is already zero!", Toast.LENGTH_SHORT).show();
            }else {
                decreaseBtn.setEnabled(true);
                quantity_tb.setText(String.valueOf(value));
            }
        });

        backBtn.setOnClickListener(view -> {
            if(fBranch.equals("ThirTea Ann")) {
                Intent intent = new Intent(this, thirTeaAnn_list.class);
                startActivity(intent);
                finish();
            }else if(fBranch.equals("Kusina ni Lea")) {
                Intent intent = new Intent(this, KNL_list.class);
                startActivity(intent);
                finish();
            }else if(fBranch.equals("Mang Inasal")) {
                Intent intent = new Intent(this, inasal_list.class);
                startActivity(intent);
                finish();
            }else if(fBranch.equals("Sizzling 99")) {
                Intent intent = new Intent(this, sizzling_list.class);
                startActivity(intent);
                finish();
            }else if(fBranch.equals("El Sorbetero")) {
                Intent intent = new Intent(this, ElSorbetero_list.class);
                startActivity(intent);
                finish();
            }else if(fBranch.equals("Kafe Point")) {
                Intent intent = new Intent(this, kape_point_list.class);
                startActivity(intent);
                finish();
            }else if(fBranch.equals("Rotonda Cafe")) {
                Intent intent = new Intent(this, rotonda_list.class);
                startActivity(intent);
                finish();
            }else if(fBranch.equals("Circle")) {
                Intent intent = new Intent(this, crcle_list.class);
                startActivity(intent);
                finish();
            }
            else if(fBranch.equals("Nice & Spice")) {
                Intent intent = new Intent(this, nice_spice_list.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void var() {
        aName = findViewById(R.id.aName);
        fDescription = findViewById(R.id.fDescription);
        food_pic = findViewById(R.id.food_pic);
        backBtn = findViewById(R.id.backBtn);
        btAdd = findViewById(R.id.btAdd);
        fPrice = findViewById(R.id.fPrice);

        quantity_tb = findViewById(R.id.quantity_tb);
        decreaseBtn = findViewById(R.id.decreaseBtn);
        increaseBtn = findViewById(R.id.increaseBtn);
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}