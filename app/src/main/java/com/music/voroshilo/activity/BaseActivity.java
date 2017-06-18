package com.music.voroshilo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.music.voroshilo.application.MusicApplication;

import butterknife.ButterKnife;

abstract public class BaseActivity extends AppCompatActivity {

    private MusicApplication app;
    private InterstitialAd interstitialAd;

    private boolean isActivityPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MusicApplication) this.getApplicationContext();
        createAdManager();
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

    private void createAdManager() {
        if (interstitialAd == null) {
            interstitialAd = new InterstitialAd(this);
            interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
            interstitialAd.loadAd(new AdRequest.Builder().build());
        }
    }

    public void showAd() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }
}
