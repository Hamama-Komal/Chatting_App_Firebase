package com.example.chatapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.chatapp.ModelClasses.UserClass;
import com.example.chatapp.databinding.ActivityProfileSetupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileSetupActivity extends AppCompatActivity {

    ActivityProfileSetupBinding binding;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;

    Uri selectedImg;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileSetupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating...!");
        progressDialog.setCancelable(false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        // To select Image from Gallery
        binding.imageView.setOnClickListener(v -> {

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 45);

        });


        // Sending Name and Profile image into database
        binding.btnSet.setOnClickListener(v -> {

            String name = binding.getName.getText().toString();
            if (name.isEmpty()) {
                binding.getName.setError("Please enter name");
                return;
            }

            progressDialog.show();

            // If image is selected
            if (selectedImg != null) {

                StorageReference reference = firebaseStorage.getReference().child("Profile").child(firebaseAuth.getUid());
                reference.putFile(selectedImg).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUri = uri.toString();

                                    String uid = firebaseAuth.getUid();
                                    String phone = firebaseAuth.getCurrentUser().getPhoneNumber();
                                    String name = binding.getName.getText().toString();

                                    UserClass userClass = new UserClass(uid, name, phone, imageUri);
                                    firebaseDatabase.getReference().child("Users").child(uid).setValue(userClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            progressDialog.dismiss();
                                            startActivity(new Intent(ProfileSetupActivity.this, MainActivity.class));
                                            finish();
                                        }
                                    });

                                }
                            });
                        }
                    }
                });

            }
            // If user select no image
            else {

                String uid = firebaseAuth.getUid();
                String phone = firebaseAuth.getCurrentUser().getPhoneNumber();


                UserClass userClass = new UserClass(uid, name, phone, "No Image Found");
                firebaseDatabase.getReference().child("Users").child(uid).setValue(userClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        startActivity(new Intent(ProfileSetupActivity.this, MainActivity.class));
                        finish();
                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (data.getData() != null) {
                binding.profilePic.setImageURI(data.getData());
                selectedImg = data.getData();
            }
        }
    }
}