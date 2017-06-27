package com.music.voroshilo.interfaces;

public interface CurrentSongListener {
    boolean updateCurrentSongInfo(String url, String imageUrl);

    void downloadSong(String imageUrl, String mp3Url, String title);

    void reportSong();
}
