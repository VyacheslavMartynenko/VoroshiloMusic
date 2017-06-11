package com.music.voroshilo.util;

import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.widget.SeekBar;

import java.io.IOException;

public class SongPlayer {
    private static final int SEEK_BAR_START_PROGRESS = 0;
    private static final int SEEK_BAR_TIME_UPDATE = 1000;
    private String currentUrl = "";

    private Handler handler = new Handler();
    private MediaPlayer player;
    private SeekBar seekBar;

    public SongPlayer(MediaPlayer player, SeekBar seekBar) {
        this.player = player;
        this.seekBar = seekBar;
        preparePlayer();
    }

    private void preparePlayer() {
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                startUpdatingSeekBar();
            }
        });
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
    }

    public void startUpdatingSeekBar() {
        if (player.isPlaying()) {
            handler.postDelayed(runnable, SEEK_BAR_TIME_UPDATE);
        }
    }

    public void stopUpdatingSeekBar() {
        handler.removeCallbacks(runnable);
    }

    public boolean playOrPauseSong(String url) {
        if (!currentUrl.equals(url)) {
            startPlayer(url);
            return true;
        } else {
            pausePlayer();
            return false;
        }
    }

    private void startPlayer(String url) {
        try {
            currentUrl = url;

            player.reset();
            player.setDataSource(url);
            player.prepareAsync();
        } catch (IOException e) {
            Log.e("onPlay: ", Log.getStackTraceString(e));
        }
    }

    private void pausePlayer() {
        currentUrl = "";
        player.reset();
        stopUpdatingSeekBar();
        seekBar.setProgress(SEEK_BAR_START_PROGRESS);
    }

    public void release() {
        player.release();
        player = null;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            int progress = player.getCurrentPosition();
            seekBar.setProgress(progress);
            handler.postDelayed(this, SEEK_BAR_TIME_UPDATE);
        }
    };

    private SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            stopUpdatingSeekBar();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            player.seekTo(seekBar.getProgress());
            startUpdatingSeekBar();
        }
    };
}
