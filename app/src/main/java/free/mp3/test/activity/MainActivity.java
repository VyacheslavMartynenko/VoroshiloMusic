package free.mp3.test.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import free.mp3.test.R;
import free.mp3.test.adapter.SongsRecycleViewAdapter;
import free.mp3.test.dialog.RatingDialogFragment;
import free.mp3.test.dialog.ReportDialogFragment;
import free.mp3.test.interfaces.CurrentSongListener;
import free.mp3.test.model.networking.DataBody;
import free.mp3.test.model.networking.Song;
import free.mp3.test.networking.ApiBuilder;
import free.mp3.test.networking.request.SongRequest;
import free.mp3.test.networking.task.ApkDownloadTask;
import free.mp3.test.networking.task.SongDownloadTask;
import free.mp3.test.util.EndlessRecyclerViewScrollListener;
import free.mp3.test.util.KeyboardUtil;
import free.mp3.test.util.SongIconChanger;
import free.mp3.test.util.SongPlayer;
import free.mp3.test.util.preferences.UserPreferences;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends BaseActivity implements CurrentSongListener {
    private SongDownloadTask songDownloadTask = new SongDownloadTask();
    private SongsRecycleViewAdapter songAdapter = new SongsRecycleViewAdapter(this, new ArrayList<>());
    private SongPlayer player;

    @BindView(R.id.full_container)
    RelativeLayout fullContainer;

    @BindView(R.id.main_content_container)
    RelativeLayout mainContentContainer;

    @BindView(R.id.song_seek_bar)
    SeekBar songSeekBar;

    @BindView(R.id.burst_container)
    LinearLayout burstContainer;

    @BindView(R.id.main_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.search_edit_text)
    EditText searchEditText;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.progress_bar_more)
    ProgressBar progressBarMore;

    @BindView(R.id.current_song_container)
    CardView currentSongContainer;

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
        if (UserPreferences.getInstance().getAdNetSearch() != DataBody.NO) {
            showAd();
        }
    }

    @OnClick(R.id.download_button)
    void redirectToDownload() {
        String marketUrl = UserPreferences.getInstance().getBurstUrl();
        if (marketUrl != null) {
            if (!marketUrl.endsWith(".apk")) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(marketUrl)));
            } else {
                new ApkDownloadTask().downloadFile(marketUrl);
            }
        }
    }

    @OnClick(R.id.play_button)
    public void playOrPause() {
        songAdapter.playOrPauseSong();
        if (UserPreferences.getInstance().getAdNetPlay() != DataBody.NO) {
            showAd();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.policy:
                startPolicyActivity(ApiBuilder.PRIVACY_URL);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                requestMoreSongs(searchEditText.getText().toString());
            }
        };
        recyclerView.setAdapter(songAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.addOnScrollListener(scrollListener);


        player = new SongPlayer(songSeekBar);
        requestSettings();
    }

    @Override
    protected void onResume() {
        super.onResume();
        player.startUpdatingSeekBar();
    }

    @Override
    protected void onPause() {
        player.stopUpdatingSeekBar();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        player.release();
        super.onDestroy();
    }

    private void requestSongs(String query) {
        progressBar.setVisibility(View.VISIBLE);
        SongRequest.requestSongs(query, 0, new SongRequest.SongCallback() {
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

    private void requestMoreSongs(String query) {
        progressBarMore.setVisibility(View.VISIBLE);
        SongRequest.requestSongs(query, songAdapter.getOffset(), new SongRequest.SongCallback() {
            @Override
            public void onSuccess(List<Song> list) {
                progressBarMore.setVisibility(View.GONE);
                songAdapter.addSongList(list);
            }

            @Override
            public void onError(Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                Log.e("onResponse: ", Log.getStackTraceString(throwable));
            }
        });
    }

    private void requestSettings() {
        @DataBody.DisplayMode
        int displayMode = UserPreferences.getInstance().getBurstStatus();
        switch (displayMode) {
            case DataBody.BUTTON:
                recyclerView.setVisibility(View.GONE);
                burstContainer.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) burstContainer.getLayoutParams();
                params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                params.addRule(RelativeLayout.BELOW, 0);
                String text = UserPreferences.getInstance().getBurstText();
                if (text != null) {
                    TextView textView = ButterKnife.findById(burstContainer, R.id.download_text);
                    textView.setText(text);
                }
                break;
            case DataBody.MUSIC_AND_BUTTON:
                recyclerView.setVisibility(View.VISIBLE);
                burstContainer.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams newParams = (RelativeLayout.LayoutParams) burstContainer.getLayoutParams();
                newParams.addRule(RelativeLayout.CENTER_IN_PARENT, 0);
                newParams.addRule(RelativeLayout.BELOW, R.id.search_edit_text);
                String burstText = UserPreferences.getInstance().getBurstText();
                if (burstText != null) {
                    TextView textView = ButterKnife.findById(burstContainer, R.id.download_text);
                    textView.setText(burstText);
                }
                requestSongs("");
                break;
            case DataBody.MUSIC:
                recyclerView.setVisibility(View.VISIBLE);
                burstContainer.setVisibility(View.GONE);
                requestSongs("");
                break;
        }

        if (UserPreferences.getInstance().getPopupStatus() != DataBody.NO) {
            boolean isAppRated = UserPreferences.getInstance().isAppRated();
            if (MainActivity.this.isVisible() && !isAppRated) {
                String popUpUrl = UserPreferences.getInstance().getPopupUrl();
                RatingDialogFragment dialog = RatingDialogFragment.newInstance(popUpUrl);
                dialog.show(getSupportFragmentManager(), "rating");
            }
        }

        showBanner(mainContentContainer, fullContainer);
    }

    @Override
    public boolean updateCurrentSongInfo(String url, String imageUrl) {
        if (currentSongContainer.getVisibility() != View.VISIBLE) {
            currentSongContainer.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams newParams = (RelativeLayout.LayoutParams) progressBarMore.getLayoutParams();
            newParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
            newParams.addRule(RelativeLayout.ABOVE, R.id.current_song_container);
        }
        boolean isPlaying = player.playOrPauseSong(url);
        SongIconChanger.switchDrawable(getApplicationContext(), playButton, isPlaying);
        SongIconChanger.loadDrawableWithPicasso(getApplicationContext(), coverImage, imageUrl);
        if (isPlaying && downloadProgressBar.getVisibility() == View.VISIBLE && !SongDownloadTask.isDownloading()) {
            downloadProgressBar.setProgress(SongDownloadTask.INITIAL_PROGRESS);
        }
        return isPlaying;
    }

    @Override
    public void downloadSong(final String imageUrl, final String mp3Url, final String title) {
        downloadIfPermissionGranted(imageUrl, mp3Url, title);
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void downloadIfPermissionGranted(final String imageUrl, final String mp3Url, final String title) {
        if (currentSongContainer.getVisibility() != View.VISIBLE) {
            currentSongContainer.setVisibility(View.VISIBLE);
        }
        if (downloadProgressBar.getVisibility() != View.VISIBLE) {
            downloadProgressBar.setVisibility(View.VISIBLE);
            if (!player.isPlaying()) {
                SongIconChanger.loadDrawableWithPicasso(getApplicationContext(), coverImage, imageUrl);
            }
        }
        songDownloadTask.downloadFile(mp3Url, title, downloadProgressBar);
    }

    @Override
    public void reportSong(String songName, String videoId) {
        if (MainActivity.this.isVisible()) {
            ReportDialogFragment reportDialogFragment = ReportDialogFragment.newInstance(songName, videoId);
            reportDialogFragment.show(getSupportFragmentManager(), "report");
        }
    }

    @Override
    public void showPrivacy(String url) {
        startPolicyActivity(url);
    }

    public void startPolicyActivity(String url) {
        Intent intent = new Intent(this, PolicyActivity.class);
        intent.putExtra(PolicyActivity.URL, url);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}