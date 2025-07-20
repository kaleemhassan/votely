package com.example.votely;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Nominee3Fragment extends Fragment {

    private View bottomNav;

    public Nominee3Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nominee3, container, false); // Make sure the layout file is named `fragment_nominee2.xml`
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomNav = requireActivity().findViewById(R.id.custom_bottom_nav);
        if (bottomNav != null) {
            bottomNav.setVisibility(View.GONE);  // Hide bottom nav
        }

        // Twitter Click
        LinearLayout twitter = view.findViewById(R.id.onTwitterClick);
        twitter.setOnClickListener(v -> openLink("https://twitter.com/ImranKhanPTI"));

        // Facebook Click
        LinearLayout facebook = view.findViewById(R.id.onFacebookClick);
        facebook.setOnClickListener(v -> openLink("https://facebook.com/ImranKhanOfficial"));

        // Instagram Click
        LinearLayout instagram = view.findViewById(R.id.onInstagramClick);
        instagram.setOnClickListener(v -> openLink("https://instagram.com/imrankhan.pti"));
    }

    private void openLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Restore nav bar visibility
        if (bottomNav != null) {
            bottomNav.setVisibility(View.VISIBLE);
        }
    }
}
