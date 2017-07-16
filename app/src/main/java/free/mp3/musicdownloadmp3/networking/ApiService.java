package free.mp3.musicdownloadmp3.networking;

import free.mp3.musicdownloadmp3.model.networking.SettingsBody;
import free.mp3.musicdownloadmp3.model.networking.SongsResponseBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("settings.php")
    Call<SettingsBody> getSettings();

    @GET("search.php")
    Call<SongsResponseBody> getSongsList(@Query("q") String query, @Query("offset") int offset, @Query("limit") int limit);

    @GET("report-song.php")
    Call<ResponseBody> reportSong(@Query("fullname") String fullName, @Query("message") String message);

}
