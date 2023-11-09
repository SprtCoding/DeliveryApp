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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class edit_profile extends AppCompatActivity {
    private CircleImageView userPic;
    private TextInputEditText fullNameET, addressET, phoneET, emailET;
    private MaterialButton updateBtn;
    private FirebaseAuth mAuth;
    private FirebaseUser mUSer;
    private FirebaseDatabase mDb;
    private DatabaseReference userRef;
    private ImageView add_photo, backBtn;
    public Uri imageUri;
    private StorageReference storageReference;
    private String myUri;
    private ProgressDialog loadingBar;
    String cus_name, cus_email, cus_phone, cus_address, cus_photo;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initialize();

        loadingBar = new ProgressDialog(edit_profile.this);
        loadingBar.setTitle("Updating Account!");
        loadingBar.setMessage("Please wait...");
        loadingBar.setCanceledOnTouchOutside(true);

        mAuth = FirebaseAuth.getInstance();
        mUSer = mAuth.getCurrentUser();

        mDb = FirebaseDatabase.getInstance();
        userRef = mDb.getReference("Users");

        storageReference = FirebaseStorage.getInstance().getReference("ProfilePicture");

        i = getIntent();
        cus_name = i.getStringExtra("cus_name");
        cus_email = i.getStringExtra("cus_email");
        cus_phone = i.getStringExtra("cus_phone");
        cus_address = i.getStringExtra("cus_address");
        cus_photo = i.getStringExtra("cus_pic");

        Picasso.get().load(cus_photo).fit().centerInside().into(userPic);
        fullNameET.setText(cus_name);
        addressET.setText(cus_address);
        phoneET.setText(cus_phone);
        emailET.setText(cus_email);

        add_photo.setOnClickListener(v ->
                ImagePicker
                        .Companion
                        .with(this)
                        .crop()
                        .cropOval()
                        .compress(720)
                        .maxResultSize(512, 512)
                        .start(10));

        updateBtn.setOnClickListener(view -> updateAccount());

        backBtn.setOnClickListener(view -> finish());

    }

    private void updateAccount() {
        loadingBar.show();
        final String fullName = fullNameET.getText().toString();
        final String address = addressET.getText().toString();
        final String phone = phoneET.getText().toString();
        final String email = emailET.getText().toString();

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
        }else {
            if(mUSer != null) {
                uploadImage();
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("UID", mUSer.getUid());
                hashMap.put("Fullname", fullName);
                hashMap.put("Address", address);
                hashMap.put("Phone", phone);
                hashMap.put("Email", email);
                hashMap.put("Position", "NormalUser");

                userRef.child(mUSer.getUid()).updateChildren(hashMap)
                        .addOnCompleteListener(task1 -> {
                            loadingBar.hide();
                            if(task1.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Updated Successfully!", Toast.LENGTH_SHORT).show();
                                finish();
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
        return mime.getExtensionFromMimeType(cR.getType(uri));
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
                                mDb.getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(hashMap);
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

    private void initialize() {
        add_photo = findViewById(R.id.add_photo);
        userPic = findViewById(R.id.userPic);
        fullNameET = findViewById(R.id.fullNameET);
        addressET = findViewById(R.id.addressET);
        phoneET = findViewById(R.id.phoneET);
        emailET = findViewById(R.id.emailET);
        updateBtn = findViewById(R.id.updateBtn);
        backBtn = findViewById(R.id.backBtn);
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }
}