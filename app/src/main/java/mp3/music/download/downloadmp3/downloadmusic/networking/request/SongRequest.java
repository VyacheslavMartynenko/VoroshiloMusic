package mp3.music.download.downloadmp3.downloadmusic.networking.request;

import android.support.annotation.NonNull;

import java.util.List;

import mp3.music.download.downloadmp3.downloadmusic.model.networking.Song;
import mp3.music.download.downloadmp3.downloadmusic.model.networking.SongsDataBody;
import mp3.music.download.downloadmp3.downloadmusic.model.networking.SongsResponseBody;
import mp3.music.download.downloadmp3.downloadmusic.networking.NetworkBuilder;
import mp3.music.download.downloadmp3.downloadmusic.util.preferences.UserPreferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongRequest {
    private static final int LIMIT = 20;

    public interface SongCallback {
        void onSuccess(List<Song> list, String token);

        void onError(Throwable throwable);
    }

    public static void requestSongs(String query, int offset, String token, final SongCallback songCallback) {
        String url = UserPreferences.getInstance().getMusicUrl();
        Call<SongsResponseBody> call;
        if (query != null && !query.equals("")) {
            call = NetworkBuilder.getApiService().getSongsList(url, query, offset, LIMIT, token);
        } else {
            call = NetworkBuilder.getApiService().getSongsList(url, null, offset, LIMIT, token);
        }
        call.enqueue(new Callback<SongsResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<SongsResponseBody> call, @NonNull Response<SongsResponseBody> response) {
                if (response.isSuccessful()) {
                    SongsResponseBody body = response.body();
                    if (body != null) {
                        SongsDataBody songsDataBody = body.getData();
                        if (songsDataBody != null) {
                            List<Song> list = songsDataBody.getSongsList();
                            if (list != null) {
                                songCallback.onSuccess(list, songsDataBody.getNextPageToken());
                            } else {
                                songCallback.onError(new NullPointerException());
                            }
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
