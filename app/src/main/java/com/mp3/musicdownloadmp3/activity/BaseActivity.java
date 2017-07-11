package com.mp3.musicdownloadmp3.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import com.appodeal.ads.Appodeal;
import com.mp3.musicdownloadmp3.application.MusicApplication;
import com.mp3.musicdownloadmp3.model.networking.DataBody;
import com.mp3.musicdownloadmp3.util.preferences.UserPreferences;
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
                Appodeal.disableNetwork(this, "cheetah");
                String appKey = "2c7b18d1306efd0ff21a20c17f85a414812a8ce62cf00af1";
                Appodeal.disableLocationPermissionCheck();
                Appodeal.initialize(this, appKey, Appodeal.INTERSTITIAL);
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
        @DataBody.AdMode
        int adStatus = UserPreferences.getInstance().getAdStatus();
        switch (adStatus) {
            case DataBody.APPODEAL:
                Appodeal.show(this, Appodeal.INTERSTITIAL);
                break;
            case DataBody.NO:
                break;
            case DataBody.START_APP:
                startAppAd.showAd();
                break;
        }
    }
}
