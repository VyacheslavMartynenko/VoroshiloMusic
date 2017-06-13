package com.music.voroshilo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.music.voroshilo.R;
import com.music.voroshilo.interfaces.CurrentSongListener;
import com.music.voroshilo.model.networking.Song;
import com.music.voroshilo.networking.task.DownloadAsyncTask;
import com.music.voroshilo.util.SongIconChanger;

import java.util.List;

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

        private View.OnClickListener getDownloadClickListener(final String url) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DownloadAsyncTask().execute();
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
        holder.downloadButton.setOnClickListener(holder.getDownloadClickListener(song.getImageUrl()));
        SongIconChanger.loadDrawableWithPicasso(context, holder.coverImageView, song.getImageUrl());
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }
}
