package mp3.music.download.downloadmp3.downloadmusic.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
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

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import mp3.music.download.downloadmp3.downloadmusic.R;
import mp3.music.download.downloadmp3.downloadmusic.adapter.SongsRecycleViewAdapter;
import mp3.music.download.downloadmp3.downloadmusic.dialog.PolicyDialogFragment;
import mp3.music.download.downloadmp3.downloadmusic.dialog.RatingDialogFragment;
import mp3.music.download.downloadmp3.downloadmusic.interfaces.SongListener;
import mp3.music.download.downloadmp3.downloadmusic.model.networking.DataBody;
import mp3.music.download.downloadmp3.downloadmusic.model.networking.Song;
import mp3.music.download.downloadmp3.downloadmusic.networking.NetworkBuilder;
import mp3.music.download.downloadmp3.downloadmusic.networking.request.SongRequest;
import mp3.music.download.downloadmp3.downloadmusic.networking.task.ApkDownloadTask;
import mp3.music.download.downloadmp3.downloadmusic.networking.task.SongDownloadTask;
import mp3.music.download.downloadmp3.downloadmusic.service.MusicFirebaseMessagingService;
import mp3.music.download.downloadmp3.downloadmusic.util.EndlessRecyclerViewScrollListener;
import mp3.music.download.downloadmp3.downloadmusic.util.KeyboardUtil;
import mp3.music.download.downloadmp3.downloadmusic.util.SongIconChanger;
import mp3.music.download.downloadmp3.downloadmusic.util.SongPlayer;
import mp3.music.download.downloadmp3.downloadmusic.util.preferences.UserPreferences;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends BaseActivity implements SongListener {
    private SongDownloadTask songDownloadTask = new SongDownloadTask();
    private SongsRecycleViewAdapter songAdapter = new SongsRecycleViewAdapter(this, new ArrayList<>());
    private SongPlayer player;
    private String token;

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

    @OnEditorAction(R.id.search_edit_text)
    public boolean searchSongsWithDone() {
        searchSongs();
        return true;
    }

    @OnClick(R.id.search_button)
    public void searchSongs() {
        String searchText = searchEditText.getText().toString();
        requestSongs(searchText);
        KeyboardUtil.hideKeyboard(this);
        if (UserPreferences.getInstance().getAdNetSearch() != DataBody.NO) {
            showAd();
        }
        Answers.getInstance().logCustom(new CustomEvent("Search Song").putCustomAttribute("Song name", searchText));
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
                startPolicyActivity(NetworkBuilder.PRIVACY_URL);
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
                Answers.getInstance().logCustom(new CustomEvent("Pagination").putCustomAttribute("Song count", totalItemsCount));
            }
        };
        recyclerView.setAdapter(songAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerView.addOnScrollListener(scrollListener);


        player = new SongPlayer(songSeekBar);
        requestSettings();

        Intent intent = getIntent();
        if (intent != null && intent.getAction() != null && intent.getAction().equals(MusicFirebaseMessagingService.FIREBASE_ACTION)) {
            handleIntent(intent);
        }
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

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent != null && intent.getAction().equals(MusicFirebaseMessagingService.FIREBASE_ACTION)) {
            handleIntent(intent);
        } else {
            super.onNewIntent(intent);
        }
    }

    private void handleIntent(Intent intent) {
        boolean isUpdate = intent.getBooleanExtra(MusicFirebaseMessagingService.IS_UPDATE, false);
        if (isUpdate) {
            String packageName = intent.getStringExtra(MusicFirebaseMessagingService.PACKAGE_NAME);
            if (packageName != null) {
                Intent updateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(updateIntent);
                Answers.getInstance().logCustom(new CustomEvent("Update notification"));
            }
        } else {
            Answers.getInstance().logCustom(new CustomEvent("Info notification"));
        }
    }

    private void requestSongs(String query) {
        progressBar.setVisibility(View.VISIBLE);
        SongRequest.requestSongs(query, 0, null, new SongRequestCallback(this, SongRequestCallback.REQUEST_SONGS));
    }

    private void requestMoreSongs(String query) {
        progressBarMore.setVisibility(View.VISIBLE);
        SongRequest.requestSongs(query, songAdapter.getOffset(), token, new SongRequestCallback(this, SongRequestCallback.REQUEST_MORE_SONGS));
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
                    TextView textView = findViewById(R.id.download_text);
                    textView.setText(text);
                }
                Answers.getInstance().logCustom(new CustomEvent("Burst full screen"));
                break;
            case DataBody.MUSIC_AND_BUTTON:
                recyclerView.setVisibility(View.VISIBLE);
                burstContainer.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams newParams = (RelativeLayout.LayoutParams) burstContainer.getLayoutParams();
                newParams.addRule(RelativeLayout.CENTER_IN_PARENT, 0);
                newParams.addRule(RelativeLayout.BELOW, R.id.search_edit_text);
                String burstText = UserPreferences.getInstance().getBurstText();
                if (burstText != null) {
                    TextView textView = findViewById(R.id.download_text);
                    textView.setText(burstText);
                }
                requestSongs("");
                Answers.getInstance().logCustom(new CustomEvent("Burst on top"));
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
    public boolean updateCurrentSongInfo(String url, String imageUrl, boolean isCache) {
        if (currentSongContainer.getVisibility() != View.VISIBLE) {
            currentSongContainer.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams newParams = (RelativeLayout.LayoutParams) progressBarMore.getLayoutParams();
            newParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
            newParams.addRule(RelativeLayout.ABOVE, R.id.current_song_container);
        }
        boolean isPlaying = player.playOrPauseSong(url);
        SongIconChanger.switchDrawable(getApplicationContext(), playButton, isPlaying);
        if (!isCache) {
            SongIconChanger.loadDrawableWithPicasso(getApplicationContext(), coverImage, imageUrl);
        }
        if (isPlaying && downloadProgressBar.getVisibility() == View.VISIBLE && !SongDownloadTask.isDownloading()) {
            downloadProgressBar.setProgress(SongDownloadTask.INITIAL_PROGRESS);
        }
        return isPlaying;
    }

    @Override
    public void downloadSong(final String imageUrl, final String mp3Url, final String title) {
        MainActivityPermissionsDispatcher.downloadIfPermissionGrantedWithCheck(this, imageUrl, mp3Url, title);
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
        Answers.getInstance().logCustom(new CustomEvent("Download Song").putCustomAttribute("Song name", title));
    }

    @Override
    public void reportSong(String songName, String videoId) {
        if (MainActivity.this.isVisible()) {
            PolicyDialogFragment policyDialogFragment = PolicyDialogFragment.newInstance(songName, videoId);
            policyDialogFragment.show(getSupportFragmentManager(), "report");
            Answers.getInstance().logCustom(new CustomEvent("Report click").putCustomAttribute("Song name", songName));
        }
    }

    @Override
    public void showPrivacy(String url) {
        startPolicyActivity(url);
        Answers.getInstance().logCustom(new CustomEvent("Policy show"));
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

    private static class SongRequestCallback implements SongRequest.SongCallback {
        private static final int REQUEST_SONGS = 0;
        private static final int REQUEST_MORE_SONGS = 1;

        private WeakReference<MainActivity> mainActivityWeakReference;
        private int type;

        SongRequestCallback(MainActivity mainActivity, int type) {
            this.mainActivityWeakReference = new WeakReference<>(mainActivity);
            this.type = type;
        }

        @Override
        public void onSuccess(List<Song> list, String token) {
            MainActivity mainActivity = mainActivityWeakReference.get();
            if (mainActivity != null) {
                if (type == REQUEST_SONGS) {
                    mainActivity.progressBar.setVisibility(View.GONE);
                    mainActivity.songAdapter.updateSongList(list);
                } else if (type == REQUEST_MORE_SONGS) {
                    mainActivity.progressBarMore.setVisibility(View.GONE);
                    mainActivity.songAdapter.addSongList(list);
                }
                mainActivity.token = token;
            }
        }

        @Override
        public void onError(Throwable throwable) {
            MainActivity mainActivity = mainActivityWeakReference.get();
            if (mainActivity != null) {
                mainActivity.progressBar.setVisibility(View.GONE);
            }
        }
    }
}