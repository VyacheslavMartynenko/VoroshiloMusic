package free.mp3.test.networking.task;

import free.mp3.test.model.networking.Download;

abstract class BaseDownloadTask {
    @Download.Type
    private int type;

    @Download.Type
    abstract int setType();

    BaseDownloadTask() {
        this.type = setType();
    }
}