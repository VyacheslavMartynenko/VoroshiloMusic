package com.music.voroshilo.inerface;

public interface CurrentSongListener {
    public boolean updateCurrentSongInfo(String text);

    public boolean isPlaying();
}
