package com.music.voroshilo.networking.task;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.music.voroshilo.R;
import com.music.voroshilo.application.MusicApplication;
import com.music.voroshilo.model.networking.Download;
import com.music.voroshilo.util.NotificationUtil;

import java.io.File;

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
        String filePath = File.separator + appName + File.separator + title + ".mp3";

        DownloadManager dm = (DownloadManager) musicApplication.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("Download " + title);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filePath);
        dm.enqueue(request);

        BroadcastReceiver onComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    MusicApplication.getInstance().unregisterReceiver(this);
                    NotificationUtil.showNotification(Download.APK, "Download complete " + title,
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + filePath);
                } catch (Exception e) {
                    Log.e(ApkDownloadTask.this.getClass().getSimpleName(), Log.getStackTraceString(e));
                }
            }
        };
        MusicApplication.getInstance().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
}