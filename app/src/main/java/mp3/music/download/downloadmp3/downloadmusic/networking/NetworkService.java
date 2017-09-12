package mp3.music.download.downloadmp3.downloadmusic.networking;

import mp3.music.download.downloadmp3.downloadmusic.model.networking.SettingsBody;
import mp3.music.download.downloadmp3.downloadmusic.model.networking.SongsResponseBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface NetworkService {
    @GET
    Call<SettingsBody> getSettings(@Url String url, @Query("dev_id") String deviceId, @Query("gcm_token") String gcmToken);

    @GET
    Call<SongsResponseBody> getSongsList(@Url String url, @Query("q") String query, @Query("limit") int limit, @Query("page_token") String token);

    @GET()
    Call<ResponseBody> reportSong(@Url String url, @Query("dev_id") String deviceId, @Query("fullname") String fullName, @Query("msg") String message, @Query("video_id") String videoId);

}
