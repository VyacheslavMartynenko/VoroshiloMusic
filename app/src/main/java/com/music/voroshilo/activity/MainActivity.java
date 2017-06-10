package com.music.voroshilo.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.music.voroshilo.R;
import com.music.voroshilo.model.networking.SongsResponseBody;
import com.music.voroshilo.networking.ApiBuilder;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.search_edit_text)
    EditText searchEditText;

    @OnClick(R.id.search_button)
    public void searhSongs() {
        String searchText = searchEditText.getText().toString();
        requestSongs(searchText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void requestSongs(String query) {
        ApiBuilder.getMusicService().getSongsList(query).enqueue(new Callback<SongsResponseBody>() {
            @Override
            public void onResponse(Call<SongsResponseBody> call, Response<SongsResponseBody> response) {
                Log.e("onResponse: ", String.valueOf(response.code()));
                if (response.isSuccessful()) {
                    Log.e("onResponse: ", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<SongsResponseBody> call, Throwable t) {
                Log.e("onResponse: ", Log.getStackTraceString(t));
            }
        });
    }
}
