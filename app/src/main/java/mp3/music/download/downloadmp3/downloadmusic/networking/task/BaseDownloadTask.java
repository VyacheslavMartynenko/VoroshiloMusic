package mp3.music.download.downloadmp3.downloadmusic.networking.task;

import mp3.music.download.downloadmp3.downloadmusic.model.networking.Download;

abstract class BaseDownloadTask {
    @Download.Type
    private int type;

    @Download.Type
    abstract int setType();

    BaseDownloadTask() {
        this.type = setType();
    }
}