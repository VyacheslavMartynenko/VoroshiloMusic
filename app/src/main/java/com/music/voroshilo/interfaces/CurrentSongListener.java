package com.music.voroshilo.interfaces;

public interface CurrentSongListener {
    public boolean updateCurrentSongInfo(String url, String imageUrl);

    public boolean isPlaying();
}
