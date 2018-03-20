package com.example.a2048game;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by Alexander Lewis on 4/13/2017.
 */

public class ButtonFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.v("ButtonFragment: ", "onCreateView()");

        View view =
                inflater.inflate(R.layout.button_fragment, container, false);

        // configure listeners for the buttons
        ImageButton upButton = (ImageButton) view.findViewById(R.id.upArrow);
        ImageButton downButton = (ImageButton) view.findViewById(R.id.downArrow);
        ImageButton leftButton = (ImageButton) view.findViewById(R.id.leftArrow);
        ImageButton rightButton = (ImageButton) view.findViewById(R.id.rightArrow);
        Button restartButton = (Button) view.findViewById(R.id.restartButton);
        upButton.setOnClickListener(upButtonListener);
        downButton.setOnClickListener(downButtonListener);
        leftButton.setOnClickListener(leftButtonListener);
        rightButton.setOnClickListener(rightButtonListener);
        restartButton.setOnClickListener(restartButtonListener);

        Log.v("ButtonFragment: ", "onCreateView(end)");
        return view;
    }

    // called when the up button is touched
    private View.OnClickListener upButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v("ButtonFragment: ", "upButtonListener");
            GridFragment.updateGrid(null);
            GridFragment.moveUp();
            ScoreFragment.updateScore();
            Log.v("ButtonFragment: ", "upButtonListener(end)");
        }
    };

    // called when the up button is touched
    private View.OnClickListener downButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v("ButtonFragment: ", "downButtonListener");
            GridFragment.updateGrid(null);
            GridFragment.moveDown();
            ScoreFragment.updateScore();
            Log.v("ButtonFragment: ", "downButtonListener(end)");
        }
    };

    // called when the up button is touched
    private View.OnClickListener leftButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v("ButtonFragment: ", "leftButtonListener");
            GridFragment.updateGrid(null);
            GridFragment.moveLeft();
            ScoreFragment.updateScore();
            Log.v("ButtonFragment: ", "leftButtonListener(end)");
        }
    };

    // called when the up button is touched
    private View.OnClickListener rightButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v("ButtonFragment: ", "rightButtonListener");
            GridFragment.updateGrid(null);
            GridFragment.moveRight();
            ScoreFragment.updateScore();
            Log.v("ButtonFragment: ", "rightButtonListener(end)");
        }
    };

    // called when the up button is touched
    private View.OnClickListener restartButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v("ButtonFragment: ", "restartButtonListener");
            GridFragment.resetGame();
            Log.v("ButtonFragment: ", "restartButtonListener(end)");
        }
    };

}
