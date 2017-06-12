package com.music.voroshilo.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.music.voroshilo.R;

public class SongIconChanger {
    public static void switchDrawable(Context context, ImageView imageView, boolean isPlaying) {
        if (isPlaying) {
            imageView.setImageDrawable(ContextCompat
                    .getDrawable(context, R.drawable.ic_pause_black_24dp));
        } else {
            imageView.setImageDrawable(ContextCompat
                    .getDrawable(context, R.drawable.ic_play_arrow_black_24dp));
        }
    }
}
