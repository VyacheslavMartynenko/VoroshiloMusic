package free.mp3.musicdownloadmp3.model.networking;

import android.support.annotation.IntDef;

import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DataBody {
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

    @SerializedName("popup_text")
    private int popupText;

    @SerializedName("popup_url")
    private String popupUrl;

    @SerializedName("is_burst")
    private int burstStatus;

    @SerializedName("burst_text")
    private int burstText;

    @SerializedName("burst_url")
    private String burstUrl;

    @SerializedName("show_tutorial")
    private int tutorialStatus;

    public DataBody(int netType, int popup, int popupText, String popupUrl, int burstStatus, int burstText, String burstUrl, int tutorialStatus) {
        this.netType = netType;
        this.popup = popup;
        this.popupText = popupText;
        this.popupUrl = popupUrl;
        this.burstStatus = burstStatus;
        this.burstText = burstText;
        this.burstUrl = burstUrl;
        this.tutorialStatus = tutorialStatus;
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

    public int getPopupText() {
        return popupText;
    }

    public void setPopupText(int popupText) {
        this.popupText = popupText;
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

    public int getBurstText() {
        return burstText;
    }

    public void setBurstText(int burstText) {
        this.burstText = burstText;
    }

    public String getBurstUrl() {
        return burstUrl;
    }

    public void setBurstUrl(String burstUrl) {
        this.burstUrl = burstUrl;
    }

    public int getTutorialStatus() {
        return tutorialStatus;
    }

    public void setTutorialStatus(int tutorialStatus) {
        this.tutorialStatus = tutorialStatus;
    }
}
