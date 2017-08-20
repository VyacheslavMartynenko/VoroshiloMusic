package free.mp3.test.util.preferences;

import android.content.Context;

public class SharedPreferencesProvider {
    private static SharedPreferencesProvider instance;

    public static SharedPreferencesProvider getInstance() {
        if (instance == null) {
            instance = new SharedPreferencesProvider();
        }
        return instance;
    }

    public void initialize(Context context) {
        UserPreferences.init(context);
    }
}
