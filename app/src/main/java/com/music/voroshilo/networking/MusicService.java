package com.music.voroshilo.networking;

import com.music.voroshilo.model.networking.SongsResponseBody;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MusicService {
    @GET("search.php")
    Call<SongsResponseBody> getSongsList(@Query("q") String query);
}
