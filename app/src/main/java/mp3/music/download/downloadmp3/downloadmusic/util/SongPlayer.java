package mp3.music.download.downloadmp3.downloadmusic.util;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import mp3.music.download.downloadmp3.downloadmusic.R;
import mp3.music.download.downloadmp3.downloadmusic.activity.BaseActivity;
import mp3.music.download.downloadmp3.downloadmusic.application.MusicApplication;

public class SongPlayer {
    //todo service
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
        player.setOnPreparedListener(mediaPlayer -> new Handler(Looper.getMainLooper()).post(() -> {
            BaseActivity activity = MusicApplication.getInstance().getCurrentActivity();
            if (activity != null && activity.isVisible()) {
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                startUpdatingSeekBar();
            }
        }));
        player.setOnErrorListener((mediaPlayer, i, i1) -> {
            switch (i) {
                case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                    startPlayer(currentUrl);
                    break;
                default:
                    break;
            }
            return false;
        });
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
    }

    public void startUpdatingSeekBar() {
        if (isPlaying()) {
            handler.postDelayed(runnable, SEEK_BAR_TIME_UPDATE);
        }
    }

    public void stopUpdatingSeekBar() {
        if (isPlaying()) {
            handler.removeCallbacks(runnable);
        }
    }

    public boolean isPlaying() {
        return player.isPlaying();
    }

    public boolean playOrPauseSong(String url) {
        if (!currentUrl.equals(url)) {
            startPlayer(url);
            return true;
        } else {
            if (isPlaying()) {
                pausePlayer();
                return false;
            } else {
                resumePlayer();
                return true;
            }
        }
    }

    private void startPlayer(String url) {
        try {
            currentUrl = url;

            player.reset();
            player.setDataSource(url);
            player.prepareAsync();
        } catch (Exception e) {
            BaseActivity baseActivity = MusicApplication.getInstance().getCurrentActivity();
            if (baseActivity != null && baseActivity.isVisible()) {
                Toast.makeText(baseActivity, R.string.download_error_message, Toast.LENGTH_LONG).show();
            }
            Log.e(SongPlayer.class.getSimpleName(), Log.getStackTraceString(e));
            Crashlytics.logException(e);
        }
    }

    private void pausePlayer() {
        stopUpdatingSeekBar();
        player.pause();
    }

    private void resumePlayer() {
        player.seekTo(seekBar.getProgress());
        player.start();
        startUpdatingSeekBar();
    }

    public void release() {
        player.release();
        player = null;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (player != null) {
                int progress = player.getCurrentPosition();
                seekBar.setProgress(progress);
                handler.postDelayed(this, SEEK_BAR_TIME_UPDATE);
            }
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
            if (isPlaying()) {
                player.seekTo(seekBar.getProgress());
                startUpdatingSeekBar();
            }
        }
    };
}