package com.example.votely;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;

public class ProfileFragment extends Fragment {

    private TextView tvUserName, tvUserEmail;
    private Button btnLogout;
    private ImageView imgProfilePic;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public ProfileFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvUserName = view.findViewById(R.id.tvName);
        tvUserEmail = view.findViewById(R.id.tvEmail);
        btnLogout = view.findViewById(R.id.logout_button);
        imgProfilePic = view.findViewById(R.id.imgProfile); // optional

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            tvUserEmail.setText(user.getEmail());
            loadProfileFromFirestore(user.getUid());
        } else {
            Toast.makeText(getContext(), "No user signed in", Toast.LENGTH_SHORT).show();
        }

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void loadProfileFromFirestore(String uid) {
        db.collection("users").document(uid).get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        String name = document.getString("name");
                        if (name != null) tvUserName.setText(name);
                    } else {
                        tvUserName.setText("Votely User");
                    }
                })
                .addOnFailureListener(e -> {
                    tvUserName.setText("Votely User");
                    Toast.makeText(getContext(), "Failed to load profile", Toast.LENGTH_SHORT).show();
                });
    }
}
