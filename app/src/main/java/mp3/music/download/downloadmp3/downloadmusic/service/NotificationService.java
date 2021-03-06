package mp3.music.download.downloadmp3.downloadmusic.service;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;

import java.io.File;

import mp3.music.download.downloadmp3.downloadmusic.application.MusicApplication;
import mp3.music.download.downloadmp3.downloadmusic.model.networking.Download;

public class NotificationService extends IntentService {
    public static final String MUSIC_DOWNLOAD_ACTION = "mp3.music.download.downloadmp3.downloadmusic.download";
    public static final String MUSIC_DOWNLOAD_PATH = "mp3.music.download.downloadmp3.downloadmusic.path";
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
        Uri uri;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(MusicApplication.getInstance().getApplicationContext(),
                    "mp3.music.download.downloadmp3.downloadmusic.fileProvider", file);
        } else {
            uri = Uri.fromFile(file);
        }

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
                Intent musicIntent = new Intent(Intent.ACTION_VIEW).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                musicIntent.setDataAndType(uri, "audio/*");

                if (musicIntent.resolveActivityInfo(getPackageManager(), 0) != null) {
                    startActivity(musicIntent);
                }
                break;
        }
    }
}
