package com.music.voroshilo.networking.request;

import android.support.annotation.NonNull;

import com.music.voroshilo.model.networking.Song;
import com.music.voroshilo.model.networking.SongsResponseBody;
import com.music.voroshilo.networking.ApiBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongRequest {
    public interface SongCallback {
        void onSuccess(List<Song> list);

        void onError(Throwable throwable);
    }

    public void requestSongs(String query, final SongCallback songCallback) {
        ApiBuilder.getMusicService().getSongsList(query).enqueue(new Callback<SongsResponseBody>() {
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
