package com.sprtech.quickbite;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.Toast;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class customer_dashboard extends AppCompatActivity {

    private BottomNavigationView bnv;
    HomeFragment homeFragment = new HomeFragment();
    NotificationFragment notificationFragment = new NotificationFragment();
    MessageFragment messageFragment = new MessageFragment();
    MeFragment meFragment = new MeFragment();
    FloatingActionButton cart_floating_button;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDb;
    private DatabaseReference orderRef;
    private int numCount = 0;
    BadgeDrawable badgeDrawable;
    String QuickBiteUID = "gfA1z5sjGBeWknBSCSQgQrld7BW2";

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);
        var();

        //Snackbar.make(cart_floating_button, "", BaseTransientBottomBar.LENGTH_SHORT).setAnchorView(cart_floating_button).show();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mDb = FirebaseDatabase.getInstance();
        orderRef = mDb.getReference("NotificationOrder");

        badgeDrawable = bnv.getOrCreateBadge(R.id.notification);
        badgeDrawable.setVisible(true);
        badgeDrawable.setVerticalOffset(dpToPx(customer_dashboard.this,3));
        badgeDrawable.setBackgroundColor(getResources().getColor(R.color.red));
        badgeDrawable.setBadgeTextColor(getResources().getColor(R.color.white));

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, homeFragment).commit();

        bnv.setBackground(null);
        bnv.getMenu().getItem(2).setEnabled(false);
        bnv.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, homeFragment).commit();
                    return true;
                case R.id.notification:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, notificationFragment).commit();
                    badgeDrawable.setNumber(0);
                    badgeDrawable.setVisible(false);
                    return true;
                case R.id.about:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, messageFragment).commit();
                    return true;
                case R.id.me:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, meFragment).commit();
                    return true;
            }
            return false;
        });

        cart_floating_button.setOnClickListener(v -> {
            Intent i = new Intent(this, cart.class);
            startActivity(i);
        });

        if(mUser != null) {
            orderRef.child(mUser.getUid()).child(QuickBiteUID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        numCount = (int) snapshot.getChildrenCount();
                        badgeDrawable.setNumber(numCount);
                    }else {
                        badgeDrawable.setVisible(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(customer_dashboard.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            orderRef.child(mUser.getUid()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    badgeDrawable.setNumber(numCount);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    badgeDrawable.setNumber(numCount);
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    badgeDrawable.setNumber(numCount);
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(customer_dashboard.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    public static int dpToPx(Context context, int dp) {
        Resources resources = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,resources.getDisplayMetrics()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    private void var() {
        cart_floating_button = findViewById(R.id.cart_floating_button);
        bnv = findViewById(R.id.bottom_nav);
    }
}