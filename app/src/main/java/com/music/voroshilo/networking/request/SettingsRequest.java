package com.music.voroshilo.networking.request;

import android.support.annotation.NonNull;

import com.music.voroshilo.model.networking.DataBody;
import com.music.voroshilo.model.networking.SettingsBody;
import com.music.voroshilo.networking.ApiBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsRequest {
    public interface SettingsCallback {
        void onSuccess();

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
                            callback.onSuccess();
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
