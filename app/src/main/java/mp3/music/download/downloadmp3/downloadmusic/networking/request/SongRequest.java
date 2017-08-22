package mp3.music.download.downloadmp3.downloadmusic.networking.request;

import android.support.annotation.NonNull;

import java.util.List;

import mp3.music.download.downloadmp3.downloadmusic.model.networking.Song;
import mp3.music.download.downloadmp3.downloadmusic.model.networking.SongsResponseBody;
import mp3.music.download.downloadmp3.downloadmusic.networking.ApiBuilder;
import mp3.music.download.downloadmp3.downloadmusic.util.preferences.UserPreferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongRequest {
    public static final int LIMIT = 20;

    public interface SongCallback {
        void onSuccess(List<Song> list);

        void onError(Throwable throwable);
    }

    public static void requestSongs(String query, int offset, final SongCallback songCallback) {
        String url = UserPreferences.getInstance().getMusicUrl();
        Call<SongsResponseBody> call;
        if (query != null && !query.equals("")) {
            call = ApiBuilder.getApiService().getSongsList(url, query, offset, LIMIT);
        } else {
            call = ApiBuilder.getApiService().getSongsList(url, null, offset, LIMIT);
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
                        } else {
                            songCallback.onError(new NullPointerException());
                        }
                    } else {
                        songCallback.onError(new NullPointerException());
                    }
                } else {
                    songCallback.onError(new Exception(String.valueOf(response.code())));
                }
            }

            @Override
            public void onFailure(@NonNull Call<SongsResponseBody> call, @NonNull Throwable t) {
                songCallback.onError(t);
            }
        });
    }
}
