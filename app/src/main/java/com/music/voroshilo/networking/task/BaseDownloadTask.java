package com.music.voroshilo.networking.task;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.music.voroshilo.R;
import com.music.voroshilo.activity.BaseActivity;
import com.music.voroshilo.application.MusicApplication;
import com.music.voroshilo.model.networking.Download;
import com.music.voroshilo.util.NotificationUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

abstract class BaseDownloadTask {
    @Download.Type
    private int type;

    @Download.Type
    abstract int setType();

    BaseDownloadTask() {
        this.type = setType();
    }

    //todo rx-buffer
    boolean writeResponseBodyToDisk(ResponseBody body, String dir, String file) {
        try {
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(dir),
                    MusicApplication.getInstance().getString(R.string.app_name));
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return false;
                }
            }
            final File downloadFile = new File(mediaStorageDir + File.separator + file);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(downloadFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                }
                outputStream.flush();
                showCompleteMessage(downloadFile.getPath());
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    private void showCompleteMessage(final String filePath) {
        final BaseActivity activity = MusicApplication.getInstance().getCurrentActivity();
        if (activity != null && activity.isVisible()) {
            activity.runOnUiThread(() -> Toast.makeText(activity,
                    activity.getString(R.string.download_complete_message, filePath),
                    Toast.LENGTH_LONG).show());
        }
        Context context = MusicApplication.getInstance().getApplicationContext();
        String title = context.getString(R.string.app_name);
        String text = context.getString(R.string.download_complete_message, filePath);
        NotificationUtil.showNotification(type, title, text, filePath);
    }
}