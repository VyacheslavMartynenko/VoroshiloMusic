package mp3.music.download.downloadmp3.downloadmusic.model.networking;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SongsResponseBody {
    @SerializedName("videos")
    private List<Song> songsList;

    @SerializedName("next_page_token")
    private String nextPageToken;

    public SongsResponseBody(List<Song> songsList, String nextPageToken) {
        this.songsList = songsList;
        this.nextPageToken = nextPageToken;
    }

    public List<Song> getSongsList() {
        return songsList;
    }

    public void setSongsList(List<Song> songsList) {
        this.songsList = songsList;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }
}
