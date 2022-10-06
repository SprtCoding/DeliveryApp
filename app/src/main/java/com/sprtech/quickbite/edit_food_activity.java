package com.sprtech.quickbite;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class edit_food_activity extends AppCompatActivity {
    private ImageView backBtn;
    private CircleImageView foodPic;
    private MaterialButton addBtn;
    private TextInputEditText foodNameET, foodDescriptionET, foodCategoryET, acroNameET,
            priceET;
    private FirebaseAuth mAuth;
    private FirebaseDatabase myDB;
    private DatabaseReference foodRef;
    private ProgressDialog loadingBar;
    private AutoCompleteTextView autoCompleteTextView;
    Intent i;
    String foodName, foodBranch, foodAName, foodDescription, foodCategory, foodPrice, foodPics, foodID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food);
        initialize();
        i = getIntent();
        foodName = i.getStringExtra("foodName");
        foodBranch = i.getStringExtra("foodBranch");
        foodAName = i.getStringExtra("foodAName");
        foodDescription = i.getStringExtra("foodDescription");
        foodCategory = i.getStringExtra("foodCategory");
        foodPrice = i.getStringExtra("foodPrice");
        foodPics = i.getStringExtra("foodPic");
        foodID = i.getStringExtra("foodID");

        //set data to editText
        foodNameET.setText(foodName);
        foodDescriptionET.setText(foodDescription);
        foodCategoryET.setText(foodCategory);
        acroNameET.setText(foodAName);
        priceET.setText(foodPrice);
        autoCompleteTextView.setText(foodBranch);
        Picasso.get().load(foodPics).placeholder(R.drawable.burger).fit().centerInside().into(foodPic);

        //ref
        mAuth = FirebaseAuth.getInstance();
        myDB = FirebaseDatabase.getInstance();
        foodRef = myDB.getReference("Foods");

        String[] store = new String[] {
                "ThirTea Ann",
                "Mang Inasal",
                "Kusina ni Lea",
                "Rotonda Cafe",
                "Sizzling 99",
                "Kafe Point",
                "El Sorbetero",
                "Nice & Spice",
                "Thirst Zone"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.dropdown_item,
                store
        );

        autoCompleteTextView.setAdapter(adapter);

        loadingBar = new ProgressDialog(edit_food_activity.this);
        loadingBar.setTitle("Updating Foods!");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(true);

        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, admin_dashboard.class);
            startActivity(intent);
            finish();
        });

        addBtn.setOnClickListener(view -> {
            loadingBar.show();
            String branch = autoCompleteTextView.getText().toString().trim();
            String nameFood = foodNameET.getText().toString();
            String description = foodDescriptionET.getText().toString();
            String category = foodCategoryET.getText().toString();
            String acroName = acroNameET.getText().toString();
            String price = priceET.getText().toString();
            if(TextUtils.isEmpty(branch)){
                Toast.makeText(this, "Branch is empty!", Toast.LENGTH_SHORT).show();
                loadingBar.hide();
            }else if(TextUtils.isEmpty(nameFood)){
                Toast.makeText(this, "Food Name is empty!", Toast.LENGTH_SHORT).show();
                loadingBar.hide();
            }else if(TextUtils.isEmpty(description)){
                Toast.makeText(this, "Description is empty!", Toast.LENGTH_SHORT).show();
                loadingBar.hide();
            }else if(TextUtils.isEmpty(category)){
                Toast.makeText(this, "Category is empty!", Toast.LENGTH_SHORT).show();
                loadingBar.hide();
            }else if(TextUtils.isEmpty(price)){
                Toast.makeText(this, "Price is empty!", Toast.LENGTH_SHORT).show();
                loadingBar.hide();
            }else {

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("foodUID", foodID);
                hashMap.put("branch", branch);
                hashMap.put("fName", nameFood);
                hashMap.put("fdescription", description);
                hashMap.put("fcategory", category);
                hashMap.put("facroname", acroName);
                hashMap.put("fprice", price);

                assert foodID != null;
                foodRef.child(foodID).updateChildren(hashMap).addOnCompleteListener(task2 -> {
                    loadingBar.hide();
                    if(task2.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Food Updated Successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(edit_food_activity.this, admin_dashboard.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });

    }

    private void initialize() {
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        backBtn = findViewById(R.id.backBtn);
        foodPic = findViewById(R.id.foodPic);
        addBtn = findViewById(R.id.addBtn);

        foodNameET = findViewById(R.id.foodNameET);
        foodDescriptionET = findViewById(R.id.foodDescriptionET);
        foodCategoryET = findViewById(R.id.foodCategoryET);
        acroNameET = findViewById(R.id.acroNameET);
        priceET = findViewById(R.id.priceET);
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

}