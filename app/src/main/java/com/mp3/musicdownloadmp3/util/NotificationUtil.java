package com.mp3.musicdownloadmp3.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

import com.mp3.musicdownloadmp3.R;
import com.mp3.musicdownloadmp3.application.MusicApplication;
import com.mp3.musicdownloadmp3.service.NotificationService;

import static com.mp3.musicdownloadmp3.service.NotificationService.DOWNLOAD_TYPE;
import static com.mp3.musicdownloadmp3.service.NotificationService.MUSIC_DOWNLOAD_ACTION;
import static com.mp3.musicdownloadmp3.service.NotificationService.MUSIC_DOWNLOAD_PATH;

public class NotificationUtil {
    private static final int MUSIC_DOWNLOAD_NOTIFICATION_ID = 100;

    public static void showNotification(int type, String title, String path) {
        Context context = MusicApplication.getInstance().getApplicationContext();
        Notification notification = new NotificationCompat
                .Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                .setContentIntent(createPendingIntent(type, path))
                .build();
        NotificationManagerCompat.from(MusicApplication.getInstance().getApplicationContext())
                .notify(MUSIC_DOWNLOAD_NOTIFICATION_ID, notification);
    }

    private static PendingIntent createPendingIntent(int type, String path) {
        Context context = MusicApplication.getInstance().getApplicationContext();
        Intent intent = new Intent(context, NotificationService.class);
        intent.setAction(MUSIC_DOWNLOAD_ACTION);
        intent.putExtra(MUSIC_DOWNLOAD_PATH, path);
        intent.putExtra(DOWNLOAD_TYPE, type);
        return PendingIntent.getService(context, MUSIC_DOWNLOAD_NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}