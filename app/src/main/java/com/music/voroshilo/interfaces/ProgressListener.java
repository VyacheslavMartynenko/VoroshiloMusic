package com.music.voroshilo.interfaces;

public interface ProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}
