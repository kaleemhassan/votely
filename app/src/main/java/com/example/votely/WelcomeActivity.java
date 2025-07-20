package com.example.votely;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "WelcomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // FirebaseAuth check
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            // User is already logged in, go to HomeActivity
            startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
            finish();
        } else {
            // User is not logged in, setup Get Started button
            Button letsBeginButton = findViewById(R.id.letsBeginButton);

            if (letsBeginButton != null) {
                letsBeginButton.setOnClickListener(v -> {
                    try {
                        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                        finish();
                    } catch (Exception e) {
                        Log.e(TAG, "Error navigating to LoginActivity", e);
                        Toast.makeText(this, "An error occurred while navigating", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Log.e(TAG, "Button letsBeginButton not found in layout");
                Toast.makeText(this, "Button not found", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
