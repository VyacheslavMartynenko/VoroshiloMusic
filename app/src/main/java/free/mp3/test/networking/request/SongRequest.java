package free.mp3.test.networking.request;

import android.support.annotation.NonNull;

import free.mp3.test.model.networking.Song;
import free.mp3.test.model.networking.SongsResponseBody;
import free.mp3.test.networking.ApiBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongRequest {
    public static final int LIMIT = 20;

    public interface SongCallback {
        void onSuccess(List<Song> list);

        void onError(Throwable throwable);
    }

    public void requestSongs(String query, int offset, final SongCallback songCallback) {
        Call<SongsResponseBody> call;
        if (query != null && !query.equals("")) {
            call = ApiBuilder.getApiService().getSongsList(query, offset, LIMIT);
        } else {
            call = ApiBuilder.getApiService().getSongsList(null, offset, LIMIT);
        }
        call.enqueue(new Callback<SongsResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<SongsResponseBody> call, @NonNull Response<SongsResponseBody> response) {
                if (response.isSuccessful()) {
                    SongsResponseBody body = response.body();
                    if (body != null) {
                        List<Song> list = body.getSongsList();
                        if (list != null) {
                            songCallback.onSuccess(list);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SongsResponseBody> call, @NonNull Throwable t) {
                songCallback.onError(t);
            }
        });
    }
}
