package mp3.music.download.downloadmp3.downloadmusic.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import mp3.music.download.downloadmp3.downloadmusic.R;
import mp3.music.download.downloadmp3.downloadmusic.application.MusicApplication;

public class MusicFirebaseMessagingService extends FirebaseMessagingService {
    private static final int FIREBASE_NOTIFICATION = 103;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Map<String, String> data = remoteMessage.getData();
        String packageName = data.get("pckg_name");
        if (notification != null) {
            sendNotification(notification.getTitle(), notification.getBody(), packageName);
        }
    }

    private void sendNotification(String title, String text, String packageName) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(packageName)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, FIREBASE_NOTIFICATION, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Context context = MusicApplication.getInstance().getApplicationContext();
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                .setContentIntent(pendingIntent)
                .build();
        NotificationManagerCompat.from(context).notify(FIREBASE_NOTIFICATION, notification);
    }
}
