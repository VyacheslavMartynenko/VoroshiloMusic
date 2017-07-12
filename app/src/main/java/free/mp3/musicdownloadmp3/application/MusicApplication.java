package free.mp3.musicdownloadmp3.application;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import free.mp3.musicdownloadmp3.activity.BaseActivity;
import free.mp3.musicdownloadmp3.util.preferences.SharedPreferencesProvider;

import io.fabric.sdk.android.Fabric;

public class MusicApplication extends Application {
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
