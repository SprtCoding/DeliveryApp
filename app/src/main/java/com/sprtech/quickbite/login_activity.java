package com.sprtech.quickbite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

public class login_activity extends AppCompatActivity {

    private MaterialButton loginBtn, signupBtn;
    private TextInputEditText emailET, passwordET;
    private FirebaseAuth mAuth;
    private FirebaseDatabase myDB;
    private DatabaseReference userRef, tokenRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        var();
        myDB = FirebaseDatabase.getInstance();
        userRef = myDB.getReference("Users");

        loadingBar = new ProgressDialog(login_activity.this);
        loadingBar.setTitle("Logging In!");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(true);

        tokenRef = myDB.getReference("UserToken");

        signupBtn.setOnClickListener(v -> {
            Intent intent = new Intent(login_activity.this, register_activity.class);
            startActivity(intent);
        });

        loginBtn.setOnClickListener(v -> {
            loadingBar.show();
            final String email = emailET.getText().toString();
            final String password = passwordET.getText().toString();

            if(TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Email is required.", Toast.LENGTH_SHORT).show();
                loadingBar.hide();
            }else if(TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "Password is required.", Toast.LENGTH_SHORT).show();
                loadingBar.hide();
            }else {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            loadingBar.hide();
                            if(task.isSuccessful()) {
                                FirebaseMessaging.getInstance().getToken()
                                        .addOnCompleteListener(task1 -> {
                                            if (!task1.isSuccessful()) {
                                                Toast.makeText(login_activity.this, "Fetching FCM registration token failed" + task.getException(), Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                            // Get new FCM registration token
                                            String token = task1.getResult();
                                            tokenRef.child(mAuth.getCurrentUser().getUid()).child("token").setValue(token);
                                        });

                                userRef.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()) {
                                            if(snapshot.hasChild("Position")) {
                                                String position = snapshot.child("Position").getValue(String.class);
                                                assert position != null;
                                                if(position.equals("Admin")) {
                                                    Toast.makeText(getApplicationContext(), "Login Successfully!", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(login_activity.this, admin_dashboard.class);
                                                    startActivity(intent);
                                                    finish();
                                                }else if(position.equals("NormalUser")){
                                                    Toast.makeText(getApplicationContext(), "Login Successfully!", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(login_activity.this, customer_dashboard.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }else {
                                Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    private void var() {
        mAuth = FirebaseAuth.getInstance();

        signupBtn = findViewById(R.id.signupBtn);
        loginBtn = findViewById(R.id.loginBtn);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
    }
}