package com.sprtech.quickbite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sprtech.quickbite.Adapters.DeliveryFeeAdapter;
import com.sprtech.quickbite.Models.DeliveryFeeModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class view_all_deliveryFee extends AppCompatActivity {
    private ImageView backBtn;
    private RecyclerView deliveryFee_recycle;
    private LinearLayout no_data_layout, delivery_fee_layout;
    private FirebaseDatabase mDB;
    private DatabaseReference orderRef;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private TextInputEditText dateET;
    private TextView totalFee;
    private AutoCompleteTextView rider;
    LinearLayoutManager linearLayoutManager;
    List<DeliveryFeeModel> deliveryFeeModels;
    DeliveryFeeAdapter deliveryFeeAdapter;
    double totalFees = 0;
    double dFee;
    String dDate, riderName, dID, r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_delivery_fee);
        initiate();
        linearLayoutManager = new LinearLayoutManager(view_all_deliveryFee.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        deliveryFee_recycle.setHasFixedSize(true);
        deliveryFee_recycle.setLayoutManager(linearLayoutManager);

        deliveryFeeModels = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mDB = FirebaseDatabase.getInstance();
        orderRef = mDB.getReference("TotalDeliveryFee");

        String[] d = new String[] {
                "Marlo Barrera",
                "Gerald Gragasin",
                "Leandro Sy",
                "Ian Bauto",
                "Franco Bautista",
                "Jasper Delas Alas",
                "Jaybee Sunga",
                "Dave Morales",
                "Andrew Doronio",
                "Lance Casao",
        };

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                this,
                R.layout.dropdown_item,
                d
        );

        rider.setAdapter(adapter1);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        dateET.requestFocus();
        dateET.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    view_all_deliveryFee.this,
                    (datePicker, year1, month1, day1) -> {
                        month1 = month1 + 1;
                        String strMonth = "" + month1;
                        String strDay = "" + day1;
                        if(month1 < 10) {
                            strMonth = "0" + month1;
                        }
                        if(day1 < 10) {
                            strDay = "0" + day1;
                        }
                        String date = strMonth + "-" + strDay + "-" + year1;
                        dateET.setText(date);
                    }, year, month, day);
            datePickerDialog.show();
        });

        if(mUser != null) {
            orderRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        for(DataSnapshot ds : snapshot.getChildren()) {
                            Map<String, Object> map = (Map<String, Object>) ds.getValue();
                            assert map != null;
                            Object date = map.get("orderDate");

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(view_all_deliveryFee.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            orderRef.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        delivery_fee_layout.setVisibility(View.VISIBLE);
                        no_data_layout.setVisibility(View.GONE);
                        deliveryFeeModels.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            DeliveryFeeModel deliveryFee = dataSnapshot.getValue(DeliveryFeeModel.class);
                            deliveryFeeModels.add(deliveryFee);
                        }
                        deliveryFee_recycle.scrollToPosition(deliveryFeeModels.size()-1);
                        new Handler().postDelayed(() -> deliveryFee_recycle.smoothScrollToPosition(deliveryFeeModels.size()-1),350);
                        deliveryFeeAdapter = new DeliveryFeeAdapter(view_all_deliveryFee.this, deliveryFeeModels);
                        deliveryFee_recycle.setAdapter(deliveryFeeAdapter);



                    }else {
                        delivery_fee_layout.setVisibility(View.GONE);
                        no_data_layout.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(view_all_deliveryFee.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, admin_dashboard.class);
            startActivity(intent);
            finish();
        });

        if(dateET != null) {
            dateET.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @SuppressLint("SetTextI18n")
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @SuppressLint("SetTextI18n")
                @Override
                public void afterTextChanged(Editable editable) {
                    String riderName = rider.getText().toString();
                    getTotal(editable, riderName);

                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void getTotal(Editable editable, String riderName) {
        for(DeliveryFeeModel feeModel : deliveryFeeModels) {
            if(feeModel.getOrderDate().contains(editable.toString()) && feeModel.getDriver().contains(riderName)) {
                totalFees += Double.parseDouble(feeModel.getDeliveryFee());
                totalFee.setText("₱" + String.valueOf(totalFees));
            }else {
                totalFee.setText("No Delivery on this date.");
                totalFee.setTextSize(12);
            }
        }
    }

    private void initiate() {
        rider = findViewById(R.id.rider);
        totalFee = findViewById(R.id.totalFee);
        backBtn = findViewById(R.id.backBtn);
        dateET = findViewById(R.id.dateET);
        deliveryFee_recycle = findViewById(R.id.deliveryFee_recycle);
        no_data_layout = findViewById(R.id.no_data_layout);
        delivery_fee_layout = findViewById(R.id.delivery_fee_layout);
    }
}