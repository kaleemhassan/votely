package com.example.votely;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.widget.ImageView;
import android.widget.LinearLayout;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Find views
        LinearLayout nominee1Card = view.findViewById(R.id.nominee1_card);
        LinearLayout nominee2Card = view.findViewById(R.id.nominee2_card);
        LinearLayout nominee3Card = view.findViewById(R.id.nominee3_card);
        View votingCard = view.findViewById(R.id.voting_card);
        ImageView votingArrow = view.findViewById(R.id.voting_arrow);

        // Set click listeners
        nominee1Card.setOnClickListener(v -> openFragment(new Nominee1Fragment()));
        nominee2Card.setOnClickListener(v -> openFragment(new Nominee2Fragment()));
        nominee3Card.setOnClickListener(v -> openFragment(new Nominee3Fragment()));

        View.OnClickListener votingClickListener = v -> openFragment(new VotingFragment());
        votingCard.setOnClickListener(votingClickListener);
        votingArrow.setOnClickListener(votingClickListener);

        return view;
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity()
                .getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.fragment_container_view, fragment); // Make sure your main layout has this ID
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
