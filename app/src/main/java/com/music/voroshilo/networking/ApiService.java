package com.music.voroshilo.networking;

import com.music.voroshilo.model.networking.SettingsBody;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ApiService {
    @GET("api/v1/newgeneration/settings")
    Call<SettingsBody> getSettings();

    @GET("http://t3.mp3free.click/report.php")
    Call<ResponseBody> reportSong(@Query("fullname") String fullName, @Query("message") String message);

    @GET
    @Streaming
    Call<ResponseBody> getApkFile(@Url String url);
}
