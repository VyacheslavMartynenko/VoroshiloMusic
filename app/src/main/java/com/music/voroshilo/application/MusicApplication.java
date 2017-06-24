package com.music.voroshilo.application;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;
import com.music.voroshilo.activity.BaseActivity;
import com.music.voroshilo.util.preferences.SharedPreferencesProvider;

public class MusicApplication extends Application {
    private static MusicApplication instance;
    private BaseActivity currentActivity = null;

    public static synchronized MusicApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
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
