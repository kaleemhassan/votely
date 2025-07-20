package com.example.votely;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
// Import MaterialButton if you are passing MaterialButton instances
import com.google.android.material.button.MaterialButton;

import android.widget.Button;
import android.widget.Toast; // Keep if used

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.card.MaterialCardView;

public class VotingFragment extends Fragment {

    private MaterialCardView card1, card2, card3;
    // Change Button to MaterialButton if these are indeed your rounded_icon_buttons
    private MaterialButton voteBtn1, voteBtn2, voteBtn3;
    // private View dimView; // Uncomment if you bring this back

    private String selectedName;
    private int selectedImageResId;
    private MaterialButton currentSelectedVoteButton = null; // To keep track of the currently selected button

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_voting, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Hide bottom nav
        View bottomNav = requireActivity().findViewById(R.id.custom_bottom_nav);
        if (bottomNav != null) bottomNav.setVisibility(View.GONE);

        // Initialize views
        card1 = view.findViewById(R.id.card_nominee_1);
        card2 = view.findViewById(R.id.card_nominee_2);
        card3 = view.findViewById(R.id.card_nominee_3);

        // Ensure these IDs match your MaterialButton instances in the XML
        voteBtn1 = view.findViewById(R.id.rounded_icon_button1);
        voteBtn2 = view.findViewById(R.id.rounded_icon_button2);
        voteBtn3 = view.findViewById(R.id.rounded_icon_button3);

        Button nextButton = view.findViewById(R.id.next_button);

        // Set up click listeners for CARDS (if cards themselves are clickable to select)
        // If only the MaterialButtons are clickable for selection, this might change.
        // Assuming your existing structure where clicking the card area selects.
        card1.setOnClickListener(v -> animateCardClick(v, () -> selectCandidate(1)));
        voteBtn1.setOnClickListener(v -> { // If MaterialButtons are also directly clickable for selection
            animateButtonClick(v, () -> selectCandidate(1));
        });


        card2.setOnClickListener(v -> animateCardClick(v, () -> selectCandidate(2)));
        voteBtn2.setOnClickListener(v -> animateButtonClick(v, () -> selectCandidate(2)));

        card3.setOnClickListener(v -> animateCardClick(v, () -> selectCandidate(3)));
        voteBtn3.setOnClickListener(v -> animateButtonClick(v, () -> selectCandidate(3)));


        nextButton.setOnClickListener(v -> {
            if (selectedName != null) {
                ConfirmVoteFragment frag = ConfirmVoteFragment.newInstance(
                        selectedName, selectedImageResId);
                FragmentTransaction ft = requireActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(
                        R.anim.slide_up, R.anim.fade_out,
                        R.anim.fade_in, R.anim.slide_down);
                ft.add(R.id.fragment_container_view, frag)
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(getContext(), "Please select a candidate", Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize all to unselected state visually
        resetCard(card1, voteBtn1);
        resetCard(card2, voteBtn2);
        resetCard(card3, voteBtn3);
    }

    // Helper for card click animation
    private void animateCardClick(View cardView, Runnable onEndAction) {
        // You can add card-specific animation if needed, or remove if not distinct
        onEndAction.run(); // For now, just run action
    }

    // Helper for button click animation (your existing one)
    private void animateButtonClick(View buttonView, Runnable onEndAction) {
        buttonView.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).withEndAction(() -> buttonView.animate().scaleX(1f).scaleY(1f).setDuration(100).withEndAction(onEndAction).start()).start();
    }


    private void selectCandidate(int id) {
        // First, reset all cards and their associated buttons to their default state
        resetCard(card1, voteBtn1);
        resetCard(card2, voteBtn2);
        resetCard(card3, voteBtn3);

        // Then, highlight the selected one
        switch (id) {
            case 1:
                highlightCard(card1, voteBtn1);
                selectedName = "Imran Khan"; // Or get from card's data
                selectedImageResId = R.drawable.nominee1;
                currentSelectedVoteButton = voteBtn1;
                break;
            case 2:
                highlightCard(card2, voteBtn2);
                selectedName = "Bilawal Bhutto";
                selectedImageResId = R.drawable.nominee2;
                currentSelectedVoteButton = voteBtn2;
                break;
            case 3:
                highlightCard(card3, voteBtn3);
                selectedName = "Nawaz Sharif";
                selectedImageResId = R.drawable.nominee3;
                currentSelectedVoteButton = voteBtn3;
                break;
        }
    }

    // Update to use MaterialButton
    private void resetCard(MaterialCardView card, MaterialButton btn) {
        if (card == null || btn == null || getContext() == null) return; // Basic null safety

        card.setStrokeColor(ContextCompat.getColor(requireContext(), R.color.color_stroke_default));
        // The button's background is now controlled by its checked state and the selector drawable.
        // So, just set its checked state to false.
        btn.setChecked(false);
        // btn.setText(""); // Already set in XML
        // btn.setSelected(false); // Using btn.setChecked(false) is more appropriate for checkable behavior
    }

    // Update to use MaterialButton
    private void highlightCard(MaterialCardView card, MaterialButton btn) {
        if (card == null || btn == null || getContext() == null) return; // Basic null safety

        card.setStrokeColor(ContextCompat.getColor(requireContext(), R.color.color_stroke_selected));
        // The button's background is now controlled by its checked state and the selector drawable.
        // So, just set its checked state to true.
        btn.setChecked(true);
        // btn.setText(""); // Already set in XML
        // btn.setTextColor(...); // The text color for an empty text button isn't relevant here.
        // If you add an icon later, you might use app:iconTint in XML
        // or programmatically setIconTintList.
        // btn.setSelected(true); // Using btn.setChecked(true) is more appropriate
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        View bottomNav = requireActivity().findViewById(R.id.custom_bottom_nav);
        if (bottomNav != null) bottomNav.setVisibility(View.VISIBLE);
    }
}
