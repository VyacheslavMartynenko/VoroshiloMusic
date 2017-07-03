package com.music.voroshilo.networking;

import com.music.voroshilo.model.networking.SettingsBody;
import com.music.voroshilo.model.networking.SongsResponseBody;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("api/v1/newgeneration/settings")
    Call<SettingsBody> getSettings();

    @GET("api/v1/audiko/tracks/search?offset=0&limit=10")
    Call<SongsResponseBody> getTopSongsList();

    @GET("api/v1/audiko/tracks/search?offset=0&limit=10")
    Call<SongsResponseBody> getSongsList(@Query("q") String query);

    @GET("http://t3.mp3free.click/report.php")
    Call<ResponseBody> reportSong(@Query("fullname") String fullName, @Query("message") String message);

}
