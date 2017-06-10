package com.music.voroshilo.networking;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiBuilder {
    private static final String BASE_URL = "https://mp3download.tube/";
    private static ApiBuilder apiBuilder;
    private final MusicService musicService;

    public static MusicService getMusicService() {
        return getInstance().musicService;
    }

    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
    }

    private ApiBuilder() {
        musicService = getRetrofit().create(MusicService.class);
    }

    private static ApiBuilder getInstance() {
        if (apiBuilder == null) {
            apiBuilder = new ApiBuilder();
        }
        return apiBuilder;
    }
}