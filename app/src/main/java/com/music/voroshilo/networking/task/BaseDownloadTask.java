package com.music.voroshilo.networking.task;

import com.music.voroshilo.model.networking.Download;

abstract class BaseDownloadTask {
    @Download.Type
    private int type;

    @Download.Type
    abstract int setType();

    BaseDownloadTask() {
        this.type = setType();
    }
}