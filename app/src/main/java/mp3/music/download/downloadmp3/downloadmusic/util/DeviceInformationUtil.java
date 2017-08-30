package mp3.music.download.downloadmp3.downloadmusic.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import mp3.music.download.downloadmp3.downloadmusic.activity.BaseActivity;

public class DeviceInformationUtil {
    @SuppressLint("HardwareIds")
    public static String getDeviceUniqueID(BaseActivity activity) {
        return Settings.Secure.getString(activity.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("HardwareIds")
    public static String getIMEI(BaseActivity activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return telephonyManager.getImei();
        } else {
            return telephonyManager.getDeviceId();
        }
    }
}
