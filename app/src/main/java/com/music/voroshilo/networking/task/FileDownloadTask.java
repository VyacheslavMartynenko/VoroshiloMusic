package com.music.voroshilo.networking.task;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.music.voroshilo.R;
import com.music.voroshilo.activity.BaseActivity;
import com.music.voroshilo.application.MusicApplication;
import com.music.voroshilo.interfaces.ProgressListener;
import com.music.voroshilo.networking.ApiBuilder;
import com.music.voroshilo.util.NotificationUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileDownloadTask {
    //todo singleton
    //todo try service
    public static final int INITIAL_PROGRESS = 0;
    private static List<AsyncTask> downloadTaskList = new ArrayList<>();

    public static boolean isDownloading() {
        return downloadTaskList.size() > 0;
    }

    public static void downloadFile(String url, final String title, final ProgressBar progressBar) {
        final ProgressListener progressListener = new ProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                progressBar.setProgress((int) bytesRead);
            }
        };

        ApiBuilder.getDownloadService(progressListener).getFile(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    final ResponseBody responseBody = response.body();
                    if (responseBody != null && responseBody.contentLength() > 0) {
                        downloadTaskList.add(createDownloadTask(progressBar, responseBody, title));
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
                Log.e(FileDownloadTask.class.getSimpleName(), Log.getStackTraceString(t));
            }
        });
    }

    private static AsyncTask<Void, Void, Void> createDownloadTask(final ProgressBar progressBar, final ResponseBody responseBody, final String title) {
        return new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setMax((int) responseBody.contentLength());
                        progressBar.setProgress(INITIAL_PROGRESS);
                    }
                });
                writeResponseBodyToDisk(responseBody, title);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                downloadTaskList.remove(this);
                super.onPostExecute(aVoid);
            }
        }.execute();
    }

    private static boolean writeResponseBodyToDisk(ResponseBody body, String title) {
        try {
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC),
                    MusicApplication.getInstance().getString(R.string.app_name));
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return false;
                }
            }
            final File musicFile = new File(mediaStorageDir + File.separator + title + ".mp3");
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(musicFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                }
                outputStream.flush();
                showCompleteMessage(musicFile.getPath(), mediaStorageDir.getPath());
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    private static void showCompleteMessage(final String filePath, final String dirPath) {
        final BaseActivity activity = MusicApplication.getInstance().getCurrentActivity();
        if (activity != null && activity.isVisible()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity,
                            activity.getString(R.string.download_complete_message, filePath),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
        Context context = MusicApplication.getInstance().getApplicationContext();
        String title = context.getString(R.string.app_name);
        String text = context.getString(R.string.download_complete_message, filePath);
        NotificationUtil.showNotification(title, text, dirPath);
    }
}
