package com.music.voroshilo.networking.task;

import android.app.DownloadManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;

import com.music.voroshilo.R;
import com.music.voroshilo.application.MusicApplication;
import com.music.voroshilo.model.networking.Download;
import com.music.voroshilo.util.NotificationUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.DOWNLOAD_SERVICE;

public class SongDownloadTask extends BaseDownloadTask {
    public static final int INITIAL_PROGRESS = 0;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;
    private static List<Long> downloadTaskList = new ArrayList<>();

    @Override
    int setType() {
        return Download.MUSIC;
    }

    public static boolean isDownloading() {
        return downloadTaskList.size() > 0;
    }

    public void downloadFile(String url, final String title, final ProgressBar progressBar) {
        MusicApplication musicApplication = MusicApplication.getInstance();
        String appName = musicApplication.getString(R.string.app_name);
        String filePath = File.separator + appName + File.separator + title + ".mp3";

        DownloadManager dm = (DownloadManager) musicApplication.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("Download " + title);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filePath);
        long id = dm.enqueue(request);
        downloadTaskList.add(id);

        runnable = () -> {
            int progress = getProgress(dm, id);
            progressBar.setProgress(progress);
            if (progress != 100) {
                handler.postDelayed(runnable, 1000);
            } else {
                handler.removeCallbacks(runnable);
                downloadTaskList.remove(id);
                NotificationUtil.showNotification(Download.MUSIC, "Download complete " + title,
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + filePath);
            }
        };
        handler.post(runnable);
    }

    private int getProgress(DownloadManager dm, long id) {
        int progress = 0;

        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);

        Cursor c = dm.query(query);
        if (c.moveToFirst()) {
            int sizeIndex = c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
            int downloadedIndex = c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
            long size = c.getInt(sizeIndex);
            long downloaded = c.getInt(downloadedIndex);
            if (size != -1) progress = (int) (downloaded * 100.0 / size);
        }
        return progress;
    }
}