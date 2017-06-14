package com.music.voroshilo.util;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.music.voroshilo.interfaces.RuntimePermissionListener;

public class PermissionUtil {
    public static void checkPermission(AppCompatActivity activity, int code, String permission, RuntimePermissionListener permissionListener) {
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED) {
            permissionListener.onGranted();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{permission}, code);
        }
    }
}
