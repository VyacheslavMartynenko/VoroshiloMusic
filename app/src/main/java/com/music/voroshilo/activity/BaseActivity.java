package com.music.voroshilo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import com.music.voroshilo.application.MusicApplication;
import com.music.voroshilo.model.networking.DataBody;
import com.music.voroshilo.util.preferences.UserPreferences;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import butterknife.ButterKnife;

abstract public class BaseActivity extends AppCompatActivity {

    private MusicApplication app;

    private StartAppAd startAppAd = new StartAppAd(this);
    private boolean isActivityPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MusicApplication) this.getApplicationContext();
        @DataBody.AdMode
        int adStatus = UserPreferences.getInstance().getAdStatus();
        switch (adStatus) {
            case DataBody.APPODEAL:
                break;
            case DataBody.NO:
                break;
            case DataBody.START_APP:
                StartAppSDK.init(this, "205295421", true);
                StartAppAd.disableSplash();
                StartAppAd.disableAutoInterstitial();
                break;
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    protected void onResume() {
        isActivityPaused = false;
        super.onResume();
        app.setCurrentActivity(this);
    }

    protected void onPause() {
        isActivityPaused = true;
        clearReferences();
        super.onPause();
    }

    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }

    private void clearReferences() {
        Activity currActivity = app.getCurrentActivity();
        if (this.equals(currActivity)) {
            app.setCurrentActivity(null);
        }
    }

    public boolean isVisible() {
        return !isActivityPaused;
    }

    public void showAd() {
        startAppAd.showAd();
    }
}
