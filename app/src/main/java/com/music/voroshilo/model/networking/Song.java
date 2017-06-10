package com.music.voroshilo.model.networking;

import com.google.gson.annotations.SerializedName;

public class Song {
    @SerializedName("title")
    String title;

    @SerializedName("img_small")
    String imageUrl;

    @SerializedName("mp3Url")
    String mp3Url;

    public Song(String title, String imageUrl, String mp3Url) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.mp3Url = mp3Url;
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
}
