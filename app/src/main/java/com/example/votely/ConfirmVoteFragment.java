package com.example.votely;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ConfirmVoteFragment extends Fragment {

    private String candidateName;
    private int candidateImageResId;
    private View dimView;
    private ImageView candidateImageView;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public ConfirmVoteFragment() {}

    public static ConfirmVoteFragment newInstance(String name, int imageResId) {
        ConfirmVoteFragment fragment = new ConfirmVoteFragment();
        Bundle args = new Bundle();
        args.putString("candidateName", name);
        args.putInt("candidateImageResId", imageResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_confirm_vote, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView selectedCandidateImage = view.findViewById(R.id.selected_candidate_image);
        TextView selectedCandidateName = view.findViewById(R.id.selected_candidate_name);
        MaterialButton confirmVoteButton = view.findViewById(R.id.confirm_vote_button);
        TextView changeVoteButton = view.findViewById(R.id.change_vote_button);

        // Find dimView from parent fragment
       // dimView = requireActivity().findViewById(R.id.dim_view);
      //  if (dimView == null) {
      //      dimView = new View(requireContext());
      //      dimView.setId(R.id.dim_view);
      //      dimView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
      //      dimView.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.black));
       //     dimView.setAlpha(0.5f);
       //     ((ViewGroup) requireActivity().findViewById(R.id.fragment_container_view)).addView(dimView);
       // }

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        if (getArguments() != null) {
            candidateName = getArguments().getString("candidateName");
            candidateImageResId = getArguments().getInt("candidateImageResId");
        }

        selectedCandidateName.setText(candidateName);
        selectedCandidateImage.setImageResource(candidateImageResId);

        confirmVoteButton.setOnClickListener(v -> confirmVote());
        changeVoteButton.setOnClickListener(v -> {
          //  if (dimView != null) {
          //      dimView.animate().alpha(0f).setDuration(300).withEndAction(() -> dimView.setVisibility(View.GONE)).start();
          //  }
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void confirmVote() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();

        // Verify user exists in users collection (document ID is uid)
        db.collection("users").document(userId).get()
                .addOnSuccessListener(userDoc -> {
                    if (!userDoc.exists()) {
                        Toast.makeText(getContext(), "User not found in database", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Check if user has already voted
                    db.collection("votes").document(userId).get()
                            .addOnSuccessListener(voteDoc -> {
                                if (voteDoc.exists()) {
                                    Toast.makeText(getContext(), "You have already voted!", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Proceed with voting
                                    DocumentReference candidateRef = db.collection("candidates").document(candidateName);
                                    candidateRef.get().addOnSuccessListener(candidateDoc -> {
                                        if (!candidateDoc.exists()) {
                                            // Fallback: Create candidate document if it doesn't exist
                                            Map<String, Object> candidateData = new HashMap<>();
                                            candidateData.put("votes", 0);
                                            candidateData.put("party", candidateName.equals("Imran Khan") ? "Pakistan Tehreek-e-Insaf" :
                                                    candidateName.equals("Bilawal Bhutto") ? "Pakistan Peoples Party" :
                                                            "Pakistan Muslim League N");
                                            candidateRef.set(candidateData).addOnSuccessListener(aVoid -> proceedWithVote(userId, candidateRef))
                                                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Error initializing candidate: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                        } else {
                                            proceedWithVote(userId, candidateRef);
                                        }
                                    }).addOnFailureListener(e -> Toast.makeText(getContext(), "Error checking candidate: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                }
                            })
                            .addOnFailureListener(e -> Toast.makeText(getContext(), "Error checking vote status: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error verifying user: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void proceedWithVote(String userId, DocumentReference candidateRef) {
        // Increment vote count for candidate
        candidateRef.update("votes", com.google.firebase.firestore.FieldValue.increment(1))
                .addOnSuccessListener(aVoid -> {
                    // Mark user as voted
                    Map<String, Object> voteData = new HashMap<>();
                    voteData.put("votedFor", candidateName);
                    voteData.put("timestamp", System.currentTimeMillis());

                    db.collection("votes").document(userId).set(voteData)
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(getContext(), "Vote submitted successfully!", Toast.LENGTH_SHORT).show();
                                if (dimView != null) {
                                    dimView.animate().alpha(0f).setDuration(300).withEndAction(() -> dimView.setVisibility(View.GONE)).start();
                                }
                                requireActivity().getSupportFragmentManager().popBackStack("HomeFragment", 0);
                            })
                            .addOnFailureListener(e -> Toast.makeText(getContext(), "Error saving vote: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error submitting vote: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Ensure dimView persists for back navigation but is hidden
       // if (dimView != null) {
       //     dimView.setVisibility(View.GONE);
      //  }
    }
}