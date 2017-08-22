package mp3.music.download.downloadmp3.downloadmusic.util;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import mp3.music.download.downloadmp3.downloadmusic.activity.BaseActivity;

public class KeyboardUtil {
    public static void hideKeyboard(BaseActivity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View displayedView = activity.getCurrentFocus();
        if (displayedView != null) {
            inputMethodManager.hideSoftInputFromWindow(displayedView.getWindowToken(), 0);
        }
    }
}
