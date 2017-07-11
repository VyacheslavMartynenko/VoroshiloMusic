package com.mp3.musicdownloadmp3.util;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.mp3.musicdownloadmp3.activity.BaseActivity;
import com.mp3.musicdownloadmp3.interfaces.RuntimePermissionListener;

public class PermissionUtil {
    public static void checkPermission(BaseActivity activity, int code, String permission, RuntimePermissionListener permissionListener) {
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
            permissionListener.onGranted();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, code);
        }
    }
}
