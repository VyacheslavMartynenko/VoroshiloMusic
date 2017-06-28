package com.music.voroshilo.networking;

import com.music.voroshilo.interfaces.ProgressListener;
import com.music.voroshilo.networking.repsonse.ProgressResponseBody;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiBuilder {
    public static final String PRIVACY_URL = "http://audiko.net/privacy.html";
    public static final String LICENSE_URL = "https://creativecommons.org/licenses/by/3.0/";

    private static final String BASE_URL = "https://mp3download.tube/";
    private static final String BASE_API_URL = "http://mp3download.guru/";

    private static ApiBuilder apiBuilder;
    private final MusicService musicService;
    private final ApiService apiService;

    private static ApiBuilder getInstance() {
        if (apiBuilder == null) {
            apiBuilder = new ApiBuilder();
        }
        return apiBuilder;
    }

    private ApiBuilder() {
        musicService = getRetrofit(false, false, null).create(MusicService.class);
        apiService = getRetrofit(true, false, null).create(ApiService.class);
    }

    public static MusicService getMusicService() {
        return getInstance().musicService;
    }

    public static ApiService getApiService() {return getInstance().apiService;}

    public static DownloadService getDownloadService(ProgressListener listener) {
        return getInstance().getRetrofit(false, true, listener).create(DownloadService.class);
    }

    private Retrofit getRetrofit(boolean isApi, boolean isDownloadService, ProgressListener listener) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()));

        if (isApi) {
            builder.baseUrl(BASE_API_URL);
        } else {
            builder.baseUrl(BASE_URL);
        }

        if (isDownloadService) {
            builder.client(getOkHttpClient(listener));
        }

        return builder.build();
    }

    private OkHttpClient getOkHttpClient(final ProgressListener progressListener) {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(chain -> {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                            .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                            .build();
                })
                .build();
    }
}