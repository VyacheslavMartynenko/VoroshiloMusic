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

import java.util.Locale;

import static com.music.voroshilo.service.NotificationService.DOWNLOAD_TYPE;
import static com.music.voroshilo.service.NotificationService.MUSIC_DOWNLOAD_ACTION;
import static com.music.voroshilo.service.NotificationService.MUSIC_DOWNLOAD_PATH;

public class NotificationUtil {
    private static final int MUSIC_DOWNLOAD_NOTIFICATION_ID = 100;
    private static final int MUSIC_DOWNLOAD_NOTIFICATION_PROGRESS = 101;

    private static NotificationManagerCompat managerCompat = NotificationManagerCompat
            .from(MusicApplication.getInstance().getApplicationContext());

    public static void showNotification(int type, String title, String text, String path) {
        Context context = MusicApplication.getInstance().getApplicationContext();
        Notification notification = new NotificationCompat
                .Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
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

    public static void showProgressNotification(long bytesRead, long contentLength, boolean done) {
        if (!done) {
            Context context = MusicApplication.getInstance().getApplicationContext();
            Notification notification = new NotificationCompat
                    .Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText(String.format(Locale.US, "Downloading file \"%1.2f MB / \"%1.2f MB", (bytesRead / (double) 1024000), (contentLength / (double) 1024000)))
                    .setProgress((int) contentLength, (int) bytesRead, false)
                    .setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                    .build();

            managerCompat.notify(MUSIC_DOWNLOAD_NOTIFICATION_PROGRESS, notification);
        } else {
            managerCompat.cancel(MUSIC_DOWNLOAD_NOTIFICATION_PROGRESS);
        }
    }
}