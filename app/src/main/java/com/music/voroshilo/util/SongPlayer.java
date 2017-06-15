package com.music.voroshilo.util;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
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

    public SongPlayer(SeekBar seekBar) {
        this.seekBar = seekBar;
        preparePlayer();
    }

    private void preparePlayer() {
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mediaPlayer) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        mediaPlayer.start();
                        seekBar.setMax(mediaPlayer.getDuration());
                        startUpdatingSeekBar();
                    }
                });
            }
        });
        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                switch (i) {
                    case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                        startPlayer(currentUrl);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
    }

    private void startUpdatingSeekBar() {
        if (player.isPlaying()) {
            handler.postDelayed(runnable, SEEK_BAR_TIME_UPDATE);
        }
    }

    private void stopUpdatingSeekBar() {
        if (player.isPlaying()) {
            handler.removeCallbacks(runnable);
        }
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
        stopUpdatingSeekBar();
        seekBar.setProgress(SEEK_BAR_START_PROGRESS);
        player.reset();
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
            if (player.isPlaying()) {
                player.seekTo(seekBar.getProgress());
                startUpdatingSeekBar();
            }
        }
    };
}