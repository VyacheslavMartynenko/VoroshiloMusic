package com.music.voroshilo.interfaces;

public interface CurrentSongListener {
    public boolean updateCurrentSongInfo(String text);

    public boolean isPlaying();
}
