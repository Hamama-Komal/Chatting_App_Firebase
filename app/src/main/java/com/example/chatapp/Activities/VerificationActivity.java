package com.example.chatapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.chatapp.databinding.ActivityVerificationBinding;
import com.google.firebase.auth.FirebaseAuth;

public class VerificationActivity extends AppCompatActivity {

    ActivityVerificationBinding binding;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        // same user remain signIn
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(VerificationActivity.this, MainActivity.class));
            finish();
        }


        binding.edtPhone.requestFocus();

        // For sending Getting phone number and moving to next activity
        binding.btnContinue.setOnClickListener(v -> {

            String phone = binding.edtPhone.getText().toString();

            if (phone.isEmpty()) {
                binding.edtPhone.setError("Please enter your phone number");
                return;
            } else {

                Intent intent = new Intent(new Intent(VerificationActivity.this, OPTActivity.class));
                intent.putExtra("phoneNo", binding.edtPhone.getText().toString());
                startActivity(intent);
                finish();
            }
        });
    }
}