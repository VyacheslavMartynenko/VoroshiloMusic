package mp3.music.download.downloadmp3.downloadmusic.networking.task;

import android.app.DownloadManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ProgressBar;

import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mp3.music.download.downloadmp3.downloadmusic.R;
import mp3.music.download.downloadmp3.downloadmusic.activity.BaseActivity;
import mp3.music.download.downloadmp3.downloadmusic.application.MusicApplication;
import mp3.music.download.downloadmp3.downloadmusic.model.networking.DataBody;
import mp3.music.download.downloadmp3.downloadmusic.model.networking.Download;
import mp3.music.download.downloadmp3.downloadmusic.util.NotificationUtil;
import mp3.music.download.downloadmp3.downloadmusic.util.preferences.UserPreferences;

import static android.content.Context.DOWNLOAD_SERVICE;

public class SongDownloadTask extends BaseDownloadTask {
    public static final int INITIAL_PROGRESS = 0;
    private static final int MAX_PROGRESS = 100;
    private static final int UPDATE_TIME = 1000;

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
        String filePath = title + ".mp3";

        DownloadManager dm = (DownloadManager) musicApplication.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(musicApplication.getString(R.string.download_progress) + " " + title);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filePath);
        long id = dm.enqueue(request);
        downloadTaskList.add(id);

        runnable = () -> {
            int progress = getProgress(dm, id);
            progressBar.setProgress(progress);
            if (progress != MAX_PROGRESS) {
                handler.postDelayed(runnable, UPDATE_TIME);
            } else {
                handler.removeCallbacks(runnable);
                downloadTaskList.remove(id);
                NotificationUtil.showNotification(Download.MUSIC, musicApplication.getString(R.string.download_complete) + " " + title,
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + filePath);
            }
        };
        handler.post(runnable);

        BaseActivity activity = MusicApplication.getInstance().getCurrentActivity();
        if (activity != null) {
            if (UserPreferences.getInstance().getAdNetDownload() != DataBody.NO) {
                activity.showAd();
            }
        }
    }

    private int getProgress(DownloadManager dm, long id) {
        int progress = 0;

        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);

        Cursor cursor = null;
        try {
            cursor = dm.query(query);
            if (cursor.moveToFirst()) {
                int sizeIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES);
                int downloadedIndex = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                long size = cursor.getInt(sizeIndex);
                long downloaded = cursor.getInt(downloadedIndex);
                if (size != -1) progress = (int) (downloaded * 100.0 / size);
            }
        } catch (Exception e) {
            Log.e(SongDownloadTask.class.getSimpleName(), Log.getStackTraceString(e));
            Crashlytics.logException(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return progress;
    }
}