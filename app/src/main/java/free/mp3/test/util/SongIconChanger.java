package free.mp3.test.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import free.mp3.test.R;

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

    public static void loadDrawableWithPicasso(Context context, ImageView imageView, String url) {
        Picasso.with(context)
                .load(url)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.ic_music_note_black_24dp)
                .into(imageView);
    }
}
