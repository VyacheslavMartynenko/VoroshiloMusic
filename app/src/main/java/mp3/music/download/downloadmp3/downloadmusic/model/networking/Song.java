package mp3.music.download.downloadmp3.downloadmusic.model.networking;

import com.google.gson.annotations.SerializedName;

public class Song {
    @SerializedName("title")
    private String title;

    @SerializedName("img_small")
    private String imageUrl;

    @SerializedName("mp3Url")
    private String mp3Url;

    @SerializedName("videoId")
    private String videoId;

    private boolean isSelected;

    public Song(String title, String imageUrl, String mp3Url, String videoId) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.mp3Url = mp3Url;
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMp3Url() {
        return mp3Url;
    }

    public void setMp3Url(String mp3Url) {
        this.mp3Url = mp3Url;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
