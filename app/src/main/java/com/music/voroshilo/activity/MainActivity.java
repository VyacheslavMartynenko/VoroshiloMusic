package com.music.voroshilo.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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
    private SongPlayer player = new SongPlayer(new MediaPlayer());

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

        searchSongs();
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
    public void updateCurrentSongInfo(String url) {
        if (currentSongContainer.getVisibility() != View.VISIBLE) {
            currentSongContainer.setVisibility(View.VISIBLE);
        }
        player.playSong(url);
    }
}