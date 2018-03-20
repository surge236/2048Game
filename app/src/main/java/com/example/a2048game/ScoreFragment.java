package com.example.a2048game;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * Created by Alexander Lewis on 4/13/2017.
 */

public class ScoreFragment extends Fragment {

    private static TextView scoreTextView; // shows the user's current score
    private static TextView topScoreTextView; // shows the user's top score
    private static GridFragment gameView; // references the game grid

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        Log.v("ScoreFragment: ", "onCreateView()");

        View view =
                inflater.inflate(R.layout.score_fragment, container, false);

        // get references to GUI components
        scoreTextView =
                (TextView) view.findViewById(R.id.scoreTextView);
        topScoreTextView =
                (TextView) view.findViewById(R.id.topScoreTextView);

        // set scoreTextView's text
        scoreTextView.setText(
                getString(R.string.score, GridFragment.getScore()));

        // set topScoreView's text
        topScoreTextView.setText(
                getString(R.string.top_score, GridFragment.getTopScore()));

        Log.v("ScoreFragment: ", "onCreateView(end)");
        return view;

    }

    // update the player's top score
    public static void updateTopScore() {
        Log.v("ScoreFragment: ", "updateTopScore()");
        int newScore = GridFragment.getTopScore();
        String newScoreString = "Top Score: " + newScore;
        if (newScore > -1) topScoreTextView.setText(newScoreString);
        else topScoreTextView.setText("0");
        Log.v("ScoreFragment: ", "updateTopScore(end)");
    }

    // update the player's score
    public static void updateScore() {
        Log.v("ScoreFragment: ", "updateScore()");
        int newScore = GridFragment.getScore();
        String newScoreString = "Score: " + newScore;
        if (newScore > -1) scoreTextView.setText(newScoreString);
        else scoreTextView.setText("0");
        Log.v("ScoreFragment: ", "updateScore(end)");
    }

}
