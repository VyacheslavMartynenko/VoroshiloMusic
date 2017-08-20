package free.mp3.test.util.preferences;

import android.content.Context;
import android.content.SharedPreferences;

abstract class AbstractPreferences {
    final SharedPreferences preferences;

    AbstractPreferences(Context context, String name) {
        preferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }
}
