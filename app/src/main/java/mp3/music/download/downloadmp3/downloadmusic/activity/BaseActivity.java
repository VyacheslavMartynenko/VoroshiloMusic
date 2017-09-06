package mp3.music.download.downloadmp3.downloadmusic.activity;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.appodeal.ads.Appodeal;
import com.startapp.android.publish.ads.banner.Banner;
import com.startapp.android.publish.adsCommon.Ad;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;
import com.startapp.android.publish.adsCommon.adListeners.AdEventListener;

import java.util.Random;

import butterknife.ButterKnife;
import mp3.music.download.downloadmp3.downloadmusic.R;
import mp3.music.download.downloadmp3.downloadmusic.application.MusicApplication;
import mp3.music.download.downloadmp3.downloadmusic.model.networking.DataBody;
import mp3.music.download.downloadmp3.downloadmusic.util.preferences.UserPreferences;

abstract public class BaseActivity extends AppCompatActivity implements AdEventListener {

    private MusicApplication app;
    private boolean isActivityPaused = false;
    private StartAppAd startAppAd = new StartAppAd(this);
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MusicApplication) this.getApplicationContext();
        @DataBody.AdMode
        int adStatus = UserPreferences.getInstance().getAdNetType();
        switch (adStatus) {
            case DataBody.APPODEAL:
                Appodeal.disableNetwork(this, "cheetah");
                String appKey = UserPreferences.getInstance().getAppodealKey();
                Appodeal.disableLocationPermissionCheck();
                Appodeal.initialize(this, appKey, Appodeal.INTERSTITIAL);
                break;
            case DataBody.NO:
                break;
            case DataBody.START_APP:
                StartAppSDK.init(this, UserPreferences.getInstance().getStartappKey(), true);
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

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        startAppAd.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        startAppAd.onSaveInstanceState(outState);
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
        int adStatus = UserPreferences.getInstance().getAdNetType();
        switch (adStatus) {
            case DataBody.APPODEAL:
                int value = random.nextInt(100);
                if (value < 25) {
                    Appodeal.show(this, Appodeal.NON_SKIPPABLE_VIDEO);
                } else if (value < 50) {
                    Appodeal.show(this, Appodeal.SKIPPABLE_VIDEO);
                } else {
                    Appodeal.show(this, Appodeal.INTERSTITIAL);
                }
                break;
            case DataBody.NO:
                break;
            case DataBody.START_APP:
                int newValue = random.nextInt(100);
                if (newValue < 25) {
                    startAppAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, this);
                } else if (newValue < 50) {
                    startAppAd.loadAd(StartAppAd.AdMode.VIDEO, this);
                } else {
                    startAppAd.loadAd(StartAppAd.AdMode.FULLPAGE, this);
                }
                break;
        }
    }

    @Override
    public void onReceiveAd(Ad ad) {
        if (!this.isFinishing()) {
            startAppAd.showAd();
        }
    }

    @Override
    public void onFailedToReceiveAd(Ad ad) {
    }

    public void showBanner(ViewGroup viewGroup, ViewGroup viewGroupWrapper) {
        if (UserPreferences.getInstance().getAdNetBanner() != DataBody.NO) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewGroup.getLayoutParams();

            @DataBody.AdMode
            int adStatus = UserPreferences.getInstance().getAdNetType();
            switch (adStatus) {
                case DataBody.APPODEAL:
                    setMargins(params);
                    Appodeal.show(this, Appodeal.BANNER_BOTTOM);
                    break;
                case DataBody.NO:
                    break;
                case DataBody.START_APP:
                    setMargins(params);
                    Banner startAppBanner = new Banner(getApplicationContext());
                    RelativeLayout.LayoutParams bannerParameters = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    bannerParameters.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    bannerParameters.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    ViewCompat.setElevation(startAppBanner, 6);
                    viewGroupWrapper.addView(startAppBanner, bannerParameters);
                    break;
            }
        }
    }

    private void setMargins(RelativeLayout.LayoutParams params) {
        Resources resources = getResources();
        int margin = resources.getDimensionPixelSize(R.dimen.fab_margin);
        int elevateMarin = resources.getDimensionPixelSize(R.dimen.fab_elevate_margin);
        params.setMargins(margin, margin, margin, elevateMarin);
    }
}
