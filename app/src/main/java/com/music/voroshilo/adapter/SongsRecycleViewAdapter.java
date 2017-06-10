package com.music.voroshilo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.music.voroshilo.R;
import com.music.voroshilo.inerface.CurrentSongListener;
import com.music.voroshilo.model.networking.Song;

import java.util.List;

public class SongsRecycleViewAdapter extends RecyclerView.Adapter<SongsRecycleViewAdapter.SongViewHolder> {
    private List<Song> songList;
    private CurrentSongListener listener;

    public void updateSongList(List<Song> newSongList) {
        songList.clear();
        songList.addAll(newSongList);
        notifyDataSetChanged();
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
                listener.updateCurrentSongInfo(songTitleTextView.getText().toString());
            }
        };

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
        Song song = songList.get(position);
        holder.songTitleTextView.setText(song.getTitle());
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }
}
