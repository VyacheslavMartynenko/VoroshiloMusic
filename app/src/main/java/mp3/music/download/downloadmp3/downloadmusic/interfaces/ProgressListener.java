package mp3.music.download.downloadmp3.downloadmusic.interfaces;

public interface ProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}
