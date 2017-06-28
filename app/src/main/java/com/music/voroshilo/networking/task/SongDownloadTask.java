package com.music.voroshilo.networking.task;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.music.voroshilo.R;
import com.music.voroshilo.application.MusicApplication;
import com.music.voroshilo.interfaces.ProgressListener;
import com.music.voroshilo.model.networking.Download;
import com.music.voroshilo.networking.ApiBuilder;
import com.music.voroshilo.util.NotificationUtil;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongDownloadTask extends BaseDownloadTask {
    public static final int INITIAL_PROGRESS = 0;
    private static List<AsyncTask> downloadTaskList = new ArrayList<>();

    @Override
    int setType() {
        return Download.MUSIC;
    }

    public static boolean isDownloading() {
        return downloadTaskList.size() > 0;
    }

    public void downloadFile(String url, final String title, final ProgressBar progressBar) {
        final ProgressListener progressListener = (bytesRead, contentLength, done) -> {
            progressBar.setProgress((int) bytesRead);
            NotificationUtil.showProgressNotification(bytesRead, contentLength, done);
        };

        ApiBuilder.getDownloadService(progressListener).getFile(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    final ResponseBody responseBody = response.body();
                    if (responseBody != null && responseBody.contentLength() > 0) {
                        downloadTaskList.add(createDownloadTask(progressBar, responseBody, title));
                    } else {
                        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(MusicApplication.getInstance().getApplicationContext(),
                                R.string.download_error_message, Toast.LENGTH_SHORT).show());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e(SongDownloadTask.class.getSimpleName(), Log.getStackTraceString(t));
            }
        });
    }

    private AsyncTask<Void, Void, Void> createDownloadTask(final ProgressBar progressBar, final ResponseBody responseBody, final String title) {
        return new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    progressBar.setMax((int) responseBody.contentLength());
                    progressBar.setProgress(INITIAL_PROGRESS);
                });
                writeResponseBodyToDisk(responseBody, Environment.DIRECTORY_MUSIC, title.concat(".mp3"));

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                downloadTaskList.remove(this);
                super.onPostExecute(aVoid);
            }
        }.execute();
    }
}