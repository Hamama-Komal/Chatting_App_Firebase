package com.example.chatapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.chatapp.databinding.ActivityOptactivityBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OPTActivity extends AppCompatActivity {

    ActivityOptactivityBinding binding;
    FirebaseAuth firebaseAuth;

    String verificationID;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOptactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Sending OPT..");
        dialog.setCancelable(false);
        dialog.show();


        String phoneNo = getIntent().getStringExtra("phoneNo");
        binding.textView.setText("Verify "+ phoneNo);

        firebaseAuth = FirebaseAuth.getInstance();

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNo)
                .setTimeout(30L, TimeUnit.SECONDS)
                .setActivity(OPTActivity.this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);

                        dialog.dismiss();
                        verificationID = s;

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                        binding.optview.requestFocus();
                    }
                })
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);

       binding.btnContinue.setOnClickListener(v -> {
          String opt = binding.optview.getText().toString();
           PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID,opt);

           firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful()){
                       startActivity(new Intent(OPTActivity.this, ProfileSetupActivity.class));
                       finishAffinity();
                   }
                   else{
                       Toast.makeText(OPTActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                   }
               }
           });
       });
    }
}