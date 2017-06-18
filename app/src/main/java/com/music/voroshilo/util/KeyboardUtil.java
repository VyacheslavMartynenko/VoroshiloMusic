package com.music.voroshilo.util;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtil {
    public static void hideKeyboard(AppCompatActivity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View displayedView = activity.getCurrentFocus();
        if (displayedView != null) {
            inputMethodManager.hideSoftInputFromWindow(displayedView.getWindowToken(), 0);
        }
    }
}
