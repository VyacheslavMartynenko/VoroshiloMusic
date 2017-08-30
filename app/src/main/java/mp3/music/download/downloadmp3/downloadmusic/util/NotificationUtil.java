package mp3.music.download.downloadmp3.downloadmusic.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

import mp3.music.download.downloadmp3.downloadmusic.R;
import mp3.music.download.downloadmp3.downloadmusic.application.MusicApplication;
import mp3.music.download.downloadmp3.downloadmusic.service.NotificationService;

public class NotificationUtil {
    private static final int MUSIC_DOWNLOAD_NOTIFICATION_ID = 100;

    public static void showNotification(int type, String title, String path) {
        Context context = MusicApplication.getInstance().getApplicationContext();
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                .setContentIntent(createPendingIntent(type, path))
                .build();
        NotificationManagerCompat.from(context).notify(MUSIC_DOWNLOAD_NOTIFICATION_ID, notification);
    }

    private static PendingIntent createPendingIntent(int type, String path) {
        Context context = MusicApplication.getInstance().getApplicationContext();
        Intent intent = new Intent(context, NotificationService.class);
        intent.setAction(NotificationService.MUSIC_DOWNLOAD_ACTION);
        intent.putExtra(NotificationService.MUSIC_DOWNLOAD_PATH, path);
        intent.putExtra(NotificationService.DOWNLOAD_TYPE, type);
        return PendingIntent.getService(context, MUSIC_DOWNLOAD_NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}