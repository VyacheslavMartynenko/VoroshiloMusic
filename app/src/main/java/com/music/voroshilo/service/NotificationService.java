package com.music.voroshilo.service;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

public class NotificationService extends IntentService {
    public static final String MUSIC_DOWNLOAD_ACTION = "com.music.voroshilo.util.download";
    public static final String MUSIC_DOWNLOAD_PATH = "com.music.voroshilo.util.path";

    public NotificationService() {
        super(NotificationService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            Log.e("Start", "Complete");
            String action = intent.getAction();
            if (action != null && action.equals(MUSIC_DOWNLOAD_ACTION)) {
                String path = intent.getStringExtra(MUSIC_DOWNLOAD_PATH);
                if (path != null) {
                    showFolder(path);
                }
            }
        }
    }

    private void showFolder(String path) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(path);
        intent.setDataAndType(uri, "*/*");
        Intent chooserIntent = Intent.createChooser(intent, "Open").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (chooserIntent.resolveActivityInfo(getPackageManager(), 0) != null) {
            startActivity(chooserIntent);
        }
    }
}
