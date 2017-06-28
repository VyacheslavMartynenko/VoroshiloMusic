package com.music.voroshilo.service;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.music.voroshilo.model.networking.Download;

import java.io.File;

public class NotificationService extends IntentService {
    public static final String MUSIC_DOWNLOAD_ACTION = "com.music.voroshilo.util.download";
    public static final String MUSIC_DOWNLOAD_PATH = "com.music.voroshilo.util.path";
    public static final String DOWNLOAD_TYPE = "download_type";

    public NotificationService() {
        super(NotificationService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null && action.equals(MUSIC_DOWNLOAD_ACTION)) {
                String path = intent.getStringExtra(MUSIC_DOWNLOAD_PATH);
                if (path != null) {
                    @Download.Type int type = intent.getIntExtra(DOWNLOAD_TYPE, Download.APK);
                    showFolder(type, path);
                }
            }
        }
    }

    private void showFolder(@Download.Type int type, String path) {
        File file = new File(path);
        Uri uri = Uri.fromFile(file);

        switch (type) {
            case Download.APK:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setDataAndType(uri, "*/*");
                Intent chooserIntent = Intent.createChooser(intent, "Open").setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                if (chooserIntent.resolveActivityInfo(getPackageManager(), 0) != null) {
                    startActivity(chooserIntent);
                }
                break;
            case Download.MUSIC:
                Intent musicIntent = new Intent(Intent.ACTION_VIEW).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                musicIntent.setDataAndType(uri, "audio/*");

                if (musicIntent.resolveActivityInfo(getPackageManager(), 0) != null) {
                    startActivity(musicIntent);
                }
                break;
        }
    }
}
