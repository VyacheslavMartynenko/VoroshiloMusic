package free.mp3.test.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import free.mp3.test.R;
import free.mp3.test.interfaces.CurrentSongListener;
import free.mp3.test.model.networking.Song;
import free.mp3.test.networking.ApiBuilder;
import free.mp3.test.util.SongIconChanger;

import java.util.List;

public class SongsRecycleViewAdapter extends RecyclerView.Adapter<SongsRecycleViewAdapter.SongViewHolder> {
    private List<Song> songList;
    private CurrentSongListener listener;
    private int currentPlayingSongPosition = RecyclerView.NO_POSITION;

    public int getOffset() {
        return songList != null ? songList.size() : 0;
    }

    public void updateSongList(List<Song> newSongList) {
        songList.clear();
        songList.addAll(newSongList);
        currentPlayingSongPosition = RecyclerView.NO_POSITION;
        notifyDataSetChanged();
    }

    public void addSongList(List<Song> newSongList) {
        songList.addAll(newSongList);
        notifyItemRangeChanged(songList.size(), newSongList.size());
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

    class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView coverImageView;
        final TextView songTitleTextView;
        final ImageView downloadButton;
        final ImageView playButton;
        final ImageView licenseButton;
        final Button reportButton;

        SongViewHolder(View itemView) {
            super(itemView);
            coverImageView = (ImageView) itemView.findViewById(R.id.cover_image);
            songTitleTextView = (TextView) itemView.findViewById(R.id.song_title);
            downloadButton = (ImageView) itemView.findViewById(R.id.download_button);
            playButton = (ImageView) itemView.findViewById(R.id.play_button);
            licenseButton = (ImageView) itemView.findViewById(R.id.license_button);
            reportButton = (Button) itemView.findViewById(R.id.report_button);

            playButton.setOnClickListener(this);
            licenseButton.setOnClickListener(this);
            downloadButton.setOnClickListener(this);
            reportButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v instanceof ImageView) {
                switch (v.getId()) {
                    case R.id.download_button:
                        if (currentPlayingSongPosition == RecyclerView.NO_POSITION) {
                            currentPlayingSongPosition = getAdapterPosition();
                        }
                        Song song = songList.get(getAdapterPosition());
                        listener.downloadSong(song.getImageUrl(), song.getMp3Url(), song.getTitle());
                        break;
                    case R.id.play_button:
                        playOrPauseSong(getAdapterPosition());
                        break;
                    case R.id.license_button:
                        listener.showPrivacy(ApiBuilder.LICENSE_URL);
                        break;
                    default:
                        break;
                }
            } else if (v instanceof Button) {
                Song song = songList.get(getAdapterPosition());
                listener.reportSong(song.getTitle(), song.getVideoId());
            }
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
        SongIconChanger.loadDrawableWithPicasso(context, holder.coverImageView, song.getImageUrl());
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }
}