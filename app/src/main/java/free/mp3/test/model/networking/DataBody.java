package free.mp3.test.model.networking;

import android.support.annotation.IntDef;

import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DataBody {
    public static final int MULTIPLE = 0;
    public static final int FIRST = 1;
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MULTIPLE, FIRST})
    public @interface LaunchMode {
    }

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

    @SerializedName("adnet_type")
    private int adNetType;

    @SerializedName("adnet_tutorial")
    private int adNetTutorial;

    @SerializedName("adnet_download")
    private int adNetDownload;

    @SerializedName("adnet_play")
    private int adNetPlay;

    @SerializedName("adnet_search")
    private int adNetSearch;

    @SerializedName("adnet_banner")
    private int adNetBanner;

    @SerializedName("popup")
    private int popupStatus;

    @SerializedName("popup_text")
    private String popupText;

    @SerializedName("popup_url")
    private String popupUrl;

    @SerializedName("is_burst")
    private int burstStatus;

    @SerializedName("burst_text")
    private String burstText;

    @SerializedName("burst_url")
    private String burstUrl;

    @SerializedName("appodeal_key")
    private String appodealKey;

    @SerializedName("startapp_key")
    private String startappKey;

    @SerializedName("show_tutorial")
    private int tutorialStatus;

    @SerializedName("music_url")
    private String musicUrl;

    public DataBody(int adNetType, int adNetTutorial, int adNetDownload, int adNetPlay, int adNetSearch, int adNetBanner, int popupStatus, String popupText, String popupUrl, int burstStatus, String burstText, String burstUrl, String appodealKey, String startappKey, int tutorialStatus, String musicUrl) {
        this.adNetType = adNetType;
        this.adNetTutorial = adNetTutorial;
        this.adNetDownload = adNetDownload;
        this.adNetPlay = adNetPlay;
        this.adNetSearch = adNetSearch;
        this.adNetBanner = adNetBanner;
        this.popupStatus = popupStatus;
        this.popupText = popupText;
        this.popupUrl = popupUrl;
        this.burstStatus = burstStatus;
        this.burstText = burstText;
        this.burstUrl = burstUrl;
        this.appodealKey = appodealKey;
        this.startappKey = startappKey;
        this.tutorialStatus = tutorialStatus;
        this.musicUrl = musicUrl;
    }

    public int getAdNetType() {
        return adNetType;
    }

    public void setAdNetType(int adNetType) {
        this.adNetType = adNetType;
    }

    public int getAdNetTutorial() {
        return adNetTutorial;
    }

    public void setAdNetTutorial(int adNetTutorial) {
        this.adNetTutorial = adNetTutorial;
    }

    public int getAdNetDownload() {
        return adNetDownload;
    }

    public void setAdNetDownload(int adNetDownload) {
        this.adNetDownload = adNetDownload;
    }

    public int getAdNetPlay() {
        return adNetPlay;
    }

    public void setAdNetPlay(int adNetPlay) {
        this.adNetPlay = adNetPlay;
    }

    public int getAdNetSearch() {
        return adNetSearch;
    }

    public void setAdNetSearch(int adNetSearch) {
        this.adNetSearch = adNetSearch;
    }

    public int getAdNetBanner() {
        return adNetBanner;
    }

    public void setAdNetBanner(int adNetBanner) {
        this.adNetBanner = adNetBanner;
    }

    public int getPopupStatus() {
        return popupStatus;
    }

    public void setPopupStatus(int popupStatus) {
        this.popupStatus = popupStatus;
    }

    public String getPopupText() {
        return popupText;
    }

    public void setPopupText(String popupText) {
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

    public String getBurstText() {
        return burstText;
    }

    public void setBurstText(String burstText) {
        this.burstText = burstText;
    }

    public String getBurstUrl() {
        return burstUrl;
    }

    public void setBurstUrl(String burstUrl) {
        this.burstUrl = burstUrl;
    }

    public String getAppodealKey() {
        return appodealKey;
    }

    public void setAppodealKey(String appodealKey) {
        this.appodealKey = appodealKey;
    }

    public String getStartappKey() {
        return startappKey;
    }

    public void setStartappKey(String startappKey) {
        this.startappKey = startappKey;
    }

    public int getTutorialStatus() {
        return tutorialStatus;
    }

    public void setTutorialStatus(int tutorialStatus) {
        this.tutorialStatus = tutorialStatus;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }
}
