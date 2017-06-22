package com.music.voroshilo.networking;

import android.support.annotation.NonNull;

import com.music.voroshilo.interfaces.ProgressListener;
import com.music.voroshilo.networking.repsonse.ProgressResponseBody;

import java.io.IOException;

import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiBuilder {
    //todo popup request
    private static final String BASE_URL = "https://mp3download.tube/";

    private static ApiBuilder apiBuilder;
    private final MusicService musicService;

    private static ApiBuilder getInstance() {
        if (apiBuilder == null) {
            apiBuilder = new ApiBuilder();
        }
        return apiBuilder;
    }

    private ApiBuilder() {
        musicService = getRetrofit(false, null).create(MusicService.class);
    }

    public static MusicService getMusicService() {
        return getInstance().musicService;
    }

    public static DownloadService getDownloadService(ProgressListener listener) {
        return getInstance().getRetrofit(true, listener).create(DownloadService.class);
    }

    private Retrofit getRetrofit(boolean isDownloadService, ProgressListener listener) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()));

        if (isDownloadService) {
            builder.client(getOkHttpClient(listener));
        }

        return builder.build();
    }

    private OkHttpClient getOkHttpClient(final ProgressListener progressListener) {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());
                        return originalResponse.newBuilder()
                                .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                                .build();
                    }
                })
                .build();
    }
}