package com.degreat.apps.kwamelearn;

/***
 *
 Copyright (c) 2015 De-Great Yartey

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 **/

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GameFragment extends Fragment {

    public static final int SUBTRACTION = 16;
    public static final int ADDITION = 17;
    public static final int CORRECT = 5;
    public static final int WRONG = 6;
    public static final int INDICATE_LEVEL = 7;

    private static final String TAG = "GameFragment";
    private static final String HIGH_SCORE = "high_score";
    private static final int FEEDBACK = 1;
    private static final String LIMIT_PREF = "limit_pref";
    private static final String IS_PLUS = "is_plus";

    public static final int NEXT_LEVEL = 19;
    public static final int PREV_LEVEL = 20;

    private int operandX, operandY;
    private int uAnswer;
    private int score;

    private boolean is_plus = true;

    // all associated views
    private TextView operandXText, operandYText, answerBox;
    private TextView operator;

    // .. numbers buttons
    private ProgressBar progress;

    private SharedPreferences highScorePref;

    private int MAX_SCORE = 20;
    private int LIMIT = 10;


    private View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();

            switch (id) {
                case R.id.done_button:
                    checkAnswer();
                    break;

                case R.id.clear_button:
                    backspace();
                    break;

                case R.id.menu_button:
                    // display the menu dialog
                    MenuDialog optionsMenu = MenuDialog.newInstance(is_plus);
                    optionsMenu.setTargetFragment(GameFragment.this, FEEDBACK);

                    optionsMenu.show(getFragmentManager(), TAG);
                    break;

                case R.id.number1:
                    enterNumber(1);
                    break;
                case R.id.number0:
                    enterNumber(0);
                    break;
                case R.id.number3:
                    enterNumber(3);
                    break;

                case R.id.number2:
                    enterNumber(2);
                    break;
                case R.id.number4:
                    enterNumber(4);
                    break;
                case R.id.number5:
                    enterNumber(5);
                    break;
                case R.id.number6:
                    enterNumber(6);
                    break;
                case R.id.number7:
                    enterNumber(7);
                    break;
                case R.id.number8:
                    enterNumber(8);
                    break;
                case R.id.number9:
                    enterNumber(9);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.activity_learn, null);

        Button number1, number2, number3, number4, number5, number6,
                number7, number8, number9, number0;
        Button doneButton, clearButton, menuButton;

        highScorePref = getActivity().getPreferences(Context.MODE_PRIVATE);

        operandXText = (TextView) contentView.findViewById(R.id.operand_x);
        operandYText = (TextView) contentView.findViewById(R.id.operand_y);

        number1 = (Button) contentView.findViewById(R.id.number1);
        number0 = (Button) contentView.findViewById(R.id.number0);
        number2 = (Button) contentView.findViewById(R.id.number2);
        number3 = (Button) contentView.findViewById(R.id.number3);
        number4 = (Button) contentView.findViewById(R.id.number4);
        number5 = (Button) contentView.findViewById(R.id.number5);

        number6 = (Button) contentView.findViewById(R.id.number6);
        number7 = (Button) contentView.findViewById(R.id.number7);
        number8 = (Button) contentView.findViewById(R.id.number8);
        number9 = (Button) contentView.findViewById(R.id.number9);

        doneButton = (Button) contentView.findViewById(R.id.done_button);
        answerBox = (TextView) contentView.findViewById(R.id.answer_box);
        clearButton = (Button) contentView.findViewById(R.id.clear_button);

        menuButton = (Button) contentView.findViewById(R.id.menu_button);
        operator = (TextView) contentView.findViewById(R.id.operator);

        progress = (ProgressBar) contentView.findViewById(R.id.learn_progress);

        // assign listeners
        doneButton.setOnClickListener(buttonListener);
        clearButton.setOnClickListener(buttonListener);
        menuButton.setOnClickListener(buttonListener);

        number1.setOnClickListener(buttonListener);
        number2.setOnClickListener(buttonListener);
        number3.setOnClickListener(buttonListener);
        number4.setOnClickListener(buttonListener);
        number5.setOnClickListener(buttonListener);

        number6.setOnClickListener(buttonListener);
        number7.setOnClickListener(buttonListener);
        number8.setOnClickListener(buttonListener);
        number9.setOnClickListener(buttonListener);
        number0.setOnClickListener(buttonListener);

        progress.setMax(MAX_SCORE);

        loadScore();
        viewNextQuestion();

        return contentView;
    }

    // private mechanics

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == SUBTRACTION) {
            if (is_plus) {
                score = 0; // start
                is_plus = false;
            }

        } else if (resultCode == ADDITION) {
            score = 0;
            is_plus = true;
        } else if (resultCode == NEXT_LEVEL) {
            setChallenge(NEXT_LEVEL);
        } else if (resultCode == PREV_LEVEL) {
            setChallenge(PREV_LEVEL);
        }

        viewNextQuestion();
    }

    /**
     * @return the answer expected of the user
     */
    private int getAnswer() {
        if (!is_plus)
            return operandX - operandY;
        return operandX + operandY;
    }

    /**
     * Checks if the answer submitted is correct or not and displays a corresponding feedback
     * dialog
     */
    private void checkAnswer() {
        CorrectWrongDialog dialog;

        /**
         * If the score is equal to the maximum score for the level, show a dialog of success
         * and the transition to a new challenging level.
         * else if the score is negative (and the LIMIT > 10) then reduce the difficulty again
         *
         * A new challenge means increasing the LIMIT, so that the questions demand big answers
         */
        if (score == MAX_SCORE) {
            dialog = CorrectWrongDialog.newInstance(INDICATE_LEVEL, (LIMIT + 10) / 10);
            setChallenge(NEXT_LEVEL);
        }

        // cannot go lesser than first level
        /*else if ((score < 0) && (LIMIT>10)) {
            dialog = CorrectWrongDialog.newInstance(INDICATE_LEVEL, (LIMIT-10)/10);
            setChallenge(RETARD);
            score = 0;
        }*/

        // ^ eliminated because it ain't cool

        else if (getAnswer() == uAnswer) {
            score += 1;
            dialog = CorrectWrongDialog.newInstance(CORRECT);
        } else {
            // cannot go lesser than first level
            /* if (LIMIT > 10){
                score -= 1;
            }*/
            // reducing scores not interesting now
            dialog = CorrectWrongDialog.newInstance(WRONG);
        }


        dialog.setTargetFragment(GameFragment.this, FEEDBACK);
        dialog.show(getFragmentManager(), TAG);
    }


    /**
     * creates a new challenge by increasing the LIMIT or decreasing the LIMIT
     * (in future, the MAX_SCORE)
     */
    private void setChallenge(int result) {
        if (LIMIT == 500)
            return;

        score = 0;

        if (result == NEXT_LEVEL) {
            LIMIT += 10;
        } else {
            if (LIMIT != 10)
                LIMIT -= 10;
        }
    }

    /**
     * Enters the number i into the answer box if the length of the answer is less than 2
     *
     * @param i the number to be entered into the answer box
     */
    private void enterNumber(int i) {
        String sa = String.valueOf(uAnswer);
        Log.i(TAG, "value of sa: " + sa);
        if (sa.length() < 3) {
            sa += i;
        }

        uAnswer = Integer.valueOf(sa);
        showAnswerBox();
    }

    /**
     * Displays the user's entered value into the answer box
     */
    private void showAnswerBox() {
        if (uAnswer == 0) {
            answerBox.setText("");
        } else {
            answerBox.setText("" + uAnswer);
        }
    }

    /**
     * Views the next arithmetic question while updating the score progress bar
     * Clears the answer box of the previous answer
     */
    private void viewNextQuestion() {
        showScore();
        // clear the answer box of previously entered answer
        backspace();

        //setLimit();

        operandX = (int) (Math.random() * LIMIT);
        operandY = (int) (Math.random() * LIMIT);

        // the limit is LIMIT
        while ((getAnswer() > LIMIT) && (operandY != 0)) {
            operandY = (int) (Math.random() * LIMIT);
        }

        // swap the values of x and y if x < y in the case of subtraction
        if ((!is_plus) && (operandY > operandX)) {
            int tempVal = operandX;
            operandX = operandY;
            operandY = tempVal;
        }

        operandXText.setText("" + operandX);
        operandYText.setText("" + operandY);

        if (is_plus) {
            operator.setText("+");
        } else {
            operator.setText("-");
        }
    }

    /**
     * Resets the uAnswer to 0
     */
    private void backspace() {
        uAnswer = 0;
        showAnswerBox();
    }


    /**
     * Displays the score as a progress in the score progress  bar
     */
    private void showScore() {
        progress.setProgress(score);
    }

    /**
     * Saves the score earned in a SharedPreference, saves is_plus and the LIMIT as well
     */
    private void saveScore() {
        SharedPreferences.Editor editor = getActivity().getPreferences(Context.MODE_PRIVATE).edit();
        editor.putInt(HIGH_SCORE, score);
        editor.putInt(LIMIT_PREF, LIMIT);
        editor.putBoolean(IS_PLUS, is_plus);
        editor.apply();

        Log.i(TAG, "SCORE SAVED");
    }

    /**
     * Loads up the score from the SharedPreference, loads is_plus and the LIMIT as well
     */
    private void loadScore() {
        score = highScorePref.getInt(HIGH_SCORE, 0);
        LIMIT = highScorePref.getInt(LIMIT_PREF, 10);
        is_plus = highScorePref.getBoolean(IS_PLUS, true);
        Log.i(TAG, "SCORE LOADED");
    }

    //..

    /**
     * Save the score when the activity gets paused
     */
    @Override
    public void onPause() {
        super.onPause();
        saveScore();
    }

    // previous level and next level coming soon
}
