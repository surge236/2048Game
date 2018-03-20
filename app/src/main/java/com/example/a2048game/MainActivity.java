package com.example.a2048game;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public static int topScore = 0;

    SharedPreferences sharedpreferences;
    public static final String GAMEPREF= "pref_game";
    public static final String SCORE = "pref_score";
    public static final String TOPSCORE = "pref_topScore";
    public static final String FILLEDBLOCKS = "pref_filledBlocks";
    public static final String BLOCK0 = "pref_block0"; public static final String BLOCK1 = "pref_block1";
    public static final String BLOCK2 = "pref_block2"; public static final String BLOCK3 = "pref_block3";
    public static final String BLOCK4 = "pref_block4"; public static final String BLOCK5 = "pref_block5";
    public static final String BLOCK6 = "pref_block6"; public static final String BLOCK7 = "pref_block7";
    public static final String BLOCK8 = "pref_block8"; public static final String BLOCK9 = "pref_block9";
    public static final String BLOCK10 = "pref_block10"; public static final String BLOCK11 = "pref_block11";
    public static final String BLOCK12 = "pref_block12"; public static final String BLOCK13 = "pref_block13";
    public static final String BLOCK14 = "pref_block14"; public static final String BLOCK15 = "pref_block15";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("MainActivity: ", "onCreate()");

        if (savedInstanceState == null) {
            setContentView(R.layout.activity_main);

            // determine screen size
            int screenSize = getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK;

            // allow only portrait configuration
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        // pull values from the sharedPreferences
        sharedpreferences = getSharedPreferences(GAMEPREF,
                Context.MODE_PRIVATE);
        ButtonFragment bFragment = (ButtonFragment)
                getSupportFragmentManager().findFragmentById(
                        R.id.buttonFragment);
        //GridFragment gFragment = (GridFragment)////////////////////////////////////////////////////
        //        getSupportFragmentManager().findFragmentById(
        //                R.id.gridFragment);
        ScoreFragment sFragment = (ScoreFragment)
                getSupportFragmentManager().findFragmentById(
                        R.id.scoreFragment);

        if (sharedpreferences.contains(SCORE)) {
            GridFragment.updateScore(sharedpreferences.getInt(SCORE, 0));
        }
        if (sharedpreferences.contains(TOPSCORE)) {
            GridFragment.updateTopScore(sharedpreferences.getInt(TOPSCORE, 0));
        }
        if (sharedpreferences.contains(FILLEDBLOCKS)) {
            GridFragment.updateFilledBlocks(sharedpreferences.getInt(FILLEDBLOCKS, 0));
        }
        if (sharedpreferences.contains(BLOCK0)) {
            int[] tempGrid = new int[16];
            tempGrid[0] = sharedpreferences.getInt(BLOCK0, -1); tempGrid[1] = sharedpreferences.getInt(BLOCK1, -1);
            tempGrid[2] = sharedpreferences.getInt(BLOCK2, -1); tempGrid[3] = sharedpreferences.getInt(BLOCK3, -1);
            tempGrid[4] = sharedpreferences.getInt(BLOCK4, -1); tempGrid[5] = sharedpreferences.getInt(BLOCK5, -1);
            tempGrid[6] = sharedpreferences.getInt(BLOCK6, -1); tempGrid[7] = sharedpreferences.getInt(BLOCK7, -1);
            tempGrid[8] = sharedpreferences.getInt(BLOCK8, -1); tempGrid[9] = sharedpreferences.getInt(BLOCK9, -1);
            tempGrid[10] = sharedpreferences.getInt(BLOCK10, -1); tempGrid[11] = sharedpreferences.getInt(BLOCK11, -1);
            tempGrid[12] = sharedpreferences.getInt(BLOCK12, -1); tempGrid[13] = sharedpreferences.getInt(BLOCK13, -1);
            tempGrid[14] = sharedpreferences.getInt(BLOCK14, -1); tempGrid[15] = sharedpreferences.getInt(BLOCK15, -1);
            GridFragment.updateGrid(tempGrid);
        }

        Log.v("MainActivity: ", "onCreate(end)");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("MainActivity: ", "onPause()");
        save();
        Log.v("MainActivity: ", "onPause(end)");
    }

    public void save() {
        Log.v("MainActivity: ", "save()");
        //GridFragment gameFragment = (GridFragment)
        //        getSupportFragmentManager().findFragmentById(
        //                R.id.gridFragment);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        int[] savedGrid = GridFragment.getGrid();

        editor.putInt(SCORE, GridFragment.getScore());
        editor.putInt(TOPSCORE, GridFragment.getTopScore());
        editor.putInt(FILLEDBLOCKS, GridFragment.getFilledBlocks());
        editor.putInt(BLOCK0, savedGrid[0]); editor.putInt(BLOCK1, savedGrid[1]);
        editor.putInt(BLOCK2, savedGrid[2]); editor.putInt(BLOCK3, savedGrid[3]);
        editor.putInt(BLOCK4, savedGrid[4]); editor.putInt(BLOCK5, savedGrid[5]);
        editor.putInt(BLOCK6, savedGrid[6]); editor.putInt(BLOCK7, savedGrid[7]);
        editor.putInt(BLOCK8, savedGrid[8]); editor.putInt(BLOCK9, savedGrid[9]);
        editor.putInt(BLOCK10, savedGrid[10]); editor.putInt(BLOCK11, savedGrid[11]);
        editor.putInt(BLOCK12, savedGrid[12]); editor.putInt(BLOCK13, savedGrid[13]);
        editor.putInt(BLOCK14, savedGrid[14]); editor.putInt(BLOCK15, savedGrid[15]);
        editor.commit();
        Log.v("MainActivity: ", "save(end)");
    }

}
