package com.example.votely;

import android.content.Intent;
import android.os.Bundle;
import android.text.*;
import android.util.Patterns;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, mobileEditText, identityEditText, passwordEditText;
    private TextView emailErrorText, mobileErrorText, identityErrorText, passwordErrorText, nameErrorText;
    private boolean isFormattingIdentity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameEditText = findViewById(R.id.name_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        mobileEditText = findViewById(R.id.mobile_edit_text);
        identityEditText = findViewById(R.id.identity_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);

        nameErrorText = findViewById(R.id.name_error_text);
        emailErrorText = findViewById(R.id.email_error_text);
        mobileErrorText = findViewById(R.id.mobile_error_text);
        identityErrorText = findViewById(R.id.identity_error_text);
        passwordErrorText = findViewById(R.id.password_error_text);

        mobileEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 10) {
                    mobileEditText.setText(s.subSequence(0, 10));
                    mobileEditText.setSelection(10);
                }
            }
        });

        identityEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (isFormattingIdentity) return;
                isFormattingIdentity = true;
                String raw = s.toString().replaceAll("-", "");
                if (raw.length() > 13) raw = raw.substring(0, 13);
                StringBuilder formatted = new StringBuilder();
                for (int i = 0; i < raw.length(); i++) {
                    if (i == 5 || i == 12) formatted.append("-");
                    formatted.append(raw.charAt(i));
                }
                identityEditText.setText(formatted.toString());
                identityEditText.setSelection(formatted.length());
                isFormattingIdentity = false;
            }
        });

        findViewById(R.id.signup_button).setOnClickListener(v -> {
            if (validateInputs()) {
                String name = nameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String phone = "+92" + mobileEditText.getText().toString().trim();
                String cnic = identityEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                Intent intent = new Intent(SignupActivity.this, OtpAuthenticationActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                intent.putExtra("phone", phone);
                intent.putExtra("cnic", cnic);
                intent.putExtra("password", password);
                startActivity(intent);
            }
        });
    }

    private boolean validateInputs() {
        boolean isValid = true;
        clearErrors();

        if (TextUtils.isEmpty(nameEditText.getText())) {
            showError(nameErrorText, "Full name is required.");
            isValid = false;
        }

        String email = emailEditText.getText().toString().trim();
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError(emailErrorText, "Invalid email format.");
            isValid = false;
        }

        String mobile = mobileEditText.getText().toString().trim();
        if (!mobile.matches("\\d{10}")) {
            showError(mobileErrorText, "Enter exactly 10 digits.");
            isValid = false;
        }

        String identity = identityEditText.getText().toString().trim().replaceAll("-", "");
        if (!identity.matches("\\d{13}")) {
            showError(identityErrorText, "Enter exactly 13 digits.");
            isValid = false;
        }

        String password = passwordEditText.getText().toString().trim();
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])(?=\\S+$).{8,}$";
        if (!Pattern.compile(passwordPattern).matcher(password).matches()) {
            showError(passwordErrorText, "Password must have 8+ chars with digit, upper, lower, and special.");
            isValid = false;
        }

        return isValid;
    }

    private void showError(TextView view, String message) {
        view.setText(message);
        view.setVisibility(View.VISIBLE);
    }

    private void clearErrors() {
        nameErrorText.setVisibility(View.GONE);
        emailErrorText.setVisibility(View.GONE);
        mobileErrorText.setVisibility(View.GONE);
        identityErrorText.setVisibility(View.GONE);
        passwordErrorText.setVisibility(View.GONE);
    }
}
