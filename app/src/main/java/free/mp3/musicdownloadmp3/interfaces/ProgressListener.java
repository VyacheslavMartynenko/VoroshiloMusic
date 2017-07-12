package free.mp3.musicdownloadmp3.interfaces;

public interface ProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}
