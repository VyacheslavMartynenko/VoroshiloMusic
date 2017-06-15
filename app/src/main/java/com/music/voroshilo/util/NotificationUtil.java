package com.music.voroshilo.util;

import android.app.Notification;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

import com.music.voroshilo.R;
import com.music.voroshilo.application.MusicApplication;

public class NotificationUtil {
    public static void showNotification(String title, String text) {
        Context context = MusicApplication.getInstance().getApplicationContext();
        Notification notification = new NotificationCompat
                .Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                .build();
        NotificationManagerCompat.from(MusicApplication.getInstance().getApplicationContext())
                .notify(0, notification);
    }
}
