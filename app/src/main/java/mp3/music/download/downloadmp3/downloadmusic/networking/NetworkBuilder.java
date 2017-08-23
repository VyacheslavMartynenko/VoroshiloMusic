package mp3.music.download.downloadmp3.downloadmusic.networking;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkBuilder {
    public static final String PRIVACY_URL = "http://audiko.net/privacy.html";
    public static final String LICENSE_URL = "https://creativecommons.org/licenses/by/3.0/";
    private static final String BASE_API_URL = "http://mp3download.guru/api/v1/imba/";

    private static NetworkBuilder networkBuilder;
    private final NetworkService networkService;

    private static NetworkBuilder getInstance() {
        if (networkBuilder == null) {
            networkBuilder = new NetworkBuilder();
        }
        return networkBuilder;
    }

    private NetworkBuilder() {
        networkService = getRetrofit().create(NetworkService.class);
    }

    public static NetworkService getApiService() {
        return getInstance().networkService;
    }

    private Retrofit getRetrofit() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()));

        return builder.build();
    }
}