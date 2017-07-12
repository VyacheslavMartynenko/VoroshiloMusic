package free.mp3.musicdownloadmp3.model.networking;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SongsResponseBody {
    @SerializedName("videos")
    List<Song> songsList;

    public SongsResponseBody(List<Song> songsList) {
        this.songsList = songsList;
    }

    public List<Song> getSongsList() {
        return songsList;
    }

    public void setSongsList(List<Song> songsList) {
        this.songsList = songsList;
    }
}
