package mp3.music.download.downloadmp3.downloadmusic.service;

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
import mp3.music.download.downloadmp3.downloadmusic.activity.MainActivity;
import mp3.music.download.downloadmp3.downloadmusic.application.MusicApplication;

public class MusicFirebaseMessagingService extends FirebaseMessagingService {
    private static final int FIREBASE_UPDATE = 103;
    private static final int FIREBASE_INFO = 104;

    private static final String UPDATE = "update";
    private static final String INFO = "info";

    public static final String IS_UPDATE = "Is Update";
    public static final String PACKAGE_NAME = "Package Name";
    public static final String FIREBASE_ACTION = "Firebase Action";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        sendNotification(data.get("title"), data.get("body"), data.get("msg_type"), data.get("pckg_name"));
    }

    private void sendNotification(String title, String text, String type, String packageName) {
        Context context = MusicApplication.getInstance().getApplicationContext();
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));

        Intent intent = new Intent(MusicApplication.getInstance().getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (type.equals(UPDATE)) {
            intent.setAction(FIREBASE_ACTION);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(PACKAGE_NAME, packageName);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, FIREBASE_UPDATE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.setContentIntent(pendingIntent);

            NotificationManagerCompat.from(context).notify(FIREBASE_UPDATE, notification.build());
        } else if (type.equals(INFO)) {
            intent.setAction(FIREBASE_ACTION);
            intent.putExtra(IS_UPDATE, false);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, FIREBASE_INFO, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.setContentIntent(pendingIntent);

            NotificationManagerCompat.from(context).notify(FIREBASE_INFO, notification.build());

        }
    }
}
