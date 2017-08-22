package mp3.music.download.downloadmp3.downloadmusic.model.networking;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Download {
    public static final int MUSIC = 1;
    public static final int APK = 2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MUSIC, APK})
    public @interface Type {
    }
}
