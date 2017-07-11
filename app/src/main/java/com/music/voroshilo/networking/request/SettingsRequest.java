//package com.music.voroshilo.networking.request;
//
//import android.os.StrictMode;
//import android.support.annotation.NonNull;
//import android.util.Log;
//
//import com.music.voroshilo.model.networking.DataBody;
//import com.music.voroshilo.model.networking.SettingsBody;
//import com.music.voroshilo.networking.ApiBuilder;
//
//import java.io.IOException;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class SettingsRequest {
//    public interface SettingsCallback {
//        void onSuccess(DataBody data);
//
//        void onError(Throwable throwable);
//    }
//
//    public void requestSettings(final SettingsCallback callback) {
//        ApiBuilder.getApiService().getSettings().enqueue(new Callback<SettingsBody>() {
//            @Override
//            public void onResponse(@NonNull Call<SettingsBody> call, @NonNull Response<SettingsBody> response) {
//                if (response.isSuccessful()) {
//                    SettingsBody settingsBody = response.body();
//                    if (settingsBody != null) {
//                        DataBody dataBody = settingsBody.getData();
//                        if (dataBody.getPopup() != 0) {
//                            callback.onSuccess(dataBody);
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<SettingsBody> call, @NonNull Throwable t) {
//                callback.onError(t);
//            }
//        });
//    }
//}

package com.music.voroshilo.networking.request;

        import android.os.StrictMode;
        import android.util.Log;

        import com.music.voroshilo.model.networking.DataBody;
        import com.music.voroshilo.model.networking.SettingsBody;
        import com.music.voroshilo.networking.ApiBuilder;

        import java.io.IOException;

        import retrofit2.Response;

public class SettingsRequest {
    public interface SettingsCallback {
        void onSuccess(DataBody data);

        void onError(Throwable throwable);
    }

    public void requestSettings(final SettingsCallback callback) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Response<SettingsBody> response = ApiBuilder.getApiService().getSettings().execute();
            SettingsBody settingsBody = response.body();
            if (settingsBody != null) {
                DataBody dataBody = settingsBody.getData();
                if (dataBody.getPopup() != 0) {
                    callback.onSuccess(dataBody);
                }
            } else {
                callback.onError(new Error("Empty body"));
            }
        } catch (IOException e) {
            callback.onError(e);
            Log.e(SettingsRequest.class.getSimpleName(), Log.getStackTraceString(e));
        }
    }
}

