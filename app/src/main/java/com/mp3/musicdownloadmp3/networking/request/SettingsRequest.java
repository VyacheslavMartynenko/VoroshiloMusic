package com.mp3.musicdownloadmp3.networking.request;

import android.support.annotation.NonNull;

import com.mp3.musicdownloadmp3.model.networking.DataBody;
import com.mp3.musicdownloadmp3.model.networking.SettingsBody;
import com.mp3.musicdownloadmp3.networking.ApiBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsRequest {
    public interface SettingsCallback {
        void onSuccess(DataBody data);

        void onError(Throwable throwable);
    }

    public void requestSettings(final SettingsCallback callback) {
        ApiBuilder.getApiService().getSettings().enqueue(new Callback<SettingsBody>() {
            @Override
            public void onResponse(@NonNull Call<SettingsBody> call, @NonNull Response<SettingsBody> response) {
                if (response.isSuccessful()) {
                    SettingsBody settingsBody = response.body();
                    if (settingsBody != null) {
                        DataBody dataBody = settingsBody.getData();
                        if (dataBody.getPopup() != 0) {
                            callback.onSuccess(dataBody);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SettingsBody> call, @NonNull Throwable t) {
                callback.onError(t);
            }
        });
    }
}

