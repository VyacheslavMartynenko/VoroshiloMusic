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
    Call<SettingsBody> getSettings(@Url String url, @Query("is_first") int isFirst);

    @GET
    Call<SongsResponseBody> getSongsList(@Url String url, @Query("q") String query, @Query("offset") int offset, @Query("limit") int limit);

    @GET("report-song")
    Call<ResponseBody> reportSong(@Query("fullname") String fullName, @Query("message") String message);

}
