package com.example.votely;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout tabHome, tabStats, tabProfile;
    private TextView textHome, textStats, textProfile;
    private View selectedTabHighlight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tabHome = findViewById(R.id.tab_home);
        tabStats = findViewById(R.id.tab_stats);
        tabProfile = findViewById(R.id.tab_profile);

        textHome = findViewById(R.id.text1);
        textStats = findViewById(R.id.text2);
        textProfile = findViewById(R.id.text3);

        selectedTabHighlight = findViewById(R.id.selected_tab_highlight);

        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment(), "HOME_FRAGMENT_TAG");
            updateTabUI(tabHome);
        } else {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container_view);
            if (currentFragment instanceof HomeFragment) {
                updateTabUI(tabHome);
            } else if (currentFragment instanceof StatsFragment) {
                updateTabUI(tabStats);
            } else if (currentFragment instanceof ProfileFragment) {
                updateTabUI(tabProfile);
            } else {
                // Fallback or if no fragment found, default to home
                updateTabUI(tabHome);
            }
        }

        tabHome.setOnClickListener(v -> {
            if (!(getSupportFragmentManager().findFragmentById(R.id.fragment_container_view) instanceof HomeFragment)) {
                replaceFragment(new HomeFragment(), "HOME_FRAGMENT_TAG");
            }
            updateTabUI(tabHome);
        });

        tabStats.setOnClickListener(v -> {
            if (!(getSupportFragmentManager().findFragmentById(R.id.fragment_container_view) instanceof StatsFragment)) {
                replaceFragment(new StatsFragment(), "STATS_FRAGMENT_TAG");
            }
            updateTabUI(tabStats);
        });

        tabProfile.setOnClickListener(v -> {
            if (!(getSupportFragmentManager().findFragmentById(R.id.fragment_container_view) instanceof ProfileFragment)) {
                replaceFragment(new ProfileFragment(), "PROFILE_FRAGMENT_TAG");
            }
            updateTabUI(tabProfile);
        });
    }

    private void replaceFragment(Fragment fragment, String tag) {
        // ... (your existing replaceFragment code)
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.setCustomAnimations(
                R.anim.fade_in, R.anim.fade_out,
                R.anim.fade_in, R.anim.fade_out
        );
        transaction.replace(R.id.fragment_container_view, fragment, tag);

        if (!tag.equals("HOME_FRAGMENT_TAG") || fragmentManager.getBackStackEntryCount() > 0) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }

    private void updateTabUI(View selectedTab) {
        textHome.setVisibility(View.GONE);
        textStats.setVisibility(View.GONE);
        textProfile.setVisibility(View.GONE);

        if (selectedTab.getId() == R.id.tab_home) {
            textHome.setVisibility(View.VISIBLE);
        } else if (selectedTab.getId() == R.id.tab_stats) {
            textStats.setVisibility(View.VISIBLE);
        } else if (selectedTab.getId() == R.id.tab_profile) {
            textProfile.setVisibility(View.VISIBLE);
        }

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) selectedTabHighlight.getLayoutParams();

        params.startToStart = ConstraintLayout.LayoutParams.UNSET;
        params.endToEnd = ConstraintLayout.LayoutParams.UNSET;
        // Also clear any other horizontal constraints that might conflict
        params.startToEnd = ConstraintLayout.LayoutParams.UNSET;
        params.endToStart = ConstraintLayout.LayoutParams.UNSET;

        // Constrain the highlight to the start and end of the *selectedTab*
        params.startToStart = selectedTab.getId();
        params.endToEnd = selectedTab.getId();

        selectedTabHighlight.setLayoutParams(params);
        // It's good practice to call requestLayout if you're changing constraints
        // that affect size or position, though it often works without it.
        // selectedTabHighlight.requestLayout();
    }
}
