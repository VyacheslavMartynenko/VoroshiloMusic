package mp3.music.download.downloadmp3.downloadmusic.model.networking;

import com.google.gson.annotations.SerializedName;

public class SongsResponseBody {
    @SerializedName("result")
    private String result;

    @SerializedName("data")
    private SongsDataBody data;

    public SongsResponseBody(String result, SongsDataBody data) {
        this.result = result;
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public SongsDataBody getData() {
        return data;
    }

    public void setData(SongsDataBody data) {
        this.data = data;
    }
}
