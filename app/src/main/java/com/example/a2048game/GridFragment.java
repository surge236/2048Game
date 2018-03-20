package com.example.a2048game;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Alexander Lewis on 4/13/2017.
 */

public class GridFragment extends LinearLayout {

    private static final String TAG = "Game Activity";

    // touch controls
    private float mPreviousX;
    private float mPreviousY;

    private static final int GRIDSIZE = 4;
    private static final int FOURPROB = 25;

    private static int[][] grid;
    private static int filledBlocks = 0;
    private static int totalCount = 0;     // increment by 2 when 2 is added and 4 when 4 is added.
    private static int score = 0; // increment on combine by combined value.
    private static int topScore = 0; // top score ever earned.
    private static int actionPerformed = 0; // tracks if a motion control was executed

    private static GridLayout gameLinearLayout; // layout that contains the game

    public GridFragment(Context context, AttributeSet attrs) {

        super(context, attrs);
        Log.v("GridFragment: ", "GridFragment()");

        init();
        grid = new int[GRIDSIZE][GRIDSIZE];

        // get references to GUI components
        gameLinearLayout =
                (GridLayout) this.findViewById(R.id.gameLinearLayout);

        // set gameGrid contents text as empty
        //TextView block1 = (TextView) row1.getChildAt(0); block1.setText(getString(R.string.block1, " "));
        TextView block1 = (TextView) gameLinearLayout.getChildAt(0); block1.setText(R.string.block1);
        TextView block2 = (TextView) gameLinearLayout.getChildAt(1); block2.setText(R.string.block2);
        TextView block3 = (TextView) gameLinearLayout.getChildAt(2); block3.setText(R.string.block3);
        TextView block4 = (TextView) gameLinearLayout.getChildAt(3); block4.setText(R.string.block4);
        TextView block5 = (TextView) gameLinearLayout.getChildAt(4); block5.setText(R.string.block5);
        TextView block6 = (TextView) gameLinearLayout.getChildAt(5); block6.setText(R.string.block6);
        TextView block7 = (TextView) gameLinearLayout.getChildAt(6); block7.setText(R.string.block7);
        TextView block8 = (TextView) gameLinearLayout.getChildAt(7); block8.setText(R.string.block8);
        TextView block9 = (TextView) gameLinearLayout.getChildAt(8); block9.setText(R.string.block9);
        TextView block10 = (TextView) gameLinearLayout.getChildAt(9); block10.setText(R.string.block10);
        TextView block11 = (TextView) gameLinearLayout.getChildAt(10); block11.setText(R.string.block11);
        TextView block12 = (TextView) gameLinearLayout.getChildAt(11); block12.setText(R.string.block12);
        TextView block13 = (TextView) gameLinearLayout.getChildAt(12); block13.setText(R.string.block13);
        TextView block14 = (TextView) gameLinearLayout.getChildAt(13); block14.setText(R.string.block14);
        TextView block15 = (TextView) gameLinearLayout.getChildAt(14); block15.setText(R.string.block15);
        TextView block16 = (TextView) gameLinearLayout.getChildAt(15); block16.setText(R.string.block16);

        initiateGrid();
        updateGrid(null);

        Log.v("GridFragment: ", "created");
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        float dx;
        float dy;

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                if (actionPerformed == 0) {
                    mPreviousY = y;
                    mPreviousX = x;
                    actionPerformed = 1;
                }
                return true;

            case MotionEvent.ACTION_UP:

                dx = x - mPreviousX;
                dy = y - mPreviousY;

                int dxInt = (int) dx;
                int dyInt = (int) dy;
                Log.v("dx: ", Integer.toString(dxInt));
                Log.v("dy: ", Integer.toString(dyInt));

                // determine if the motion is along the x or y axis
                int type = 0; // 0 means x axis, 1 means y axis
                float tempX = dx; float tempY = dy;
                if (tempX < 0) tempX = -tempX;
                if (tempY < 0) tempY = -tempY;
                if (tempX < tempY) {
                    type = 1;
                }

                Log.v("x or y?: ", Integer.toString(type));

                // determine if the motion is to the left/top of the screen or right/bottom
                float temp = 0;
                int motionType = 0; // 0 means left/top, 1 mean right/bottom
                if (type == 0) temp = dx; else temp = dy;
                if (temp > 0) motionType = 1;

                Log.v("0=l/t,1=r/b: ", Integer.toString(motionType));

                // swipe from center to left of screen
                if (motionType == 0 && type == 0 && actionPerformed == 1) {
                    updateGrid(null);
                    moveLeft();
                    ScoreFragment.updateScore();
                    actionPerformed = 0;
                }

                // swipe from center to right of screen
                if (motionType == 1 && type == 0 && actionPerformed == 1) {
                    updateGrid(null);
                    moveRight();
                    ScoreFragment.updateScore();
                    actionPerformed = 0;
                }

                // swipe from center to bottom of screen
                if (motionType == 1 && type == 1 && actionPerformed == 1) {
                    updateGrid(null);
                    moveDown();
                    ScoreFragment.updateScore();
                    actionPerformed = 0;
                }

                // swipe from center to top of screen
                if (motionType == 0 && type == 1 && actionPerformed == 1) {
                    updateGrid(null);
                    moveUp();
                    ScoreFragment.updateScore();
                    actionPerformed = 0;
                }
                return true;

        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

    private void init() {
        inflate(getContext(), R.layout.quiz_fragment, this);
    }

    // Store values in sharedPreferences
    public void updateSharedPreferences() {

        Log.v("GridFragment: ", "updateSharedPreferences");

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        editor.putInt("score", score);
        editor.putInt("filledBlocks", filledBlocks);
        editor.putInt("topScore", topScore);
        editor.putInt("block0", grid[0][0]); editor.putInt("block1", grid[0][1]);
        editor.putInt("block2", grid[0][2]); editor.putInt("block3", grid[0][3]);
        editor.putInt("block4", grid[1][0]); editor.putInt("block5", grid[1][1]);
        editor.putInt("block6", grid[1][2]); editor.putInt("block7", grid[1][3]);
        editor.putInt("block8", grid[2][0]); editor.putInt("block9", grid[2][1]);
        editor.putInt("block10", grid[2][2]); editor.putInt("block11", grid[2][3]);
        editor.putInt("block12", grid[3][0]); editor.putInt("block13", grid[3][1]);
        editor.putInt("block14", grid[3][2]); editor.putInt("block15", grid[3][3]);
        editor.commit();

        Log.v("GridFragment: ", "updateSharedPreferences(end)");
    }

    // Pull values from sharedPreferences
    public void retrieveSharedPreferences() {

        Log.v("GridFragment: ", "retrieveSharedPreferences");

        SharedPreferences sharedpreferences = getContext().getSharedPreferences("MyPref", 0);

        score = sharedpreferences.getInt("score", 0);
        topScore = sharedpreferences.getInt("topScore", 0);
        filledBlocks = sharedpreferences.getInt("filledBlocks", 0);
        grid[0][0] = sharedpreferences.getInt("block0", -1);
        grid[0][1] = sharedpreferences.getInt("block1", -1);
        grid[0][2] = sharedpreferences.getInt("block2", -1);
        grid[0][3] = sharedpreferences.getInt("block3", -1);
        grid[1][0] = sharedpreferences.getInt("block4", -1);
        grid[1][1] = sharedpreferences.getInt("block5", -1);
        grid[1][2] = sharedpreferences.getInt("block6", -1);
        grid[1][3] = sharedpreferences.getInt("block7", -1);
        grid[2][0] = sharedpreferences.getInt("block8", -1);
        grid[2][1] = sharedpreferences.getInt("block9", -1);
        grid[2][2] = sharedpreferences.getInt("block10", -1);
        grid[2][3] = sharedpreferences.getInt("block11", -1);
        grid[3][0] = sharedpreferences.getInt("block12", -1);
        grid[3][1] = sharedpreferences.getInt("block13", -1);
        grid[3][2] = sharedpreferences.getInt("block14", -1);
        grid[3][3] = sharedpreferences.getInt("block15", -1);

        Log.v("GridFragment: ", "retrieveSharedPreferences(end)");
    }

    // reset the game
    public static void resetGame() {
        Log.v("GridFragment: ", "resetGame()");
        // updates the top score if necessary
        updateTopScore(-1);
        // recreates the grid and resets values
        initiateGrid();
        // update the score now that it is reset
        updateScore(-1);
        Log.v("GridFragment: ", "resetGame(end)");
    }

    // return the player's top score
    public static int getTopScore() {
        Log.v("GridFragment: ", "getTopScore()");
        Log.v("GridFragment: ", "getTopScore(end)");
        return topScore;
    }

    // update the player's top score
    public static void updateTopScore(int newScore) {
        Log.v("GridFragment: ", "updateTopScore()");

        if (newScore > -1) topScore = newScore;
        else if (topScore < score) {
            topScore = score;
        }

        ScoreFragment.updateTopScore();
        Log.v("GridFragment: ", "updateTopScore(end)");
    }

    // return the player's score
    public static int getScore() {
        Log.v("GridFragment: ", "getScore()");
        Log.v("GridFragment: ", "getScore(end)");
        return score;
    }

    // update the player's score
    public static void updateScore(int newScore) {
        Log.v("GridFragment: ", "updateScore()");
        if (newScore > -1) score = newScore;
        ScoreFragment.updateScore();
        Log.v("GridFragment: ", "updateScore(end)");
    }

    // return the filled blocks
    public static int getFilledBlocks() {
        Log.v("GridFragment: ", "getFilledBlocks()");
        Log.v("GridFragment: ", "getFilledBlocks(end)");
        return filledBlocks;
    }

    // update the filled blocks
    public static void updateFilledBlocks(int newFilled) {
        Log.v("GridFragment: ", "updateFilledBlocks()");
        filledBlocks = newFilled;
        Log.v("GridFragment: ", "updateFilledBlocks(end)");
    }

    // return the grid's contents
    public static int[] getGrid() {
        Log.v("GridFragment: ", "getGrid()");
        int[] returned = new int[GRIDSIZE * GRIDSIZE];
        for (int i = 0; i < GRIDSIZE; i++) {
            for (int j = 0; j < GRIDSIZE; j++) {
                returned[(i * GRIDSIZE) + j] = grid[i][j];
            }
        }
        Log.v("GridFragment: ", "getGrid(end)");
        return returned;
    }

    // update the grid contents
    public static void updateGrid(int[] tempGrid) {
        Log.v("GridFragment: ", "updateGrid()");

        // if a new grid is passed with the method, replace the current grid with it.
        if (tempGrid != null) {
            for (int i = 0; i < GRIDSIZE; i++) {
                for (int j = 0; j < GRIDSIZE; j++) {
                    grid[i][j] = tempGrid[(i * GRIDSIZE) + j];
                }
            }
        }
        // update the grid's contents
        String blockValue = " ";
        for (int i = 0; i < gameLinearLayout.getChildCount(); i++) {
            int row = i / 4; int column = i % 4;
                if (grid[row][column] == -1) blockValue = " ";
                else blockValue = Integer.toString(grid[row][column]);
                TextView block = (TextView) gameLinearLayout.getChildAt(i);
                block.setText(blockValue);
        }
        Log.v("GridFragment: ", "updateGrid(end)");
    }

    /* Performs necessary actions when the user indicates to move the tiles to the left */
    public static void moveLeft() {
        Log.v("GridFragment: ", "moveLeft()");
        int valid = 0;
        int[] temp = new int[GRIDSIZE];
        int[] tempCheck = new int[GRIDSIZE];
        for (int i = 0; i < GRIDSIZE; i++) {
            for (int j = 0; j < GRIDSIZE; j++) {
                temp[j] = grid[i][j];
                tempCheck[j] = grid[i][j];
            }
            int index = 0;
            int lastValue = -1;
            int lastColumn = 0;                                                                         //new//
            for (int x = 0; x < GRIDSIZE; x++) {    // adjust the row
                if (temp[x] != -1) { // find a filled block in the row.
                    // record the last value found or compare the current value to the last.
                    if (lastValue == -1) {
                        lastValue = temp[x];
                        lastColumn = x;                                                             //new//
                    } else {
                        if (lastValue == temp[x]) {

                            //animation control                                                     //new//
                            int row = i; int column = x;                                                         //new//
                            int firstBlock = (row * 4) + lastColumn + 1;                                      //new//
                            int secondBlock = (row * 4) + column + 1;                               //new//
                            int destinationBlock = (row * 4) + index + 1;                           //new//
                            animate(firstBlock, destinationBlock);                     //new//
                            animate(secondBlock, destinationBlock);                                 //new//

                            temp[index] = lastValue + temp[x];
                            totalCount -= lastValue;
                            lastValue = -1;
                            score += temp[index];
                            filledBlocks--;
                            index++;
                        } else {

                            //animation control                                                     //new//
                            int row = i;                                                            //new//
                            int firstBlock = (row * 4) + lastColumn + 1;                            //new//
                            int destinationBlock = (row * 4) + index + 1;                           //new//
                            animate(firstBlock, destinationBlock);                      //new//

                            temp[index] = lastValue;
                            lastValue = temp[x];
                            lastColumn = x;
                            index++;
                        }
                    }
                }
                // If we've looked through the row, set the rest of the row to empty.
                if (x == GRIDSIZE - 1) {
                    // set the last value in its proper position.
                    if (lastValue != -1) {

                        //animation control                                                     //new//
                        int row = i;                                                            //new//
                        int firstBlock = (row * 4) + lastColumn + 1;                            //new//
                        int destinationBlock = (row * 4) + index + 1;                           //new//
                        animate(firstBlock, destinationBlock);                      //new//

                        temp[index] = lastValue;
                        index++;
                    }
                    while (index < GRIDSIZE) {
                        temp[index] = -1;
                        index++;
                    }
                }
                // set the new row values in the grid.
                for (int k = 0; k < GRIDSIZE; k++) {
                    // confirm if at least one change to the row has occurred.
                    if (temp[k] != tempCheck[k]) valid = 1;
                    grid[i][k] = temp[k];
                }
            }
        }
        if (valid == 1) {
            addValue();
            //updateGrid(null);
            if (onVictory() == true) {
            } else if (onDefeat() == true) {
            }
        }
        Log.v("GridFragment: ", "moveLeft(end)");
    }

    /* Performs necessary actions when the user indicates to move the tiles to the right */
    public static void moveRight() {
        Log.v("GridFragment: ", "moveRight()");
        int valid = 0;
        int[] temp = new int[GRIDSIZE];
        int[] tempCheck = new int[GRIDSIZE];
        for (int i = 0; i < GRIDSIZE; i++) {
            for (int j = 0; j < GRIDSIZE; j++) {
                temp[j] = grid[i][j];
                tempCheck[j] = grid[i][j];
            }
            int index = 3;
            int lastValue = -1;
            int lastColumn = 0;
            for (int x = 3; x >= 0; x--) {    // adjust the row
                if (temp[x] != -1) { // find a filled block in the row.
                    // record the last value found or compare the current value to the last.
                    if (lastValue == -1) {
                        lastValue = temp[x];
                        lastColumn = x;
                    } else {
                        if (lastValue == temp[x]) {

                            //animation control
                            int row = i; int column = x;                                                         //new//
                            int firstBlock = (row * 4) + lastColumn + 1;                                      //new//
                            int secondBlock = (row * 4) + column + 1;                               //new//
                            int destinationBlock = (row * 4) + index + 1;                           //new//
                            animate(firstBlock, destinationBlock);                     //new//
                            animate(secondBlock, destinationBlock);

                            temp[index] = lastValue + temp[x];
                            totalCount -= lastValue;
                            lastValue = -1;
                            score += temp[index];
                            filledBlocks--;
                            index--;
                        } else {

                            //animation control                                                     //new//
                            int row = i;                                                            //new//
                            int firstBlock = (row * 4) + lastColumn + 1;                            //new//
                            int destinationBlock = (row * 4) + index + 1;                           //new//
                            animate(firstBlock, destinationBlock);                      //new//

                            temp[index] = lastValue;
                            lastValue = temp[x];
                            lastColumn = x;
                            index--;
                        }
                    }
                }
                // If we've looked through the row, set the rest of the row to empty.
                if (x == 0) {
                    // set the last value in its proper position.
                    if (lastValue != -1) {

                        //animation control                                                     //new//
                        int row = i;                                                            //new//
                        int firstBlock = (row * 4) + lastColumn + 1;                            //new//
                        int destinationBlock = (row * 4) + index + 1;                           //new//
                        animate(firstBlock, destinationBlock);                      //new//

                        temp[index] = lastValue;
                        index--;
                    }
                    while (index >= 0) {
                        temp[index] = -1;
                        index--;
                    }
                }
                // set the new row values in the grid.
                for (int k = 0; k < GRIDSIZE; k++) {
                    // confirm if at least one change to the row has occurred.
                    if (temp[k] != tempCheck[k]) valid = 1;
                    grid[i][k] = temp[k];
                }
            }
        }
        if (valid == 1) {
            addValue();
            //updateGrid(null);
            if (onVictory() == true) {
            } else if (onDefeat() == true) {}
        }
        Log.v("GridFragment: ", "moveRight(end)");
    }

    /* Performs necessary actions when the user indicates to move the tiles upward */
    public static void moveUp() {
        Log.v("GridFragment: ", "moveUp()");
        int valid = 0;
        int[] temp = new int[GRIDSIZE];
        int[] tempCheck = new int[GRIDSIZE];
        for (int i = 0; i < GRIDSIZE; i++) {
            for (int j = 0; j < GRIDSIZE; j++) {
                temp[j] = grid[j][i];
                tempCheck[j] = grid[j][i];
            }
            int index = 0;
            int lastValue = -1;
            int lastRow = 0;
            for (int x = 0; x < GRIDSIZE; x++) {    // adjust the row
                if (temp[x] != -1) { // find a filled block in the row.
                    // record the last value found or compare the current value to the last.
                    if (lastValue == -1) {
                        lastValue = temp[x];
                        lastRow = x;
                    } else {
                        if (lastValue == temp[x]) {

                            //animation control                                                     //new//
                            int row = x; int column = i;                                                         //new//
                            int firstBlock = (lastRow * 4) + column + 1;                                      //new//
                            int secondBlock = (row * 4) + column + 1;                               //new//
                            int destinationBlock = (index * 4) + column + 1;                           //new//
                            animate(firstBlock, destinationBlock);                     //new//
                            animate(secondBlock, destinationBlock);

                            temp[index] = lastValue + temp[x];
                            totalCount -= lastValue;
                            lastValue = -1;
                            score += temp[index];
                            filledBlocks--;
                            index++;
                        } else {

                            //animation control                                                     //new//
                            int row = x; int column = i;                                                         //new//
                            int firstBlock = (lastRow * 4) + column + 1;                            //new//
                            int destinationBlock = (index * 4) + column + 1;                           //new//
                            animate(firstBlock, destinationBlock);                      //new//

                            temp[index] = lastValue;
                            lastValue = temp[x];
                            lastRow = x;
                            index++;
                        }
                    }
                }
                // If we've looked through the row, set the rest of the row to empty.
                if (x == GRIDSIZE - 1) {
                    // set the last value in its proper position.
                    if (lastValue != -1) {

                        //animation control                                                     //new//
                        int row = x; int column = i;                                                         //new//
                        int firstBlock = (lastRow * 4) + column + 1;                            //new//
                        int destinationBlock = (index * 4) + column + 1;                           //new//
                        animate(firstBlock, destinationBlock);                      //new//

                        temp[index] = lastValue;
                        index++;
                    }
                    while (index < GRIDSIZE) {
                        temp[index] = -1;
                        index++;
                    }
                }
                // set the new row values in the grid.
                for (int k = 0; k < GRIDSIZE; k++) {
                    // confirm if at least one change to the row has occurred.
                    if (temp[k] != tempCheck[k]) valid = 1;
                    grid[k][i] = temp[k];
                }
            }
        }
        if (valid == 1) {
            addValue();
            //updateGrid(null);
            if (onVictory() == true) {
            } else if (onDefeat() == true) {}
        }
        Log.v("GridFragment: ", "moveUp(end)");
    }

    /* Performs necessary actions when the user indicates to move the tiles downward */
    public static void moveDown() {
        Log.v("GridFragment: ", "moveDown()");
        int valid = 0;
        int[] temp = new int[GRIDSIZE];
        int[] tempCheck = new int[GRIDSIZE];
        for (int i = 0; i < GRIDSIZE; i++) {
            for (int j = 0; j < GRIDSIZE; j++) {
                temp[j] = grid[j][i];
                tempCheck[j] = grid[j][i];
            }
            int index = 3;
            int lastValue = -1;
            int lastRow = 0;
            for (int x = 3; x >= 0; x--) {    // adjust the row
                if (temp[x] != -1) { // find a filled block in the row.
                    // record the last value found or compare the current value to the last.
                    if (lastValue == -1) {
                        lastValue = temp[x];
                        lastRow = x;
                    } else {
                        if (lastValue == temp[x]) {

                            //animation control                                                     //new//
                            int row = x; int column = i;                                                         //new//
                            int firstBlock = (lastRow * 4) + column + 1;                                      //new//
                            int secondBlock = (row * 4) + column + 1;                               //new//
                            int destinationBlock = (index * 4) + column + 1;                           //new//
                            animate(firstBlock, destinationBlock);                     //new//
                            animate(secondBlock, destinationBlock);

                            temp[index] = lastValue + temp[x];
                            totalCount -= lastValue;
                            lastValue = -1;
                            score += temp[index];
                            filledBlocks--;
                            index--;
                        } else {

                            //animation control                                                     //new//
                            int row = x; int column = i;                                                         //new//
                            int firstBlock = (lastRow * 4) + column + 1;                            //new//
                            int destinationBlock = (index * 4) + column + 1;                           //new//
                            animate(firstBlock, destinationBlock);                      //new//

                            temp[index] = lastValue;
                            lastValue = temp[x];
                            lastRow = x;
                            index--;
                        }
                    }
                }
                // If we've looked through the row, set the rest of the row to empty.
                if (x == 0) {
                    // set the last value in its proper position.
                    if (lastValue != -1) {

                        //animation control                                                     //new//
                        int row = x; int column = i;                                                         //new//
                        int firstBlock = (lastRow * 4) + column + 1;                            //new//
                        int destinationBlock = (index * 4) + column + 1;                           //new//
                        animate(firstBlock, destinationBlock);                      //new//

                        temp[index] = lastValue;
                        index--;
                    }
                    while (index >= 0) {
                        temp[index] = -1;
                        index--;
                    }
                }
                // set the new row values in the grid.
                for (int k = 0; k < GRIDSIZE; k++) {
                    // confirm if at least one change to the row has occurred.
                    if (temp[k] != tempCheck[k]) valid = 1;
                    grid[k][i] = temp[k];
                }
            }
        }
        if (valid == 1) {
            addValue();
            //updateGrid(null);
            if (onVictory() == true) {
            } else if (onDefeat() == true) {}
        }
        Log.v("GridFragment: ", "moveDown(end)");
    }

    // initiates a new grid, also effectively resets the grid for a reset.
    private static void initiateGrid() {
        Log.v("GridFragment: ", "initiateGrid()");
        grid = new int[GRIDSIZE][GRIDSIZE];
        // set all values in the grid to -1 indicating an empty space.
        for (int i = 0; i < GRIDSIZE; i++) {
            for (int j = 0; j < GRIDSIZE; j++){
                grid[i][j] = -1;
            }
        }
        filledBlocks = 0;
        totalCount = 0;
        score = 0;
        addValue();
        addValue();
        updateGrid(null);
        Log.v("GridFragment: ", "initiateGrid(end)");
    }

    // checks if victory conditions are met.
    private static boolean onVictory() {
        Log.v("GridFragment: ", "onVictory()");
        for (int i = 0; i < GRIDSIZE; i++) {
            for (int j = 0; j < GRIDSIZE; j++) {
                if (grid[i][j] == 2048) {
                    // display toast exclaiming victory and wait a short duration.
                    Context context = gameLinearLayout.getContext();
                    CharSequence text = "You Win!\nScore: " + Integer.toString(score);
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    // add code to delay execution on defeat so the user can see the grid that lost

                    // Restart the game.
                    resetGame();
                    Log.v("GridFragment: ", "onVictory(end)");
                    return true;
                }
            }
        }
        Log.v("GridFragment: ", "onVictory(end)");
        return false;
    }

    // checks if defeat conditions are met.
    private static boolean onDefeat() {
        Log.v("GridFragment: ", "onDefeat()");
        // return false if the grid isn't full
        if (filledBlocks < (GRIDSIZE * GRIDSIZE)) {
            Log.v("GridFragment: ", "onDefeat(end)");
            return false;
        }
        // search the grid for any two tiles adjacent to each other with the same value.
        for (int i = 0; i < GRIDSIZE; i++) {
            for (int j = 1; j < GRIDSIZE; j++) {
                // check the columns
                if (grid[j][i] == grid[j-1][i]) {
                    Log.v("GridFragment: ", "onDefeat(end)");
                    return false;
                }
                // check the rows
                else if (grid[i][j] == grid[i][j-1]) {
                    Log.v("GridFragment: ", "onDefeat(end)");
                    return false;
                }
            }
        }
        // display toast exclaiming defeat and wait a short duration.
        Context context = gameLinearLayout.getContext();
        CharSequence text = "You Lose!\nScore: " + Integer.toString(score);
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        // add code to delay execution on defeat so the user can see the grid that lost

        // Restart the game.
        resetGame();
        Log.v("GridFragment: ", "onDefeat(end)");
        return true;
    }

    // adds a new value to the grid.
    private static void addValue() {
        Log.v("GridFragment: ", "addValue()");
        if (filledBlocks == GRIDSIZE * GRIDSIZE) {}
        else {
            // determine if the number is 2 or 4.
            int number = 0;
            Random rand = new Random();
            int roll1 = rand.nextInt(100);
            if (roll1 < FOURPROB) {
                number = 4;
            } else {
                number = 2;
            }
            // increment global values effected by new tile.
            filledBlocks++;
            totalCount += number;
            // generate a random position for the new tile, if the tile is already taken, then
            // reroll (look into improving this method).
            int check = 0;
            while (check == 0) {
                int roll2 = rand.nextInt((GRIDSIZE * GRIDSIZE));
                int row = roll2 / GRIDSIZE;
                int column = roll2 % GRIDSIZE;
                if (grid[row][column] == -1) {
                    grid[row][column] = number;
                    check = 1;
                }
            }
        }
        Log.v("GridFragment: ", "addValue(end)");
    }

    // Animates the motion of the block at index1 and index2, to the block
    // at indexF.
    private static void animate(int index1, int indexF) {

        Log.v(Integer.toString(index1), Integer.toString(indexF));
        // identify which textView block is associated to each parameter.
        TextView block1 = (TextView) gameLinearLayout.getChildAt(0); TextView block2 = (TextView) gameLinearLayout.getChildAt(1);
        TextView block3 = (TextView) gameLinearLayout.getChildAt(2); TextView block4 = (TextView) gameLinearLayout.getChildAt(3);
        TextView block5 = (TextView) gameLinearLayout.getChildAt(4); TextView block6 = (TextView) gameLinearLayout.getChildAt(5);
        TextView block7 = (TextView) gameLinearLayout.getChildAt(6); TextView block8 = (TextView) gameLinearLayout.getChildAt(7);
        TextView block9 = (TextView) gameLinearLayout.getChildAt(8); TextView block10 = (TextView) gameLinearLayout.getChildAt(9);
        TextView block11 = (TextView) gameLinearLayout.getChildAt(10); TextView block12 = (TextView) gameLinearLayout.getChildAt(11);
        TextView block13 = (TextView) gameLinearLayout.getChildAt(12); TextView block14 = (TextView) gameLinearLayout.getChildAt(13);
        TextView block15 = (TextView) gameLinearLayout.getChildAt(14); TextView block16 = (TextView) gameLinearLayout.getChildAt(15);

        TextView firstParam = block1; TextView destination = block1;
        if (index1 == 1) firstParam = block1; if (indexF == 1) destination = block1;
        if (index1 == 2) firstParam = block2; if (indexF == 2) destination = block2;
        if (index1 == 3) firstParam = block3; if (indexF == 3) destination = block3;
        if (index1 == 4) firstParam = block4; if (indexF == 4) destination = block4;
        if (index1 == 5) firstParam = block5; if (indexF == 5) destination = block5;
        if (index1 == 6) firstParam = block6; if (indexF == 6) destination = block6;
        if (index1 == 7) firstParam = block7; if (indexF == 7) destination = block7;
        if (index1 == 8) firstParam = block8; if (indexF == 8) destination = block8;
        if (index1 == 9) firstParam = block9; if (indexF == 9) destination = block9;
        if (index1 == 10) firstParam = block10; if (indexF == 10) destination = block10;
        if (index1 == 11) firstParam = block11; if (indexF == 11) destination = block11;
        if (index1 == 12) firstParam = block12; if (indexF == 12) destination = block12;
        if (index1 == 13) firstParam = block13; if (indexF == 13) destination = block13;
        if (index1 == 14) firstParam = block14; if (indexF == 14) destination = block14;
        if (index1 == 15) firstParam = block15; if (indexF == 15) destination = block15;
        if (index1 == 16) firstParam = block16; if (indexF == 16) destination = block16;

        // get the x and y coordinates of the blocks that are to be moved
        float firstParamX = firstParam.getX(); float firstParamY = firstParam.getY();
        float destParamX = destination.getX(); float destParamY = destination.getY();

        // for access within run method
        final TextView finParam1 = firstParam; final float param1x = firstParamX; final float param1y = firstParamY;

        // animate the motion to the location
            firstParam.animate()
                    .x(destination.getX())
                    .y(destination.getY())
                    .setDuration(500)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            finParam1.animate()
                                    .x(param1x)
                                    .y(param1y)
                                    .setDuration(0)
                                    .start();
                            updateGrid(null);
                        }
                    })
                    .start();
    }

}
