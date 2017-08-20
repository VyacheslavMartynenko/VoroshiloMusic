package free.mp3.test.networking.request;

import android.support.annotation.NonNull;

import free.mp3.test.model.networking.DataBody;
import free.mp3.test.model.networking.SettingsBody;
import free.mp3.test.networking.ApiBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsRequest {
    public interface SettingsCallback {
        void onSuccess(DataBody data);

        void onError(Throwable throwable);
    }

    public void requestSettings(final int isFirstLaunch, final SettingsCallback callback) {
        ApiBuilder.getApiService().getSettings(isFirstLaunch).enqueue(new Callback<SettingsBody>() {
            @Override
            public void onResponse(@NonNull Call<SettingsBody> call, @NonNull Response<SettingsBody> response) {
                if (response.isSuccessful()) {
                    SettingsBody settingsBody = response.body();
                    if (settingsBody != null) {
                        DataBody dataBody = settingsBody.getData();
                        callback.onSuccess(dataBody);
                    } else {
                        callback.onError(new NullPointerException());
                    }
                } else {
                    callback.onError(new Exception(String.valueOf(response.code())));
                }
            }

            @Override
            public void onFailure(@NonNull Call<SettingsBody> call, @NonNull Throwable t) {
                callback.onError(t);
            }
        });
    }
}

