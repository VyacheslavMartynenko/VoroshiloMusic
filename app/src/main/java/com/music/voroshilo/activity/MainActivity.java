package com.music.voroshilo.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.music.voroshilo.R;
import com.music.voroshilo.adapter.SongsRecycleViewAdapter;
import com.music.voroshilo.inerface.CurrentSongListener;
import com.music.voroshilo.model.networking.Song;
import com.music.voroshilo.networking.request.SongRequest;
import com.music.voroshilo.util.SongPlayer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements CurrentSongListener {
    private SongsRecycleViewAdapter songAdapter = new SongsRecycleViewAdapter(this, new ArrayList<Song>());
    private SongPlayer player;

    @BindView(R.id.song_seek_bar)
    SeekBar songSeekBar;

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

    @OnClick(R.id.search_button)
    public void searchSongs() {
        requestSongs(searchEditText.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView.setAdapter(songAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        player = new SongPlayer(new MediaPlayer(), songSeekBar);
        searchSongs();
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

    @Override
    public boolean updateCurrentSongInfo(String url) {
        if (currentSongContainer.getVisibility() != View.VISIBLE) {
            currentSongContainer.setVisibility(View.VISIBLE);
        }
        boolean isPlaying = player.playOrPauseSong(url);
        if (isPlaying) {
            playButton.setImageDrawable(ContextCompat
                    .getDrawable(getApplicationContext(), R.drawable.ic_pause_black_24dp));
        } else {
            playButton.setImageDrawable(ContextCompat
                    .getDrawable(getApplicationContext(), R.drawable.ic_play_arrow_black_24dp));
        }
        return isPlaying;
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }
}