package mp3.music.download.downloadmp3.downloadmusic.interfaces;

public interface CurrentSongListener {
    boolean updateCurrentSongInfo(String url, String imageUrl, boolean isCache);

    void downloadSong(String imageUrl, String mp3Url, String title);

    void reportSong(String songName, String videoId);

    void showPrivacy(String url);
}
