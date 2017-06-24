package com.music.voroshilo.networking.task;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.music.voroshilo.R;
import com.music.voroshilo.application.MusicApplication;
import com.music.voroshilo.networking.ApiBuilder;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApkDownloadTask extends BaseDownloadTask {

    public void downloadFile(String url) {
        ApiBuilder.getApiService().getApkFile(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    final ResponseBody responseBody = response.body();
                    if (responseBody != null && responseBody.contentLength() > 0) {
                        createDownloadTask(responseBody);
                    } else {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MusicApplication.getInstance().getApplicationContext(),
                                        R.string.download_error_message, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e(SongDownloadTask.class.getSimpleName(), Log.getStackTraceString(t));
            }
        });
    }

    private AsyncTask<Void, Void, Void> createDownloadTask(final ResponseBody responseBody) {
        return new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                writeResponseBodyToDisk(responseBody, Environment.DIRECTORY_DOWNLOADS, "music_downloader.apk");

                return null;
            }
        }.execute();
    }
}