package free.mp3.musicdownloadmp3.model.networking;

import android.support.annotation.IntDef;

import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DataBody {
    public static final int MUSIC = 0;
    public static final int BUTTON = 1;
    public static final int MUSIC_AND_BUTTON = 2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MUSIC, MUSIC_AND_BUTTON, BUTTON})
    public @interface DisplayMode {
    }

    public static final int NO = 0;
    public static final int START_APP = 1;
    public static final int APPODEAL = 2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({NO, START_APP, APPODEAL})
    public @interface AdMode {
    }

    @SerializedName("net_type")
    private int netType;

    @SerializedName("popup")
    private int popup;

    @SerializedName("popup_url")
    private String popupUrl;

    @SerializedName("is_burst")
    private int burstStatus;

    @SerializedName("burst_url")
    private String burstUrl;

    public DataBody(int netType, int popup, String popupUrl, int burstStatus, String burstUrl) {
        this.netType = netType;
        this.popup = popup;
        this.popupUrl = popupUrl;
        this.burstStatus = burstStatus;
        this.burstUrl = burstUrl;
    }

    public int getNetType() {
        return netType;
    }

    public void setNetType(int netType) {
        this.netType = netType;
    }

    public int getPopup() {
        return popup;
    }

    public void setPopup(int popup) {
        this.popup = popup;
    }

    public String getPopupUrl() {
        return popupUrl;
    }

    public void setPopupUrl(String popupUrl) {
        this.popupUrl = popupUrl;
    }

    public int getBurstStatus() {
        return burstStatus;
    }

    public void setBurstStatus(int burstStatus) {
        this.burstStatus = burstStatus;
    }

    public String getBurstUrl() {
        return burstUrl;
    }

    public void setBurstUrl(String burstUrl) {
        this.burstUrl = burstUrl;
    }
}
