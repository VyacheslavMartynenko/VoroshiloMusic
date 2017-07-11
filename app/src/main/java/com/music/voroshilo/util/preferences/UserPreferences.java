package com.music.voroshilo.util.preferences;

import android.content.Context;
import android.view.Display;

import com.music.voroshilo.model.networking.DataBody;

public class UserPreferences extends AbstractPreferences {
    private static final String PREFERENCES = "UserPreferences";
    private static final String IS_FIRST_LAUNCH = "IsFirstLaunch";
    private static final String IS_APP_RATED = "IsFirstLaunch";
    private static final String POP_UP_URL = "PopUpUrl";
    private static final String BURST_STATUS = "BurstStatus";
    private static final String MARKET_URL = "MarketUrl";
    private static final String AD_STATUS = "AdStatus";

    private static UserPreferences instance;

    public static UserPreferences getInstance() {
        return instance;
    }

    private UserPreferences(Context context, String name) {
        super(context, name);
    }

    static void init(Context context) {
        instance = new UserPreferences(context, PREFERENCES);
    }

    public boolean isFirstLaunch() {
        return preferences.getBoolean(IS_FIRST_LAUNCH, true);
    }

    public void setIsFirstLaunch() {
        preferences.edit().putBoolean(IS_FIRST_LAUNCH, false).apply();
    }

    public String getPopUpUrl() {
        return preferences.getString(POP_UP_URL, null);
    }

    public void setPopUpUrl(String popUpUrl) {
        preferences.edit().putString(POP_UP_URL, popUpUrl).apply();
    }

    public boolean isAppRated() {
        return preferences.getBoolean(IS_APP_RATED, false);
    }

    public void setIsAppRated() {
        preferences.edit().putBoolean(IS_APP_RATED, true).apply();
    }

    public int getBurstStatus() {
        return preferences.getInt(BURST_STATUS, DataBody.MUSIC);
    }

    public void setBustStatus(int burstStatus) {
        preferences.edit().putInt(BURST_STATUS, burstStatus).apply();
    }

    public String getMarketUrl() {
        return preferences.getString(MARKET_URL, null);
    }

    public void setMarketUrl(String marketUrl) {
        preferences.edit().putString(MARKET_URL, marketUrl).apply();
    }

    public int getAdStatus() {
        return preferences.getInt(AD_STATUS, DataBody.APPODEAL);
    }

    public void setAdStatus(int adStatus) {
        preferences.edit().putInt(AD_STATUS, adStatus).apply();
    }
}