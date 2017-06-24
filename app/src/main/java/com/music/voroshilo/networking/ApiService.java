package com.music.voroshilo.networking;

import com.music.voroshilo.model.networking.SettingsBody;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("api/v1/newgeneration/settings")
    Call<SettingsBody> getSettings();
}
