package mp3.music.download.downloadmp3.downloadmusic.application;

import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import mp3.music.download.downloadmp3.downloadmusic.activity.BaseActivity;
import mp3.music.download.downloadmp3.downloadmusic.util.preferences.SharedPreferencesProvider;

public class MusicApplication extends MultiDexApplication {
    private static MusicApplication instance;
    private BaseActivity currentActivity = null;

    public static synchronized MusicApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        SharedPreferencesProvider.getInstance().initialize(getApplicationContext());
        instance = this;
    }

    public BaseActivity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(BaseActivity currentActivity) {
        this.currentActivity = currentActivity;
    }
}
