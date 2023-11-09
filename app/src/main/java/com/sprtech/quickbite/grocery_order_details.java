package com.sprtech.quickbite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
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

import java.util.HashMap;

public class grocery_order_details extends AppCompatActivity {
    private TextView name;
    private TextView order_location;
    private TextView order_phone;
    private TextView groceryOrder;
    private FirebaseAuth mAuth;
    private FirebaseUser mUSer;
    private FirebaseDatabase mDb;
    private DatabaseReference nOrderRef, orderRef, orderHistoryRef;
    private ImageView backBtn;
    private MaterialButton sendNotificationBt, setStatusBt;
    String cusID, cusTotalOrder, cusLocation, cusPhone, cusName, userToken, QuickbiteName, orderStatus, ID, orderID, order;
    AlertDialog alertDialog, alertDialogSet;
    private ProgressDialog loadingBar, loadingBarSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_order_details);
        initiate();
        mAuth = FirebaseAuth.getInstance();
        mUSer = mAuth.getCurrentUser();

        Intent i = getIntent();
        cusID = i.getStringExtra("cusID");
        cusLocation = i.getStringExtra("cusLocation");
        cusPhone = i.getStringExtra("cusPhone");
        cusName = i.getStringExtra("cusName");
        QuickbiteName = "QuickBite";
        ID = i.getStringExtra("ID");
        orderID = i.getStringExtra("orderID");
        order = i.getStringExtra("order");

        mDb = FirebaseDatabase.getInstance();
        nOrderRef = mDb.getReference("NotificationOrder");
        orderRef = mDb.getReference("Orders");
        orderHistoryRef = mDb.getReference("OrderHistory");
        DatabaseReference deliveryFeeRef = mDb.getReference("TotalDeliveryFee");

        loadingBar = new ProgressDialog(grocery_order_details.this);
        loadingBar.setTitle("Sending Message");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(true);

        loadingBarSet = new ProgressDialog(grocery_order_details.this);
        loadingBarSet.setTitle("Setting Status");
        loadingBarSet.setMessage("Please wait...");
        loadingBarSet.setCanceledOnTouchOutside(true);

        //message
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Send Message");

        View inView = getLayoutInflater().inflate(R.layout.message_dialog, null);
        TextInputEditText sendMessageInput;
        MaterialButton sendBtn;

        //dialog message
        sendMessageInput = inView.findViewById(R.id.messageET);
        sendBtn = inView.findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(view -> {
            String message = sendMessageInput.getText().toString();
            loadingBar.show();
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("SenderName", "QuickBite");
                hashMap.put("SenderMessage", message);
                hashMap.put("Status", "");
                nOrderRef.child(cusID).child(mUSer.getUid()).child(orderID).updateChildren(hashMap).addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        loadingBar.dismiss();
                        alertDialog.dismiss();
                        sendNotification(cusID, QuickbiteName);
                        Toast.makeText(this, "Message sent successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }, 3000);
        });

        builder.setView(inView);
        alertDialog = builder.create();

        sendNotificationBt.setOnClickListener(view -> {
            alertDialogSet.dismiss();
            alertDialog.show();
        });

        //set
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Set Status");

        View inView1 = getLayoutInflater().inflate(R.layout.set_status_dialog, null);
        AutoCompleteTextView status, driver, time;
        MaterialButton setBtn;

        //dialog message
        time = inView1.findViewById(R.id.time);
        driver = inView1.findViewById(R.id.driver);
        status = inView1.findViewById(R.id.status);
        setBtn = inView1.findViewById(R.id.setBtn);

        String[] estimatedTime = new String[] {
                "15 minutes",
                "20 minutes",
                "30 minutes",
                "40 minutes",
                "50 minutes",
                "60 minutes",
        };

        String[] store = new String[] {
                "On Going",
                "Out for Delivery",
        };

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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.dropdown_item,
                store
        );

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                this,
                R.layout.dropdown_item,
                d
        );

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(
                this,
                R.layout.dropdown_item,
                estimatedTime
        );

        status.setAdapter(adapter);
        driver.setAdapter(adapter1);
        time.setAdapter(adapter3);

        setBtn.setOnClickListener(view -> {
            String stats = status.getText().toString();
            String dr = driver.getText().toString();
            String et = time.getText().toString();
            loadingBarSet.show();
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("Status", stats);
                hashMap.put("Driver", dr);
                hashMap.put("OrderID", orderID);
                hashMap.put("estimatedTime", et);
                nOrderRef.child(cusID).child(mUSer.getUid()).child(orderID).updateChildren(hashMap).addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("Driver", dr);
                        orderRef.child(mUSer.getUid()).child(orderID).updateChildren(map);
                        loadingBarSet.dismiss();
                        alertDialogSet.dismiss();
                        sendNotification1(cusID, stats, dr);
                        Toast.makeText(this, "Status set successfully", Toast.LENGTH_SHORT).show();

                    }
                });
            }, 3000);
        });

        if(mUSer != null) {
            nOrderRef.child(cusID).child(mUSer.getUid()).child(orderID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        if(snapshot.hasChild("Status")) {
                            orderStatus = snapshot.child("Status").getValue(String.class);
                            assert orderStatus != null;
                            if(orderStatus.equals("Delivered")){
                                orderRef.child(mUSer.getUid()).child(orderID).removeValue();
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("cusName", cusName);
                                map.put("cusTotalOrder", cusTotalOrder);
                                map.put("cusLocation", cusLocation);
                                map.put("cusPhone", cusPhone);
                                orderHistoryRef.child(mUSer.getUid()).child(cusID).setValue(map)
                                        .addOnCompleteListener(task1 -> {
                                            if(task1.isSuccessful()) {
                                                Intent i = new Intent(grocery_order_details.this, admin_dashboard.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        });
                            }else if(orderStatus.equals("On Going") || orderStatus.equals("Out for Delivery")) {
                                setStatusBt.setVisibility(View.GONE);
                            }else {
                                setStatusBt.setVisibility(View.VISIBLE);
                            }
                        }else {
                            setStatusBt.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(grocery_order_details.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        builder1.setView(inView1);
        alertDialogSet = builder1.create();

        setStatusBt.setOnClickListener(view -> {
            alertDialogSet.show();
            alertDialog.dismiss();
        });

        name.setText(cusName);
        order_location.setText(cusLocation);
        order_phone.setText(cusPhone);
        groceryOrder.setText(order);

        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(grocery_order_details.this, admin_dashboard.class);
            startActivity(intent);
            finish();
        });
    }

    private void sendNotification1(String cusID, String stats, String dr) {
        FirebaseDatabase.getInstance().getReference("UserToken").child(cusID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    userToken = snapshot.child("token").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(grocery_order_details.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            FCMNotificationSender.sendNotification(
                    getApplicationContext(),
                    userToken,
                    "QuickBite",
                    dr + "\n Your Order Status is set to " + stats
            );
        }, 3000);
    }

    private void sendNotification(String cusID, String QuickbiteName) {
        FirebaseDatabase.getInstance().getReference("UserToken").child(cusID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    userToken = snapshot.child("token").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(grocery_order_details.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            FCMNotificationSender.sendNotification(
                    getApplicationContext(),
                    userToken,
                    "QuickBite",
                    QuickbiteName + " Send you a message"
            );
        }, 3000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    private void initiate() {
        groceryOrder = findViewById(R.id.groceryOrder);
        name = findViewById(R.id.name);
        order_location = findViewById(R.id.order_location);
        order_phone = findViewById(R.id.order_phone);
        backBtn = findViewById(R.id.backBtn);
        sendNotificationBt = findViewById(R.id.sendNotificationBt);
        setStatusBt = findViewById(R.id.setStatusBt);
    }
}