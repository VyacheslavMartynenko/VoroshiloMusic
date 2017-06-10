package com.music.voroshilo.util;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

public class SongPlayer {
    private MediaPlayer player;

    public SongPlayer(MediaPlayer player) {
        this.player = player;
        preparePlayer();
    }

    private void preparePlayer() {
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
    }

    public void playSong(String url) {
        try {
            player.setDataSource(url);
            player.prepareAsync();
        } catch (IOException e) {
            Log.e("onPlay: ", Log.getStackTraceString(e));
        }
    }

    public void pausePlayer() {
        player.pause();
    }

    public void stopPlayer() {
        player.stop();
    }

    public void release() {
        player.release();
    }
}
