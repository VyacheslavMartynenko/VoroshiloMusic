package free.mp3.test.networking;

import free.mp3.test.model.networking.SettingsBody;
import free.mp3.test.model.networking.SongsResponseBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiService {
    @GET("settings.php")
    Call<SettingsBody> getSettings(@Query("is_first") int isFirst);

    @GET
    Call<SongsResponseBody> getSongsList(@Url String url, @Query("q") String query, @Query("offset") int offset, @Query("limit") int limit);

    @GET("report-song.php")
    Call<ResponseBody> reportSong(@Query("fullname") String fullName, @Query("message") String message);

}
