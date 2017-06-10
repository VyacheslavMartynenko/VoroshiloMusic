package com.music.voroshilo.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;

import com.music.voroshilo.R;
import com.music.voroshilo.adapter.SongsRecycleViewAdapter;
import com.music.voroshilo.model.networking.Song;
import com.music.voroshilo.model.networking.SongsResponseBody;
import com.music.voroshilo.networking.ApiBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {
    private SongsRecycleViewAdapter songAdapter = new SongsRecycleViewAdapter(new ArrayList<Song>());

    @BindView(R.id.main_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.search_edit_text)
    EditText searchEditText;

    @OnClick(R.id.search_button)
    public void searchSongs() {
        String searchText = searchEditText.getText().toString();
        requestSongs(searchText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView.setAdapter(songAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void requestSongs(String query) {
        ApiBuilder.getMusicService().getSongsList(query).enqueue(new Callback<SongsResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<SongsResponseBody> call, @NonNull Response<SongsResponseBody> response) {
                if (response.isSuccessful()) {
                    SongsResponseBody body = response.body();
                    if (body != null) {
                        List<Song> list = body.getSongsList();
                        if (list != null) {
                            songAdapter.updateSongList(list);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SongsResponseBody> call, @NonNull Throwable t) {
                Log.e("onResponse: ", Log.getStackTraceString(t));
            }
        });
    }
}
