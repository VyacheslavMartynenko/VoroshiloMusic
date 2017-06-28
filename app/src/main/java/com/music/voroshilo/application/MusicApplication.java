package com.music.voroshilo.application;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.music.voroshilo.activity.BaseActivity;
import com.music.voroshilo.util.preferences.SharedPreferencesProvider;

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
