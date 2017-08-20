package free.mp3.test.util;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import free.mp3.test.activity.BaseActivity;
import free.mp3.test.interfaces.RuntimePermissionListener;

public class PermissionUtil {
    public static void checkPermission(BaseActivity activity, int code, String permission, RuntimePermissionListener permissionListener) {
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
            permissionListener.onGranted();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, code);
        }
    }
}
