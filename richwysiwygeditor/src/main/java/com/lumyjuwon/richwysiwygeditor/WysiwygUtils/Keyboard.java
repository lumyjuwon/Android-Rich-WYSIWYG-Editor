package com.lumyjuwon.richwysiwygeditor.WysiwygUtils;

import android.app.Dialog;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import static android.content.Context.INPUT_METHOD_SERVICE;


public class Keyboard {

    public static void closeKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public static void showKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }

    public static void showSoftKeyboard(Dialog dialog){
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

}
