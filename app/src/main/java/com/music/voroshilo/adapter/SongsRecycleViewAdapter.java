package com.music.voroshilo.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.music.voroshilo.R;
import com.music.voroshilo.application.MusicApplication;
import com.music.voroshilo.interfaces.CurrentSongListener;
import com.music.voroshilo.interfaces.ProgressListener;
import com.music.voroshilo.model.networking.Song;
import com.music.voroshilo.networking.ApiBuilder;
import com.music.voroshilo.networking.task.DownloadAsyncTask;
import com.music.voroshilo.util.SongIconChanger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class SongsRecycleViewAdapter extends RecyclerView.Adapter<SongsRecycleViewAdapter.SongViewHolder> {
    private List<Song> songList;
    private CurrentSongListener listener;
    private int currentPlayingSongPosition = RecyclerView.NO_POSITION;

    public void updateSongList(List<Song> newSongList) {
        songList.clear();
        songList.addAll(newSongList);
        currentPlayingSongPosition = RecyclerView.NO_POSITION;
        notifyDataSetChanged();
    }

    private void playOrPauseSong(int adapterPosition) {
        if (adapterPosition != RecyclerView.NO_POSITION) {
            Song song = songList.get(adapterPosition);
            boolean isSelected = listener.updateCurrentSongInfo(song.getMp3Url(), song.getImageUrl());
            song.setSelected(isSelected);
            notifyItemChanged(currentPlayingSongPosition);
            if (currentPlayingSongPosition != adapterPosition) {
                currentPlayingSongPosition = adapterPosition;
                notifyItemChanged(currentPlayingSongPosition);
            }
        }
    }

    public void playOrPauseSong() {
        playOrPauseSong(currentPlayingSongPosition);
    }

    public SongsRecycleViewAdapter(CurrentSongListener listener, List<Song> songList) {
        this.listener = listener;
        this.songList = songList;
    }

    class SongViewHolder extends RecyclerView.ViewHolder {
        final ImageView coverImageView;
        final TextView songTitleTextView;
        final ImageView downloadButton;
        final ImageView playButton;

        private View.OnClickListener playClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playOrPauseSong(getAdapterPosition());
            }
        };

        private View.OnClickListener getDownloadClickListener(final String url, final String title) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downloadFile(url, title);
                }
            };
        }

        SongViewHolder(View itemView) {
            super(itemView);
            coverImageView = (ImageView) itemView.findViewById(R.id.cover_image);
            songTitleTextView = (TextView) itemView.findViewById(R.id.song_title);
            downloadButton = (ImageView) itemView.findViewById(R.id.download_button);
            playButton = (ImageView) itemView.findViewById(R.id.play_button);

            playButton.setOnClickListener(playClickListener);
        }
    }

    @Override
    public SongsRecycleViewAdapter.SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_main_item, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SongsRecycleViewAdapter.SongViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        Song song = songList.get(position);
        if (position == currentPlayingSongPosition) {
            SongIconChanger.switchDrawable(context, holder.playButton, song.isSelected());
        } else {
            SongIconChanger.switchDrawable(context, holder.playButton, false);
        }
        holder.songTitleTextView.setText(song.getTitle());
        holder.downloadButton.setOnClickListener(holder.getDownloadClickListener(song.getMp3Url(), song.getTitle()));
        SongIconChanger.loadDrawableWithPicasso(context, holder.coverImageView, song.getImageUrl());
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    private void downloadFile(String url, final String title) {
        final ProgressListener progressListener = new ProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                Log.e("R", String.valueOf(bytesRead / (double) contentLength));
                Log.e("R", String.valueOf(done));
            }
        };

        ApiBuilder.getDownloadService(progressListener).getFile(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            writeResponseBodyToDisk(response.body(), title);
                            return null;
                        }
                    }.execute();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(SongsRecycleViewAdapter.this.getClass().getSimpleName(), Log.getStackTraceString(t));
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String title) {
        try {
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "VoroshiloMusic");
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return false;
                }
            }
            File futureStudioIconFile = new File(mediaStorageDir + File.separator + title + ".mp3");
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
