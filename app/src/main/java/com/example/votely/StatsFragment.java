package com.example.votely;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class StatsFragment extends Fragment {
    public StatsFragment()  {}

    private FirebaseFirestore db;
    private CollectionReference candidatesRef;

    private TextView totalVotesText, votingPercentageText;
    private TextView hoursText, minutesText, secondsText;

    private TextView winnerName, winnerVotes, winnerVotesPercentage;
    private ImageView winnerImage;

    private TextView secondName, secondVotes, secondVotesPercentage;
    private ImageView secondImage;

    private TextView thirdName, thirdVotes, thirdVotesPercentage;
    private ImageView thirdImage;

    private View firstProgress, secondProgress, thirdProgress;

    private final Handler timerHandler = new Handler();
    private Runnable timerRunnable;

    private final long TOTAL_DAY_MILLIS = 24 * 60 * 60 * 1000;

    static class Candidate {
        String name;
        long votes;
        String party;

        Candidate(String name, long votes, String party) {
            this.name = name;
            this.votes = votes;
            this.party = party;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        db = FirebaseFirestore.getInstance();
        candidatesRef = db.collection("candidates");

        totalVotesText = view.findViewById(R.id.total_votes);
        votingPercentageText = view.findViewById(R.id.voting_percentage);
        hoursText = view.findViewById(R.id.hours_text);
        minutesText = view.findViewById(R.id.minutes_text);
        secondsText = view.findViewById(R.id.seconds_text);

        winnerName = view.findViewById(R.id.winner_name);
        winnerVotes = view.findViewById(R.id.winner_votes);
        winnerVotesPercentage = view.findViewById(R.id.winner_votes_percentage);
        winnerImage = view.findViewById(R.id.winner_image);

        secondName = view.findViewById(R.id.secondpos_name);
        secondVotes = view.findViewById(R.id.secondpos_votes);
        secondVotesPercentage = view.findViewById(R.id.secondpos_votes_percentage);
        secondImage = view.findViewById(R.id.secondpos_image);

        thirdName = view.findViewById(R.id.thirdpos_name);
        thirdVotes = view.findViewById(R.id.thirdpos_votes);
        thirdVotesPercentage = view.findViewById(R.id.thirdpos_votes_percentage);
        thirdImage = view.findViewById(R.id.thirdpos_image);

        firstProgress = view.findViewById(R.id.first_progress);
        secondProgress = view.findViewById(R.id.second_progress);
        thirdProgress = view.findViewById(R.id.third_progress);

        fetchVotesAndUpdateUI();
        startTimer();

        return view;
    }

    public void onClickRefresh(View view) {
        fetchVotesAndUpdateUI();
        startTimer();
    }

    private void fetchVotesAndUpdateUI() {
        candidatesRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Candidate> candidates = new ArrayList<>();
                long totalVotes = 0;

                for (QueryDocumentSnapshot doc : task.getResult()) {
                    String name = doc.getId();
                    long votes = doc.getLong("votes") != null ? doc.getLong("votes") : 0;
                    String party = doc.getString("party") != null ? doc.getString("party") : "";
                    totalVotes += votes;
                    candidates.add(new Candidate(name, votes, party));
                }

                totalVotesText.setText(String.valueOf(totalVotes));

                Collections.sort(candidates, (a, b) -> Long.compare(b.votes, a.votes));

                updateCandidateCard(winnerName, winnerVotes, winnerVotesPercentage, winnerImage, firstProgress, candidates.get(0), totalVotes, true);
                updateCandidateCard(secondName, secondVotes, secondVotesPercentage, secondImage, secondProgress, candidates.get(1), totalVotes, false);
                updateCandidateCard(thirdName, thirdVotes, thirdVotesPercentage, thirdImage, thirdProgress, candidates.get(2), totalVotes, false);
            }
        });
    }

    private void updateCandidateCard(TextView nameView, TextView voteView, TextView percentView, ImageView imageView, View progressView, Candidate candidate, long totalVotes, boolean isWinner) {
        nameView.setText(candidate.name);
        voteView.setText(String.valueOf(candidate.votes));
        long percent = totalVotes > 0 ? (candidate.votes * 100 / totalVotes) : 0;
        percentView.setText(percent + "%");

        switch (candidate.name) {
            case "Imran Khan":
                imageView.setImageResource(R.drawable.nominee1);
                break;
            case "Bilawal Bhutto":
                imageView.setImageResource(R.drawable.nominee2);
                break;
            case "Nawaz Sharif":
                imageView.setImageResource(R.drawable.nominee3);
                break;
            default:
                imageView.setImageResource(R.drawable.nominee1);
                break;
        }

        progressView.post(() -> {
            View parent = (View) progressView.getParent();
            int fullWidth = parent.getWidth();
            int fillWidth = (int) (fullWidth * (percent / 100.0));
            ViewGroup.LayoutParams params = progressView.getLayoutParams();
            params.width = fillWidth;
            progressView.setLayoutParams(params);
            progressView.setBackgroundResource(isWinner ? R.drawable.progress_yellow_rounded : R.drawable.progress_blue_rounded);
        });
    }

    private void startTimer() {
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                long startOfDay = calendar.getTimeInMillis();
                long elapsed = now - startOfDay;
                long remaining = TOTAL_DAY_MILLIS - elapsed;
                if (remaining <= 0) remaining = TOTAL_DAY_MILLIS;

                int hours = (int) (remaining / (1000 * 60 * 60));
                int minutes = (int) ((remaining / (1000 * 60)) % 60);
                int seconds = (int) ((remaining / 1000) % 60);

                hoursText.setText(String.format("%02d", hours));
                minutesText.setText(String.format("%02d", minutes));
                secondsText.setText(String.format("%02d", seconds));

                int percentLeft = (int) ((remaining * 100) / TOTAL_DAY_MILLIS);
                votingPercentageText.setText(percentLeft + "%");

                timerHandler.postDelayed(this, 1000);
            }
        };
        timerHandler.post(timerRunnable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        timerHandler.removeCallbacks(timerRunnable);
    }
}
