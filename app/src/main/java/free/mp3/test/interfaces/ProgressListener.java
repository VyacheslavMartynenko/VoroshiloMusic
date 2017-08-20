package free.mp3.test.interfaces;

public interface ProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}
