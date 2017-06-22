package com.music.voroshilo.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

import com.music.voroshilo.R;
import com.music.voroshilo.application.MusicApplication;
import com.music.voroshilo.service.NotificationService;

import static com.music.voroshilo.service.NotificationService.MUSIC_DOWNLOAD_ACTION;
import static com.music.voroshilo.service.NotificationService.MUSIC_DOWNLOAD_PATH;

public class NotificationUtil {
    private static final int MUSIC_DOWNLOAD_NOTIFICATION_ID = 100;

    public static void showNotification(String title, String text, String path) {
        Context context = MusicApplication.getInstance().getApplicationContext();
        Notification notification = new NotificationCompat
                .Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                .setContentIntent(createPendingIntent(path))
                .build();
        NotificationManagerCompat.from(MusicApplication.getInstance().getApplicationContext())
                .notify(MUSIC_DOWNLOAD_NOTIFICATION_ID, notification);
    }

    private static PendingIntent createPendingIntent(String path) {
        Context context = MusicApplication.getInstance().getApplicationContext();
        Intent intent = new Intent(context, NotificationService.class);
        intent.setAction(MUSIC_DOWNLOAD_ACTION);
        intent.putExtra(MUSIC_DOWNLOAD_PATH, path);
        return PendingIntent.getService(context, MUSIC_DOWNLOAD_NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}