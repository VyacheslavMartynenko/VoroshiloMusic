package com.music.voroshilo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.music.voroshilo.R;
import com.music.voroshilo.adapter.SongsRecycleViewAdapter;
import com.music.voroshilo.dialog.RatingDialogFragment;
import com.music.voroshilo.interfaces.CurrentSongListener;
import com.music.voroshilo.interfaces.RuntimePermissionListener;
import com.music.voroshilo.model.networking.DataBody;
import com.music.voroshilo.model.networking.Song;
import com.music.voroshilo.networking.request.SettingsRequest;
import com.music.voroshilo.networking.request.SongRequest;
import com.music.voroshilo.networking.task.FileDownloadTask;
import com.music.voroshilo.util.KeyboardUtil;
import com.music.voroshilo.util.PermissionUtil;
import com.music.voroshilo.util.SongIconChanger;
import com.music.voroshilo.util.SongPlayer;
import com.music.voroshilo.util.preferences.UserPreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements CurrentSongListener {
    private static final int WRITE_EXTERNAL_STORAGE_PERMISSION = 100;
    private RuntimePermissionListener permissionListener;
    private SongsRecycleViewAdapter songAdapter = new SongsRecycleViewAdapter(this, new ArrayList<Song>());
    private SongPlayer player;
    private String marketUrl;

    @BindView(R.id.song_seek_bar)
    SeekBar songSeekBar;

    @BindView(R.id.download_button)
    Button downloadButton;

    @BindView(R.id.main_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.search_edit_text)
    EditText searchEditText;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.current_song_container)
    LinearLayout currentSongContainer;

    @BindView(R.id.cover_image)
    ImageView coverImage;

    @BindView(R.id.play_button)
    ImageView playButton;

    @BindView(R.id.download_progress_bar)
    ProgressBar downloadProgressBar;

    @OnClick(R.id.search_button)
    public void searchSongs() {
        requestSongs(searchEditText.getText().toString());
        KeyboardUtil.hideKeyboard(this);
        showAd();
    }

    @OnClick(R.id.download_button)
    void redirectToDownload() {
        if (marketUrl != null) {
            if (!marketUrl.endsWith(".apk")) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(marketUrl)));
            } else {
                Log.e("DOWNLOAD", "Apk");
                //todo apk download
            }
        }
    }

    @OnClick(R.id.play_button)
    public void playOrPause() {
        songAdapter.playOrPauseSong();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView.setAdapter(songAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        player = new SongPlayer(songSeekBar);
        requestSettings();
    }

    @Override
    protected void onDestroy() {
        player.release();
        super.onDestroy();
    }

    private void requestSongs(String query) {
        progressBar.setVisibility(View.VISIBLE);
        new SongRequest().requestSongs(query, new SongRequest.SongCallback() {
            @Override
            public void onSuccess(List<Song> list) {
                progressBar.setVisibility(View.GONE);
                songAdapter.updateSongList(list);
            }

            @Override
            public void onError(Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                Log.e("onResponse: ", Log.getStackTraceString(throwable));
            }
        });
    }

    private void requestSettings() {
        new SettingsRequest().requestSettings(new SettingsRequest.SettingsCallback() {
            @Override
            public void onSuccess(DataBody data) {
                boolean isAppRated = UserPreferences.getInstance().isAppRated();
                if (MainActivity.this.isVisible() && !isAppRated && !isFirstLaunch()) {
                    RatingDialogFragment dialog = RatingDialogFragment.newInstance(data.getPopupUrl());
                    dialog.show(getSupportFragmentManager(), "rating");
                }
                marketUrl = data.getBurstUrl();
                @DataBody.DisplayMode
                int displayMode = data.getBurstStatus();
                switch (displayMode) {
                    case DataBody.BUTTON:
                        recyclerView.setVisibility(View.GONE);
                        downloadButton.setVisibility(View.VISIBLE);
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) downloadButton.getLayoutParams();
                        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                        params.addRule(RelativeLayout.BELOW, 0);
                        break;
                    case DataBody.MUSIC_AND_BUTTON:
                        recyclerView.setVisibility(View.VISIBLE);
                        downloadButton.setVisibility(View.VISIBLE);
                        RelativeLayout.LayoutParams newParams = (RelativeLayout.LayoutParams) downloadButton.getLayoutParams();
                        newParams.addRule(RelativeLayout.CENTER_IN_PARENT, 0);
                        newParams.addRule(RelativeLayout.BELOW, R.id.search_edit_text);
                        requestSongs("");
                        break;
                    case DataBody.MUSIC:
                        recyclerView.setVisibility(View.VISIBLE);
                        downloadButton.setVisibility(View.GONE);
                        requestSongs("");
                        break;
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Log.e("onResponse: ", Log.getStackTraceString(throwable));
            }
        });
    }

    private boolean isFirstLaunch() {
        boolean isFirstRun = UserPreferences.getInstance().isFirstLaunch();
        if (isFirstRun) {
            UserPreferences.getInstance().setIsFirstLaunch();
        }
        return isFirstRun;
    }

    @Override
    public boolean updateCurrentSongInfo(String url, String imageUrl) {
        if (currentSongContainer.getVisibility() != View.VISIBLE) {
            currentSongContainer.setVisibility(View.VISIBLE);
        }
        boolean isPlaying = player.playOrPauseSong(url);
        SongIconChanger.switchDrawable(getApplicationContext(), playButton, isPlaying);
        SongIconChanger.loadDrawableWithPicasso(getApplicationContext(), coverImage, imageUrl);
        if (isPlaying && downloadProgressBar.getVisibility() == View.VISIBLE && !FileDownloadTask.isDownloading()) {
            downloadProgressBar.setProgress(FileDownloadTask.INITIAL_PROGRESS);
        }
        return isPlaying;
    }

    @Override
    public void downloadSong(final String imageUrl, final String mp3Url, final String title) {
        permissionListener = new RuntimePermissionListener() {
            @Override
            public void onGranted() {
                if (currentSongContainer.getVisibility() != View.VISIBLE) {
                    currentSongContainer.setVisibility(View.VISIBLE);
                }
                if (downloadProgressBar.getVisibility() != View.VISIBLE) {
                    downloadProgressBar.setVisibility(View.VISIBLE);
                    if (!player.isPlaying()) {
                        SongIconChanger.loadDrawableWithPicasso(getApplicationContext(), coverImage, imageUrl);
                    }
                }
                FileDownloadTask.downloadFile(mp3Url, title, downloadProgressBar);
            }

            @Override
            public void onDenied() {
                Log.e(MainActivity.class.getSimpleName(), "Permission Denied");
            }
        };
        PermissionUtil.checkPermission(MainActivity.this, WRITE_EXTERNAL_STORAGE_PERMISSION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, permissionListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionListener.onGranted();
                } else {
                    permissionListener.onDenied();
                }
                permissionListener = null;
            }
        }
    }
}