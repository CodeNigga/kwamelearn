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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class CorrectWrongDialog extends DialogFragment {

    private static final String WHAT_TO = "what_to";
    private static final String LEVEL = "LEVEL";

    /**
     * @param whatTo describes the dialog to show
     * @return returns a new instance of the CorrectWrongDialog
     */
    public static CorrectWrongDialog newInstance(int whatTo) {
        Bundle args = new Bundle();
        args.putInt(WHAT_TO, whatTo);

        CorrectWrongDialog dialog = new CorrectWrongDialog();
        dialog.setArguments(args);

        return dialog;
    }

    public static CorrectWrongDialog newInstance(int whatTo, int level) {
        Bundle args = new Bundle();
        args.putInt(WHAT_TO, whatTo);
        args.putInt(LEVEL, level);

        CorrectWrongDialog dialog = new CorrectWrongDialog();
        dialog.setArguments(args);

        return dialog;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);

        int whatTo = getArguments().getInt(WHAT_TO);

        View solImage = null;
        switch (whatTo) {
            case GameFragment.CORRECT:
                solImage = inflater.inflate(R.layout.correct_view, null);
                break;

            case GameFragment.WRONG:
                solImage = inflater.inflate(R.layout.wrong_view, null);
                break;

            case GameFragment.INDICATE_LEVEL:
                int level = getArguments().getInt(LEVEL);

                solImage = inflater.inflate(R.layout.level_show, null);
                TextView levelNumber = (TextView) solImage.findViewById(R.id.level_number);
                levelNumber.setText("" + level);
                break;

            /*case GameFragment.RETARD:
                solImage = inflater.inflate(R.layout.retard_view, null);
                break;*/
        }

        builder.setView(solImage);

        return builder.create();
    }


    @Override
    public void onResume() {
        super.onResume();
        Handler timeOutHandler = new Handler();
        timeOutHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CorrectWrongDialog.this.dismiss();
            }
        }, 1200);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
    }
}
