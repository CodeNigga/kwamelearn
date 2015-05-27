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

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class MenuDialog extends DialogFragment {

    private static final String PM = "plusOrMinus";

    private boolean is_plus;

    public static MenuDialog newInstance(boolean plusOrMinus) {
        Bundle args = new Bundle();
        args.putBoolean(PM, plusOrMinus);

        MenuDialog dialog = new MenuDialog();
        dialog.setArguments(args);

        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        is_plus = getArguments().getBoolean(PM);
        if (is_plus) {
            builder.setItems(R.array.menu_array_minus, selectListener);
        } else {
            builder.setItems(R.array.menu_array_plus, selectListener);
        }

        return builder.create();
    }

    private Dialog.OnClickListener selectListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case 0:
                    if (is_plus) {
                        sendDialogResponse(GameFragment.SUBTRACTION);
                    } else {
                        sendDialogResponse(GameFragment.ADDITION);
                    }

                    break;

                case 1:
                    sendDialogResponse(GameFragment.NEXT_LEVEL);
                    break;

                case 2:
                    sendDialogResponse(GameFragment.PREV_LEVEL);
            }
        }
    };

    private void sendDialogResponse(int response) {
        getTargetFragment().onActivityResult(getTargetRequestCode(), response, null);
    }
}
