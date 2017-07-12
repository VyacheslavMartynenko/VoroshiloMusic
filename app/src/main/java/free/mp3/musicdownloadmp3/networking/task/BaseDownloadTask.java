package free.mp3.musicdownloadmp3.networking.task;

import free.mp3.musicdownloadmp3.model.networking.Download;

abstract class BaseDownloadTask {
    @Download.Type
    private int type;

    @Download.Type
    abstract int setType();

    BaseDownloadTask() {
        this.type = setType();
    }
}