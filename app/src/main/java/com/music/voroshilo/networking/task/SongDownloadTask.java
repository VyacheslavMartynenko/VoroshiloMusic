package com.music.voroshilo.networking.task;

import android.app.DownloadManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;

import com.music.voroshilo.application.MusicApplication;
import com.music.voroshilo.model.networking.Download;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.DOWNLOAD_SERVICE;

public class SongDownloadTask extends BaseDownloadTask {
    public static final int INITIAL_PROGRESS = 0;
    private Handler handler = new Handler(Looper.getMainLooper());
    private static List<AsyncTask> downloadTaskList = new ArrayList<>();

    @Override
    int setType() {
        return Download.MUSIC;
    }

    public static boolean isDownloading() {
        return downloadTaskList.size() > 0;
    }

    public void downloadFile(String url, final String title, final ProgressBar progressBar) {
        MusicApplication musicApplication = MusicApplication.getInstance();
        DownloadManager dm = (DownloadManager) musicApplication.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("Download " + title);
        request.setDestinationInExternalFilesDir(musicApplication.getApplicationContext(), Environment.DIRECTORY_DOWNLOADS, title + ".mp3");
        long id = dm.enqueue(request);
        handler.post(getRunnable(progressBar, dm, id));
    }

    private Runnable getRunnable(ProgressBar progressBar, DownloadManager dm, long id) {
        return () -> {
            progressBar.setProgress(getProgress(dm, id));
            handler.postDelayed(getRunnable(progressBar, dm, id), 1000);
        };
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