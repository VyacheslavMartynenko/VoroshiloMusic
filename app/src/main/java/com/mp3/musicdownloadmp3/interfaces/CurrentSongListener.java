package com.mp3.musicdownloadmp3.interfaces;

public interface CurrentSongListener {
    boolean updateCurrentSongInfo(String url, String imageUrl);

    void downloadSong(String imageUrl, String mp3Url, String title);

    void reportSong(String songName, String videoId);

    void showPrivacy(String url);
}
