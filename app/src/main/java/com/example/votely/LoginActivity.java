package com.example.votely;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private TextView emailErrorText, passwordErrorText;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        emailErrorText = findViewById(R.id.email_error_text);
        passwordErrorText = findViewById(R.id.password_error_text);

        // Set empty text to preserve layout space
        clearErrors();

        findViewById(R.id.login_button).setOnClickListener(v -> {
            if (validateInputs()) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null) {
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                    finish();
                                }
                            } else {
                                showError(passwordErrorText, "Login failed: Invalid email or password.");
                                passwordEditText.setError("Login failed");
                            }
                        });
            }
        });

        findViewById(R.id.signup_link).setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            finish();
        });
    }

    private boolean validateInputs() {
        boolean isValid = true;
        clearErrors();

        String email = emailEditText.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            showError(emailErrorText, "Email is required.");
            emailEditText.setError("Required");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError(emailErrorText, "Invalid email format.");
            emailEditText.setError("Invalid email");
            isValid = false;
        }

        String password = passwordEditText.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            showError(passwordErrorText, "Password is required.");
            passwordEditText.setError("Required");
            isValid = false;
        }

        return isValid;
    }

    private void showError(TextView view, String message) {
        view.setText(message);
    }

    private void clearErrors() {
        emailErrorText.setText("");
        passwordErrorText.setText("");
    }
}
