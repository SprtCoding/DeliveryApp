package com.sprtech.quickbite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private MaterialButton gtBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        var();

        gtBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, login_activity.class);
            startActivity(intent);
            finish();
        });
    }

    private void var() {
        gtBtn = findViewById(R.id.get_started_bt);
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}