package mp3.music.download.downloadmp3.downloadmusic.networking.task;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import mp3.music.download.downloadmp3.downloadmusic.R;
import mp3.music.download.downloadmp3.downloadmusic.application.MusicApplication;
import mp3.music.download.downloadmp3.downloadmusic.model.networking.Download;
import mp3.music.download.downloadmp3.downloadmusic.util.NotificationUtil;

import static android.content.Context.DOWNLOAD_SERVICE;

public class ApkDownloadTask extends BaseDownloadTask {

    @Override
    int setType() {
        return Download.APK;
    }

    public void downloadFile(String url) {
        String title = "MusicDownloader";
        MusicApplication musicApplication = MusicApplication.getInstance();
        String appName = musicApplication.getString(R.string.app_name);
        String filePath = File.separator + appName + File.separator + title + ".apk";

        DownloadManager dm = (DownloadManager) musicApplication.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(musicApplication.getString(R.string.download_progress) + title);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filePath);
        dm.enqueue(request);

        BroadcastReceiver onComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    MusicApplication.getInstance().unregisterReceiver(this);
                    NotificationUtil.showNotification(Download.APK, musicApplication.getString(R.string.download_complete) + title,
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + filePath);
                } catch (Exception e) {
                    Log.e(ApkDownloadTask.this.getClass().getSimpleName(), Log.getStackTraceString(e));
                }
            }
        };
        MusicApplication.getInstance().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
}