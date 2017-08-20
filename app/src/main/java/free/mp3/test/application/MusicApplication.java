package free.mp3.test.application;

import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;

import free.mp3.test.activity.BaseActivity;
import free.mp3.test.util.preferences.SharedPreferencesProvider;
import io.fabric.sdk.android.Fabric;

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
