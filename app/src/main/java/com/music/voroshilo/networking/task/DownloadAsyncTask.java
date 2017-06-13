package com.music.voroshilo.networking.task;

import android.os.AsyncTask;
import android.util.Log;

import com.music.voroshilo.interfaces.ProgressListener;
import com.music.voroshilo.networking.ApiBuilder;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class DownloadAsyncTask extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... strings) {
        final ProgressListener progressListener = new ProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                Log.e("R", String.valueOf(bytesRead));
                Log.e("R", String.valueOf(contentLength));
                Log.e("R", String.valueOf(done));
            }
        };

        try {
            Response<ResponseBody> response = ApiBuilder.getDownloadService(progressListener).getFile("https://publicobject.com/helloworld.txt").execute();
            Log.e("R", response.body().string());
        } catch (IOException e) {
            Log.e("R", e.toString());
        }

        return null;
    }
}
