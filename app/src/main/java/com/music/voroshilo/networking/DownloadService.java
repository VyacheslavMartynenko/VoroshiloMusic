package com.music.voroshilo.networking;

import com.music.voroshilo.networking.repsonse.ProgressResponseBody;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface DownloadService {
    @Streaming
    @GET
    Call<ProgressResponseBody> getFile(@Url String url);

}
