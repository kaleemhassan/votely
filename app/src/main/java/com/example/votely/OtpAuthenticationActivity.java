package com.example.votely;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.*;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class OtpAuthenticationActivity extends AppCompatActivity {

    private EditText otpEditText;
    private TextView otpErrorText;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private String name, email, cnic, phone, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_authentication);

        otpEditText = findViewById(R.id.otp_input_layout).findViewById(R.id.otp_input_edit_text);
        otpErrorText = findViewById(R.id.otp_error_text);
        TextView resendCodeLink = findViewById(R.id.resend_code_link);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        cnic = getIntent().getStringExtra("cnic");
        phone = getIntent().getStringExtra("phone");
        password = getIntent().getStringExtra("password");

        resendCodeLink.setText("Send OTP (Disabled)");

        resendCodeLink.setOnClickListener(v -> {
            Toast.makeText(this, "OTP sending is disabled in dev mode.", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.verify_button).setOnClickListener(v -> {
            String code = otpEditText.getText().toString().trim();
            if (TextUtils.isEmpty(code) || code.length() != 6 || !TextUtils.isDigitsOnly(code)) {
                showError("Enter a valid 6-digit OTP.");
                return;
            }

            if (code.equals("000000")) {
                // Proceed with signup directly
                createUserInFirebase();
            } else {
                showError("Invalid OTP entered.");
            }
        });

        /*
        // Original Firebase OTP logic — commented out
        sendVerificationCode(phone);
        */
    }

    private void createUserInFirebase() {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            saveUserToFirestore(user.getUid());
                        }
                    } else {
                        showError("Account creation failed: " + task.getException().getMessage());
                    }
                });
    }

    private void saveUserToFirestore(String uid) {
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("cnic", cnic);
        user.put("phone", phone);

        db.collection("users").document(uid).set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> showError("Firestore error: " + e.getMessage()));
    }

    private void showError(String message) {
        otpErrorText.setText(message);
        otpErrorText.setVisibility(View.VISIBLE);
    }

    /*
    // Firebase PhoneAuth logic — commented out

    private void sendVerificationCode(String number) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(number)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                    String code = credential.getSmsCode();
                    if (code != null) {
                        otpEditText.setText(code);
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    showError("Verification failed: " + e.getMessage());
                }

                @Override
                public void onCodeSent(@NonNull String verificationIdParam, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                    verificationId = verificationIdParam;
                    Toast.makeText(OtpAuthenticationActivity.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                }
            };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        saveUserToFirestore(user.getUid());
                    }
                } else {
                    showError("Verification failed: " + task.getException().getMessage());
                }
            });
    }
    */
}
