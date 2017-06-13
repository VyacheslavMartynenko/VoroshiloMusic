package com.music.voroshilo.networking.task;

import android.os.AsyncTask;
import android.util.Log;

import com.music.voroshilo.interfaces.ProgressListener;
import com.music.voroshilo.networking.repsonse.ProgressResponseBody;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadAsyncTask extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... strings) {
        Request request = new Request.Builder()
                .url("https://publicobject.com/helloworld.txt")
                .build();

        final ProgressListener progressListener = new ProgressListener() {
            @Override public void update(long bytesRead, long contentLength, boolean done) {
                Log.e("R", String.valueOf(bytesRead));
                Log.e("R", String.valueOf(contentLength));
                Log.e("R", String.valueOf(done));
            }
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override public Response intercept(Interceptor.Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());
                        return originalResponse.newBuilder()
                                .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                                .build();
                    }
                })
                .build();

        try {
            Response response = client.newCall(request).execute();
            Log.e("R", response.body().string());
        } catch (IOException e) {
            Log.e("R", e.toString());
        }

        return null;
    }
}
