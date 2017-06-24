package com.music.voroshilo.util.preferences;

import android.content.Context;

public class UserPreferences extends AbstractPreferences {
    private static final String PREFERENCES = "UserPreferences";
    private static final String IS_FIRST_LAUNCH = "IsFirstLaunch";
    private static final String IS_APP_RATED = "IsFirstLaunch";

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

    public boolean isAppRated() {
        return preferences.getBoolean(IS_APP_RATED, false);
    }

    public void setIsAppRated() {
        preferences.edit().putBoolean(IS_APP_RATED, true).apply();
    }
}