package com.music.voroshilo.networking.task;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.music.voroshilo.R;
import com.music.voroshilo.activity.BaseActivity;
import com.music.voroshilo.application.MusicApplication;
import com.music.voroshilo.interfaces.ProgressListener;
import com.music.voroshilo.networking.ApiBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileDownloadTask {
    private static final int INITIAL_PROGRESS = 0;

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
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            final ResponseBody responseBody = response.body();
                            if (responseBody != null && responseBody.contentLength() > 0) {
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setMax((int) responseBody.contentLength());
                                        progressBar.setProgress(INITIAL_PROGRESS);
                                    }
                                });
                                writeResponseBodyToDisk(responseBody, title);
                            }
                            return null;
                        }
                    }.execute();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e(FileDownloadTask.class.getSimpleName(), Log.getStackTraceString(t));
            }
        });
    }

    private static boolean writeResponseBodyToDisk(ResponseBody body, String title) {
        try {
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "VoroshiloMusic");
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
                showCompleteMessage(musicFile.getCanonicalPath());
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

    private static void showCompleteMessage(final String path) {
        final BaseActivity activity = MusicApplication.getInstance().getCurrentActivity();
        if (activity != null && activity.isVisible()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity,
                            activity.getString(R.string.download_complete_message, path),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
