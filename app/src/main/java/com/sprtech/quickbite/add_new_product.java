package com.sprtech.quickbite;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class add_new_product extends AppCompatActivity {
    private ImageView backBtn, add_photo;
    private CircleImageView foodPic;
    private MaterialButton addBtn;
    private TextInputEditText foodNameET, foodDescriptionET, foodCategoryET, acroNameET,
            priceET;
    private FirebaseAuth mAuth;
    private FirebaseDatabase myDB;
    private DatabaseReference foodRef;
    private ProgressDialog loadingBar;
    public Uri imageUri;
    private StorageReference storageReference;
    private String myUri;
    private AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);
        var();
        mAuth = FirebaseAuth.getInstance();
        myDB = FirebaseDatabase.getInstance();
        foodRef = myDB.getReference("Foods");
        storageReference = FirebaseStorage.getInstance().getReference("FoodImages");

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

        loadingBar = new ProgressDialog(add_new_product.this);
        loadingBar.setTitle("Saving Foods!");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(true);

        backBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this, admin_dashboard.class);
            startActivity(intent);
            finish();
        });

        addBtn.setOnClickListener(view -> {
            loadingBar.show();
            uploadImage();
        });

        add_photo.setOnClickListener(v ->
                ImagePicker
                        .Companion.with(this)
                        .crop()
                        .cropOval()
                        .compress(720)
                        .maxResultSize(512, 512)
                        .start(10));
    }

    private void var() {
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        backBtn = findViewById(R.id.backBtn);
        add_photo = findViewById(R.id.add_photo);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            foodPic.setImageURI(imageUri);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImage() {
        if(imageUri != null) {
            StorageReference myFoodRef = storageReference.child(System.currentTimeMillis()
                    +" ." + getFileExtension(imageUri));

            myFoodRef.putFile(imageUri)
                    .addOnCompleteListener(task -> {
                        Toast.makeText(this, "Image Saved Successfully!", Toast.LENGTH_SHORT).show();

                        UploadTask uploadTask = myFoodRef.putFile(imageUri);

                        uploadTask.continueWithTask((Continuation) task1 -> {

                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return myFoodRef.getDownloadUrl();
                        }).addOnCompleteListener(task1 -> {
                            if(task.isSuccessful()) {
                                Uri downloadURI = (Uri) task1.getResult();
                                assert downloadURI != null;
                                myUri = downloadURI.toString();

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
                                }else if(TextUtils.isEmpty(acroName)){
                                    Toast.makeText(this, "Acro Name is empty!", Toast.LENGTH_SHORT).show();
                                    loadingBar.hide();
                                }else if(TextUtils.isEmpty(price)){
                                    Toast.makeText(this, "Price is empty!", Toast.LENGTH_SHORT).show();
                                    loadingBar.hide();
                                }else {
                                    String id = foodRef.push().getKey();

                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("foodUID", id);
                                    hashMap.put("branch", branch);
                                    hashMap.put("fName", nameFood);
                                    hashMap.put("fdescription", description);
                                    hashMap.put("fcategory", category);
                                    hashMap.put("facroname", acroName);
                                    hashMap.put("fprice", price);
                                    hashMap.put("fpic", myUri);

                                    assert id != null;
                                    foodRef.child(id).setValue(hashMap).addOnCompleteListener(task2 -> {
                                        loadingBar.hide();
                                        if(task2.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Food Save Successfully!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(add_new_product.this, admin_dashboard.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                }
                            }
                        });

                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }else {
            Toast.makeText(this, "No File Selected!", Toast.LENGTH_SHORT).show();
        }

    }
}