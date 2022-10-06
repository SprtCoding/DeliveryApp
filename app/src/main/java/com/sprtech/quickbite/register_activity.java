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
import android.widget.ImageView;
import android.widget.Toast;

import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class register_activity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private TextInputEditText fullNameET, addressET, phoneET, emailET, passwordET, conPasswordET;
    private MaterialButton signUpBtn;
    private ProgressDialog loadingBar;
    private ImageView add_photo;
    private CircleImageView userPic;
    private DatabaseReference tokenRef;
    public Uri imageUri;
    private StorageReference storageReference;
    private String myUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        var();
        loadingBar = new ProgressDialog(register_activity.this);
        loadingBar.setTitle("Creating Account!");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(true);

        tokenRef = db.getReference("UserToken");

        storageReference = FirebaseStorage.getInstance().getReference("ProfilePicture");

        signUpBtn.setOnClickListener(v -> registerAccount());

        add_photo.setOnClickListener(v ->
                    ImagePicker
                    .Companion
                    .with(this)
                    .crop()
                    .cropOval()
                    .compress(720)
                    .maxResultSize(512, 512)
                    .start(10));

    }

    private void registerAccount() {
        loadingBar.show();
        final String fullName = fullNameET.getText().toString();
        final String address = addressET.getText().toString();
        final String phone = phoneET.getText().toString();
        final String email = emailET.getText().toString();
        final String pass = passwordET.getText().toString();
        final String conPass = conPasswordET.getText().toString();

        if(TextUtils.isEmpty(fullName)) {
            Toast.makeText(getApplicationContext(), "Full Name is required.", Toast.LENGTH_SHORT).show();
            loadingBar.hide();
        }else if(TextUtils.isEmpty(address)) {
            Toast.makeText(getApplicationContext(), "Address is required.", Toast.LENGTH_SHORT).show();
            loadingBar.hide();
        }else if(TextUtils.isEmpty(phone)) {
            Toast.makeText(getApplicationContext(), "Phone Number is required.", Toast.LENGTH_SHORT).show();
            loadingBar.hide();
        }else if(TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Email is required.", Toast.LENGTH_SHORT).show();
            loadingBar.hide();
        }else if(TextUtils.isEmpty(pass)) {
            Toast.makeText(getApplicationContext(), "Password is required.", Toast.LENGTH_SHORT).show();
            loadingBar.hide();
        }else if(TextUtils.isEmpty(conPass)) {
            Toast.makeText(getApplicationContext(), "Confirm Password is required.", Toast.LENGTH_SHORT).show();
            loadingBar.hide();
        }else {
            if(!pass.equals(conPass)) {
                Toast.makeText(getApplicationContext(), "Password not match.", Toast.LENGTH_SHORT).show();
                loadingBar.hide();
            }else {
                mAuth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()) {
                                FirebaseMessaging.getInstance().getToken()
                                        .addOnCompleteListener(task1 -> {
                                            if (!task1.isSuccessful()) {
                                                Toast.makeText(register_activity.this, "Fetching FCM registration token failed" + task.getException(), Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                            // Get new FCM registration token
                                            String token = task1.getResult();
                                            tokenRef.child(mAuth.getCurrentUser().getUid()).child("token").setValue(token);
                                        });

                                uploadImage();
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("UID", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                hashMap.put("Fullname", fullName);
                                hashMap.put("Address", address);
                                hashMap.put("Phone", phone);
                                hashMap.put("Email", email);
                                hashMap.put("Position", "NormalUser");

                                db.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(hashMap)
                                        .addOnCompleteListener(task1 -> {
                                            loadingBar.hide();
                                            if(task1.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), "Registered Successfully!", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(register_activity.this, customer_dashboard.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });

                            }else {
                                Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();

                            }
                        });
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            userPic.setImageURI(imageUri);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(imageUri));
    }

    private void uploadImage() {
        if(imageUri != null) {
            StorageReference myFoodRef = storageReference.child(mAuth.getCurrentUser().getUid()
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
                            if(task1.isSuccessful()) {
                                Uri downloadURI = (Uri) task1.getResult();
                                assert downloadURI != null;
                                myUri = downloadURI.toString();

                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("userPic", myUri);
                                db.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(hashMap);
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

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }

    private void var() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        fullNameET = findViewById(R.id.fullNameET);
        addressET = findViewById(R.id.addressET);
        phoneET = findViewById(R.id.phoneET);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        conPasswordET = findViewById(R.id.conPasswordET);
        signUpBtn = findViewById(R.id.signUpBtn);
        add_photo = findViewById(R.id.add_photo);
        userPic = findViewById(R.id.userPic);
    }
}